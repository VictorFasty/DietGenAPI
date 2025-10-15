package com.DietIA.NextGROUP.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class OpenAIService {


    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    // chave do openIA (lembrar de colocar no Application.yml)
    @Value("${openai.api.key}")
    private String openAiApiKey;


    //requisicao http
    private final WebClient webClient;

    //transforma em json
    private final ObjectMapper objectMapper;

    //construtor padrao
    public OpenAIService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(OPENAI_API_URL).build();
        this.objectMapper = objectMapper;
    }

    /**
     * Envia um prompt para a API da OpenAI e retorna a resposta de texto.
     * @param prompt A pergunta detalhada para a IA.
     * @return A resposta em texto gerada pela IA.
     */
    public String getDietFromOpenAI(String prompt) {
        //modelo da ia.
        String model = "gpt-3.5-turbo";

        // Montagem do corpo da requisicao.
        // Usamos as classes auxiliares (records) definidas no final deste arquivo.
        OpenAIRequest requestBody = new OpenAIRequest(
                model,
                List.of(new Message("user", prompt))
        );

        try {
            // Faz a chamada POST para a API, enviando o corpo da requisição.
            String jsonResponse = webClient.post()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve() // Executa a chamada
                    .bodyToMono(String.class) // Pega o corpo da resposta como uma String
                    .block(); // Espera a resposta chegar (sincronamente)

            // Converte a resposta JSON para nosso objeto OpenAIResponse.
            OpenAIResponse response = objectMapper.readValue(jsonResponse, OpenAIResponse.class);

            // Verifica se a resposta tem o conteúdo esperado e retorna apenas o texto.
            if (response != null && !response.choices().isEmpty()) {
                return response.choices().get(0).message().content();
            }

        } catch (JsonProcessingException e) {
            // Trata erros de conversão do JSON
            throw new RuntimeException("Erro ao processar a resposta JSON da OpenAI", e);
        } catch (Exception e) {
            // Trata outros erros (rede, autenticação, etc.)
            throw new RuntimeException("Erro ao chamar a API da OpenAI", e);
        }

        return "Não foi possível gerar a dieta. Tente novamente.";
    }

    // --- CLASSES AUXILIARES (RECORDS) PARA MONTAR O JSON DA REQUISIÇÃO E RESPOSTA ---

    /**
     * Representa a estrutura do corpo da requisição para a API da OpenAI.
     */
    private record OpenAIRequest(String model, List<Message> messages) {}

    /**
     * Representa uma única mensagem na conversa com a IA.
     */
    private record Message(String role, String content) {}

    /**
     * Representa a estrutura principal da resposta JSON da OpenAI.
     * @JsonProperty é usado porque o campo no JSON é "created", que é uma palavra reservada em alguns contextos.
     */
    private record OpenAIResponse(String id, String object, @JsonProperty("created") long created, String model, List<Choice> choices, Usage usage) {}

    /**
     * Representa uma das possíveis respostas ("escolhas") geradas pela IA.
     */
    private record Choice(int index, Message message, Object logprobs, @JsonProperty("finish_reason") String finishReason) {}

    /**
     * Representa as informações de uso de tokens na resposta.
     */
    private record Usage(@JsonProperty("prompt_tokens") int promptTokens, @JsonProperty("completion_tokens") int completionTokens, @JsonProperty("total_tokens") int totalTokens) {}
}

