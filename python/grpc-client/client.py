import grpc
import textrank_pb2
import textrank_pb2_grpc


def run():
    input = """
Graph-based ranking algorithms like Kleinberg’s
HITS algorithm (Kleinberg, 1999) or Google’s
PageRank (Brin and Page, 1998) have been successfully used in citation analysis, social networks, and
the analysis of the link-structure of the World Wide
Web. Arguably, these algorithms can be singled out
as key elements of the paradigm-shift triggered in
the field of Web search technology, by providing a
Web page ranking mechanism that relies on the collective knowledge of Web architects rather than individual content analysis of Web pages. In short, a
graph-based ranking algorithm is a way of deciding
on the importance of a vertex within a graph, by taking into account global information recursively computed from the entire graph, rather than relying only
on local vertex-specific information.
Applying a similar line of thinking to lexical
or semantic graphs extracted from natural language
documents, results in a graph-based ranking model
that can be applied to a variety of natural language
processing applications, where knowledge drawn
from an entire text is used in making local ranking/selection decisions. Such text-oriented ranking
methods can be applied to tasks ranging from automated extraction of keyphrases, to extractive summarization and word sense disambiguation (Mihalcea et
al., 2004).
In this paper, we introduce the TextRank graphbased ranking model for graphs extracted from natural language texts. We investigate and evaluate the
application of TextRank to two language processing
tasks consisting of unsupervised keyword and sen-    
tence extraction, and show that the results obtained
with TextRank are competitive with state-of-the-art
systems developed in these areas.
"""
    channel = grpc.insecure_channel("localhost:50051")
    stub = textrank_pb2_grpc.TextRankServiceStub(channel)

    message = textrank_pb2.Message(text=input)

    response = stub.Summarize(message)
    print("Received: " + response.text)


if __name__ == "__main__":
    run()
