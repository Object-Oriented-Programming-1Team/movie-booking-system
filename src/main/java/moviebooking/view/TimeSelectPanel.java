package main.java.moviebooking.view;

import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Movie;
import main.java.moviebooking.model.Screening;
import main.java.moviebooking.service.ScreenManager;
import main.java.moviebooking.service.ScreeningManager;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.*;

import java.awt.*;

public class TimeSelectPanel extends JPanel implements GuiConstants {

    private final MainController mainController;
    private final ScreeningManager screeningManager;
        
    private Movie movie;
    
    private JPanel timeButtonPanel;
    private JLabel titleLabel;
    private JPanel screenSelectPanel;
    private JPanel dateSelectPanel;

    private String selectedDateStr;

    public TimeSelectPanel(MainController mainController, ScreeningManager screeningManager, ScreenManager screenManager) {
        this.mainController = mainController;
        this.screeningManager = screeningManager;

        setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
    }

    public void setMovie(Movie movie){
        this.movie = movie;
        this.selectedDateStr = "ALL"; 
        
        updateFilterButtons();
        updatePanelInfo();
    }

    private void updatePanelInfo(){
        if (movie == null) return;

        titleLabel.setText(movie.getMovieTitle());

        timeButtonPanel.removeAll();

        List<Screening> screenings = screeningManager.findScreeningsByMovieId(this.movie.getMovieId());
        
        //상영관 타입별로 묶기
        Map<String, List<Screening>> groupedScreenings = new HashMap<>();
        if(!screenings.isEmpty()){
            for (Screening screening : screenings){
                String screenType = screening.getScreen().getScreenType();
                String dateText = screening.getStartTime().format(DateTimeFormatter.ofPattern("MM월 dd일"));
    
                boolean dateMatch = selectedDateStr.equals("ALL") || selectedDateStr.equals(dateText);
                if(dateMatch){
                    groupedScreenings.computeIfAbsent(screenType, k -> new ArrayList<>()).add(screening);
                }
            }
        }

        //화면 그리기
        if (groupedScreenings.isEmpty()) {
            JLabel noScreeningLabel = new JLabel("해당 조건의 상영 정보가 없습니다.");
            noScreeningLabel.setForeground(Color.WHITE);
            noScreeningLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 20));
            noScreeningLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(Color.BLACK);
            wrapper.add(noScreeningLabel, BorderLayout.CENTER);
            wrapper.setPreferredSize(new Dimension(800, 50)); 
            timeButtonPanel.add(wrapper);
        } else {
            for (Map.Entry<String, List<Screening>> entry : groupedScreenings.entrySet()) {
                String typeName = entry.getKey();
                List<Screening> typeList = entry.getValue();

                JLabel headerLabel = new JLabel(typeName);
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setFont(new Font(ENGLISH_FONT, Font.BOLD, 40));
                headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

                JPanel gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
                gridPanel.setBackground(Color.BLACK);
                
                for (Screening s : typeList) {
                    gridPanel.add(createTimeButton(s));
                }
                
                JPanel groupPanel = new JPanel(new BorderLayout());
                groupPanel.setBackground(Color.BLACK);
                groupPanel.add(headerLabel, BorderLayout.NORTH);
                groupPanel.add(gridPanel, BorderLayout.CENTER);
                
                timeButtonPanel.add(groupPanel);
            }
        }

        timeButtonPanel.revalidate();
        timeButtonPanel.repaint();
    }

    //디자인 상으로는 버튼 할당 시, 투명도 100 이지만 회색빛은 어떨까해서 넣어봤습니다.    
    private void updateFilterButtons() {
        for (Component c : screenSelectPanel.getComponents()) {
                JButton btn = (JButton) c;
                if(btn==null) return;
                btn.setBackground(Color.WHITE); // 선택 안됨
                btn.setForeground(Color.BLACK);
        }
        for (Component c : dateSelectPanel.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                if (btn.getText().equals(selectedDateStr) || (selectedDateStr.equals("ALL") && btn.getText().equals("ALL"))) {
                    btn.setBackground(Color.GRAY); // 선택됨
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(Color.WHITE); // 선택 안됨
                    btn.setForeground(Color.BLACK);
                }
            }
        }
    }

    private JPanel createTopPanel(){
        JButton backButton = new JButton("◀");
        styleButton(backButton, 50);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.GRAY);
        backButton.addActionListener(e -> mainController.showBookMovieView(movie));

        JLabel contextLabel = new JLabel("상영 시간 선택");
        contextLabel.setForeground(Color.WHITE);
        contextLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));
        contextLabel.setHorizontalAlignment(SwingConstants.CENTER);

        screenSelectPanel = new JPanel();
        screenSelectPanel.setBackground(Color.BLACK);
        
        JPanel centerPanelOnTop = new JPanel();
        centerPanelOnTop.setBackground(Color.BLACK);
        centerPanelOnTop.add(createTitlePanel());
        centerPanelOnTop.add(contextLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(centerPanelOnTop, BorderLayout.CENTER);
        topPanel.add(screenSelectPanel, BorderLayout.SOUTH);

        JLabel emptyLabel = new JLabel("   ");
        emptyLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        topPanel.add(emptyLabel, BorderLayout.EAST);
        
        return topPanel;
    }

    private JPanel createTitlePanel() {
        titleLabel = new JLabel();
        titleLabel.setText("영화 제목 로딩 중...");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 40));

        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.setBackground(RED_COLOR);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createCenterPanel(){
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.BLACK);

        dateSelectPanel = new JPanel();
        dateSelectPanel.setBackground(Color.BLACK);
        
        dateSelectPanel.add(createFilterButton("ALL","ALL"));
        for(int i=10; i<=14; i++){
            LocalDate date = LocalDate.of(2025,11,i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            String yoil = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);
            dateSelectPanel.add(createFilterButton("<html><center>" + yoil+"<br>" + i + "일"+"</center></html>","11월 "+i+"일"));
        }

        // GridLayout 대신 BoxLayout 사용 (세로로 그룹을 쌓기 위함)
        timeButtonPanel = new JPanel();
        timeButtonPanel.setLayout(new BoxLayout(timeButtonPanel, BoxLayout.Y_AXIS));
        timeButtonPanel.setBackground(Color.BLACK);
        timeButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JScrollPane scrollPane = new JScrollPane(timeButtonPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        centerPanel.add(dateSelectPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return centerPanel;
    }

    private JButton createFilterButton(String text,String dateStr){
        JButton button = new JButton(text);
        styleButton(button, 16);
        button.setPreferredSize(new Dimension(90,90));

        button.addActionListener(e -> {
            System.out.println(dateStr);
            this.selectedDateStr = dateStr.equals("전체") ? "ALL" : dateStr;
            updateFilterButtons();
            updatePanelInfo();
        });
        return button;
    }

    private JButton createTimeButton(Screening screening){
        String dateText = screening.getStartTime().format(DateTimeFormatter.ofPattern("MM월 dd일"));
        String timeText = screening.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTimeText = screening.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String screenName = screening.getScreen().getScreenName();

        JButton button = new JButton("<html><center>" + dateText +"<br>"+ timeText + " ~ " + endTimeText +"<br>" + screenName + "</center></html>");
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font(KOREAN_FONT, Font.BOLD, 16));
        button.setPreferredSize(new Dimension(180, 80)); 
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        button.addActionListener(e ->{
            // TODO: 좌석 선택 화면 이동 로직
            // mainController.showSeatPanel(screening);
            System.out.println("상영 선택됨: " + screening.getScreeningId());
        });
        return button;
    }

    private void styleButton(JButton btn, int fontSize) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font(KOREAN_FONT, Font.BOLD, fontSize));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
    }
}