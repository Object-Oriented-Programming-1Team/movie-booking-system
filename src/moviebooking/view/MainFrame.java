package moviebooking.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("Movie Booking System");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        add(mainPanel);
    }

    public void addPanel(JPanel panel, String name){
        mainPanel.add(panel, name);
    }

    public void showPanel(String panelName){
        cardLayout.show(mainPanel, panelName);
    }
}
