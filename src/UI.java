import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class UI {

static Frame frame;
static Panel mainPanel, addPanel;
static Button addButton;


ArrayList<SearchString> searchStrings = new ArrayList<>();



    public UI() {
        searchStrings.add(new SearchString("Test", "1234"));
        new PokemonList();
        frame = new Frame("Test", Color.WHITE, 1000, 800, true);
        mainPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, 0, 1000, 800, true, null);
        addButton = new Button("Auswählen", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        mainPanel.add(addButton);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);

        for(int i = 0; i < searchStrings.size(); i++){
            model.addElement(searchStrings.get(i).getTitle());
        }





        frame.getContentPane().add(mainPanel);


        addPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, 0, 980, 760, true, new BorderLayout());


        JList<String> pokemonJList = new JList<>(PokemonList.pokemonList.toArray(new String[0]));
        pokemonJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
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
                    System.out.println(pokemonJList.getSelectedIndex());

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
        addPanel.add(searchField, BorderLayout.NORTH);
        addPanel.add(scrollPane, BorderLayout.CENTER);

        frame.getContentPane().add(addPanel);









    }


}
