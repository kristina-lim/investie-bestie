package com.natekvinnesland.investie_bestie;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Entity;
import org.knowm.xchart.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StockService {

    private static final String CSV_DIRECTORY = "src/main/resources/data/";
    private static final String KIND = "StockBeta"; // Datastore Kind for storing beta calculations

    // Initialize Google Cloud Datastore client
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    // Load prices from a CSV file
    public List<Double> loadPrices(String filename) {
        List<Double> prices = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_DIRECTORY + filename))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 2 || values[1].isEmpty()) {
                    continue; // Skip rows with missing or invalid "Close" values
                }
                prices.add(Double.parseDouble(values[1])); // Column "Close"
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prices;
    }

    // Calculate returns
    public List<Double> calculateReturns(List<Double> prices) {
        List<Double> returns = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            returns.add((prices.get(i) - prices.get(i - 1)) / prices.get(i - 1));
        }
        return returns;
    }

    // Calculate covariance
    public double calculateCovariance(List<Double> x, List<Double> y) {
        double meanX = x.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double meanY = y.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        double covariance = 0.0;
        for (int i = 0; i < x.size(); i++) {
            covariance += (x.get(i) - meanX) * (y.get(i) - meanY);
        }
        return covariance / (x.size() - 1);
    }

    // Calculate variance
    public double calculateVariance(List<Double> data) {
        double mean = data.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return data.stream()
                .mapToDouble(d -> Math.pow(d - mean, 2))
                .average()
                .orElse(0.0);
    }

    // Calculate beta
    public double calculateBeta(String stockFilename) {
        // Load stock and benchmark prices
        List<Double> stockPrices = loadPrices(stockFilename);
        List<Double> benchmarkPrices = loadPrices("SPY.csv");

        // Ensure both have the same number of data points
        int minSize = Math.min(stockPrices.size(), benchmarkPrices.size());
        stockPrices = stockPrices.subList(0, minSize);
        benchmarkPrices = benchmarkPrices.subList(0, minSize);

        // Calculate returns
        List<Double> stockReturns = calculateReturns(stockPrices);
        List<Double> benchmarkReturns = calculateReturns(benchmarkPrices);

        // Calculate beta
        double covariance = calculateCovariance(stockReturns, benchmarkReturns);
        double variance = calculateVariance(benchmarkReturns);
        double beta = covariance / variance;

        // Save beta to Datastore
        saveBetaToDatastore(stockFilename.replace(".csv", ""), beta);

        return beta;
    }

    // Save Beta Calculation to Datastore
    public void saveBetaToDatastore(String ticker, double beta) {
        Key key = datastore.newKeyFactory().setKind(KIND).newKey(ticker);
        Entity entity = Entity.newBuilder(key)
                .set("ticker", ticker)
                .set("beta", beta)
                .build();
        datastore.put(entity);
    }

    // Parse CSV to StockData objects
    public List<StockData> parseCsvToStockData(String fileName) {
        List<StockData> stockDataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_DIRECTORY + fileName))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Skip rows with missing values
                if (values.length < 5 || Arrays.stream(values).anyMatch(String::isEmpty)) {
                    System.out.println("Skipping row due to missing values: " + Arrays.toString(values));
                    continue;
                }
                try {
                    StockData stockData = new StockData(
                            LocalDate.parse(values[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")), // Date in the first column
                            Double.parseDouble(values[1]), // Closing price
                            Double.parseDouble(values[2]), // 20-day MA
                            Double.parseDouble(values[3]), // 50-day MA
                            Double.parseDouble(values[4])  // 200-day MA
                    );
                    stockDataList.add(stockData);
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.out.println("Skipping row due to invalid format: " + Arrays.toString(values));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockDataList;
    }

    public byte[] generateChart(String stockFilename) throws IOException {
        // Parse CSV into StockData objects
        List<StockData> stockDataList = parseCsvToStockData(stockFilename);

        // Prepare data for the chart
        List<Integer> dates = new ArrayList<>();
        List<Double> closePrices = new ArrayList<>();
        List<Double> ma20 = new ArrayList<>();
        List<Double> ma50 = new ArrayList<>();
        List<Double> ma200 = new ArrayList<>();

        for (int i = 0; i < stockDataList.size(); i++) {
            StockData data = stockDataList.get(i);
            dates.add(i + 1);
            closePrices.add(data.getClose());
            ma20.add(data.getMa20());
            ma50.add(data.getMa50());
            ma200.add(data.getMa200());
        }

        // Create the chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Stock Data").xAxisTitle("Days").yAxisTitle("Price").build();

        // Add series to the chart
        chart.addSeries("Closing Price", dates, closePrices);
        chart.addSeries("20-day MA", dates, ma20);
        chart.addSeries("50-day MA", dates, ma50);
        chart.addSeries("200-day MA", dates, ma200);

        // Customize chart appearance (optional)
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setMarkerSize(4);

        // Export chart to PNG as a byte array
        return BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
    }
}