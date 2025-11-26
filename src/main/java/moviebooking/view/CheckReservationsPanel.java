package main.java.moviebooking.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;

// 둥근 빨간 버튼
class RoundSearchButton extends JButton {
    public RoundSearchButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        g.drawRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
    }
}

// 흰색 둥근 입력창
class RoundedTextField extends JTextField {
    public RoundedTextField() {
        super();
        setOpaque(false);  // 우리가 직접 배경을 그림
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setForeground(Color.BLACK);
        setCaretColor(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // 테두리는 배경과 같은 흰색으로 살짝만
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.dispose();
    }
}

public class CheckReservationsPanel extends JPanel implements GuiConstants {

    private final MainController mainController;
    private RoundedTextField bookingNumberField; // 필드로 빼서 초기화 용이하게 함

    public CheckReservationsPanel(MainController mainController) {
        this.mainController = mainController;

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);     // 상단 로고 및 뒤로가기
        add(createCenterPanel(), BorderLayout.CENTER); // 가운데 예매 조회 폼
    }

    /** 화면이 다시 보일 때 입력창 초기화 (선택 사항) */
    public void clearField() {
        if(bookingNumberField != null) {
            bookingNumberField.setText("예약번호");
            bookingNumberField.setForeground(Color.GRAY);
        }
    }

    /** 상단 로고 및 뒤로가기 버튼 */
    private JComponent createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        // 뒤로가기 버튼 추가
        JButton backButton = new JButton("◀");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);

        // 메인(영화 목록) 화면으로 이동
        backButton.addActionListener(e -> mainController.showAllMoviesView());

        topPanel.add(backButton, BorderLayout.WEST);

        JLabel logoLabel = new JLabel("MVP");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font(ENGLISH_FONT, Font.BOLD, 24));
        logoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // 로고를 오른쪽에 배치하거나, 가운데 배치하려면 별도 패널 필요. 여기선 간단히 오른쪽(EAST)에 둠.
        topPanel.add(logoLabel, BorderLayout.EAST);

        return topPanel;
    }

    /** 가운데 큰 '예매 조회' 제목 + 둥근 입력창(예약번호만) + 둥근 조회 버튼 */
    private JComponent createCenterPanel() {
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.BLACK);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.BLACK);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 40, 0));

        // 상단 빨간 바 형태의 "예매 조회" 라벨
        JLabel titleLabel = new JLabel("예매 조회", SwingConstants.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(RED_COLOR);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 22));
        titleLabel.setPreferredSize(new Dimension(260, 60));
        titleLabel.setMaximumSize(new Dimension(260, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);

        formPanel.add(Box.createVerticalStrut(60)); // 간격 조정

        // 흰색 둥근 입력창 (예약번호) - 전화번호 필드 삭제됨
        Dimension fieldSize = new Dimension(360, 40);

        bookingNumberField = new RoundedTextField();
        bookingNumberField.setPreferredSize(fieldSize);
        bookingNumberField.setMaximumSize(fieldSize);
        bookingNumberField.setFont(new Font(KOREAN_FONT, Font.PLAIN, 14));
        bookingNumberField.setAlignmentX(Component.CENTER_ALIGNMENT);
        setupPlaceholder(bookingNumberField, "예약번호");

        formPanel.add(bookingNumberField);
        formPanel.add(Box.createVerticalStrut(40));

        // 둥근 조회 버튼
        RoundSearchButton searchButton = new RoundSearchButton("조회");
        searchButton.setBackground(RED_COLOR);
        searchButton.setForeground(Color.BLACK); // 글자 검정색
        searchButton.setFont(new Font(KOREAN_FONT, Font.BOLD, 14));
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.setPreferredSize(new Dimension(90, 36));
        searchButton.setMaximumSize(new Dimension(90, 36));
        searchButton.setBorderPainted(false);

        // 조회 버튼 클릭 시 -> Controller에게 검색 위임
        searchButton.addActionListener(e -> {
            String bookingNo = bookingNumberField.getText().trim();

            if (bookingNo.isEmpty() || bookingNo.equals("예약번호")) {
                JOptionPane.showMessageDialog(this, "예약번호를 입력해주세요.", "알림", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 실제 조회 로직 호출
            mainController.searchBooking(bookingNo);
        });

        formPanel.add(searchButton);

        centerWrapper.add(formPanel);
        return centerWrapper;
    }

    /** 입력창에 연한 회색 플레이스홀더 넣기 */
    private void setupPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }
}