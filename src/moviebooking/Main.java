package moviebooking;

import javax.swing.SwingUtilities;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;
import moviebooking.model.Screen;
import moviebooking.model.Screening;
import moviebooking.service.MovieManager;
import moviebooking.service.ScreenManager;
import moviebooking.service.ScreeningFactory;
import moviebooking.service.ScreeningManager;
import moviebooking.view.GUIMain;

public class Main {
    public static void main(String[] args) {
        ScreeningFactory screeningFactory = new ScreeningFactory();
        MovieManager movieManager = new MovieManager();
        ScreenManager screenManager = new ScreenManager();
        ScreeningManager screeningManager = new ScreeningManager(screeningFactory,movieManager,screenManager);

        movieManager.loadData("movies.txt");
        screenManager.loadData("screens.txt");
        screeningManager.loadData("screenings.txt");

        SwingUtilities.invokeLater(() -> {
            new GUIMain(movieManager, screenManager, screeningManager);
        });
    }
}
