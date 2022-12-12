import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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

        if(e.getSource() == UI.addButton){
            JList<String> pokemonJList = new JList<>(PokemonList.pokemonList.toArray(new String[0]));
            pokemonJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            pokemonJList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    // Get the selected item from the JList
                    String selectedItem = pokemonJList.getSelectedValue();

                    // Print the selected item to the console
                    System.out.println("Selected item: " + selectedItem);
                }
            });
            pokemonJList.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        // Get the selected item from the JList
                        String selectedItem = pokemonJList.getSelectedValue();

                        // Print the selected item to the console
                        System.out.println("Double-clicked item: " + selectedItem);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });


            JScrollPane scrollPane = new JScrollPane(pokemonJList);


            // Create the text field for the search query
            JTextField searchField = new JTextField(20);

            // Add a document listener to the text field to update the list of Pokemon
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateList();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateList();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateList();
                }

                private void updateList() {
                    // Get the search query
                    String query = searchField.getText();

                    // Update the list of Pokemon to only include those that match the search query
                    pokemonJList.setListData(
                            PokemonList.pokemonList.stream()
                                    .filter(p -> p.toLowerCase().contains(query.toLowerCase()))
                                    .toArray(String[]::new)
                    );
                }
            });

            Panel panel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, 0, 980, 760, true, new BorderLayout());
            //JPanel panel = new JPanel(new BorderLayout());
            panel.add(searchField, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);
            //panel.setVisible(true);
            UI.panel.setVisible(false);
            UI.frame.add(panel);
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
