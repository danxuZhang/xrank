import grpc
from concurrent import futures

import textrank_pb2
import textrank_pb2_grpc
from textrank import extract_sentences


def summarize(input: str) -> str:
    sentences = extract_sentences(input, summary_length=50)
    return sentences


class TextRankService(textrank_pb2_grpc.TextRankServiceServicer):
    def Summarize(self, request, context):
        summarized_text = summarize(request.text)
        return textrank_pb2.Message(text=summarized_text)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    textrank_pb2_grpc.add_TextRankServiceServicer_to_server(TextRankService(), server)
    server.add_insecure_port("[::]:50051")
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
