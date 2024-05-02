from dotenv import load_dotenv
import os
from langchain.chains.summarize import load_summarize_chain
from langchain_community.document_loaders import WebBaseLoader
from langchain_openai import ChatOpenAI

# Load API key
load_dotenv()
api_key = os.getenv("OPENAI_API_KEY")
if api_key is not None:
    print("Your API key is:", api_key)
else:
    print("API key not found in the .env file")

# Initialize lang chain
loader = WebBaseLoader("https://python.langchain.com/docs/"
                       "get_started/quickstart")
docs = loader.load()
llm = ChatOpenAI(temperature=0, model_name="gpt-3.5-turbo-1106")
chain = load_summarize_chain(llm, chain_type="stuff")
chain.invoke(docs)
