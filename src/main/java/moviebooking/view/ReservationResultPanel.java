package main.java.moviebooking.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;

//  둥근 버튼 클래스
class RoundButton extends JButton {
    public RoundButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
        super.paintComponent(g);
    }
    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        g.drawRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
    }
}

public class ReservationResultPanel extends JPanel implements GuiConstants {

    private final MainController mainController;

    private JLabel bookingNumberLabel;
    private JLabel cinemaInfoLabel;
    private JLabel movieTitleLabel;
    private JLabel seatInfoLabel;

    public ReservationResultPanel(MainController mainController) {
        this.mainController = mainController;
        initUI();
    }

    private void initUI() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
    }

    private JComponent createTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.BLACK);
        top.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel logo = new JLabel("MVP");
        logo.setFont(new Font(ENGLISH_FONT, Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        top.add(logo, BorderLayout.WEST);

        JButton goMainButton = new JButton("메인으로");
        goMainButton.setBackground(RED_COLOR);
        goMainButton.setForeground(Color.WHITE);
        goMainButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 14));
        goMainButton.setOpaque(true);
        goMainButton.setBorderPainted(false);

        goMainButton.addActionListener(e -> mainController.showMovieListView());
        top.add(goMainButton, BorderLayout.EAST);

        return top;
    }

    private JComponent createCenterPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.BLACK);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        wrapper.add(Box.createVerticalStrut(20));

        // ---------------------------
        //  결제 완료 박스
        // ---------------------------
        JPanel donePanel = new JPanel();
        donePanel.setBackground(Color.BLACK);
        donePanel.setLayout(new BoxLayout(donePanel, BoxLayout.Y_AXIS));

        Dimension doneSize = new Dimension(520, 150);
        donePanel.setPreferredSize(doneSize);
        donePanel.setMaximumSize(doneSize);

        donePanel.setBorder(new CompoundBorder(
                new LineBorder(Color.WHITE, 2, true),
                new EmptyBorder(25, 40, 25, 40)
        ));

        JLabel doneLabel = new JLabel("결제 완료 !");
        doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        doneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        doneLabel.setForeground(Color.WHITE);
        doneLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));

        bookingNumberLabel = new JLabel("예약번호 : ");
        bookingNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookingNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookingNumberLabel.setForeground(Color.WHITE);
        bookingNumberLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 18));

        donePanel.add(doneLabel);
        donePanel.add(Box.createVerticalStrut(15));
        donePanel.add(bookingNumberLabel);

        JPanel doneWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        doneWrapper.setBackground(Color.BLACK);
        doneWrapper.add(donePanel);

        wrapper.add(doneWrapper);
        wrapper.add(Box.createVerticalStrut(25));

        // ---------------------------
        //  둥근 "예약내역" 버튼
        // ---------------------------
        RoundButton detailButton = new RoundButton("예약내역");
        detailButton.setBackground(RED_COLOR);
        detailButton.setForeground(Color.BLACK);  // 글자색 검정
        detailButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 18));

        Dimension btnSize = new Dimension(200, 50);
        detailButton.setPreferredSize(btnSize);
        detailButton.setMaximumSize(btnSize);

        detailButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(detailButton);

        wrapper.add(Box.createVerticalStrut(35));

        // ---------------------------
        // 아래 빨간 상세 박스
        // ---------------------------
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.BLACK);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        Dimension infoSize = new Dimension(520, 190);
        infoPanel.setPreferredSize(infoSize);
        infoPanel.setMaximumSize(infoSize);

        infoPanel.setBorder(new CompoundBorder(
                new LineBorder(RED_COLOR, 2, true),
                new EmptyBorder(20, 30, 20, 30)
        ));

        cinemaInfoLabel = new JLabel("CGV 강남점     2025.11.11(화)   20:00    4관");
        cinemaInfoLabel.setForeground(Color.WHITE);
        cinemaInfoLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 18));
        cinemaInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cinemaInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        movieTitleLabel = new JLabel("좀비딸");
        movieTitleLabel.setForeground(Color.WHITE);
        movieTitleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));
        movieTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        movieTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        seatInfoLabel = new JLabel("좌석 | G6, G7 | 4관");
        seatInfoLabel.setForeground(Color.WHITE);
        seatInfoLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 18));
        seatInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        seatInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        infoPanel.add(cinemaInfoLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(movieTitleLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(seatInfoLabel);

        JPanel infoWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        infoWrapper.setBackground(Color.BLACK);
        infoWrapper.add(infoPanel);

        wrapper.add(infoWrapper);

        return wrapper;
    }

    public void setReservationResult(String bookingNumber,
                                     String cinemaLine,
                                     String movieTitle,
                                     String seatInfo) {
        bookingNumberLabel.setText("예약번호 : " + bookingNumber);
        cinemaInfoLabel.setText(cinemaLine);
        movieTitleLabel.setText(movieTitle);
        seatInfoLabel.setText(seatInfo);
    }
}
