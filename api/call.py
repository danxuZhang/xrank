import sys
import os
import json
from kafka import KafkaProducer
import requests


API_KEY = os.getenv("GNEWS_API_KEY")


def call_data_api(keyword):
    # Replace with the actual URL and parameters for your data API
    url = (
        f"https://gnews.io/api/v4/search?q={keyword}&apikey={API_KEY}&expand=content&lang=en"
    )
    response = requests.get(url)
    data = response.json()
    
    articles = data['articles']
    contents = []
    for article in articles:
        content = article['content']  # Access the content of the current article
        contents.append(content)  # Add the content to the list
    print(contents)
    return contents


def send_to_kafka(keyword, contents):
    # Create Producer instance
    producer = KafkaProducer(bootstrap_servers=["localhost:9093"], value_serializer=lambda v: json.dumps(v).encode('utf-8'))

    # Kafka topic
    topic = 'xrank'

    # Send data to Kafka
    for content in contents:
        producer.send(topic, {"keyword": keyword, "content": content})
    producer.flush()  # Ensure data is sent to Kafka


def main():
    keyword = sys.argv[1]  # The keyword is passed as a command-line argument
    data = call_data_api(keyword)
    # data_str = str(data)
    send_to_kafka(keyword, data)


if __name__ == "__main__":
    main()
