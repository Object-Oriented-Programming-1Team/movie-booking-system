package moviebooking.view;

import moviebooking.common.GuiConstants;
import moviebooking.controller.MainController;
import moviebooking.model.Movie;
import moviebooking.service.MovieManager;
import moviebooking.service.ScreenManager;
import moviebooking.service.ScreeningManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class MovieListPanel extends JPanel implements GuiConstants {
    private final MovieManager movieManager;
    private final ScreenManager screenManager;
    private final ScreeningManager screeningManager;
    private final MainController mainController;

    private final List<Movie> allMovies;
    private List<Movie> selectedMovies;
    private int currentPage = 0;
    private static final int moviesPerPage = 4;

    private JPanel movieDisplayPanel;

    public MovieListPanel(MovieManager movieManager, ScreenManager screenManager, ScreeningManager screeningManager, MainController mainController) {
        this.movieManager = movieManager;
        this.screenManager = screenManager;
        this.screeningManager = screeningManager;
        this.allMovies = this.movieManager.findAll();
        this.selectedMovies = new ArrayList<>(this.allMovies);
        this.mainController = mainController;

        setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        updateMovieDisplay();
    }

    private JPanel createTopPanel() {
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
                    selectedMovies = new ArrayList<>(allMovies);
                } else{
                    try {
                        LocalDate date = LocalDate.parse(keyword);
                        selectedMovies = screeningManager.findMoviesByDate(date);
                    } catch (DateTimeParseException ex) {
                        selectedMovies = movieManager.searchMoviesByTitleOrGenre(keyword);
                    }
                }
                currentPage = 0;
                updateMovieDisplay();
            }
        });

        topPanel.add(searchField, BorderLayout.CENTER);

        JButton checkReservationsField = new JButton("예매 조회하기");
        checkReservationsField.setBackground(RED_COLOR);
        checkReservationsField.setForeground(Color.WHITE);
        checkReservationsField.setFont(new Font(KOREAN_FONT, Font.BOLD, 20));

        // ✅ 1. macOS 버그 수정을 위해 Opaque(불투명) 설정
        checkReservationsField.setOpaque(true);
        // ✅ 2. 테두리를 없애서 색상이 꽉 차게 함
        checkReservationsField.setBorderPainted(false);

        checkReservationsField.addActionListener(e -> {
            mainController.showCheckReservationsView();
        });
        topPanel.add(checkReservationsField, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel topPanelOnCenter = new JPanel();
        topPanelOnCenter.setLayout(new BoxLayout(topPanelOnCenter, BoxLayout.Y_AXIS));
        topPanelOnCenter.setBackground(Color.BLACK);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("Movies");
        titleLabel.setForeground(RED_COLOR);
        titleLabel.setFont(new Font(ENGLISH_FONT, Font.BOLD, 70));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);

        
        JPanel nowShowingAndButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 200, 0));
        nowShowingAndButtonPanel.setBackground(Color.BLACK);

        JLabel nowShowingLabel = new JLabel("Now Showing");
        nowShowingLabel.setForeground(Color.WHITE);
        nowShowingLabel.setFont(new Font(ENGLISH_FONT, Font.BOLD, 50));
        nowShowingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nowShowingAndButtonPanel.add(nowShowingLabel,BorderLayout.WEST);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.BLACK);
        emptyPanel.setPreferredSize(new Dimension(200,0));
        nowShowingAndButtonPanel.add(emptyPanel,BorderLayout.CENTER);

        JButton showAllButton = new JButton("전체보기");
        showAllButton.setBackground(Color.WHITE);
        showAllButton.setForeground(Color.BLACK);
        showAllButton.setFont(new Font(KOREAN_FONT, Font.PLAIN, 20));

        // ✅ 1. macOS 버그 수정을 위해 Opaque(불투명) 설정
        showAllButton.setOpaque(true);
        // ✅ 2. 테두리를 없애서 색상이 꽉 차게 함
        showAllButton.setBorderPainted(false);

        showAllButton.addActionListener(e -> {
            mainController.ShowAllMoviesView();
        });
        nowShowingAndButtonPanel.add(showAllButton,BorderLayout.EAST);
        
        topPanelOnCenter.add(titlePanel);
        topPanelOnCenter.add(nowShowingAndButtonPanel);

        centerPanel.add(topPanelOnCenter, BorderLayout.NORTH);

        movieDisplayPanel = new JPanel(new GridLayout(1, moviesPerPage, 20, 0));
        movieDisplayPanel.setBackground(Color.BLACK);
        centerPanel.add(movieDisplayPanel, BorderLayout.CENTER);

        JButton prevButton = new JButton("◀");
        prevButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        prevButton.setBackground(Color.BLACK);
        prevButton.setForeground(Color.GRAY);

        // ✅ 1. macOS 버그 수정을 위해 Opaque(불투명) 설정
        prevButton.setOpaque(true);
        // ✅ 2. 테두리를 없애서 색상이 꽉 차게 함
        prevButton.setBorderPainted(false);

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updateMovieDisplay();
            }
        });
        centerPanel.add(prevButton, BorderLayout.WEST);

        JButton nextButton = new JButton("▶");
        nextButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        nextButton.setBackground(Color.BLACK);
        nextButton.setForeground(Color.GRAY);

        // ✅ 1. macOS 버그 수정을 위해 Opaque(불투명) 설정
        nextButton.setOpaque(true);
        // ✅ 2. 테두리를 없애서 색상이 꽉 차게 함
        nextButton.setBorderPainted(false);

        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * moviesPerPage < selectedMovies.size()) {
                currentPage++;
                updateMovieDisplay();
            }
        });
        centerPanel.add(nextButton, BorderLayout.EAST);

        return centerPanel;
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

    private void updateMovieDisplay() {
        movieDisplayPanel.removeAll();

        int startIndex = currentPage * moviesPerPage;
        int endIndex = Math.min(startIndex + moviesPerPage, selectedMovies.size());

        for (int i = startIndex; i < endIndex; i++) {
            Movie movie = selectedMovies.get(i);
            JPanel movieCard = createMovieCard(movie);
            movieDisplayPanel.add(movieCard);
        }

        int emptySlots = moviesPerPage - (endIndex - startIndex);
        for (int i = 0; i < emptySlots; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(Color.BLACK);
            movieDisplayPanel.add(emptyPanel);
        }

        movieDisplayPanel.revalidate();
        movieDisplayPanel.repaint();
    }

    private JPanel createMovieCard(Movie movie) {
        JPanel cardPanel = new JPanel(new BorderLayout(0, 15));
        cardPanel.setBackground(Color.BLACK);

        JLabel posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(210, 298));

        try {
            String imagePath = "movie_images/" + movie.getPosterUrl();
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);

            ImageIcon originalIcon;
            if (imageUrl != null) {
                originalIcon = new ImageIcon(imageUrl);
            } else {
                throw new Exception("Image not found: " + imagePath);
            }

            Image resizedImage = originalIcon.getImage().getScaledInstance(210, 298, Image.SCALE_SMOOTH);
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

        // ✅ 1. macOS 버그 수정을 위해 Opaque(불투명) 설정
        bookButton.setOpaque(true);
        // ✅ 2. 테두리를 없애서 색상이 꽉 차게 함
        bookButton.setBorderPainted(false);

        bookButton.addActionListener(e -> {
            mainController.showBookMovieView(movie);
        });
        cardPanel.add(bookButton, BorderLayout.SOUTH);

        return cardPanel;
    }
}