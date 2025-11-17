package moviebooking.view;

import moviebooking.controller.MainController;

import javax.swing.*;
import java.awt.*;

public class ShowAllMoviesPanel extends JPanel{
    MainController mainController;

    public ShowAllMoviesPanel(MainController mainController) {
        this.mainController = mainController;
        setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Show All Movies Panel");
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(e -> {
            mainController.showMovieListView();
        });
        add(backButton);
    }
    
}