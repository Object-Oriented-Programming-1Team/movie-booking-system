package moviebooking.view;

import javax.swing.JFrame;

import moviebooking.service.MovieManager;
import moviebooking.service.ScreenManager;
import moviebooking.service.ScreeningManager;

public class ShowAllMoviesFrame extends BaseFrame {
    MovieManager movieManager;
    ScreenManager screenManager;
    ScreeningManager screeningManager;

    public ShowAllMoviesFrame(MovieManager movieManager, ScreenManager screenManager, ScreeningManager screeningManager) {
        this.movieManager = movieManager;
        this.screenManager = screenManager;
        this.screeningManager = screeningManager;

        this.setTitle("Show All Movies");
        this.setSize(1280, 720); //16:9 비율
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
