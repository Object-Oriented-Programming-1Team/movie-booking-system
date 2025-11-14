package moviebooking;

import moviebooking.common.Manageable;
import moviebooking.model.Movie;
import moviebooking.service.MovieManager;
import moviebooking.service.ScreenManager;
import moviebooking.service.ScreeningFactory;
import moviebooking.service.ScreeningManager;
import moviebooking.view.GUIMain;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        MovieManager movieManager = new MovieManager();
        ScreenManager screenManager = new ScreenManager();
        ScreeningFactory screeningFactory = new ScreeningFactory();

        ScreeningManager screeningManager = new ScreeningManager(
                screeningFactory,
                movieManager,
                screenManager
        );

        movieManager.loadData("movies.txt");

        // screenManager.loadData("screens.txt");
        // screeningManager.loadData("screenings.txt");

        // Swing GUI는 안정성을 위해 'Event Dispatch Thread'(EDT)에서 실행해야 합니다.
        SwingUtilities.invokeLater(() -> {

            // 5. GUI 객체를 생성하며, 데이터가 로드된 Manager들을 '주입'합니다.
            new GUIMain(movieManager, screenManager, screeningManager);
            // (GUIMain 생성자에서 setVisible(true)이 이미 호출됩니다)
        });
    }
}
