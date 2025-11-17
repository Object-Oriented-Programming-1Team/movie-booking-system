package moviebooking;
import moviebooking.controller.MainController;
import moviebooking.service.MovieManager;
import moviebooking.service.ScreenManager;
import moviebooking.service.ScreeningFactory;
import moviebooking.service.ScreeningManager;
import moviebooking.view.*;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // 1. Service/Factory 생성 (동일)
        MovieManager movieManager = new MovieManager();
        ScreenManager screenManager = new ScreenManager();
        ScreeningFactory screeningFactory = new ScreeningFactory();
        ScreeningManager screeningManager = new ScreeningManager(screeningFactory, movieManager, screenManager);

        // 2. 데이터 로드 (동일)
        movieManager.loadData("movies.txt");
        screenManager.loadData("screens.txt");
        screeningManager.loadData("screenings.txt");

        // 3. GUI 조립
        SwingUtilities.invokeLater(() -> {

            // 3.1. 유일한 '창' 생성
            MainFrame mainFrame = new MainFrame();

            // 3.2. '화면 전환기' 생성 (창 주입)
            MainController mainController = new MainController(mainFrame);

            // 3.3. '내용물(패널)'들 생성 (Controller와 Manager 주입)
            MovieListPanel movieListPanel = new MovieListPanel(movieManager, screenManager, screeningManager,mainController);
            BookMoviePanel bookMoviePanel = new BookMoviePanel(mainController, movieManager, screenManager, screeningManager);
            CheckReservationsPanel checkReservationsPanel = new CheckReservationsPanel(mainController);

            // 3.4. Controller가 Panel들을 알도록 주입 (필요시)
            mainController.setBookMoviePanel(bookMoviePanel);

            // 3.5. '창'에 '내용물'들을 '카드'로 등록
            mainFrame.addPanel(movieListPanel, "movieList");
            mainFrame.addPanel(bookMoviePanel, "bookMovie");
            mainFrame.addPanel(checkReservationsPanel, "checkReservations");

            // 3.6. 프로그램 시작
            mainFrame.setVisible(true);
            mainController.showMovieListView(); // 첫 화면은 "movieList"
        });
    }
}