import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class Test  {

    public static void main(String[] args) {
        ArrayList<Integer> test = new ArrayList<>();
        test.add(1, 5);
        System.out.println(test.get(1));
    }

}

