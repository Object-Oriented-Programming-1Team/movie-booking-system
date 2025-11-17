package moviebooking.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import moviebooking.common.GuiConstants;
import moviebooking.controller.MainController;
import moviebooking.model.Movie;
import moviebooking.model.Screening;
import moviebooking.service.MovieManager;
import moviebooking.service.ScreenManager;
import moviebooking.service.ScreeningManager;

public class BookMoviePanel extends JPanel implements GuiConstants {
    
    // Service & Controller
    private final MovieManager movieManager;
    private final ScreenManager screenManager;
    private final ScreeningManager screeningManager;
    private final MainController mainController;

    // Data (setMovie로 주입됨)
    private Movie movie; 

    // Components (updatePanelInfo에서 갱신해야 할 컴포넌트들)
    private JLabel posterLabel;
    private JLabel titleLabel;
    private JLabel summaryDetailLabel;
    private JLabel dateValueLabel;
    private JTextArea plotArea;
    
    // Constants
    private final int infoFontSize = 20;
    private final int titleFontSize = 40;

    /**
     * '영화 상세' 패널 (리팩토링된 구조)
     * Main.java에서 '빈 껍데기'로 생성됩니다.
     */
    public BookMoviePanel(MainController mainController, MovieManager movieManager, ScreenManager screenManager, ScreeningManager screeningManager) {
        this.mainController = mainController;
        this.movieManager = movieManager;
        this.screenManager = screenManager;
        this.screeningManager = screeningManager;

        // JPanel 자체의 설정
        setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20)); // 프레임의 BorderLayout
    
        // 1. '빈 껍데기' 패널들을 미리 조립
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        updatePanelInfo(); // 갱신
    }

    /**
     * 'movie' 필드의 정보로 모든 컴포넌트의 텍스트와 이미지를 갱신합니다.
     */
    private void updatePanelInfo(){
        if (movie == null) return;

        try {
            String imagePath = "movie_images/" + movie.getPosterUrl();
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            ImageIcon originalIcon;
            if (imageUrl != null) {
                originalIcon = new ImageIcon(imageUrl);
            } else {
                throw new Exception("Image not found: " + imagePath);
            }
            Image resizedImage = originalIcon.getImage().getScaledInstance(298, 420, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(resizedImage));
            posterLabel.setText(null);
        } catch (Exception e) {
            posterLabel.setIcon(null);
            posterLabel.setText(movie.getMovieTitle() + " (이미지 로드 실패)");
            posterLabel.setForeground(Color.WHITE);
        }

        // 2. 텍스트 갱신
        titleLabel.setText(movie.getMovieTitle());
        summaryDetailLabel.setText(movie.getRating()+" , "+movie.getGenre()+" , "+movie.getRuntime()+"분");
        dateValueLabel.setText(dateTimeFormatter(movie));
        
        // TODO: 줄거리 텍스트 추가 필요. 현재는 임의의 줄거리 입력했음
        String plotText = "나의 믿은 좀비다. 이 세상 마지막 남은 유일한 좀비!\n\n"
                        + "댄스 열정을 불태우는 사춘기 딸 '수아'와 함께 티격태격 일상을 보내는 맹수 전문 사육사 '정환'.\n"
                        + "어느 날 갑자기 정환 앞에 나타난 좀비 '좀비'.\n"
                        + "처음엔 경계하던 정환도 점차 좀비의 순수한 매력에 빠져들고,\n";
        plotArea.setText(plotText);
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
            mainController.showMovieListView();
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
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));

        bottomPanel.add(createBookButton(), BorderLayout.EAST);
        bottomPanel.add(createPlotTextArea(), BorderLayout.CENTER);
        return bottomPanel;
    }

    private JButton createBookButton() {
        JButton bookButton = new JButton("예매하기");
        bookButton.setBackground(DARK_BLUE_COLOR);
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        bookButton.setPreferredSize(new Dimension(300, 80));
        bookButton.setOpaque(true);
        bookButton.setBorderPainted(false);
        bookButton.addActionListener(e -> {
            //TODO: Controller를 통해 좌석선택화면으로 이동
            // mainController.showSeatSelectionView(movie);
            System.out.println("좌석 선택 화면으로 이동: " + movie.getMovieTitle());
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
        informationPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 200, 0));

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
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
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
        plotScrollPane.setPreferredSize(new Dimension(400, 100)); // 크기 고정
        plotScrollPane.setBorder(null);
        plotScrollPane.getViewport().setOpaque(false);
        plotScrollPane.setOpaque(false);
        plotScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 150, 0, 0));

        return plotScrollPane;
    }

    private String dateTimeFormatter(Movie movie) {
        Screening firstScreening = screeningManager.findScreeningsByMovieId(movie.getMovieId());
        
        if(firstScreening == null) {
            System.out.println("[WARNING] BookMoviePanel: 상영 정보가 없습니다. movieId: " + movie.getMovieId());
            return "상영 정보 없음";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return firstScreening.getStartTime().format(formatter);
    }
}