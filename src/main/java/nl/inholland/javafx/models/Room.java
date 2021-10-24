package nl.inholland.javafx.models;

import java.time.LocalDateTime;
import java.util.List;

public class Room {
    private List<Showing> showings;
    private int id;
    private int seats;

    public Room(int id, int seats) {
        this.id = id;
        this.seats = seats;
    }

    public void setShowings(List<Showing> showings) {
        this.showings = showings;
    }

    public List<Showing> getShowings() {
        return showings;
    }

    public int getSeats() {
        return seats;
    }

    public int getId() {
        return id;
    }

    public String getIdString() {
        return Integer.toString(id);
    }

    public boolean checkShowingTime(Showing showing) {
        for(Showing s: showings) {
            LocalDateTime checkStart = s.getStart().minusMinutes(15);
            LocalDateTime checkEnd = s.getEnd().plusMinutes(15);

            if(showing.getStart().isAfter(checkStart) && showing.getStart().isBefore(checkEnd) ) {
                return false;
            } else if(showing.getEnd().isAfter(checkStart) && showing.getEnd().isBefore(checkEnd) ) {
                return false;
            }
        }
        return true;
    }

    public void setShowing(Showing showing) {
        showings.add(showing);
    }
}
