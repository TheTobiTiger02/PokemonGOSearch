import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.Objects;


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
        this.setFocusPainted(false);

    }

    public Button(String text, Color background, Color foreground, Font font, int x, int y, int width, int height){
        this.setText(text);
        this.setBackground(background);
        this.setForeground(foreground);
        this.setFont(font);
        this.setBorder(defaultBorder);
        this.setBounds(x, y, width, height);
        this.addMouseListener(this);
        this.setFocusPainted(false);

    }

    public Button(String text, Color background, Color foreground, Border border, int x, int y, int width, int height){

        this.setText(text);
        this.setBackground(background);
        this.setForeground(foreground);
        this.setFont(defaultFont);
        this.setBorder(border);
        this.setBounds(x, y, width, height);
        this.addMouseListener(this);
        this.setFocusPainted(false);

    }

    public Button(String text, Color background, Color foreground, int x, int y, int width, int height){

        this.setText(text);
        this.setBackground(background);
        this.setForeground(foreground);
        this.setFont(defaultFont);
        this.setBorder(defaultBorder);
        this.setBounds(x, y, width, height);
        this.addMouseListener(this);
        this.setFocusPainted(false);

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
        if(e.getSource() == UI.searchButton){
            UI.mainMenuPanel.setVisible(false);
            UI.showSearchMenu();
        }
        if(e.getSource() == UI.checkListButton){
            UI.mainMenuPanel.setVisible(false);
            UI.showChecklistMenu();
        }
        if(e.getSource() == UI.addSearchButton){

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
            UI.searchContinueButton.setVisible(true);
            UI.searchCompleteButton.setVisible(true);
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
        if(e.getSource() == UI.editSearchButton && UI.searchStringList.getSelectedIndex() != -1){
            UI.editSearch();
        }
        if(e.getSource() == UI.deleteSearchButton && UI.searchStringList.getSelectedIndex() != -1){
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
        if(e.getSource() == UI.searchBackButton){
            UI.fillPokemonModel();
            UI.searchContinueButton.setVisible(false);
            UI.searchCompleteButton.setVisible(false);
            UI.isAdding = false;
            UI.showSearchMenu();
        }
        if(e.getSource() == UI.searchMenuBackButton) {
            UI.searchMenuPanel.setVisible(false);
            UI.searchListPanel.setVisible(false);
            UI.showMainScreen();
        }
        if(e.getSource() == UI.searchContinueButton){
            /*UI.searchButtonPanel.setVisible(false);
            UI.pokemonPanel.setVisible(false);
            UI.searchPreviewPanel.setVisible(false);
            UI.addAttributePanel.setVisible(true);
            UI.removeAttributePanel.setVisible(true);
            UI.addPokemonPanel.setVisible(false);
             */

        }
        if(e.getSource() == UI.searchCompleteButton){
            UI.addSearch();
        }
        if(e.getSource() == UI.addChecklistButton){

        }
        if(e.getSource() == UI.editChecklistButton){

        }
        if(e.getSource() == UI.removeChecklistButton){

        }
        if(e.getSource() == UI.checklistMenuBackButton){
            UI.checklistMenuPanel.setVisible(false);
            UI.checklistListPanel.setVisible(false);
            UI.showMainScreen();
        }
        for(int i = 0; i < UI.checkListPanel.getComponentCount(); i++){
            if(e.getSource() == UI.checkListPanel.getComponent(i)){
                Color newColor;
                if(UI.checkListPanel.getComponent(i).getBackground() == Color.GRAY){
                    newColor = Color.GREEN;
                }

                else{
                    newColor = Color.GRAY;
                }
                UI.checkListPanel.getComponent(i).setBackground(newColor);
                for(Map.Entry<String, Button> entry : UI.test.entrySet()){
                    if(Objects.equals(UI.checkListPanel.getComponent(i), entry.getValue())){
                        UI.updateCheckList(entry.getKey());
                    }
                }


            }
        }
        /*for(int i = 0; i < UI.checkListButtons.size(); i++){
            if(e.getSource() == UI.checkListButtons.get(i)){
                Color newColor;
                if(UI.checkListButtons.get(i).getBackground() == Color.GRAY){
                    newColor = Color.GREEN;
                }
                else{
                    newColor = Color.GRAY;
                }
                UI.checkListButtons.get(i).setBackground(newColor);
            }
        }

         */
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
