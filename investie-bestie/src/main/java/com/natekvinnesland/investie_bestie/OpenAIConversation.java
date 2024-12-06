package com.natekvinnesland.investie_bestie;

import java.util.ArrayList;
import java.util.List;
import dev.langchain4j.model.chat.*;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

/**
 * A class to represent a conversation with a chatbot.
 * It hides the details of LangChain4j and OpenAI, so client can
 * ask just call 'askQuestion' with context (instructions, etc.) and
 * the question, and get a string back. The conversation thus far is sent
 * to OpenAI on each question.
 *
 * Client can also get sample questions based on a given context, and can reset
 * conversation to start over.
 */
public class OpenAIConversation {
    private final MessageWindowChatMemory chatMemory;
    private final ChatLanguageModel chatModel;

    // Default constructor that retrieves the API key from the environment
    public OpenAIConversation() {
        this(System.getenv("OPENAI_API_KEY"));
    }

    // New constructor that accepts an API key as an argument
    public OpenAIConversation(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("API key must not be null or empty");
        }
        this.chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o-mini")
                .build();
        this.chatMemory = MessageWindowChatMemory.withMaxMessages(10);
    }

    /**
     * askQuestion allows user to ask a question with context (e.g., instructions
     * for how OpenAI should respond. It adds the context and question to the memory,
     * in the form langchain4j wants, then asks the question, then puts response into memory and returns text of response. in the form
     */

    public String askQuestion(String context, String question) {
        try {
            SystemMessage sysMessage = SystemMessage.from(context);
            chatMemory.add(sysMessage);
            UserMessage userMessage = UserMessage.from(question);
            chatMemory.add(userMessage);

            // Generate the response from the model
            Response response = chatModel.generate(chatMemory.messages());
            AiMessage aiMessage = (AiMessage) response.content();
            chatMemory.add(aiMessage);
            return aiMessage.text();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to generate a response from OpenAI. " + e.getMessage();
        }
    }

    /**
     * generateSampleQuestions generate sample questions with a given context. You can specify the number of questions
     * and the max words that should be generated for each question. This method is
     * often used to provide user with sample questions to trigger the dialogue.
     */
    public List<String> generateSampleQuestions(String context, int count, int maxWords) {
        List<String> questions = new ArrayList<>();
        String instructions = "For the context following, please provide a list of " + count + " questions with a maximum of " + maxWords + " words per question.";
        instructions = instructions + " Return the questions as a string with delimiter '%%' between each generated question";
        SystemMessage sysMessage = SystemMessage.from(instructions);
        UserMessage userMessage = UserMessage.from(context);
        List<ChatMessage> prompt = new ArrayList<>();
        prompt.add(sysMessage);
        prompt.add(userMessage);
        Response response = chatModel.generate(prompt);
        AiMessage aiMessage = (AiMessage) response.content();
        String responseText = aiMessage.text();
        String[] questionArray = responseText.split("%%");
        return List.of(questionArray);
    }

    public void resetConversation() {
        chatMemory.clear();
    }

    /**
     * @return the messages thus far
     */
    public String toString() {

        return chatMemory.messages().toString();
    }
}