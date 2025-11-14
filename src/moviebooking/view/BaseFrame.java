package moviebooking.view;

import javax.swing.*;
import java.awt.*;

public abstract class BaseFrame extends JFrame {
    public static final String englishFont = "Bernard MT Condensed";
    public static final String koreanFont = "맑은 고딕";
    public static final Color redColorRGB = new Color(0xc01919);
    public static final Color darkBlueColorRGB = new Color(0x162470);
    
    // (참고: movieList는 상수가 아니라 MainFrame 등이 생성자에서 
    //  Manager를 통해 받아야 할 '인스턴스 변수'이므로 여기에 넣지 않습니다.)

    /**
     * [재사용 모듈]
     * 뒤로 가기 버튼을 생성합니다.
     * 이 메서드를 호출하는 'this'는 BaseFrame을 상속받은 MainFrame, TimeSelect 등이 됩니다.
     */
    //뒤로 가기 버튼 모듈
    public JButton createBackButton(JFrame currentFrame) {
        JButton backButton = new JButton("◀");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.GRAY);
        backButton.setFont(new Font(koreanFont, Font.BOLD, 50));
        
        backButton.addActionListener(e -> {
            currentFrame.setVisible(false);
        });
        
        backButton.setHorizontalAlignment(SwingConstants.LEFT);
        return backButton;
    }
}