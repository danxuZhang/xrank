import json
from kafka import KafkaConsumer, TopicPartition
from datetime import datetime
import requests
import schedule
import time

# Kafka configuration
KAFKA_BROKER_URL = 'localhost:9093'
KAFKA_TOPICS = ['Blockchain', 'Semiconductor', 'Biotechnology',
                'Artificial Intelligence', 'Vehicle']

INTERVAL = 15 # interval in minutes to post

# API configuration
API_URL = 'http://example.com/api/data'

def fetch_latest_message():

    print(f"Fetching latest message from Kafka at {datetime.now().isoformat()}")
    consumer = KafkaConsumer(
        bootstrap_servers=[KAFKA_BROKER_URL],
        enable_auto_commit=True,
        group_id=None,
        auto_offset_reset='latest'  # We start consuming at the latest messag
    )

    # Subscribe temporarily to get the latest offset
    partitions = [TopicPartition(topic, 0) for topic in KAFKA_TOPICS]
    consumer.assign(partitions)

    for partition in partitions:
        # Seek to the end of the partition to avoid consuming any messages
        consumer.seek_to_end(partition)
        last_offset = consumer.position(partition) - 1
        if last_offset >= 0:
            consumer.seek(partition, last_offset)
            for message in consumer:
                data = json.loads(message.value.decode('utf-8'))
                print(f"Received data from {message.topic}: {data}")

                post_data = {
                    "time": datetime.now().isoformat(),
                    "topic": message.keyword,
                    "content": data
                }

                response = requests.post(API_URL, json=post_data)
                print(f"Data posted to {API_URL}, status code: {response.status_code}")

                break

    consumer.close()

def main():
    # Schedule to run every 15 minutes
    schedule.every(INTERVAL).minutes.do(fetch_latest_message)

    while True:
        schedule.run_pending()
        time.sleep(1)

if __name__ == '__main__':
    main()
