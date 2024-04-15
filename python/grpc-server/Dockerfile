FROM python:3.11-slim

WORKDIR /app

COPY ./requirements.txt /app

RUN pip install -r requirements.txt

COPY . /app

RUN python -m nltk.downloader punkt
RUN python -m nltk.downloader averaged_perceptron_tagger

EXPOSE 50051

CMD ["python", "server.py"]
