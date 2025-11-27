package main.java.moviebooking.view;

import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.*;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static main.java.moviebooking.common.GuiConstants.KOREAN_FONT;

public class SeatPanel extends JPanel {
    private Screen screen;
    private Screening screening;
    private MainController mainController;

    private final Color COLOR_EMPTY = new Color(240, 240, 240);
    private final Color COLOR_SELECTED = new Color(40, 70, 150);
    private final Color COLOR_BOOKED = new Color(200, 60, 60);
    private final Color COLOR_COUPLE_EMPTY = new Color(0xff9eb5); // 연한 핑크

    private final Color COLOR_BG = Color.BLACK;

    private JLabel selectedLabel;
    private JLabel priceLabel;

    private final ArrayList<Seat> selectedSeats = new ArrayList<>();

    private Seat[][] seats;

    private int peopleCount = 1; // 기본 1명
    private JPanel seatPanel; // 좌석 버튼들을 담는 패널을 멤버 변수로 추가

    public SeatPanel(Screening screening, MainController mainController) {
        this.screening = screening;
        this.seats = screening.getSeats();
        this.screen = screening.getScreen();
        this.mainController = mainController;

        setLayout(new BorderLayout());
        setBackground(COLOR_BG);

        add(createTopPanel(screening), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

    }

    private JPanel createTopPanel(Screening screening) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);

        // ⬅ 뒤로가기 버튼
        JButton backBtn = new JButton("◀");
        backBtn.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));
        backBtn.setBackground(COLOR_BG);
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorderPainted(false);

        backBtn.addActionListener(e -> {
            mainController.showTimeSelectView(screening.getMovie());
        });

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(COLOR_BG);
        leftPanel.add(backBtn);

        // =============== 중앙 : 제목 + 정보 ===============
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(COLOR_BG);

        // 🔥 영화 제목 빨간 박스
        RoundedPanel titleBox = new RoundedPanel(25);
        titleBox.setBackground(new Color(180, 50, 50));  // 빨간 박스
        titleBox.setPreferredSize(new Dimension(450, 55));
        titleBox.setMaximumSize(new Dimension(450, 55));
        titleBox.setLayout(new BorderLayout());

        JLabel title = new JLabel(screening.getMovie().getMovieTitle(), SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        titleBox.add(title);

        // 🔥 상영관/시간 정보
        String infoText = screening.getScreen().getScreenName()
                + "   " + screening.getStartTime().toLocalDate()
                + "   " + screening.getStartTime().toLocalTime()
                + "   " + screening.getScreen().getScreenType();

        JLabel info = new JLabel(infoText, SwingConstants.CENTER);
        info.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        info.setForeground(Color.LIGHT_GRAY);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 중앙 구성
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(titleBox);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(info);

        // 전체 배치
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }




    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);

        panel.add(createScreenBar(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(COLOR_BG);

        center.add(createSeatWithLabels(), BorderLayout.CENTER);
        center.add(createRowLabels(), BorderLayout.EAST);

        panel.add(center, BorderLayout.CENTER);

        panel.add(createRightPanel(), BorderLayout.EAST);

        panel.add(createSeatGuidePanel(screen), BorderLayout.SOUTH);



        return panel;
    }


    private JPanel createScreenBar() {
        JPanel p = new JPanel();
        p.setBackground(COLOR_BG);

        p.setBorder(BorderFactory.createEmptyBorder(40, 0, 5, 0));

        JLabel screen = new JLabel("SCREEN");
        screen.setOpaque(true);
        screen.setBackground(new Color(200, 200, 200));
        screen.setForeground(Color.BLACK);
        screen.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        screen.setPreferredSize(new Dimension(400, 25));
        screen.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(screen);
        return p;
    }


    private JPanel createSeatPanel() {
        int rows = seats.length;
        int cols = seats[0].length;

        // 🔥 좌석 간 간격 증가 + 패널 여백 추가
        seatPanel = new JPanel(new GridLayout(rows, cols, 12, 12));
        seatPanel.setBackground(COLOR_BG);
        seatPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                Seat seat = seats[i][j];

                JButton btn = new RoundSeatButton();
                btn.setPreferredSize(new Dimension(40, 40));
                btn.setBorderPainted(false);

                if (seat.isBooked()) {
                    btn.setBackground(COLOR_BOOKED);
                    btn.setEnabled(false);

                } else {
                    btn.setBackground(COLOR_EMPTY);

                    btn.addActionListener(e -> {
                        if (btn.getBackground().equals(COLOR_EMPTY)) {

                            if (selectedSeats.size() >= peopleCount) {
                                JOptionPane.showMessageDialog(this,
                                        "선택한 인원 수를 초과할 수 없습니다!");
                                return;
                            }

                            btn.setBackground(COLOR_SELECTED);
                            selectedSeats.add(seat);

                        } else {
                            btn.setBackground(COLOR_EMPTY);
                            selectedSeats.remove(seat);
                        }

                        updateRightPanel(); // 우측 패널 갱신
                    });
                }

                seatPanel.add(btn);
            }
        }

        return seatPanel;
    }

    class RoundSeatButton extends JButton {

        private int radius = 12; // 둥글기 정도 조절 (10~14 추천)

        public RoundSeatButton() {
            setContentAreaFilled(false); // 기본 배경 제거
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // 현재 버튼 색상 유지
            g2.setColor(getBackground());

            // 둥근 사각형 배경 채우기
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.dispose();
        }
    }






    private JPanel createSeatWithLabels() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COLOR_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(5, 80, 50, 20));
        wrapper.add(createSeatPanel(), BorderLayout.CENTER);
        return wrapper;
    }


    private JPanel createRowLabels() {
        // 좌석이랑 세로 간격 맞추려고 6으로 (seatPanel GridLayout 간격이 6이면 여기도 6)
        JPanel p = new JPanel(new GridLayout(seats.length, 1, 0, 6));
        p.setBackground(COLOR_BG);

        // 🔥 좌석 wrapper랑 같은 위/아래 여백 주기
        p.setBorder(BorderFactory.createEmptyBorder(15, 0, 60, 60));

        for (int i = 0; i < seats.length; i++) {
            JLabel lbl = new JLabel(String.valueOf((char) ('A' + i)));
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            lbl.setHorizontalAlignment(SwingConstants.LEFT);
            p.add(lbl);
        }

        return p;
    }



    private JPanel createRightPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(260, 0));
        p.setBackground(COLOR_BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ================================
        // 1) 인원 선택 영역 (검정 배경)
        // ================================
        JPanel peopleArea = new JPanel();
        peopleArea.setLayout(new BoxLayout(peopleArea, BoxLayout.Y_AXIS));
        peopleArea.setBackground(COLOR_BG);

        JLabel peopleLabel = new JLabel("인원 선택");
        peopleLabel.setForeground(Color.WHITE);
        peopleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        peopleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner peopleSpinner = new JSpinner(spinnerModel);
        peopleSpinner.setPreferredSize(new Dimension(80, 40));
        peopleSpinner.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        peopleSpinner.addChangeListener(e -> {
            peopleCount = (int) peopleSpinner.getValue();
            resetSeatSelection();
            updateRightPanel();
        });

        JPanel spinnerPanel = new JPanel(new FlowLayout());
        spinnerPanel.setBackground(COLOR_BG);
        spinnerPanel.add(peopleSpinner);

        // peopleArea에 추가
        peopleArea.add(peopleLabel);
        peopleArea.add(Box.createVerticalStrut(10));
        peopleArea.add(spinnerPanel);

        // ================================
        // 2) 선택좌석 + 금액 네이비 박스(둥근모서리 X)
        // ================================
        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBackground(new Color(0x194C66));
        infoBox.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        selectedLabel = new JLabel("선택 좌석 : 없음");
        selectedLabel.setForeground(Color.WHITE);
        selectedLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        selectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        priceLabel = new JLabel("총 금액 : 0원");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoBox.add(selectedLabel);
        infoBox.add(Box.createVerticalStrut(15));
        infoBox.add(priceLabel);

        // ================================
        // 3) 결제 버튼
        // ================================
        JButton payBtn = new JButton("결제하기");
        payBtn.setBackground(Color.RED);
        payBtn.setOpaque(true);
        payBtn.setBorderPainted(false);
        payBtn.setFocusPainted(false);
        payBtn.setContentAreaFilled(true);
        payBtn.setForeground(Color.WHITE);
        payBtn.setFont(new Font("맑은 고딕", Font.BOLD, 25));
        payBtn.setPreferredSize(new Dimension(220, 60));

        payBtn.addActionListener(e -> {
            if (selectedSeats.size() != peopleCount) {
                JOptionPane.showMessageDialog(this,
                        "선택한 인원(" + peopleCount + "명)과 좌석 수가 다릅니다!",
                        "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int totalPrice = 0;
            for (Seat seat : selectedSeats) {
                totalPrice += seat.getPrice();
            }

            Booking booking = new Booking(
                    screening,
                    new ArrayList<>(selectedSeats)
            );

            mainController.showPaymentView(booking);
        });

        JPanel bottomArea = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomArea.setBackground(COLOR_BG);
        bottomArea.add(payBtn);

        // ================================
        // 배치
        // ================================
        JPanel topBundle = new JPanel();
        topBundle.setLayout(new BoxLayout(topBundle, BoxLayout.Y_AXIS));
        topBundle.setBackground(COLOR_BG);

        topBundle.add(peopleArea);      // 인원선택 검정배경
        topBundle.add(Box.createVerticalStrut(30));
        topBundle.add(infoBox);         // 네이비 박스

        p.add(topBundle, BorderLayout.NORTH);
        p.add(bottomArea, BorderLayout.SOUTH);

        return p;
    }


    /**
     * 인원 수가 변경되었을 때, 기존에 선택했던 좌석 정보를 모두 초기화하는 메소드
     */
    private void resetSeatSelection() {
        // 1. 선택된 좌석 리스트 비우기
        selectedSeats.clear();

        // 2. 모든 좌석 버튼을 순회하며 색상 초기화
        if (seatPanel != null) {
            for (Component comp : seatPanel.getComponents()) {
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    // 선택된 좌석(파란색)을 다시 원래의 빈 좌석 색으로 변경
                    if (button.getBackground().equals(COLOR_SELECTED)) {
                        button.setBackground(COLOR_EMPTY); // 일반 좌석
                    }
                }
            }
        }
    }




    private void updateRightPanel() {
        StringBuilder sb = new StringBuilder();
        int total = 0;

        for (Seat s : selectedSeats) {
            sb.append(s.getSeatNumber()).append(" ");
            total += s.getPrice();
        }

        if (sb.length() == 0) {
            selectedLabel.setText("선택 좌석 : 없음");
        } else {
            selectedLabel.setText("선택 좌석 : " + sb.toString().trim());
        }
        priceLabel.setText("총 금액 : " + total + "원");

        revalidate();
        repaint();

    }

    private JPanel createSeatGuidePanel(Screen screen) {

        JPanel guidePanel = new JPanel();
        guidePanel.setBackground(COLOR_BG);
        guidePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // 예약된 좌석 (빨간색)
        JPanel bookedSeatBox = new JPanel();
        bookedSeatBox.setBackground(COLOR_BOOKED);
        bookedSeatBox.setPreferredSize(new Dimension(20, 20));
        JLabel bookedLabel = new JLabel("예약된 좌석");
        bookedLabel.setForeground(Color.WHITE);

        // 일반 빈 좌석 (회색)
        JPanel emptySeatBox = new JPanel();
        emptySeatBox.setBackground(COLOR_EMPTY);
        emptySeatBox.setPreferredSize(new Dimension(20, 20));
        JLabel emptyLabel = new JLabel("빈 좌석");
        emptyLabel.setForeground(Color.WHITE);

        guidePanel.add(bookedSeatBox);
        guidePanel.add(bookedLabel);
        guidePanel.add(emptySeatBox);
        guidePanel.add(emptyLabel);

        return guidePanel;
    }


}


class RoundedPanel extends JPanel {
    private int radius;

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}
