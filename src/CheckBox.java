import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CheckBox extends JCheckBox implements ItemListener {

    public CheckBox(String text) {
        this.setText(text);
        this.addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
