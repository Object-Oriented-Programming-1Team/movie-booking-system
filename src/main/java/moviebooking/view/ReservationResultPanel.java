package main.java.moviebooking.view;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Booking;
import main.java.moviebooking.model.Seat;

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
    private JPanel bookingListPanel;

    public ReservationResultPanel(MainController mainController) {
        this.mainController = mainController;
        initUI();
    }

    public void setBookingList(List<Booking> bookings) {
        bookingListPanel.removeAll();

        if (bookings == null || bookings.isEmpty()) {
            JLabel noDataLabel = new JLabel("조회된 예약 내역이 없습니다.");
            noDataLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 20));
            noDataLabel.setForeground(Color.WHITE);
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            bookingListPanel.add(Box.createVerticalStrut(50));
            bookingListPanel.add(noDataLabel);
        } else {
            for (Booking booking : bookings) {
                // ---------------------------
                // 결제 완료 박스
                // ---------------------------
                JPanel donePanel = new JPanel();
                donePanel.setBackground(Color.BLACK);
                donePanel.setLayout(new BoxLayout(donePanel, BoxLayout.Y_AXIS));

                Dimension doneSize = new Dimension(520, 150);
                donePanel.setPreferredSize(doneSize);
                donePanel.setMaximumSize(doneSize);

                donePanel.setBorder(new CompoundBorder(
                        new LineBorder(Color.WHITE, 2, true),
                        new EmptyBorder(25, 40, 25, 40)));

                JLabel doneLabel = new JLabel("결제 완료!");
                doneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                doneLabel.setHorizontalAlignment(SwingConstants.CENTER);
                doneLabel.setForeground(Color.WHITE);
                doneLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));

                JLabel bookingNumberLabel = new JLabel("예약번호 : " + booking.getBookingId());
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

                JPanel northCardWrapper = new JPanel();
                northCardWrapper.setBackground(Color.BLACK);
                northCardWrapper.setLayout(new BoxLayout(northCardWrapper, BoxLayout.Y_AXIS));
                northCardWrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
                northCardWrapper.add(Box.createVerticalStrut(20));
                northCardWrapper.add(doneWrapper);
                northCardWrapper.add(Box.createVerticalStrut(25));

                // ---------------------------
                // 둥근 "예약내역" 버튼
                // ---------------------------
                RoundButton detailButton = new RoundButton("예약내역");
                detailButton.setBackground(RED_COLOR);
                detailButton.setForeground(Color.BLACK); // 글자색 검정
                detailButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 18));

                Dimension btnSize = new Dimension(200, 50);
                detailButton.setPreferredSize(btnSize);
                detailButton.setMaximumSize(btnSize);

                detailButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                northCardWrapper.add(detailButton);
                bookingListPanel.add(Box.createVerticalStrut(5));

                ReservationCard card = new ReservationCard(booking);
                JPanel southCardWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                southCardWrapper.setBackground(Color.BLACK);
                southCardWrapper.add(card);

                bookingListPanel.add(northCardWrapper);
                bookingListPanel.add(southCardWrapper);
                bookingListPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
                bookingListPanel.add(Box.createVerticalStrut(60));
            }
        }

        bookingListPanel.revalidate();
        bookingListPanel.repaint();
    }

    private void initUI() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);
        add(createScrollableCenterPanel(), BorderLayout.CENTER);
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

        goMainButton.addActionListener(e -> mainController.showAllMoviesView());
        top.add(goMainButton, BorderLayout.EAST);

        return top;
    }

    private JComponent createScrollableCenterPanel() {
        bookingListPanel = new JPanel();
        bookingListPanel.setLayout(new BoxLayout(bookingListPanel, BoxLayout.Y_AXIS));
        bookingListPanel.setBackground(Color.BLACK);
        bookingListPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        JScrollPane scrollPane = new JScrollPane(bookingListPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    class ReservationCard extends JPanel {
        public ReservationCard(Booking booking) {
            setBackground(Color.BLACK);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            Dimension infoSize = new Dimension(520, 190);
            setPreferredSize(infoSize);
            setMaximumSize(infoSize);

            setBorder(new CompoundBorder(
                    new LineBorder(RED_COLOR, 2, true),
                    new EmptyBorder(20, 30, 20, 30)));

            String dateStr = booking.getScreening().getStartTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd(E)"));
            String timeStr = booking.getScreening().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String screenName = booking.getScreening().getScreen().getScreenName();

            JLabel cinemaInfoLabel = new JLabel("CGV 강남점     " + dateStr + "   " + timeStr + "    " + screenName);
            cinemaInfoLabel.setForeground(Color.WHITE);
            cinemaInfoLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 18));
            cinemaInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            String movieTitle = booking.getScreening().getMovie().getMovieTitle();
            JLabel movieTitleLabel = new JLabel(movieTitle);
            movieTitleLabel.setForeground(Color.WHITE);
            movieTitleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));
            movieTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            StringBuilder sb = new StringBuilder();
            for (Seat seat : booking.getSeats()) {
                sb.append(seat.getSeatNumber()).append(", ");
            }
            String seatsStr = sb.length() > 2 ? sb.substring(0, sb.length() - 2) : "";

            JLabel seatInfoLabel = new JLabel("좌석 | " + seatsStr + " | " + screenName);
            seatInfoLabel.setForeground(Color.WHITE);
            seatInfoLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 18));
            seatInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(cinemaInfoLabel);
            add(Box.createVerticalStrut(15));
            add(movieTitleLabel);
            add(Box.createVerticalStrut(15));
            add(seatInfoLabel);
        }
    }
}