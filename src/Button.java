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

        switch(UI.currentState){
            case "login":
                if(e.getSource() == UI.loginButton){
                    UI.loginuser();
                    break;
                }
                if(e.getSource() == UI.registerButton){
                    UI.registerUser();
                    break;
                }
            case "mainMenu":
                if(e.getSource() == UI.searchButton){
                    //UI.hideComponents();
                    UI.showSearchMenu();
                    break;
                }
                if(e.getSource() == UI.checkListButton){
                    //UI.hideComponents();
                    UI.showChecklistMenu();
                    break;
                }
                if(e.getSource() == UI.logoutButton){
                    //UI.hideComponents();
                    UI.showLoginScreen();
                    break;
                }
            case "searchMenu":
                if(e.getSource() == UI.addSearchButton){
                    Object[] options = {"Mit Nummer", "Mit Name"};
                    int selectedOption = JOptionPane.showOptionDialog(UI.frame, "Wie soll die Suche generiert werden?", "Optionen", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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
                    UI.isEditingSearch = false;
                    UI.searchPreviewPanel.setVisible(true);
                    //UI.searchContinueButton.setVisible(true);
                    //UI.searchCompleteButton.setVisible(true);
                    //UI.addPokemonPanel.setVisible(true);
                    //UI.searchPreviewModel.clear();
                    //UI.hideComponents();
                    //UI.fillPreviewModel();
                    UI.showPokemonScreen();
                    break;
                }
                if(e.getSource() == UI.editSearchButton && UI.searchStringList.getSelectedIndex() != -1){
                    UI.editSearch();
                    break;
                }
                if(e.getSource() == UI.deleteSearchButton && UI.searchStringList.getSelectedIndex() != -1){
                    UI.deleteSearch();
                    break;
                }

                if(e.getSource() == UI.importPokemonButton){
                    UI.importPokemon();
                    break;
                }
                if(e.getSource() == UI.searchMenuBackButton) {
                    //UI.searchMenuPanel.setVisible(false);
                    //UI.searchListPanel.setVisible(false);
                    UI.showMainScreen();
                    break;
                }
            case "search":
                if(e.getSource() == UI.addPokemonButton && UI.pokemonJList.getSelectedIndex() != -1){
                    UI.addPokemonToPreview();
                    break;
                }
                if(e.getSource() == UI.removePokemonButton && UI.searchPreviewList.getSelectedIndex() != -1){
                    UI.removePokemonFromPreview();
                    break;
                }
                if(e.getSource() == UI.searchBackButton){
                    //UI.fillPokemonModel();
                    //UI.searchContinueButton.setVisible(false);
                    //UI.searchCompleteButton.setVisible(false);
                    UI.isAdding = false;
                    UI.showSearchMenu();
                    break;
                }
                if(e.getSource() == UI.searchContinueButton){
                    /*UI.searchButtonPanel.setVisible(false);
                    UI.pokemonPanel.setVisible(false);
                    UI.searchPreviewPanel.setVisible(false);
                    UI.addAttributePanel.setVisible(true);
                    UI.removeAttributePanel.setVisible(true);
                    UI.addPokemonPanel.setVisible(false);
                    break;
                     */

                }
                if(e.getSource() == UI.searchCompleteButton){
                    UI.addSearch();
                    break;
                }
            case "checklistMenu":
                if(e.getSource() == UI.addChecklistButton){
                    Object [] options = {"National", "Shiny", "Lucky", "Shadow"};
                    int selectedOption = JOptionPane.showOptionDialog(UI.frame, "Was für eine Checkliste möchtest du erstellen", "Optionen", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    switch(selectedOption){
                        case 0:
                            UI.currentState = "nationalChecklist";
                            UI.checklistType = "national";
                            break;
                        case 1:
                            UI.currentState = "shinyChecklist";
                            UI.checklistType = "shiny";
                            break;
                        case 2:
                            UI.currentState = "luckyChecklist";
                            UI.checklistType = "lucky";
                            break;
                        case 3:
                            UI.currentState = "shadowChecklist";
                            UI.checklistType = "shadow";
                            break;
                        default:
                            break;
                    }
                    UI.isEditingChecklist = false;
                    UI.clearChecklists();
                    UI.showChecklist();
                    break;
                }
                if(e.getSource() == UI.editChecklistButton){
                    UI.editChecklist();
                    break;
                }
                if(e.getSource() == UI.removeChecklistButton){
                    UI.deleteChecklist();
                    break; 
                }
                if(e.getSource() == UI.checklistMenuBackButton){
                    //UI.checklistMenuPanel.setVisible(false);
                    //UI.checklistListPanel.setVisible(false);
                    UI.showMainScreen();
                    break;
                }
            case "nationalChecklist":
                if(e.getSource() == UI.checklistBackButton){
                    //UI.nationalChecklistPanel.setVisible(false);
                    //UI.nationalChecklistScrollPane.setVisible(false);
                    //UI.checklistButtonPanel.setVisible(false);
                    UI.showChecklistMenu();
                    break;
                }
                if(e.getSource() == UI.checklistCompleteButton){
                    UI.addChecklist(UI.nationalChecklistButtons);
                    break;
                }
                for(int i = 0; i < UI.nationalChecklistButtons.size(); i++){
                    if(e.getSource() == UI.nationalChecklistButtons.get(i)){
                        Color newColor;
                        if(UI.nationalChecklistButtons.get(i).getBackground() == Color.GRAY){
                            newColor = UI.checkedColor;
                        }
                        else{
                            newColor = Color.GRAY;
                        }
                        UI.nationalChecklistButtons.get(i).setBackground(newColor);
                        break;
                    }
                }
            case "shinyChecklist":
                if(e.getSource() == UI.checklistBackButton){
                    //UI.shinyChecklistPanel.setVisible(false);
                    //UI.shinyChecklistScrollPane.setVisible(false);
                    //UI.checklistButtonPanel.setVisible(false);
                    UI.showChecklistMenu();
                    break;
                }
                if(e.getSource() == UI.checklistCompleteButton){
                    UI.addChecklist(UI.shinyChecklistButtons);
                    break;
                }
                for(int i = 0; i < UI.shinyChecklistButtons.size(); i++){
                    if(e.getSource() == UI.shinyChecklistButtons.get(i)){
                        Color newColor;
                        if(UI.shinyChecklistButtons.get(i).getBackground() == Color.GRAY){
                            newColor = UI.checkedColor;
                        }
                        else{
                            newColor = Color.GRAY;
                        }
                        UI.shinyChecklistButtons.get(i).setBackground(newColor);
                        break;
                    }
                }
            case "luckyChecklist":
                if(e.getSource() == UI.checklistBackButton){
                    //UI.luckyChecklistPanel.setVisible(false);
                    //UI.luckyChecklistScrollPane.setVisible(false);
                    //UI.checklistButtonPanel.setVisible(false);
                    UI.showChecklistMenu();
                    break;
                }
                if(e.getSource() == UI.checklistCompleteButton){
                    UI.addChecklist(UI.luckyChecklistButtons);
                    break;
                }
                for(int i = 0; i < UI.luckyChecklistButtons.size(); i++){
                    if(e.getSource() == UI.luckyChecklistButtons.get(i)){
                        Color newColor;
                        if(UI.luckyChecklistButtons.get(i).getBackground() == Color.GRAY){
                            newColor = UI.checkedColor;
                        }
                        else{
                            newColor = Color.GRAY;
                        }
                        UI.luckyChecklistButtons.get(i).setBackground(newColor);
                        break;
                    }
                }
            case "shadowChecklist":
                if(e.getSource() == UI.checklistBackButton){
                    //UI.shadowChecklistPanel.setVisible(false);
                    //UI.shadowChecklistScrollPane.setVisible(false);
                    //UI.checklistButtonPanel.setVisible(false);
                    UI.showChecklistMenu();
                    break;
                }
                if(e.getSource() == UI.checklistCompleteButton){
                    UI.addChecklist(UI.shadowChecklistButtons);
                    break;
                }
                for(int i = 0; i < UI.shadowChecklistButtons.size(); i++){
                    if(e.getSource() == UI.shadowChecklistButtons.get(i)){
                        Color newColor;
                        if(UI.shadowChecklistButtons.get(i).getBackground() == Color.GRAY){
                            newColor = UI.checkedColor;
                        }
                        else{
                            newColor = Color.GRAY;
                        }
                        UI.shadowChecklistButtons.get(i).setBackground(newColor);
                        break;
                    }
                }
        }
        
        
        

        /*for(int i = 0; i < UI.nationalChecklistPanel.getComponentCount(); i++){
            if(e.getSource() == UI.nationalChecklistPanel.getComponent(i)){
                Color newColor;
                if(UI.nationalChecklistPanel.getComponent(i).getBackground() == Color.GRAY){
                    newColor = UI.checkedColor;
                }

                else{
                    newColor = Color.GRAY;
                }
                UI.nationalChecklistPanel.getComponent(i).setBackground(newColor);
                for(Map.Entry<String, Button> entry : UI.nationalChecklistButtons.entrySet()){
                    if(Objects.equals(UI.nationalChecklistPanel.getComponent(i), entry.getValue())){
                        UI.updateCheckList(entry.getKey());
                    }
                }


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
