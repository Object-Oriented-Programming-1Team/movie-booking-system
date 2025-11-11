package moviebooking.model;

import java.util.ArrayList;

public class Booking {
    private String bookingId;
    private User user;
    private Screening screening;
    private ArrayList<Seat> seats;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
    }
}
