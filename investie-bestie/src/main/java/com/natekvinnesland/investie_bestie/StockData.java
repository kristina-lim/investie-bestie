package com.natekvinnesland.investie_bestie;

import java.time.LocalDate;

public class StockData {
    private LocalDate date;
    private double close;
    private double ma20;
    private double ma50;
    private double ma200;

    public StockData(LocalDate date, double close, double ma20, double ma50, double ma200) {
        this.date = date;
        this.close = close;
        this.ma20 = ma20;
        this.ma50 = ma50;
        this.ma200 = ma200;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getClose() {
        return close;
    }

    public double getMa20() {
        return ma20;
    }

    public double getMa50() {
        return ma50;
    }

    public double getMa200() {
        return ma200;
    }

    @Override
    public String toString() {
        return "StockData{" +
                "date=" + date +
                ", close=" + close +
                ", ma20=" + ma20 +
                ", ma50=" + ma50 +
                ", ma200=" + ma200 +
                '}';
    }
}
