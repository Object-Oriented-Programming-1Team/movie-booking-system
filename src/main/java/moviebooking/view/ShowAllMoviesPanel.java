package main.java.moviebooking.view;

import java.awt.*;
import java.net.URL;
import java.util.List;

import javax.swing.*;

import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Movie;
import main.java.moviebooking.service.MovieManager;

public class ShowAllMoviesPanel extends JPanel implements GuiConstants {
    
    private final MainController mainController;
    private final MovieManager movieManager;

    public ShowAllMoviesPanel(MainController mainController, MovieManager movieManager) {
        this.mainController = mainController;
        this.movieManager = movieManager;

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);
        add(createScrollableMovieGrid(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. 좌측: 뒤로가기 + 로고
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setBackground(Color.BLACK);
        
        JButton backButton = new JButton("◀");
        backButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));
        backButton.setForeground(Color.GRAY);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> mainController.showMovieListView());
        
        JLabel logoLabel = new JLabel("Logo");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font(ENGLISH_FONT, Font.BOLD, 30));

        leftPanel.add(backButton);
        leftPanel.add(logoLabel);

        JLabel titleLabel = new JLabel("Movies");
        titleLabel.setForeground(RED_COLOR);
        titleLabel.setFont(new Font(ENGLISH_FONT, Font.BOLD, 60));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton checkReservationsBtn = new JButton("예매 조회하기");
        checkReservationsBtn.setBackground(RED_COLOR);
        checkReservationsBtn.setForeground(Color.WHITE);
        checkReservationsBtn.setPreferredSize(new Dimension(170, 40));
        checkReservationsBtn.setFont(new Font(KOREAN_FONT, Font.BOLD, 20));
        checkReservationsBtn.setOpaque(true);
        checkReservationsBtn.setBorderPainted(false);
        checkReservationsBtn.addActionListener(e -> mainController.showCheckReservationsView());

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrapper.setBackground(Color.BLACK);
        buttonWrapper.add(checkReservationsBtn);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(buttonWrapper, BorderLayout.EAST);

        return topPanel;
    }

    private JScrollPane createScrollableMovieGrid() {
        List<Movie> allMovies = movieManager.findAll();

        // 그리드 패널 생성 (행 개수는 0으로 하여 자동 조절, 열은 5개)
        JPanel gridPanel = new JPanel(new GridLayout(0, 5, 20, 20));
        gridPanel.setBackground(Color.BLACK);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        for (Movie movie : allMovies) {
            gridPanel.add(createMovieCard(movie));
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        
        return scrollPane;
    }

    private JPanel createMovieCard(Movie movie) {
        JPanel cardPanel = new JPanel(new BorderLayout(0, 10));
        cardPanel.setBackground(Color.BLACK);

        // 포스터
        JLabel posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(200, 280));

        try {
            String imagePath = "main/java/resources/movie_images/" + movie.getPosterUrl();
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);

            ImageIcon originalIcon;
            if (imageUrl != null) {
                originalIcon = new ImageIcon(imageUrl);
            } else {
                throw new Exception("Image not found: " + imagePath);
            }

            Image resizedImage = originalIcon.getImage().getScaledInstance(200, 280, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            posterLabel.setText(movie.getMovieTitle() + " (이미지 로드 실패)");
            posterLabel.setForeground(Color.WHITE);
        }
        cardPanel.add(posterLabel, BorderLayout.CENTER);

        JButton bookButton = new JButton("예매하기");
        bookButton.setBackground(RED_COLOR);
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 14));
        bookButton.setOpaque(true);
        bookButton.setBorderPainted(false);
        bookButton.addActionListener(e -> mainController.showBookMovieView(movie));
        cardPanel.add(bookButton, BorderLayout.SOUTH);

        return cardPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 30));

        JButton getTicketButton = new JButton("GET TICKET");
        getTicketButton.setBackground(DARK_BLUE_COLOR);
        getTicketButton.setForeground(RED_COLOR);
        getTicketButton.setFont(new Font(ENGLISH_FONT, Font.PLAIN, 25));
        bottomPanel.add(getTicketButton);
        return bottomPanel;
    }
}