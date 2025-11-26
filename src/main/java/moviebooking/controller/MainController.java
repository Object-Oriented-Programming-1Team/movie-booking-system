package main.java.moviebooking.controller;

import main.java.moviebooking.model.*;
import main.java.moviebooking.service.BookingManager;
import main.java.moviebooking.view.*;

import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class MainController {

    private MainFrame mainFrame;
    private BookMoviePanel bookMoviePanel;
    private TimeSelectPanel timeSelectPanel;
    private PayPanel payPanel;
    private ReservationResultPanel reservationResultPanel;
    private BookingManager bookingManager;

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

        if (payPanel != null) {
            payPanel.setBooking(booking);
        }
        mainFrame.showPanel("pay");
    }

    public void setReservationResultPanel(ReservationResultPanel reservationResultPanel) {
        this.reservationResultPanel = reservationResultPanel;
    }
    public void showReservationResultView(Booking booking) {
        if (reservationResultPanel != null) {
            // Booking 객체에서 정보를 꺼내 Result 패널에 세팅
            String bookingNumber = booking.getBookingId().substring(0, 8).toUpperCase(); // ID 일부만 표시
            String screenName = booking.getScreening().getScreen().getScreenName();
            String cinemaLine = "CGV 강변   " + screenName; // 날짜 정보 등 추가 가능
            String movieTitle = booking.getScreening().getMovie().getMovieTitle();

            String seatInfo = booking.getSeats().stream()
                    .map(Seat::getSeatNumber)
                    .collect(Collectors.joining(", ")) + " (" + screenName + ")";

            reservationResultPanel.setReservationResult(bookingNumber, cinemaLine, movieTitle, seatInfo);
        }
        mainFrame.showPanel("result"); // Main.java에서 "result"라는 이름으로 등록해야 함
    }

    public void saveBooking(Booking booking) {
        // 매니저가 실제로 저장
        if (bookingManager != null) {
            bookingManager.save(booking);
        }
    }


    public void setBookingManager(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }
    public void searchBooking(String bookingId) {
        if (bookingManager == null) {
            System.out.println("[ERROR] BookingManager가 설정되지 않았습니다.");
            return;
        }

        Booking booking = bookingManager.findById(bookingId);

        if (booking != null) {
            showReservationResultView(booking);
        } else {
            JOptionPane.showMessageDialog(mainFrame,
                    "해당 예약 번호를 찾을 수 없습니다.",
                    "조회 실패",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
