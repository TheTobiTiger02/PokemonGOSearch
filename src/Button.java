import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Button extends JButton implements MouseListener {

    private Border defaultBorder = BorderFactory.createLineBorder(new Color(0x4C4F4F), 3);
    private Font defaultFont = new Font("Times New Roman", Font.PLAIN, 30);


    public Button(String text, Color background, Color foreground, Font font, Border border, int x, int y, int width, int height){

        this.setText(text);
        this.setBackground(background);
        this.setForeground(foreground);
        this.setFont(font);
        this.setBorder(border);
        this.setBounds(x, y, width, height);
        this.addMouseListener(this);
    }

    public Button(String text, Color background, Color foreground, Font font, int x, int y, int width, int height){

        this.setText(text);
        this.setBackground(background);
        this.setForeground(foreground);
        this.setFont(font);
        this.setBorder(defaultBorder);
        this.setBounds(x, y, width, height);
        this.addMouseListener(this);
    }

    public Button(String text, Color background, Color foreground, Border border, int x, int y, int width, int height){

        this.setText(text);
        this.setBackground(background);
        this.setForeground(foreground);
        this.setFont(defaultFont);
        this.setBorder(border);
        this.setBounds(x, y, width, height);
        this.addMouseListener(this);
    }

    public Button(String text, Color background, Color foreground, int x, int y, int width, int height){

        this.setText(text);
        this.setBackground(background);
        this.setForeground(foreground);
        this.setFont(defaultFont);
        this.setBorder(defaultBorder);
        this.setBounds(x, y, width, height);
        this.addMouseListener(this);
    }




    @Override
    public void mouseClicked(MouseEvent e) {
        if(this.getText().equals("Test")){
            System.out.println("Click");
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if(e.getSource() == UI.addButton){
            UI.titleLabel.setText("Wähle alle Pokemon aus, die zur Suche hinzugefügt werden sollen");
            UI.showPokemonScreen();
        }
        if(e.getSource() == UI.editButton){
            int index = UI.searchStringList.getSelectedIndex();
            System.out.println(index);

        }
        if(e.getSource() == UI.pokemonButton){
            UI.showPokemonScreen();
        }
        if(e.getSource() == UI.backButton){
            UI.showMainScreen();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
