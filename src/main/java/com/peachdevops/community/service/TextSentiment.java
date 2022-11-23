package com.peachdevops.community.service;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TextSentiment {
    public static Sentiment textSentiment(String text) throws Exception {
        // Instantiates a client
        try (LanguageServiceClient language = LanguageServiceClient.create()) {

            // The text to analyze
            Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();

            // Detects the sentiment of the text
            return language.analyzeSentiment(doc).getDocumentSentiment();
        }
    }
}