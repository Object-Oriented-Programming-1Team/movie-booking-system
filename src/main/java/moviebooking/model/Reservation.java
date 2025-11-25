package main.java.moviebooking.model;

import java.util.List;

public class Reservation {

    private Movie movie;
    private Screening screening;
    private List<Seat> seatList;
    private int totalPrice;

    public Reservation(Movie movie, Screening screening, List<Seat> seatList, int totalPrice) {
        this.movie = movie;
        this.screening = screening;
        this.seatList = seatList;
        this.totalPrice = totalPrice;
    }

    public Movie getMovie() {
        return movie;
    }

    public Screening getScreening() {
        return screening;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getSeatDisplayText() {

        // 커플석 판별 (좌석 2개 + 둘 다 coupleSeat = true)
        if (seatList.size() == 2
                && seatList.get(0).isCoupleSeat()
                && seatList.get(1).isCoupleSeat()) {

            return "커플석 (" +
                    seatList.get(0).getSeatNumber() +
                    " - " +
                    seatList.get(1).getSeatNumber() +
                    ")";
        }

        // 일반 좌석 표시
        StringBuilder sb = new StringBuilder();
        for (Seat seat : seatList) {
            sb.append(seat.getSeatNumber()).append(" ");
        }

        return sb.toString().trim();
    }

}



