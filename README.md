🤖 HuggingFace Wrappers — AI-as-a-Service Platform

# 🤖 HuggingFace Wrappers

A production-grade **wrapper layer** built over Hugging Face’s powerful NLP/ML APIs to simulate an internal **AI-as-a-Service** microservice. Abstracts away the complexity of dealing with token management, model endpoint orchestration, and response handling — all in a clean, scalable, and reusable design.

---

## 🚀 What It Does

- 🧠 **Unified NLP Interface**: Summarization, Sentiment Analysis, Translation, Text Classification
- 🔒 **Token-Secured API Access**: Hugging Face Bearer token injected via config
- ⚙️ **Switchable Models**: Modularized support for switching between models like `distilbart`, `bert-base`, etc.
- 💬 **Real-Time Use Cases**:
  - Article Summarization
  - Language Translation
  - Chatbot Sentiment Detection
  - Email Classification
- 📈 **Centralized Logging and Error Capture**

---

## 🧠 Architecture Principles

✅ **Clean Architecture**  
✅ **KISS**, **SOLID**, **DRY**  
✅ Service layer communicates with external APIs using a clean Adapter pattern  
✅ Abstracts HuggingFace-specific responses to clean DTOs  
✅ Configurable for future AI vendors (OpenAI, Cohere, etc.)

---

## 🔧 Tech Stack

| Tech        | Use Case |
|-------------|----------|
| Java 17     | Language |
| Spring Boot 3 | Framework |
| Spring WebClient | External API call |
| HuggingFace APIs | AI/NLP model serving |
| MapStruct   | Response mapping |
| Lombok      | Clean boilerplate |
| Swagger     | API docs |
| Logback     | Logging |
| YAML Config | Secrets & model URLs |

---

## ⚙️ API Endpoints

- `POST /api/nlp/summarize` – Summarizes text input
- `POST /api/nlp/sentiment` – Detects tone and emotion
- `POST /api/nlp/translate` – Translates to desired language
- `POST /api/nlp/classify` – Performs zero-shot classification

---

## 🧪 Local Setup

```bash
git clone https://github.com/Shubh00796/huggingface-wrapper.git
cd huggingface-wrapper
./mvnw clean install
./mvnw spring-boot:run


huggingface-wrapper/
├── controller/
├── service/
├── dto/
├── mapper/
├── config/
├── util/
└── exception/

•	Shows real-world use of AI integration into Java microservices
	•	Clean, reusable wrapper that mimics SaaS-level production code
	•	Highlights skills in external API consumption, modular code, and API abstraction
	•	Ideal for startups using LLMs in real-time products
    

 


