package main.java.moviebooking.controller;

import main.java.moviebooking.model.Movie;
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
}
