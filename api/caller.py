import sys
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
    # Placeholder for Kafka integration
    # Implement sending data to your Kafka cluster here
    print("Data sent to Kafka:", data)


def main():
    keyword = sys.argv[1]  # The keyword is passed as a command-line argument
    data = call_data_api(keyword)
    send_to_kafka(data)


if __name__ == "__main__":
    main()
