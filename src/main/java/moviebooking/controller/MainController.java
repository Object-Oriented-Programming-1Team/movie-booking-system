package main.java.moviebooking.controller;

import main.java.moviebooking.model.Movie;
import main.java.moviebooking.model.Screening;
import main.java.moviebooking.view.BookMoviePanel;
import main.java.moviebooking.view.MainFrame;
import main.java.moviebooking.view.TimeSelectPanel;

public class MainController {

    private MainFrame mainFrame;
    private BookMoviePanel bookMoviePanel;
    private TimeSelectPanel timeSelectPanel;

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
    //추가 : 결제 화면으로 넘어갈 때 사용되는 메서드
    public void showPaymentView(Reservation reservation) {
        // 현재 전달되는 데이터 목록
        // - reservation.getMovie().getMovieTitle()  → 영화 제목
        // - reservation.getScreening().getScreen().getScreenName() → 상영관 이름
        // - reservation.getScreening().getStartTime() → 상영 시간
        // - reservation.getSeatDisplayText() → 선택한 좌석들 (커플석 포함)
        // - reservation.getTotalPrice() → 총 금액


        // PayPanel 연결
    }
}
