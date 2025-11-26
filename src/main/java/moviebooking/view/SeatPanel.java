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
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));
        backBtn.setBackground(COLOR_BG);
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorderPainted(false);

        // 뒤로가기 기능 — 시간선택 화면으로 이동
        backBtn.addActionListener(e -> {
            mainController.showTimeSelectView(screening.getMovie());
        });

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(COLOR_BG);
        leftPanel.add(backBtn);

        // 기존 정보 패널
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(COLOR_BG);

        JLabel title = new JLabel(screening.getMovie().getMovieTitle(), SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 35));
        title.setForeground(Color.WHITE);
        infoPanel.add(title);

        String infoText = screening.getScreen().getScreenName()
                + "   " + screening.getStartTime().toLocalDate()
                + "   " + screening.getStartTime().toLocalTime()
                + "   " + screening.getScreen().getScreenType();

        JLabel info = new JLabel(infoText, SwingConstants.CENTER);
        info.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        info.setForeground(Color.LIGHT_GRAY);
        infoPanel.add(info);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);

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

        JLabel screen = new JLabel("SCREEN");
        screen.setOpaque(true);
        screen.setBackground(new Color(200, 200, 200));
        screen.setForeground(Color.BLACK);
        screen.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        screen.setPreferredSize(new Dimension(120, 25));
        screen.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(screen);
        return p;
    }


    private JPanel createSeatPanel() {
        int rows = seats.length;
        int cols = seats[0].length;

        // 멤버 변수 seatPanel 초기화
        seatPanel = new JPanel(new GridLayout(rows, cols, 7, 7));
        seatPanel.setBackground(COLOR_BG);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                Seat seat = seats[i][j];

                // ⭐ 통로(null)
                if (seat == null) {
                    JPanel empty = new JPanel();
                    empty.setBackground(COLOR_BG);
                    seatPanel.add(empty);
                    continue;
                }

                // ============================
                // ⭐ 커플석 UI (2칸짜리 버튼)
                // ============================
                if (seat.isCoupleSeat()) {

                    int seatIndex = j + 1; // 1-based index

                    // 왼쪽 좌석(홀수)일 때만 버튼 생성
                    if (seatIndex % 2 == 1) {

                        // 🔥 오른쪽 커플석이 실제로 존재하는지 확인
                        if (j + 1 >= cols || seats[i][j+1] == null || !seats[i][j+1].isCoupleSeat()) {
                            // 짝이 없으면 그냥 일반 좌석처럼 처리
                            JButton btn = new JButton();
                            btn.setPreferredSize(new Dimension(35, 35));
                            btn.setBorderPainted(false);
                            btn.setOpaque(true);
                            btn.setBackground(COLOR_COUPLE_EMPTY);

                            btn.addActionListener(e -> {
                                if (btn.getBackground().equals(COLOR_COUPLE_EMPTY)) {
                                    if (selectedSeats.size() >= peopleCount) {
                                        JOptionPane.showMessageDialog(this,
                                                "선택한 인원 수를 초과할 수 없습니다!");
                                        return;
                                    }
                                    btn.setBackground(COLOR_SELECTED);
                                    selectedSeats.add(seat);
                                } else {
                                    btn.setBackground(COLOR_COUPLE_EMPTY);
                                    selectedSeats.remove(seat);
                                }
                                updateRightPanel();
                            });

                            seatPanel.add(btn);
                            continue;
                        }

                        // 🔥 여기부터 정상적인 커플석 (짝이 있는 경우)
                        Seat rightSeat = seats[i][j+1];

                        JButton btn = new JButton();
                        btn.setBorderPainted(false);
                        btn.setOpaque(true);
                        btn.setBackground(COLOR_COUPLE_EMPTY);
                        btn.setPreferredSize(new Dimension(80, 35)); // 2칸 좌석

                        btn.addActionListener(e -> {

                            if (btn.getBackground().equals(COLOR_COUPLE_EMPTY)) {

                                if (selectedSeats.size() + 2 > peopleCount) {
                                    JOptionPane.showMessageDialog(this,
                                            "선택한 인원 수를 초과할 수 없습니다!");
                                    return;
                                }

                                btn.setBackground(COLOR_SELECTED);

                                selectedSeats.add(seat);
                                selectedSeats.add(rightSeat);

                            } else {
                                btn.setBackground(COLOR_COUPLE_EMPTY);

                                selectedSeats.remove(seat);
                                selectedSeats.remove(rightSeat);
                            }

                            updateRightPanel();
                        });

                        seatPanel.add(btn);
                        continue;

                    } else {
                        // 오른쪽 자리는 빈칸 처리
                        JPanel empty = new JPanel();
                        empty.setBackground(COLOR_BG);
                        seatPanel.add(empty);
                        continue;
                    }
                }


                // ============================
                // ⭐ 일반 좌석
                // ============================
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(35, 35));
                btn.setBorderPainted(false);
                btn.setOpaque(true);

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

                        updateRightPanel();
                    });
                }

                seatPanel.add(btn);
            }
        }

        return seatPanel;
    }




    private JPanel createSeatWithLabels() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(COLOR_BG);
        wrapper.add(createSeatPanel(), BorderLayout.CENTER);
        return wrapper;
    }


    private JPanel createRowLabels() {
        JPanel p = new JPanel(new GridLayout(seats.length, 1, 0, 15));
        p.setBackground(COLOR_BG);

        for (int i = 0; i < seats.length; i++) {
            JLabel lbl = new JLabel(String.valueOf((char) ('A' + i)));
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            p.add(lbl);
        }

        return p;
    }


    private JPanel createRightPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(260, 0));  // 오른쪽 패널 고정 폭
        p.setBackground(COLOR_BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === 상단 영역 (인원 선택 + 선택좌석 + 가격) ===
        JPanel topArea = new JPanel();
        topArea.setLayout(new BoxLayout(topArea, BoxLayout.Y_AXIS));
        topArea.setBackground(COLOR_BG);

        // ⭐ 인원 선택 라벨
        JLabel peopleLabel = new JLabel("인원 선택");
        peopleLabel.setForeground(Color.WHITE);
        peopleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        peopleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === JSpinner를 사용한 인원 선택 UI ===
        // 1. Spinner 모델 생성 (초기값: 1, 최소: 1, 최대: 10, 단계: 1)
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner peopleSpinner = new JSpinner(spinnerModel);
        peopleSpinner.setPreferredSize(new Dimension(80, 40));
        peopleSpinner.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        // 2. Spinner 값 변경 리스너 추가
        peopleSpinner.addChangeListener(e -> {
            // 스피너의 현재 값을 가져와 peopleCount에 저장
            peopleCount = (int) peopleSpinner.getValue();

            // 🚨 수정: 화면을 새로고침하는 대신, 선택된 좌석만 초기화
            resetSeatSelection();

            // 오른쪽 정보 패널 업데이트
            updateRightPanel();
        });

        JPanel peopleSpinnerPanel = new JPanel(new FlowLayout());
        peopleSpinnerPanel.setBackground(COLOR_BG);
        peopleSpinnerPanel.add(peopleSpinner);


        // ⭐ 선택 좌석 라벨
        selectedLabel = new JLabel("선택 좌석 : 없음");
        selectedLabel.setForeground(Color.WHITE);
        selectedLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        selectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ⭐ 가격 라벨
        priceLabel = new JLabel("총 금액 : 0원");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topArea.add(peopleLabel);
        topArea.add(Box.createVerticalStrut(10));
        topArea.add(peopleSpinnerPanel);
        topArea.add(Box.createVerticalStrut(30));
        topArea.add(selectedLabel);
        topArea.add(Box.createVerticalStrut(20));
        topArea.add(priceLabel);

        // === 하단 영역 (결제 버튼) ===
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
            // 총 금액 계산
            int totalPrice = 0;
            for (Seat seat : selectedSeats) {
                totalPrice += seat.getPrice();
            }

            // Reservation 객체 생성
            Booking booking = new Booking(
                    screening,
                    new ArrayList<>(selectedSeats)
                    );
            // 결제 화면으로 정보 전달
            mainController.showPaymentView(booking);
        });

        JPanel bottomArea = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomArea.setBackground(COLOR_BG);
        bottomArea.add(payBtn);

        // 패널 배치
        p.add(topArea, BorderLayout.NORTH);
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

        // ⭐ 커플석이 있는 경우에만 추가
        if (screen.getSeatLayoutType().equals("COUPLE_BACK")) {

            JPanel coupleSeatBox = new JPanel();
            coupleSeatBox.setBackground(COLOR_COUPLE_EMPTY); // 커플석 빈좌석 색
            coupleSeatBox.setPreferredSize(new Dimension(20, 20));

            JLabel coupleLabel = new JLabel("커플석");
            coupleLabel.setForeground(Color.WHITE);

            guidePanel.add(coupleSeatBox);
            guidePanel.add(coupleLabel);
        }

        return guidePanel;
    }


}
