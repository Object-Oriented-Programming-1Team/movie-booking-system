package moviebooking.view;

import moviebooking.model.Movie;
import moviebooking.service.MovieManager;
import moviebooking.service.ScreenManager;
import moviebooking.service.ScreeningManager;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Flow;

public class GUIMain extends BaseFrame {
    private final MovieManager movieManager;
    private final ScreenManager screenManager;
    private final ScreeningManager screeningManager;

    private final List<Movie> movieList;
    private int currentPage = 0;
    private static final int moviesPerPage = 4;

    private JPanel movieDisplayPanel;

    public GUIMain(MovieManager movieManager, ScreenManager screenManager, ScreeningManager screeningManager) {
        this.movieManager = movieManager;
        this.screenManager = screenManager;
        this.screeningManager = screeningManager;
        this.movieList = this.movieManager.findAll();

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
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(Color.BLACK);
        searchPanel.setForeground(Color.WHITE);

        JLabel searchLabel = new JLabel("검색");
        searchLabel.setBackground(Color.BLACK);
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font(koreanFont, Font.BOLD, 20));

        JTextField searchField = new JTextField();
        searchField.setBackground(Color.BLACK);
        searchField.setForeground(Color.WHITE);
        searchField.setBorder(new LineBorder(Color.WHITE, 1));
        searchField.setPreferredSize(new Dimension(400, 40));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        topPanel.add(searchPanel, BorderLayout.CENTER);

        JButton checkReservationsField = new JButton("예매 조회하기");
        checkReservationsField.setBackground(redColorRGB);
        checkReservationsField.setForeground(Color.WHITE);
        checkReservationsField.setFont(new Font(koreanFont, Font.BOLD, 20));
        checkReservationsField.addActionListener(e -> {
            // TODO: CheckReservations 클래스 구현 필요
            // 윈도우 창 새로 띄울지, 그냥 화면 자체를 넘길지 고민 필요
            CheckBooksFrame checkReservationsFrame = new CheckBooksFrame();
            checkReservationsFrame.setVisible(true);
        });
        topPanel.add(checkReservationsField, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel topPanelOnCenter = new JPanel();
        topPanelOnCenter.setLayout(new BoxLayout(topPanelOnCenter, BoxLayout.Y_AXIS));
        topPanelOnCenter.setBackground(Color.BLACK);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        JLabel titleLabel = new JLabel("Movies");
        titleLabel.setForeground(redColorRGB);
        titleLabel.setFont(new Font(englishFont, Font.BOLD, 90));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);

        JPanel nowShowingAndButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 200, 0));
        nowShowingAndButtonPanel.setBackground(Color.BLACK);

        JLabel nowShowingLabel = new JLabel("Now Showing");
        nowShowingLabel.setForeground(Color.WHITE);
        nowShowingLabel.setFont(new Font(englishFont, Font.BOLD, 50));
        nowShowingLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nowShowingAndButtonPanel.add(nowShowingLabel,BorderLayout.WEST);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.BLACK);
        emptyPanel.setPreferredSize(new Dimension(200,0));
        nowShowingAndButtonPanel.add(emptyPanel,BorderLayout.CENTER);

        JButton showAllButton = new JButton("전체보기");
        showAllButton.setBackground(Color.WHITE);
        showAllButton.setForeground(Color.BLACK);
        showAllButton.setFont(new Font(koreanFont, Font.PLAIN, 20));
        showAllButton.addActionListener(e -> {
            ShowAllMoviesFrame showAllMoviesFrame = new ShowAllMoviesFrame(movieManager, screenManager, screeningManager);
            showAllMoviesFrame.setVisible(true);
        });
        nowShowingAndButtonPanel.add(showAllButton,BorderLayout.EAST);
        
        topPanelOnCenter.add(titlePanel);
        topPanelOnCenter.add(nowShowingAndButtonPanel);
        
        centerPanel.add(topPanelOnCenter, BorderLayout.NORTH);

        // 4개 영화카드가 들어가는 패널
        movieDisplayPanel = new JPanel(new GridLayout(1, 4, 20, 10));
        movieDisplayPanel.setBackground(Color.BLACK);
        centerPanel.add(movieDisplayPanel, BorderLayout.CENTER);

        JButton prevButton = new JButton("◀");
        prevButton.setFont(new Font(koreanFont, Font.BOLD, 30));
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
        nextButton.setFont(new Font(koreanFont, Font.BOLD, 30));
        nextButton.setBackground(Color.BLACK);
        nextButton.setForeground(Color.GRAY);
        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * moviesPerPage < movieList.size()) {
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
        getTicketButton.setBackground(darkBlueColorRGB);
        getTicketButton.setForeground(redColorRGB);
        getTicketButton.setFont(new Font(englishFont, Font.PLAIN, 25));
        bottomPanel.add(getTicketButton);
        return bottomPanel;
    }

    private void updateMovieDisplay() {
        movieDisplayPanel.removeAll(); // 패널 페이지 갱신을 위해 초기화

        int startIndex = currentPage * moviesPerPage;
        int endIndex = Math.min(startIndex + moviesPerPage, movieList.size()); // 마지막 페이지에서 남는 영화 처리

        for (int i = startIndex; i < endIndex; i++) {
            Movie movie = movieList.get(i);
            JPanel movieCard = createMovieCard(movie); // 영화 카드 생성
            movieDisplayPanel.add(movieCard);
        }

        // moviesPerPage개가 안될 경우 빈 패널로 채워 레이아웃 유지
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
        JPanel cardPanel = new JPanel(new BorderLayout(0, 10));
        cardPanel.setBackground(Color.BLACK);

        JLabel posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(210, 298));
        try {
            ImageIcon originalIcon = new ImageIcon("resources/movie_images/"+movie.getPosterUrl());
            if(originalIcon.getIconWidth() == -1) {
                throw new Exception("Image not found");
            }
            //147x210 A8비율로 사이즈 조정
            //210x298 A6비율로 사이즈 조정
            Image resizedImage = originalIcon.getImage().getScaledInstance(210, 298, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            posterLabel.setText(movie.getMovieTitle() + " (이미지 로드 실패)");
            posterLabel.setForeground(Color.WHITE);
        }

        cardPanel.add(posterLabel, BorderLayout.CENTER);

        JButton bookButton = new JButton("예매하기");
        bookButton.setBackground(redColorRGB);
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font(koreanFont, Font.BOLD, 14));
        bookButton.addActionListener(e -> {
            BookMovieFrame bookMovie = new BookMovieFrame(this, movieManager, screenManager, screeningManager, movie);
            bookMovie.setVisible(true);
        });
        cardPanel.add(bookButton, BorderLayout.SOUTH);

        return cardPanel;
    }
}
