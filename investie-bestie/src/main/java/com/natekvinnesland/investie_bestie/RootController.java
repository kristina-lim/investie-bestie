package com.natekvinnesland.investie_bestie;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String home() {
        return """
               <h1>Welcome to Investie Bestie Backend!</h1>
               <p>Available endpoints:</p>
               <ul>
                 <li>/api/stocks/list - List all stocks</li>
                 <li>/api/stocks/{symbol}/beta - Get beta rating for a stock</li>
                 <li>/api/stocks/{symbol}/chart - View the stock chart</li>
                 <li>/api/stocks/{symbol}/summary - Get an AI-generated summary</li>
               </ul>
               """;
    }
}
