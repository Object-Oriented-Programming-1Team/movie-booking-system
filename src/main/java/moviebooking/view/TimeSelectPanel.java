package main.java.moviebooking.view;

import main.java.moviebooking.common.GuiConstants;
import main.java.moviebooking.controller.MainController;
import main.java.moviebooking.model.Movie;
import main.java.moviebooking.model.Screen;
import main.java.moviebooking.model.Screening;
import main.java.moviebooking.service.ScreenManager;
import main.java.moviebooking.service.ScreeningManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;

public class TimeSelectPanel extends JPanel implements GuiConstants {

    private final MainController mainController;
    private final ScreeningManager screeningManager;
    private final ScreenManager screenManager;
        
    private Movie movie;
  
    private JPanel timeButtonPanel;
    private JLabel titleLabel;
    private JPanel screenSelectPanel;
    private JPanel dateSelectPanel; 

    private String selectedScreenType;
    private String selectedDateStr;

    public TimeSelectPanel(MainController mainController, ScreeningManager screeningManager, ScreenManager screenManager) {
        this.mainController = mainController;
        this.screeningManager = screeningManager;
        this.screenManager = screenManager;

        setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
    }

    public void setMovie(Movie movie){
        this.movie = movie;
        this.selectedScreenType = "ALL";
        this.selectedDateStr = "ALL"; 
        
        updateFilterButtons();
        updatePanelInfo();
    }

    private void updatePanelInfo(){
        if (movie == null) return;
        
        titleLabel.setText(movie.getMovieTitle() + " 상영 시간표");
        
        timeButtonPanel.removeAll();

        List<Screening> screenings = screeningManager.findScreeningsByMovieId(this.movie.getMovieId());
        boolean hasScreening = false;

        //날짜와 상영관 조건이 일치하는 Screening만 표시
        if(!screenings.isEmpty()){
            for (Screening screening : screenings){
                String screenType = screening.getScreen().getScreenType();
                String dateText = screening.getStartTime().format(DateTimeFormatter.ofPattern("MM월 dd일"));

                boolean typeMatch = selectedScreenType.equals("ALL") || selectedScreenType.equals(screenType);
                boolean dateMatch = selectedDateStr.equals("ALL") || selectedDateStr.equals(dateText);

                if(typeMatch && dateMatch){
                    JButton timeButton = createTimeButton(screening);
                    timeButtonPanel.add(timeButton);
                    hasScreening = true;
                }
            }
        }

        if(!hasScreening){
            JLabel noScreeningLabel = new JLabel("해당 조건의 상영 정보가 없습니다.");
            noScreeningLabel.setForeground(Color.WHITE);
            noScreeningLabel.setFont(new Font(KOREAN_FONT, Font.PLAIN, 20));
            noScreeningLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(Color.BLACK);
            wrapper.add(noScreeningLabel, BorderLayout.CENTER);
            timeButtonPanel.add(wrapper);
        }

        timeButtonPanel.revalidate();
        timeButtonPanel.repaint();
    }

    //디자인 상으로는 버튼 할당 시, 투명도 100 이지만 회색빛은 어떨까해서 넣어봤습니다.
    private void updateFilterButtons() {
        for (Component c : screenSelectPanel.getComponents()) {
            JButton btn = (JButton) c;
            if(btn==null) return;
            if (btn.getText().equals(selectedScreenType)) {
                btn.setBackground(Color.GRAY); // 선택됨
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(Color.WHITE); // 선택 안됨
                btn.setForeground(Color.BLACK);
            }
        }
        for (Component c : dateSelectPanel.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                if (btn.getText().equals(selectedDateStr)) {
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
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        JButton backButton = new JButton("◀");
        styleButton(backButton, 50);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.GRAY);
        backButton.addActionListener(e -> mainController.showBookMovieView(movie));

        titleLabel = new JLabel("상영 시간 선택");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        screenSelectPanel = new JPanel();
        screenSelectPanel.setBackground(Color.BLACK);
        
        List<String> screenTypes = screenManager.findAllScreenTypes();
        
        screenSelectPanel.add(createFilterButton("ALL", true)); // 상영관 버튼이란걸 표시
        
        for(String type : screenTypes){
            screenSelectPanel.add(createFilterButton(type, true));
        }

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(screenSelectPanel, BorderLayout.SOUTH);

        JLabel emptyLabel = new JLabel("   ");
        emptyLabel.setFont(new Font(KOREAN_FONT, Font.BOLD, 50));
        topPanel.add(emptyLabel, BorderLayout.EAST);
        
        return topPanel;
    }

    private JPanel createCenterPanel(){
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20)); 
        centerPanel.setBackground(Color.BLACK);

        dateSelectPanel = new JPanel();
        dateSelectPanel.setBackground(Color.BLACK);
        
        dateSelectPanel.add(createFilterButton("ALL", false));
        for(int i=10; i<=14; i++){
            dateSelectPanel.add(createFilterButton("11월 " + i + "일", false));
        }

        timeButtonPanel = new JPanel(new GridLayout(0, 5, 20, 20)); // 상영시간표를 5개씩 표시
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

    //버튼 재활용을 위해 상영관 버튼인지 아닌지 boolean으로 구분
    private JButton createFilterButton(String text, boolean isScreenType){
        JButton button = new JButton(text);
        styleButton(button, 16);
        
        button.addActionListener(e -> {
            if (isScreenType) {
                this.selectedScreenType = text;
            } else {
                this.selectedDateStr = text.equals("전체") ? "ALL" : text;
            }
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