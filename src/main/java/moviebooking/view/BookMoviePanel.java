package main.java.moviebooking.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*; // (필요한 import 추가)

import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Movie;
import main.java.moviebooking.model.Screening;

import main.java.moviebooking.service.MovieManager;
import main.java.moviebooking.service.ScreenManager;
import main.java.moviebooking.service.ScreeningManager;


public class BookMoviePanel extends JPanel implements GuiConstants {

    // Service & Controller
    private final MovieManager movieManager;
    private final ScreenManager screenManager;
    private final ScreeningManager screeningManager;
    private final MainController mainController;

    private Movie movie;

    private JLabel posterLabel;
    private JLabel titleLabel;
    private JLabel summaryDetailLabel;
    private JLabel dateValueLabel;
    private JTextArea plotArea;

    private final int infoFontSize = 20;
    private final int titleFontSize = 40;

    public BookMoviePanel(MainController mainController, MovieManager movieManager, ScreenManager screenManager, ScreeningManager screeningManager) {
        this.mainController = mainController;
        this.movieManager = movieManager;
        this.screenManager = screenManager;
        this.screeningManager = screeningManager;

        // JPanel 자체의 설정
        setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20)); // 프레임의 BorderLayout

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        updatePanelInfo();
    }

    /**
     * 'movie' 필드의 정보로 모든 컴포넌트의 텍스트와 이미지를 갱신합니다.
     */
    private void updatePanelInfo(){
        if (movie == null) return;

        // 1. 포스터 라벨 갱신 (ClassLoader 사용)
        try{
            String imagePath = "main/java/resources/movie_images/" + movie.getPosterUrl();
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            ImageIcon originalIcon;
            if (imageUrl != null) {
                originalIcon = new ImageIcon(imageUrl);
            } else {
                throw new Exception("Image not found: " + imagePath);
            }
            Image resizedImage = originalIcon.getImage().getScaledInstance(298, 420, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(resizedImage));
            posterLabel.setText(null); // 이미지 로드 성공 시 텍스트 제거
        } catch (Exception e) {
            posterLabel.setIcon(null); // 아이콘 제거
            posterLabel.setText(movie.getMovieTitle() + " (이미지 로드 실패)");
            posterLabel.setForeground(Color.WHITE);
        }

        titleLabel.setText(movie.getMovieTitle());
        summaryDetailLabel.setText(movie.getRating()+" , "+movie.getGenre()+" , "+movie.getRuntime()+"분");
        dateValueLabel.setText(dateTimeFormatter(movie));

        //줄거리
        plotArea.setText(movie.getPlot());

    }


    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        // BaseFrame의 메서드 대신 Controller를 사용
        JButton backButton = new JButton("◀");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.GRAY);
        backButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);

        backButton.addActionListener(e -> {
            mainController.showAllMoviesView();
        });
        topPanel.add(backButton, BorderLayout.WEST);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 0, 20));
        centerPanel.setBackground(Color.BLACK);

        centerPanel.add(createPosterPanel());
        centerPanel.add(createInformationPanel());
        return centerPanel;
    }

    private JPanel createBottomPanel() {
        // (사용자님의 기존 코드: 줄거리(CENTER)와 버튼(EAST))
        JPanel bottomPanel = new JPanel(new BorderLayout(50,20));
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 50));

        bottomPanel.add(createBookButton(), BorderLayout.EAST);
        //bottomPanel.add(createPlotTextArea(), BorderLayout.CENTER);
        return bottomPanel;
    }

    private JButton createBookButton() {
        JButton bookButton = new JButton("예매하기");
        bookButton.setBackground(DARK_BLUE_COLOR);
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        bookButton.setPreferredSize(new Dimension(300, 80));
        bookButton.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bookButton.setOpaque(true);
        bookButton.setBorderPainted(false);
        bookButton.addActionListener(e -> {
            //TODO: Controller를 통해 좌석선택화면으로 이동
            mainController.showTimeSelectView(movie);
        });
        return bookButton;
    }

    private JPanel createPosterPanel() {
        JPanel posterPanel = new JPanel();
        posterPanel.setBackground(Color.BLACK);

        // '빈 껍데기' 라벨 (필드) 생성
        posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(298, 420));
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);

        posterPanel.add(posterLabel);
        return posterPanel;
    }

    private JPanel createInformationPanel() {
        JPanel informationPanel = new JPanel();
        informationPanel.setBackground(Color.BLACK);
        informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.Y_AXIS));

        informationPanel.add(createTitlePanel());
        informationPanel.add(createSummaryPanel());
        informationPanel.add(createOpeningDatePanel());
        informationPanel.add(createPlotTextArea());
        //informationPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));

        return informationPanel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.setBackground(RED_COLOR);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT); // BoxLayout 왼쪽 정렬

        titleLabel = new JLabel("Loading...");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, titleFontSize));
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setBackground(Color.BLACK);
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // BoxLayout 왼쪽 정렬

        JLabel summaryLabel = new JLabel("개요: ");
        summaryLabel.setForeground(Color.GRAY);
        summaryLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, infoFontSize));

        summaryDetailLabel = new JLabel("...");
        summaryDetailLabel.setForeground(Color.WHITE);
        summaryDetailLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, infoFontSize));

        summaryPanel.add(summaryLabel);
        summaryPanel.add(summaryDetailLabel);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        return summaryPanel;
    }

    private JPanel createOpeningDatePanel() {
        JPanel openingDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        openingDatePanel.setBackground(Color.BLACK);
        openingDatePanel.setAlignmentX(Component.LEFT_ALIGNMENT); // BoxLayout 왼쪽 정렬

        JLabel openingDateLabel = new JLabel("개봉일: ");
        openingDateLabel.setForeground(Color.GRAY);
        openingDateLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, infoFontSize));

        dateValueLabel = new JLabel("...");
        dateValueLabel.setForeground(Color.WHITE);
        dateValueLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, infoFontSize));

        openingDatePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        openingDatePanel.add(openingDateLabel);
        openingDatePanel.add(dateValueLabel);
        return openingDatePanel;
    }

    private JScrollPane createPlotTextArea(){
        plotArea = new JTextArea("...");
        plotArea.setFont(new Font(KOREAN_FONT, Font.PLAIN, 15));
        plotArea.setBackground(Color.BLACK);
        plotArea.setForeground(Color.WHITE);
        plotArea.setEditable(false);
        plotArea.setLineWrap(true);
        plotArea.setWrapStyleWord(true);

        JScrollPane plotScrollPane = new JScrollPane(plotArea);
        plotScrollPane.setPreferredSize(new Dimension(350, 200)); // 크기 고정
        plotScrollPane.setBorder(null);
        plotScrollPane.getViewport().setOpaque(false);
        plotScrollPane.setOpaque(false);
        plotScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        plotScrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        return plotScrollPane;
    }

    private String dateTimeFormatter(Movie movie) {
        List<Screening> screeningsByMoiveId = screeningManager.findScreeningsByMovieId(movie.getMovieId());
        LocalDateTime firstScreeningDateTime=LocalDateTime.MAX;

        for(Screening screening:screeningsByMoiveId){
            LocalDateTime curDateTime = screening.getStartTime();
            if(curDateTime.isBefore(firstScreeningDateTime)){
                firstScreeningDateTime=curDateTime;
            }
        }

        if(firstScreeningDateTime == LocalDateTime.MAX) {
            System.out.println("[WARNING] BookMoviePanel: 상영 정보가 없습니다. movieId: " + movie.getMovieId());
            return "상영 정보 없음";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return firstScreeningDateTime.format(formatter);
    }
}