package moviebooking.view;

import moviebooking.controller.MainController;

import javax.swing.*;
import java.awt.*;

public class CheckReservationsPanel extends JPanel {

    private MainController mainController;

    public CheckReservationsPanel(MainController mainController) {
        this.mainController = mainController;
        setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("예매 조회 화면");
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(e -> {
            mainController.showMovieListView();
        });
        add(backButton);
    }

}
