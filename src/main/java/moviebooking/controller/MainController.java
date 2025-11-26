package main.java.moviebooking.controller;

import main.java.moviebooking.model.*;
import main.java.moviebooking.service.BookingManager;
import main.java.moviebooking.view.*;

import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    // 기존: 단일 Booking을 받아서 처리
    public void showReservationResultView(Booking booking) {
        // 단일 객체를 리스트로 감싸서 전달
        List<Booking> list = new ArrayList<>();
        list.add(booking);
        showReservationResultList(list);
    }

    // 추가: Booking 리스트를 받아서 처리
    public void showReservationResultList(List<Booking> bookings) {
//        if (reservationResultPanel != null) {
//            reservationResultPanel.setBookingList(bookings);
//        }
        mainFrame.showPanel("result");
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
