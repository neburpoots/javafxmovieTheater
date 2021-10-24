package nl.inholland.javafx.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Movie {
    private String title;
    private double price;
    private int duration;

    public Movie(String title, double price, int duration) {
        this.title = title;
        this.price = price;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        BigDecimal bd = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        return bd;
    }

    public int getDuration() {
        return duration;
    }
}
