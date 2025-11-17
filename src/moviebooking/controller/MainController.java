package moviebooking.controller;

import moviebooking.model.Movie;
import moviebooking.view.BookMoviePanel;
import moviebooking.view.MainFrame;

public class MainController {

    private MainFrame mainFrame;
    private BookMoviePanel bookMoviePanel;

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

    public void ShowAllMoviesView(){
        mainFrame.showPanel("allMovies");
    }
}
