from vllm import LLM, SamplingParams

class LlmSummarizer:
    def __init__(self, model_name="meta-llama/Llama-2-7b", device='cuda'):
        self.sampling_params = SamplingParams(temperature=0.8, top_p=0.95)
        self.device = device
        self.llm = LLM(model="facebook/opt-125m")

    def summarize(self, text, max_length=50, num_beams=5):
        prompts =  f"Summarize the following content in ${max_length} words: {text}"
        outputs = self.llm.generate(prompts, self.sampling_params)
        return outputs
