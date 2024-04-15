import grpc
from concurrent import futures
import textrank_pb2
import textrank_pb2_grpc
from llm_summarizer import LlmSummarizer

class TextRankService(textrank_pb2_grpc.TextRankServiceServicer):
    def __init__(self):
        self.summarizer = LlmSummarizer()

    def Summarize(self, request, context):
        summary = self.summarizer.summarize(request.text)
        return textrank_pb2.Message(text=summary)

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    textrank_pb2_grpc.add_TextRankServiceServicer_to_server(TextRankService(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    print("Server running on port 50051...")
    server.wait_for_termination()

if __name__ == '__main__':
    serve()
