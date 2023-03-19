import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


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


        if(e.getSource() == UI.loginButton){
            UI.loginuser();
        }
        if(e.getSource() == UI.registerButton){
            UI.registerUser();
        }
        if(e.getSource() == UI.addButton){

            Object[] options = {"Mit Nummer", "Mit Name"};
            int selectedOption = JOptionPane.showOptionDialog(null, "Wie soll die Suche generiert werden?", "Optionen", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (selectedOption == 0) {
                UI.queryAsNumber = true;
            }
            else if (selectedOption == 1) {
                UI.queryAsNumber = false;
            }
            else{
                return;
            }
            UI.query = "";
            UI.pokemon = new ArrayList<>();
            UI.searchPreviewPanel.setVisible(true);
            UI.titleLabel.setText("Wähle alle Pokemon aus, die zur Suche hinzugefügt werden sollen");
            UI.showPokemonScreen();
        }
        if(e.getSource() == UI.editButton){
            UI.selectedSearch = UI.searchStringList.getSelectedIndex();
            System.out.println(UI.selectedSearch);

        }
        if(e.getSource() == UI.pokemonButton){
            UI.showPokemonScreen();
        }
        if(e.getSource() == UI.backButton){
            UI.showMainScreen();
            UI.scrollPane.getVerticalScrollBar().setValue(0);
            UI.fillPokemonModel();
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
