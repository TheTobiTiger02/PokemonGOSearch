import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame{



    public Frame(String title, Color backgroundColor, int width, int height, boolean visible) {

        this.setTitle(title);
        this.getContentPane().setBackground(backgroundColor);
        this.setSize(width, height);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(visible);
        this.setLocationRelativeTo(null);
        this.setLayout(null);


    }


}


