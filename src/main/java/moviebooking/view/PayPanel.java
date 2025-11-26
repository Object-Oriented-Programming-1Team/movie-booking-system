package main.java.moviebooking.view;

import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Booking;
import main.java.moviebooking.model.Seat;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * 좌석 선택 화면에서 "결제하기"를 눌렀을 때 보여줄 결제 정보 화면
 */
public class PayPanel extends JPanel implements GuiConstants {

    private final MainController mainController;
    private Booking booking;

    // --- 예매 정보 표시 라벨들 ---
    private JLabel cinemaInfoLabel;
    private JLabel movieTitleLabel;
    private JLabel dateTimeLabel;
    private JLabel seatLabel;
    private JLabel totalPriceLabel;

    private JTextField phoneField;

    public PayPanel(MainController mainController) {
        this.mainController = mainController;
        initUI();
    }

    private void initUI() {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
    }

    /** 왼쪽 상단 뒤로가기 버튼 */
    private JComponent createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JButton backButton = new JButton("<");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font(ENGLISH_FONT, Font.BOLD, 18));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);

        // 나중에 좌석 선택 화면으로 돌아가는 기능 연결
        backButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.out.println("뒤로가기: 좌석 선택 화면으로 이동 예정");
                mainController.showSeatSelectionView(booking.getScreening());
            }
        });

        topPanel.add(backButton, BorderLayout.WEST);
        return topPanel;
    }

    /** 가운데 예매 정보 + 결제 수단 선택 UI */
    private JComponent createCenterPanel() {

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(Color.BLACK);
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));

        // --------------------
        //  예매 정보 확인 제목
        // --------------------
        JLabel infoTitleLabel = new JLabel("예매 정보 확인", SwingConstants.CENTER);
        infoTitleLabel.setOpaque(true);
        infoTitleLabel.setBackground(RED_COLOR);
        infoTitleLabel.setForeground(Color.WHITE);
        infoTitleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 18));
        infoTitleLabel.setPreferredSize(new Dimension(250, 40));
        infoTitleLabel.setMaximumSize(new Dimension(250, 40));
        infoTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.add(infoTitleLabel);

        centerWrapper.add(Box.createVerticalStrut(15));

        // -------------------
        //  예매 정보 박스
        // -------------------
        JPanel infoBox = new JPanel();
        infoBox.setBackground(Color.BLACK);
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoBox.setMaximumSize(new Dimension(360, 180));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED_COLOR, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        cinemaInfoLabel = new JLabel("상영관 정보 로딩중... CGV 강변   2025.11.18(화) 20:00   4관");
        cinemaInfoLabel.setForeground(Color.WHITE);
        cinemaInfoLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 12));

        movieTitleLabel = new JLabel("영화 제목 표시");
        movieTitleLabel.setForeground(Color.WHITE);
        movieTitleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 18));

        dateTimeLabel = new JLabel("상영 시간 표시");
        dateTimeLabel.setForeground(Color.WHITE);
        dateTimeLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 14));

        seatLabel = new JLabel("좌석 정보 표시");
        seatLabel.setForeground(Color.WHITE);
        seatLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 14));

        totalPriceLabel = new JLabel("총 금액 표시");
        totalPriceLabel.setForeground(Color.WHITE);
        totalPriceLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 14));

        infoBox.add(cinemaInfoLabel);
        infoBox.add(Box.createVerticalStrut(8));
        infoBox.add(movieTitleLabel);
        infoBox.add(Box.createVerticalStrut(8));
        infoBox.add(dateTimeLabel);
        infoBox.add(Box.createVerticalStrut(5));
        infoBox.add(seatLabel);
        infoBox.add(Box.createVerticalStrut(10));
        infoBox.add(totalPriceLabel);

        centerWrapper.add(infoBox);

        centerWrapper.add(Box.createVerticalStrut(30));

        // --------------------
        //  결제 수단 선택 제목
        // --------------------
        JLabel payTitleLabel = new JLabel("결제 수단 선택", SwingConstants.CENTER);
        payTitleLabel.setOpaque(true);
        payTitleLabel.setBackground(RED_COLOR);
        payTitleLabel.setForeground(Color.WHITE);
        payTitleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 18));
        payTitleLabel.setPreferredSize(new Dimension(250, 40));
        payTitleLabel.setMaximumSize(new Dimension(250, 40));
        payTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerWrapper.add(payTitleLabel);

        centerWrapper.add(Box.createVerticalStrut(15));

        // --------------------
        //  결제 수단 박스
        // --------------------
        JPanel payBox = new JPanel();
        payBox.setBackground(Color.BLACK);
        payBox.setLayout(new BoxLayout(payBox, BoxLayout.Y_AXIS));
        payBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        payBox.setMaximumSize(new Dimension(360, 160));
        payBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED_COLOR, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        ButtonGroup payGroup = new ButtonGroup();

        JRadioButton cardPay = new JRadioButton("카드결제");
        JRadioButton kakaoPay = new JRadioButton("카카오페이");
        JRadioButton phonePay = new JRadioButton("휴대폰 결제");

        styleRadio(cardPay);
        styleRadio(kakaoPay);
        styleRadio(phonePay);

        payGroup.add(cardPay);
        payGroup.add(kakaoPay);
        payGroup.add(phonePay);

        payBox.add(cardPay);
        payBox.add(Box.createVerticalStrut(8));
        payBox.add(kakaoPay);
        payBox.add(Box.createVerticalStrut(8));
        payBox.add(phonePay);

        centerWrapper.add(payBox);

        centerWrapper.add(Box.createVerticalStrut(25));

        // --------------------
        // ⑤ 결제하기 버튼
        // --------------------
        JButton payButton = new JButton("결제하기");
        payButton.setBackground(RED_COLOR);
        payButton.setForeground(Color.WHITE);
        payButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 16));
        payButton.setFocusPainted(false);
        payButton.setBorderPainted(false);
        payButton.setOpaque(true);
        payButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        payButton.addActionListener(e -> {
            String method = "카드결제"; // 기본값
            if (kakaoPay.isSelected()) method = "카카오페이";
            else if (phonePay.isSelected()) method = "휴대폰 결제";

            // 실제 결제 로직이 들어갈 곳 (예: 매니저에게 저장 요청 등)
            JOptionPane.showMessageDialog(
                    PayPanel.this,
                    "결제 수단: " + method + "\n총 " + booking.getFormattedTotalPrice() + " 결제가 완료되었습니다.",
                    "결제 완료",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // 결제 완료 후 메인화면이나 결과화면으로 이동 로직 필요
            // mainController.showReservationResultView(booking); // 예시
        });
        centerWrapper.add(payButton);

        return centerWrapper;
    }

    /** 라디오버튼 공통 스타일 */
    private void styleRadio(JRadioButton radio) {
        radio.setBackground(Color.BLACK);
        radio.setForeground(Color.WHITE);
        radio.setFont(new Font(KOREAN_FONT, Font.PLAIN, 14));
        radio.setFocusPainted(false);
    }

    /**
     * 좌석 선택 화면에서 전달하는 예매 정보 저장용 메서드
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
        updatePanelInfo();
    }
    private void updatePanelInfo() {
        if (booking == null) return;

        // 1. 상영관 정보 (예: CGV 강변 4관)
        String screenName = booking.getScreening().getScreen().getScreenName();
        cinemaInfoLabel.setText("CGV 강변   " + screenName);

        // 2. 영화 제목
        movieTitleLabel.setText(booking.getScreening().getMovie().getMovieTitle());

        // 3. 상영 날짜 및 시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E) HH:mm");
        String dateTimeLine = booking.getScreening().getStartTime().format(formatter);
        dateTimeLabel.setText(dateTimeLine);

        // 4. 좌석 정보 (좌석 번호들을 콤마로 연결)
        String seatLine = booking.getSeats().stream()
                .map(Seat::getSeatNumber)
                .collect(Collectors.joining(", "));
        seatLabel.setText("좌석: " + seatLine);

        // 5. 총 금액
        totalPriceLabel.setText("총 결제금액: " + booking.getFormattedTotalPrice());
    }
}
