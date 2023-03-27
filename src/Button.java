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
            UI.isAdding = true;
            UI.isEditing = false;
            UI.searchPreviewPanel.setVisible(true);
            UI.continueButton.setVisible(true);
            UI.completeButton.setVisible(true);
            UI.addPokemonPanel.setVisible(true);
            UI.searchPreviewModel.clear();
            UI.titleLabel.setText("Wähle alle Pokemon aus, die zur Suche hinzugefügt werden sollen");
            UI.fillPreviewModel();
            UI.showPokemonScreen();
        }
        if(e.getSource() == UI.addPokemonButton && UI.pokemonJList.getSelectedIndex() != -1){
            UI.addPokemonToPreview();
        }
        if(e.getSource() == UI.removePokemonButton && UI.searchPreviewList.getSelectedIndex() != -1){
            UI.removePokemonFromPreview();
        }
        if(e.getSource() == UI.editButton && UI.searchStringList.getSelectedIndex() != -1){
            UI.editSearch();
        }
        if(e.getSource() == UI.deleteButton && UI.searchStringList.getSelectedIndex() != -1){
            UI.deleteSearch();
        }
        if(e.getSource() == UI.pokemonButton){
            UI.showPokemonScreen();
        }
        if(e.getSource() == UI.importPokemonButton){
            UI.importPokemon();
        }
        if(e.getSource() == UI.logoutButton){
            UI.showLoginScreen();
        }
        if(e.getSource() == UI.backButton){
            UI.fillPokemonModel();
            UI.continueButton.setVisible(false);
            UI.completeButton.setVisible(false);
            UI.isAdding = false;
            UI.showMainScreen();

        }
        if(e.getSource() == UI.continueButton){
            /*UI.pokemonButtonPanel.setVisible(false);
            UI.pokemonPanel.setVisible(false);
            UI.searchPreviewPanel.setVisible(false);
            UI.addAttributePanel.setVisible(true);
            UI.removeAttributePanel.setVisible(true);
             */
        }
        if(e.getSource() == UI.completeButton){
            UI.addSearch();
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
