package main.java.moviebooking.model;

import java.util.ArrayList;
import java.util.UUID;

public class Booking {
    private String bookingId;
    private User user;
    private Screening screening;
    private ArrayList<Seat> seats;

    public Booking(Screening screening, ArrayList<Seat> seats) {
        this.bookingId = UUID.randomUUID().toString();
//        this.user = "admin";
        this.screening = screening;
        this.seats = seats;
    }

    public int getTotalPrice() {
        if (seats == null || seats.isEmpty()) {
            return 0;
        }

        int total = 0;
        for (Seat seat : seats) {
            total += seat.getPrice();
        }
        return total;
    }

    public String getFormattedTotalPrice() {
        return String.format("%,d원", getTotalPrice());
    }

//    public String getSeatDisplayText() {
//
//        // 커플석 판별 (좌석 2개 + 둘 다 coupleSeat = true)
//        if (seatList.size() == 2
//                && seatList.get(0).isCoupleSeat()
//                && seatList.get(1).isCoupleSeat()) {
//
//            return "커플석 (" +
//                    seatList.get(0).getSeatNumber() +
//                    " - " +
//                    seatList.get(1).getSeatNumber() +
//                    ")";
//        }
//
//        // 일반 좌석 표시
//        StringBuilder sb = new StringBuilder();
//        for (Seat seat : seatList) {
//            sb.append(seat.getSeatNumber()).append(" ");
//        }
//
//        return sb.toString().trim();
//    }

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
