import sys
from confluent_kafka import Producer
import requests

API_KEY = 114514


def call_data_api(keyword):
    # Replace with the actual URL and parameters for your data API
    url = (
        f"https://newsapi.org/v2/everything?q={keyword}&"
        f"from=2024-02-11&sortBy=publishedAt&apiKey={API_KEY}"
    )
    response = requests.get(url)
    return response.json()


def send_to_kafka(data):
    # Kafka configuration
    conf = {'bootstrap.servers': "YOUR_KAFKA_BROKER_ADDRESS"}

    # Create Producer instance
    producer = Producer(**conf)

    # Kafka topic
    topic = 'your_kafka_topic'

    # Send data to Kafka
    producer.produce(topic, data.encode('utf-8'))
    producer.flush()  # Ensure data is sent to Kafka


def main():
    keyword = sys.argv[1]  # The keyword is passed as a command-line argument
    data = call_data_api(keyword)
    data_str = str(data)
    send_to_kafka(data_str)


if __name__ == "__main__":
    main()
