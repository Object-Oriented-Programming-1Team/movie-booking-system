package moviebooking.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.print.Book;
import java.util.concurrent.Flow;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import moviebooking.model.Movie;
import moviebooking.service.MovieManager;
import moviebooking.service.ScreenManager;
import moviebooking.service.ScreeningManager;

public class BookMovieFrame extends BaseFrame {
    MovieManager movieManager;
    ScreenManager screenManager;
    ScreeningManager screeningManager;
    Movie movie;
    GUIMain mainFrame;

    public BookMovieFrame(GUIMain mainFrame, MovieManager movieManager, ScreenManager screenManager, ScreeningManager screeningManager, Movie movie) {
        this.mainFrame = mainFrame;
        this.movieManager = movieManager;
        this.screenManager = screenManager;
        this.screeningManager = screeningManager;
        this.movie = movie;

        setTitle("Movie Information");
        setSize(1280, 720); //16:9 비율
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20));
    
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 0, 20));
        JButton backButton = mainFrame.createBackButton(BookMovieFrame.this);
        topPanel.add(backButton, BorderLayout.WEST);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(createPosterPanel(),BorderLayout.WEST);
        centerPanel.add(createInformationPanel(),BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JButton bookButton = new JButton("예매하기");
        bookButton.setBackground(new Color(31, 54, 166));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("맑은 고딕", Font.BOLD, 50));
        bookButton.setHorizontalAlignment(SwingConstants.RIGHT);
        bookButton.addActionListener(e -> {
            //TODO: 좌석선택화면으로 이동
            //좌석선택화면.setVisible(true);
        });

        bottomPanel.add(bookButton);
        return bottomPanel;
    }

    private JPanel createPosterPanel() {
        JPanel posterPanel = new JPanel();
        posterPanel.setBackground(Color.BLACK);
    
        JLabel posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(298, 420));
        
        try {
            ImageIcon originalIcon = new ImageIcon("resources/movie_images/"+movie.getPosterUrl());
            if(originalIcon.getIconWidth() == -1) {
                throw new Exception("Image not found");
            }
            //140x210 A8비율로 사이즈 조정
            Image resizedImage = originalIcon.getImage().getScaledInstance(298, 420, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            posterLabel.setText(movie.getMovieTitle() + " (이미지 로드 실패)");
            posterLabel.setForeground(Color.WHITE);
        }
        posterPanel.add(posterLabel);
        return posterPanel;
    }

    private JPanel createInformationPanel() {
        JPanel informationPanel = new JPanel();
        informationPanel.setBackground(Color.BLACK);
        informationPanel.setLayout(new BorderLayout(20,20));
        //제목 정보
        JPanel titlePanel = new JPanel();
        titlePanel.setSize(400, 200);
        titlePanel.setBackground(Color.RED);

        JLabel titleLabel = new JLabel(movie.getMovieTitle());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 50));

        titlePanel.add(titleLabel);

        //개요 정보
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setBackground(Color.BLACK);

        JLabel summaryLabel = new JLabel("개요: ");
        summaryLabel.setForeground(Color.GRAY);
        summaryLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        summaryLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel summaryDetaiLabel = new JLabel(movie.getRating()+" , "+movie.getGenre()+" , "+movie.getRuntime()+"분");
        summaryDetaiLabel.setForeground(Color.WHITE);
        summaryDetaiLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        summaryDetaiLabel.setHorizontalAlignment(SwingConstants.LEFT);

        summaryPanel.add(summaryLabel);
        summaryPanel.add(summaryDetaiLabel);

        //개봉일 정보
        JPanel openingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        openingPanel.setBackground(Color.BLACK);

        JLabel openingLabel = new JLabel("개봉일: ");
        openingLabel.setForeground(Color.GRAY);
        openingLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
  

        //Movie model과 입력파일에 개봉일 데이터가 없어서 임의로 설정
        JLabel openingDetaiLabel = new JLabel("2024-01-01"); // 예시 개봉일
        openingDetaiLabel.setForeground(Color.WHITE);
        openingDetaiLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
   
        openingPanel.add(openingLabel);
        openingPanel.add(openingDetaiLabel);

        informationPanel.add(titlePanel,BorderLayout.NORTH);
        informationPanel.add(summaryPanel,BorderLayout.CENTER);
        informationPanel.add(openingPanel,BorderLayout.SOUTH);

        return informationPanel;
    }
}
