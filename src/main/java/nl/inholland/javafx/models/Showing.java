package nl.inholland.javafx.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Showing {

    private Room room;
    private UUID showingId;
    private int totalTickets;
    private Movie movie;
    private LocalDateTime start;
    private List<Ticket> soldTickets;

    public Showing(Room room, Movie movie, LocalDateTime start) {
        this.soldTickets = new ArrayList<>();
        this.room = room;
        this.showingId = UUID.randomUUID();
        this.totalTickets = room.getSeats();
        this.movie = movie;
        this.start = start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getTitle() {
        return movie.getTitle();
    }

    public BigDecimal getPrice() {
        return movie.getPrice();
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Room getRoom() {
        return room;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public LocalDateTime getEnd() {
        LocalDateTime endOfMovie = start.plusMinutes(movie.getDuration());
        return endOfMovie;
    }

    public int availableTickets() {
        return room.getSeats() - soldTickets.size();
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void addTickets(String name, int amount) {
        for(int i = 0; i < amount; i++) {
            soldTickets.add(new Ticket(name));
        }
    }

    public List<Ticket> getSoldTickets() {
        return soldTickets;
    }

    public String getShowingId() {
        return showingId.toString();
    }
}
