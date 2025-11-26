package main.java.moviebooking.view;

import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Booking;
import main.java.moviebooking.model.Seat;
import main.java.moviebooking.model.User;

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

    private JComponent createCenterPanel() {

        JPanel centerWrapper = new JPanel();
        centerWrapper.setBackground(Color.BLACK);
        centerWrapper.setLayout(new BoxLayout(centerWrapper, BoxLayout.Y_AXIS));
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));

        // ==========================================
        // 1. 예매 정보 확인 섹션 (순서 수정됨)
        // ==========================================
        JLabel infoTitleLabel = new JLabel("예매 정보 확인", SwingConstants.CENTER);
        styleTitleLabel(infoTitleLabel);
        centerWrapper.add(infoTitleLabel);
        centerWrapper.add(Box.createVerticalStrut(15));

        // 정보 박스 구성
        JPanel infoBox = new JPanel();
        infoBox.setBackground(Color.BLACK);
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoBox.setMaximumSize(new Dimension(360, 180));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(RED_COLOR, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // 라벨 초기화 및 추가
        cinemaInfoLabel = new JLabel("상영관 정보 로딩중...");
        movieTitleLabel = new JLabel("영화 제목");
        dateTimeLabel = new JLabel("시간");
        seatLabel = new JLabel("좌석");
        totalPriceLabel = new JLabel("금액");

        styleInfoLabel(cinemaInfoLabel, Font.PLAIN, 12);
        styleInfoLabel(movieTitleLabel, Font.BOLD, 18);
        styleInfoLabel(dateTimeLabel, Font.PLAIN, 14);
        styleInfoLabel(seatLabel, Font.PLAIN, 14);
        styleInfoLabel(totalPriceLabel, Font.BOLD, 14);

        infoBox.add(cinemaInfoLabel);
        infoBox.add(Box.createVerticalStrut(8));
        infoBox.add(movieTitleLabel);
        infoBox.add(Box.createVerticalStrut(8));
        infoBox.add(dateTimeLabel);
        infoBox.add(Box.createVerticalStrut(5));
        infoBox.add(seatLabel);
        infoBox.add(Box.createVerticalStrut(10));
        infoBox.add(totalPriceLabel);

        // ★ 중요: 정보 박스를 먼저 추가해야 함
        centerWrapper.add(infoBox);
        centerWrapper.add(Box.createVerticalStrut(30));


        // ==========================================
        // 2. 예매자 정보 섹션 (전화번호)
        // ==========================================
        JLabel userTitleLabel = new JLabel("예매자 정보", SwingConstants.CENTER);
        styleTitleLabel(userTitleLabel);
        centerWrapper.add(userTitleLabel);
        centerWrapper.add(Box.createVerticalStrut(15));

        JPanel userBox = createUserBox();
        centerWrapper.add(userBox);
        centerWrapper.add(Box.createVerticalStrut(30));


        // ==========================================
        // 3. 결제 수단 선택 섹션
        // ==========================================
        JLabel payTitleLabel = new JLabel("결제 수단 선택", SwingConstants.CENTER);
        styleTitleLabel(payTitleLabel);
        centerWrapper.add(payTitleLabel);
        centerWrapper.add(Box.createVerticalStrut(15));

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
        cardPay.setSelected(true); // 기본값 선택

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


        // ==========================================
        // 4. 결제하기 버튼
        // ==========================================
        JButton payButton = new JButton("결제하기");
        payButton.setBackground(RED_COLOR);
        payButton.setForeground(Color.WHITE);
        payButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 16));
        payButton.setFocusPainted(false);
        payButton.setBorderPainted(false);
        payButton.setOpaque(true);
        payButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        payButton.setPreferredSize(new Dimension(200, 50)); // 버튼 크기 고정

        payButton.addActionListener(e -> processPayment());
        centerWrapper.add(payButton);

        return centerWrapper;
    }

    private void styleInfoLabel(JLabel label, int style, int size) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font(KOREAN_FONT, style, size));
    }

    //스타일
    private void styleTitleLabel(JLabel label) {
        label.setOpaque(true);
        label.setBackground(RED_COLOR);
        label.setForeground(Color.WHITE);
        label.setFont(new Font(KOREAN_FONT, Font.BOLD, 18));
        label.setPreferredSize(new Dimension(250, 40));
        label.setMaximumSize(new Dimension(250, 40));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void styleRadio(JRadioButton radio) {
        radio.setBackground(Color.BLACK);
        radio.setForeground(Color.WHITE);
        radio.setFont(new Font(KOREAN_FONT, Font.PLAIN, 14));
        radio.setFocusPainted(false);
    }

    private JPanel createUserBox() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.BLACK);
        panel.setMaximumSize(new Dimension(360, 60));

        JLabel phoneLabel = new JLabel("전화번호 : ");
        phoneLabel.setForeground(Color.WHITE);
        phoneLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 16));

        phoneField = new JTextField(11); // 01012345678
        phoneField.setFont(new Font(KOREAN_FONT, Font.PLAIN, 16));
        phoneField.setHorizontalAlignment(JTextField.CENTER);

        panel.add(phoneLabel);
        panel.add(phoneField);
        return panel;
    }

    private void processPayment() {
        // 1. 전화번호 입력 확인
        String phoneNumber = phoneField.getText().trim();
        if (phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "전화번호를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. User 객체 생성 및 Booking에 저장
        // (이름은 현재 입력받지 않으므로 'Guest'로 설정하거나 추후 확장 가능)
        User user = new User("Guest", phoneNumber);
        booking.setUser(user);

        mainController.saveBooking(booking);

        // 4. 결제 완료 메시지 및 화면 이동
        JOptionPane.showMessageDialog(this, "결제가 완료되었습니다!\n예매 내역 화면으로 이동합니다.");

        // MainController를 통해 결과 화면으로 이동
        mainController.showReservationResultView(booking);
    }

    /**
     * 좌석 선택 화면에서 전달하는 예매 정보 저장용 메서드
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
        phoneField.setText("");
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
