package com.mycompany.soundquiz.server.service;

import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.*;
import com.google.common.collect.ImmutableList;
import com.mycompany.soundquiz.server.utils.Config;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GeminiService {

    private static final String MODEL = "gemini-2.5-flash";
    private static final String API_KEY = Config.get("gemini.apikey");
    private static final Client CLIENT = Client.builder().apiKey(API_KEY).build();
    
    // Retry configuration
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    public static String chat(String prompt) {
        int retryCount = 0;
        
        while (retryCount < MAX_RETRIES) {
            try {
                List<Content> contents = ImmutableList.of(
                    Content.builder()
                        .role("user")
                        .parts(ImmutableList.of(Part.fromText(prompt)))
                        .build()
                );

                GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseModalities(ImmutableList.of("TEXT"))
                    .build();

                ResponseStream<GenerateContentResponse> responseStream =
                    CLIENT.models.generateContentStream(MODEL, contents, config);

                StringBuilder sb = new StringBuilder();

                for (GenerateContentResponse res : responseStream) {
                    if (res.candidates().isEmpty()) continue;
                    List<Part> parts = res.candidates().get().get(0).content().get().parts().get();
                    for (Part part : parts) {
                        java.util.Optional<String> textOpt = part.text();
                        if (textOpt != null && textOpt.isPresent()) {
                            sb.append(textOpt.get());
                        }
                    }
                }

                responseStream.close();
                return sb.toString();

            } catch (Exception e) {
                retryCount++;
                System.err.println("Gemini API attempt " + retryCount + " failed: " + e.getMessage());
                
                if (retryCount >= MAX_RETRIES) {
                    return handleError(e);
                }
                
                // Wait before retry
                try {
                    TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return handleError(ie);
                }
            }
        }
        
        return "Error: Maximum retry attempts exceeded";
    }
    
    private static String handleError(Exception e) {
        // Return a valid JSON structure even when API fails
        return "{\"error\": \"Service unavailable\", \"message\": \"" + 
               e.getMessage().replace("\"", "\\\"") + "\"}";
    }
}