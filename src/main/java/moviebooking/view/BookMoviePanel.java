package main.java.moviebooking.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;

import javax.swing.*; // (필요한 import 추가)

import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Movie;
import main.java.moviebooking.service.MovieManager;
import main.java.moviebooking.service.ScreenManager;
import main.java.moviebooking.service.ScreeningManager;

public class BookMoviePanel extends JPanel implements GuiConstants {
    // Service & Controller
    private final MovieManager movieManager;
    private final ScreenManager screenManager;
    private final ScreeningManager screeningManager;
    private final MainController mainController;

    // Data
    private Movie movie; // 'setMovie' 메소드로 값이 주입될 필드

    // Components (내용이 바뀌어야 하므로 필드로 선언)
    private JLabel posterLabel;
    private JLabel titleLabel;
    private JLabel summaryDetailLabel;
    private JLabel openingDetailLabel;

    /**
     * '영화 상세' 패널 생성자
     * @param mainController 화면 전환을 담당할 컨트롤러
     * @param movieManager (필요시)
     * @param screenManager (필요시)
     * @param screeningManager (필요시)
     */
    public BookMoviePanel(MainController mainController, MovieManager movieManager, ScreenManager screenManager, ScreeningManager screeningManager) {
        this.mainController = mainController;
        this.movieManager = movieManager;
        this.screenManager = screenManager;
        this.screeningManager = screeningManager;
        // (생성자에서 Movie 객체 주입 제거 - setMovie()로 받음)

        // JPanel 자체의 설정
        setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20));

        // 1. 컴포넌트들을 '빈 껍데기' 상태로 생성 및 배치
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        // (setVisible, setSize 등 JFrame 관련 코드 모두 제거)
    }

    /**
     * MainController가 '예매하기' 버튼 클릭 시 호출하는 메소드.
     * 이 패널에 표시할 영화 정보를 설정하고 화면을 갱신합니다.
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
        updatePanelInfo();
    }

    /**
     * setMovie()로 주입받은 movie 객체의 정보로
     * 이미 생성된 JLabel 컴포넌트들의 내용을 갱신합니다.
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

        // 2. 텍스트 라벨 갱신
        titleLabel.setText(movie.getMovieTitle());
        summaryDetailLabel.setText(movie.getRating()+" , "+movie.getGenre()+" , "+movie.getRuntime()+"분");
        openingDetailLabel.setText("2024-01-01"); // (개봉일 정보는 임시)
    }

    /**
     * 뒤로 가기 버튼이 있는 상단 패널 생성
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK); // 배경색 설정
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20)); // 패딩 추가

        JButton backButton = new JButton("◀");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.GRAY);
        backButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));

        // ✅ 1. macOS 버그 수정을 위해 Opaque(불투명) 설정
        backButton.setOpaque(true);
        // ✅ 2. 테두리를 없애서 색상이 꽉 차게 함
        backButton.setBorderPainted(false);

        backButton.addActionListener(e -> {
            // "뒤로 가기"는 Controller에게 요청
            mainController.showMovieListView();
        });

        topPanel.add(backButton, BorderLayout.WEST);
        return topPanel;
    }

    /**
     * 포스터와 영화 정보가 들어가는 중앙 패널 생성
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(40, 0)); // 포스터와 정보 간 간격
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        // 1. 포스터 라벨 (필드) 생성
        posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(298, 420));
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(posterLabel, BorderLayout.WEST);

        // 2. 영화 정보 패널 (정보 패널) 생성
        JPanel informationPanel = new JPanel();
        informationPanel.setBackground(Color.BLACK);
        informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.Y_AXIS)); // 세로로 쌓기

        // 2-1. 제목 라벨 (필드)
        titleLabel = new JLabel("Loading...");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        informationPanel.add(titleLabel);

        informationPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격

        // 2-2. 개요 정보
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        summaryPanel.setBackground(Color.BLACK);

        JLabel summaryLabel = new JLabel("개요: ");
        summaryLabel.setForeground(Color.GRAY);
        summaryLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 20));

        summaryDetailLabel = new JLabel("..."); // (필드)
        summaryDetailLabel.setForeground(Color.WHITE);
        summaryDetailLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 20));

        summaryPanel.add(summaryLabel);
        summaryPanel.add(summaryDetailLabel);
        informationPanel.add(summaryPanel);

        // 2-3. 개봉일 정보
        JPanel openingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        openingPanel.setBackground(Color.BLACK);

        JLabel openingLabel = new JLabel("개봉일: ");
        openingLabel.setForeground(Color.GRAY);
        openingLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 20));

        openingDetailLabel = new JLabel("..."); // (필드)
        openingDetailLabel.setForeground(Color.WHITE);
        openingDetailLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 20));

        openingPanel.add(openingLabel);
        openingPanel.add(openingDetailLabel);
        informationPanel.add(openingPanel);

        centerPanel.add(informationPanel, BorderLayout.CENTER);

        return centerPanel;
    }

    /**
     * '예매하기' 버튼이 있는 하단 패널 생성
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 40));

        JButton bookButton = new JButton("예매하기");
        bookButton.setBackground(DARK_BLUE_COLOR);
        bookButton.setForeground(RED_COLOR);
        bookButton.setFont(new Font(ENGLISH_FONT, Font.BOLD, 40));
        bookButton.addActionListener(e -> {
            // TODO: 다음 단계: Controller를 통해 좌석 선택 패널로 이동
            // mainController.showSeatSelectionView(screeningManager.findScreeningByMovie(movie));
            System.out.println("좌석 선택 화면으로 이동해야 함");
        });

        bottomPanel.add(bookButton);
        return bottomPanel;
    }
}