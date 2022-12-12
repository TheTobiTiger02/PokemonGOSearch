import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UI {

static Frame frame;
static Panel panel;
static Button addButton;



    public UI() {
        frame = new Frame("Test", Color.WHITE, 1000, 800, true);
        panel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, 0, 1000, 800, true, new BorderLayout());
        addButton = new Button("Auswählen", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        panel.add(addButton);


        frame.add(panel);

        new PokemonList();

    }


}
