package main.java.moviebooking.view;

import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Movie;
import main.java.moviebooking.model.Screening;
import main.java.moviebooking.service.ScreeningManager;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class TimeSelectPanel extends JPanel implements GuiConstants {

    private final MainController mainController;
    private final ScreeningManager screeningManager;

    private Movie movie;
    private JPanel timeButtonPanel;
    private JLabel titleLabel;

    public TimeSelectPanel(MainController mainController, ScreeningManager screeningManager) {
        this.mainController = mainController;
        this.screeningManager = screeningManager;

        setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);

    }

    public void setMovie(Movie movie){
        this.movie = movie;
        updatePanelInfo();
    }

    private void updatePanelInfo(){
        if (movie == null) return;

        titleLabel.setText(movie.getMovieTitle() + " 상영 시간표");

        //1. 시간표 초기화
        timeButtonPanel.removeAll();

        List<Screening> screenings = screeningManager.findScreeningsByMovieId(this.movie.getMovieId());
        if(screenings.isEmpty()){
            JLabel noScreeningLabel = new JLabel("상영 정보가 없습니다.");
            noScreeningLabel.setForeground(Color.WHITE);
            noScreeningLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 20));
            noScreeningLabel.setHorizontalAlignment(SwingConstants.CENTER);
            timeButtonPanel.add(noScreeningLabel);
        }else{
            for (Screening screening : screenings){
                JButton timeButton = createTimeButton(screening);
                timeButtonPanel.add(timeButton);
            }
        }

        timeButtonPanel.revalidate();
        timeButtonPanel.repaint();
    }
    private JButton createTimeButton(Screening screening){
        String timeText = screening.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String screenName = screening.getScreen().getScreenName();

        JButton button = new JButton("<html><center>" + timeText + "<br>" + screenName + "</center></html>");
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font(KOREAN_FONT, Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 80));
        button.setOpaque(true);
        button.setBorderPainted(false);

        button.addActionListener(e ->{
            // TODO: 다음 단계 - 좌석 선택 화면으로 이동
            System.out.println("상영 선택됨: " + screening.getScreeningId());
        });
        return button;
    }

    private JPanel createTopPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        JButton backButton = new JButton("◀");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.GRAY);
        backButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            // 영화 상세 화면으로 돌아가기
            mainController.showBookMovieView(movie);
        });
        titleLabel = new JLabel("상영 시간 선택");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // 레이아웃 균형을 위한 빈 라벨
        JLabel emptyLabel = new JLabel("   ");
        emptyLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        topPanel.add(emptyLabel, BorderLayout.EAST);
        return topPanel;
    }

    private JScrollPane createCenterPanel(){
        timeButtonPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // 3열
        timeButtonPanel.setBackground(Color.BLACK);
        timeButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JScrollPane scrollPane = new JScrollPane(timeButtonPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.BLACK);

        return scrollPane;
    }
}
