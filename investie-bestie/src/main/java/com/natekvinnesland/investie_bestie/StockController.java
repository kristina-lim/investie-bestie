package com.natekvinnesland.investie_bestie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final OpenAIConversation openAIConversation;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;

        // Retrieve OpenAI API key from environment variable
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("OpenAI API key not found in environment variables");
        }
        this.openAIConversation = new OpenAIConversation(apiKey);
    }

    @GetMapping("/{symbol}/summary")
    public String getStockSummary(@PathVariable String symbol) {
        double beta = stockService.calculateBeta(symbol + ".csv");
        String context = "You are a financial analyst. Explain the significance of the beta value of a stock in a bulleted format of at most 5 bullet points.";
        String question = "The beta value for the stock " + symbol + " is " + beta + ". What does this mean for potential investors?";
        return openAIConversation.askQuestion(context, question);
    }

    @GetMapping("/{symbol}/beta")
    public double getBeta(@PathVariable String symbol) {
        return stockService.calculateBeta(symbol + ".csv");
    }

    @GetMapping("/{symbol}/questions")
    public List<String> generateSampleQuestions(@PathVariable String symbol) {
        double beta = stockService.calculateBeta(symbol + ".csv");
        String context = "You are a financial analyst teaching students how to analyze stock data. Provide investment-related questions for the stock " + symbol + " and its associated beta: " + beta + ".";
        return openAIConversation.generateSampleQuestions(context, 3, 15);
    }

    @GetMapping(value = "/{symbol}/chart", produces = MediaType.IMAGE_PNG_VALUE)
    public void getChart(@PathVariable String symbol, HttpServletResponse response) {
        try {
            byte[] chart = stockService.generateChart(symbol + ".csv");
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            response.setContentLength(chart.length);
            response.getOutputStream().write(chart);
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating chart: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @GetMapping("/{symbol}/report")
    public ResponseEntity<?> getStockReport(@PathVariable String symbol) {
        Map<String, Object> report = new HashMap<>();
        try {
            double beta = stockService.calculateBeta(symbol + ".csv");
            report.put("beta", beta);
            report.put("chartEndpoint", "/api/stocks/" + symbol + "/chart");
            String context = "You are a financial analyst. Explain the significance of the beta value of a stock.";
            String question = "The beta value for the stock " + symbol + " is " + beta + ". What does this mean for potential investors?";
            String summary = openAIConversation.askQuestion(context, question);
            report.put("summary", summary);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public List<String> listStocks() {
        File folder = new File("src/main/resources/data/");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));
        return Arrays.stream(files)
                .map(file -> file.getName().replace(".csv", ""))
                .collect(Collectors.toList());
    }
}

