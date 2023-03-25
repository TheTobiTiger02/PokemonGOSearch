import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel{



    public Panel(Color background, Color foreground, int x, int y, int width, int height, Font font, boolean visible, LayoutManager layout) {

        this.setBackground(background);
        this.setForeground(foreground);
        this.setBounds(x, y, width, height);
        this.setFont(font);
        this.setLayout(layout);
        this.setVisible(visible);

    }
    public Panel(Color background, Color foreground, int x, int y, int width, int height, boolean visible, LayoutManager layout) {

        this.setBackground(background);
        this.setForeground(foreground);
        this.setBounds(x, y, width, height);
        this.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        this.setLayout(layout);
        this.setVisible(visible);

    }






}
