import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class Test  {

    public static void main(String[] args) {
        String s = "select * from pokemon where (name = 'Butterfree')";
        if(s.contains("name = 'Butterfree'")){
            System.out.println("Test");
        }
        System.out.println(s.replace("name = 'Butterfree'", ""));
    }

}

