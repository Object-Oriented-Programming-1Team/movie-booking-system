package main.java.moviebooking.controller;

import main.java.moviebooking.model.*;
import main.java.moviebooking.view.*;

import java.time.format.DateTimeFormatter;

public class MainController {

    private MainFrame mainFrame;
    private BookMoviePanel bookMoviePanel;
    private TimeSelectPanel timeSelectPanel;
    private PayPanel payPanel;

    public MainController(MainFrame mainFrame){
        this.mainFrame = mainFrame;
    }

    public void setBookMoviePanel(BookMoviePanel bookMoviePanel){
        this.bookMoviePanel = bookMoviePanel;
    }

    public void showBookMovieView(Movie movie){
        bookMoviePanel.setMovie(movie);
        mainFrame.showPanel("bookMovie");
    }

    public void showMovieListView(){
        mainFrame.showPanel("movieList");
    }

    public void showCheckReservationsView(){
        mainFrame.showPanel("checkReservations");
    }

    public void showAllMoviesView(){
        mainFrame.showPanel("allMovies");
    }
    
    public void setTimeSelectPanel(TimeSelectPanel timeSelectPanel){
        this.timeSelectPanel = timeSelectPanel;
    }

    public void showTimeSelectView(Movie movie){
        timeSelectPanel.setMovie(movie);
        mainFrame.showPanel("timeSelect");
    }

    //추가 [좌석 선택 화면]
    public void showSeatSelectionView(Screening screening) {
        SeatPanel seatPanel = new SeatPanel(screening, this);
        mainFrame.addPanel(seatPanel, "seat");
        mainFrame.showPanel("seat");
    }

    public void setPayPanel(PayPanel payPanel){
        this.payPanel = payPanel;
    }

    public void showPaymentView(Booking booking) {

        String movieTitle = booking.getScreening().getMovie().getMovieTitle();
        String screenName = booking.getScreening().getScreen().getScreenName();
        String cinemaLine = "CGV 강변   " + screenName;

        // 날짜 및 시간 포맷팅 (예: 2025.11.18 20:00)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String dateTimeLine = booking.getScreening().getStartTime().format(formatter);

        StringBuilder seatSb = new StringBuilder();
        if (booking.getSeats() != null) {
            for (Seat seat : booking.getSeats()) {
                seatSb.append(seat.getSeatNumber()).append(" ");
            }
        }
        String seatLine = seatSb.toString().trim();

        // 4. 총 금액 (Booking 객체에서 직접 가져오기) [수정된 부분]
        String totalPriceText = booking.getTotalPrice() + "원";

        // 5. PayPanel에 정보 전달 및 화면 전환
        if (payPanel != null) {
            payPanel.setReservationInfo(cinemaLine, movieTitle, dateTimeLine, seatLine, totalPriceText);
        }
        mainFrame.showPanel("pay");
    }
}
