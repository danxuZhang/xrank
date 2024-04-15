import torch
from transformers import AutoTokenizer, AutoModelForCausalLM
import deepspeed

class LlmSummarizer:
    def __init__(self, model_name="meta-llama/Llama-2-7b", device='cuda'):
        self.device = device
        self.tokenizer = AutoTokenizer.from_pretrained(model_name)
        self.model = AutoModelForCausalLM.from_pretrained(model_name, torch_dtype=torch.float16).to(self.device)
        self.model = deepspeed.init_inference(self.model, mp_size=1, dtype=torch.float16, replace_with_kernel_inject=True)

    def summarize(self, text, max_length=50, num_beams=5):
        inputs = self.tokenizer(text, return_tensors="pt").to(self.device)
        summary_ids = self.model.generate(**inputs, max_length=max_length, num_beams=num_beams, early_stopping=True)
        summary = self.tokenizer.decode(summary_ids[0], skip_special_tokens=True)
        return summary
