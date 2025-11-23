package main.java.moviebooking.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Movie;
import main.java.moviebooking.service.MovieManager;
import main.java.moviebooking.service.ScreeningManager;

public class ShowAllMoviesPanel extends JPanel implements GuiConstants {
    
    private final MainController mainController;
    private final MovieManager movieManager;
    private final ScreeningManager screeningManager;

    private JPanel movieDisplayPanel;
    private List<Movie> selectedMovies;

    public ShowAllMoviesPanel(MainController mainController, MovieManager movieManager, ScreeningManager screeningManager) {
        this.mainController = mainController;
        this.movieManager = movieManager;
        this.screeningManager = screeningManager;

        this.selectedMovies = movieManager.findAll();

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);
        add(createScrollableMovieGrid(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        
        updateMovieDisplay();
    }

    private JPanel createTopPanel() {
        // MovieListPanel의 createTopPanel과 동일하게 구성
        JPanel topPanel = new JPanel(new BorderLayout(20, 0));
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        JLabel logoLabel = new JLabel("Logo");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font(ENGLISH_FONT, Font.BOLD, 30));
        topPanel.add(logoLabel, BorderLayout.WEST);

        final String placeholder = " search title, genre, or date (YYYY-MM-DD)";
        JTextField searchField = new JTextField(placeholder);
        searchField.setBackground(Color.BLACK);
        searchField.setForeground(Color.LIGHT_GRAY);
        searchField.setBorder(new LineBorder(Color.WHITE, 1));
        searchField.setPreferredSize(new Dimension(400, 40));

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(searchField.getText().equals(placeholder)) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if(searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.LIGHT_GRAY);
                    searchField.setText(placeholder);
                }
            }
        });

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String keyword = searchField.getText().trim();

                if(keyword.isEmpty() || keyword.equals(placeholder)) {
                    selectedMovies = movieManager.findAll();
                } else{
                    try {
                        LocalDate date = LocalDate.parse(keyword);
                        selectedMovies = screeningManager.findMoviesByDate(date);
                    } catch (DateTimeParseException ex) {
                        selectedMovies = movieManager.searchMoviesByTitleOrGenre(keyword);
                    }
                }
                updateMovieDisplay();
            }
        });

        topPanel.add(searchField, BorderLayout.CENTER);

        JButton checkReservationsField = new JButton("예매 조회하기");
        checkReservationsField.setBackground(RED_COLOR);
        checkReservationsField.setForeground(Color.WHITE);
        checkReservationsField.setFont(new Font(KOREAN_FONT, Font.BOLD, 20));
        checkReservationsField.setOpaque(true);
        checkReservationsField.setBorderPainted(false);

        checkReservationsField.addActionListener(e -> {
            mainController.showCheckReservationsView();
        });
        topPanel.add(checkReservationsField, BorderLayout.EAST);

        return topPanel;
    }

    private JScrollPane createScrollableMovieGrid() {
        movieDisplayPanel = new JPanel(new GridLayout(0, 5, 20, 40)); // 5열 고정
        movieDisplayPanel.setBackground(Color.BLACK);
        movieDisplayPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JScrollPane scrollPane = new JScrollPane(movieDisplayPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); //스크롤바 내리는 속도
        
        return scrollPane;
    }

    private void updateMovieDisplay() {
        movieDisplayPanel.removeAll();

        for (Movie movie : selectedMovies) {
            movieDisplayPanel.add(createMovieCard(movie));
        }
        
        if (selectedMovies.size() <= 0) {
             // 검색 결과가 없을 때
             JLabel noResultLabel = new JLabel("검색 결과가 없습니다.");
             noResultLabel.setForeground(Color.WHITE);
             noResultLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 20));
             noResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
             movieDisplayPanel.add(noResultLabel);
        }

        movieDisplayPanel.revalidate();
        movieDisplayPanel.repaint();
    }

    private JPanel createMovieCard(Movie movie) {
        JPanel cardPanel = new JPanel(new BorderLayout(0, 10));
        cardPanel.setBackground(Color.BLACK);

        JLabel posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(200, 280)); 

        try {
            String imagePath = "main/java/resources/movie_images/" + movie.getPosterUrl();
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image img = icon.getImage().getScaledInstance(200, 280, Image.SCALE_SMOOTH);
                posterLabel.setIcon(new ImageIcon(img));
            } else {
                posterLabel.setText("이미지 없음");
                posterLabel.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            posterLabel.setText("오류");
        }
        cardPanel.add(posterLabel, BorderLayout.CENTER);

        JButton bookButton = new JButton("예매하기");
        bookButton.setBackground(RED_COLOR);
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 14));
        bookButton.setOpaque(true);
        bookButton.setBorderPainted(false);
        
        bookButton.addActionListener(e -> {
            mainController.showBookMovieView(movie);
        });
        cardPanel.add(bookButton, BorderLayout.SOUTH);

        return cardPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 30));

        JButton getTicketButton = new JButton("GET TICKET");
        getTicketButton.setBackground(new Color(30, 52, 92, 200));
        getTicketButton.setForeground(RED_COLOR);
        getTicketButton.setFont(new Font(ENGLISH_FONT, Font.BOLD, 24));
        getTicketButton.setFocusPainted(false);
        
        bottomPanel.add(getTicketButton);
        return bottomPanel;
    }
}