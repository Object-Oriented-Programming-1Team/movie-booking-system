package moviebooking.view;

import moviebooking.model.Movie;
import moviebooking.service.MovieManager;
import moviebooking.service.ScreenManager;
import moviebooking.service.ScreeningManager;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class GUIMain extends JFrame {
    private final MovieManager movieManager;
    private final ScreenManager screenManager;
    private final ScreeningManager screeningManager;

    private final List<Movie> allMovies;
    private List<Movie> selectedMovies;
    private final String englishFont = "Bernard MT Condensed";
    private int currentPage = 0;
    private static final int moviesPerPage = 4;

    private JPanel movieDisplayPanel;

    public GUIMain(MovieManager movieManager, ScreenManager screenManager, ScreeningManager screeningManager) {
        this.movieManager = movieManager;
        this.screenManager = screenManager;
        this.screeningManager = screeningManager;
        this.allMovies = this.movieManager.findAll();
        this.selectedMovies = new ArrayList<>(this.allMovies);


        setTitle("Movie Booking System");
        setSize(1280, 720); //16:9 비율
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        updateMovieDisplay();
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        JLabel logoLabel = new JLabel("Logo");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font(englishFont, Font.BOLD, 30));
        topPanel.add(logoLabel, BorderLayout.WEST);

        JTextField searchField = new JTextField(" search");
        searchField.setBackground(Color.BLACK);
        searchField.setForeground(Color.WHITE);
        searchField.setBorder(new LineBorder(Color.WHITE, 1));

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String keyword = searchField.getText().trim();

                if(keyword.isEmpty()){
                    selectedMovies = new ArrayList<>(allMovies);
                } else{
                    try {
                        // (1) 날짜로 파싱 시도 (예: 2025-11-15)
                        LocalDate date = LocalDate.parse(keyword);
                        // (2) 날짜 파싱 성공 시: ScreeningManager 호출
                        selectedMovies = screeningManager.findMoviesByDate(date);
                    } catch (DateTimeParseException ex) {
                        // (3) 날짜 파싱 실패 시: MovieManager (제목/장르) 호출
                        selectedMovies = movieManager.searchMoviesByTitleOrGenre(keyword);
                    }
                }

                currentPage = 0;
                updateMovieDisplay();
            }
        });
        topPanel.add(searchField, BorderLayout.CENTER);

        JButton checkReservationsField = new JButton("예매 조회하기");
        checkReservationsField.setBackground(Color.RED);
        checkReservationsField.setForeground(Color.WHITE);
        checkReservationsField.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        checkReservationsField.addActionListener(e -> {
            // TODO: CheckReservations 클래스 구현 필요
            // 윈도우 창 새로 띄울지, 그냥 화면 자체를 넘길지 고민 필요
            CheckReservations checkReservationsFrame = new CheckReservations();
            checkReservationsFrame.setVisible(true);
        });
        topPanel.add(checkReservationsField, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = new JLabel("Movies");
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font(englishFont, Font.BOLD, 70));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(titleLabel, BorderLayout.NORTH);

        // 4개 영화카드가 들어가는 패널
        movieDisplayPanel = new JPanel(new GridLayout(1, moviesPerPage, 20, 0));
        movieDisplayPanel.setBackground(Color.BLACK);
        centerPanel.add(movieDisplayPanel, BorderLayout.CENTER);

        JButton prevButton = new JButton("◀");
        prevButton.setFont(new Font("맑은 고딕", Font.BOLD, 50));
        prevButton.setBackground(Color.BLACK);
        prevButton.setForeground(Color.GRAY);
        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updateMovieDisplay();
            }
        });
        centerPanel.add(prevButton, BorderLayout.WEST);

        JButton nextButton = new JButton("▶");
        nextButton.setFont(new Font("맑은 고딕", Font.BOLD, 50));
        nextButton.setBackground(Color.BLACK);
        nextButton.setForeground(Color.GRAY);
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
        getTicketButton.setBackground(new Color(30, 52, 92));
        getTicketButton.setForeground(Color.RED);
        getTicketButton.setFont(new Font(englishFont, Font.PLAIN, 25));
        bottomPanel.add(getTicketButton);
        return bottomPanel;
    }

    private void updateMovieDisplay() {
        movieDisplayPanel.removeAll(); // 패널 페이지 갱신을 위해 초기화

        int startIndex = currentPage * moviesPerPage;
        int endIndex = Math.min(startIndex + moviesPerPage, selectedMovies.size());

        for (int i = startIndex; i < endIndex; i++) {
            Movie movie = selectedMovies.get(i);
            JPanel movieCard = createMovieCard(movie); // 영화 카드 생성
            movieDisplayPanel.add(movieCard);
        }

        // 4개가 안될 경우 빈 패널로 채워 레이아웃 유지
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
        try {
            String imagePath = "movie_images/" + movie.getPosterUrl();
            java.net.URL imageUrl = getClass().getClassLoader().getResource(imagePath);

            ImageIcon originalIcon;
            if (imageUrl != null) {
                originalIcon = new ImageIcon(imageUrl);
            } else {
                throw new Exception("Image not found: " + imagePath);
            }
            //210x298 A7 비율로 사이즈 조정
            Image resizedImage = originalIcon.getImage().getScaledInstance(210, 298, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            posterLabel.setText(movie.getMovieTitle() + " (이미지 로드 실패)");
            posterLabel.setForeground(Color.WHITE);
            posterLabel.setPreferredSize(new Dimension(298, 420));
        }
        cardPanel.add(posterLabel, BorderLayout.CENTER);

        JButton bookButton = new JButton("예매하기");
        bookButton.setBackground(Color.RED);
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        bookButton.addActionListener(e -> {
            this.setVisible(false);

            // TODO: 영화에 대한 정보들을 표시하는 page2 구현
            // 모든 매니저 객체와 선택한 Movie 객체를 모두 인자로 전달해야함
            
            // new TimeSelect(this, movieManager, screenManager, screeningManager, movie);
            
        });
        cardPanel.add(bookButton, BorderLayout.SOUTH);

        return cardPanel;
    }
}
