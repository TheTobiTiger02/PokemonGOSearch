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
static JLabel titleLabel;
static Panel mainListPanel, mainButtonPanel, pokemonPanel, backPanel, titlePanel;
static Button addButton,editButton, pokemonButton, backButton;
static DefaultListModel<String> searchModel, pokemonModel;
static JList<String> searchStringList, pokemonJList;

ArrayList<SearchString> searchStrings = new ArrayList<>();





    public UI() {
        new PokemonList();
        searchStrings.add(new SearchString("Test", "1234"));



        frame = new Frame("Test", Color.WHITE, 1000, 800, true);
        titlePanel = new Panel(Color.WHITE,Color.BLACK, 0, 0, 1000, 50, true, null);
        titleLabel = new JLabel("Hauptmenü", SwingConstants.CENTER);
        titleLabel.setBounds(10, 0, 1000, 50);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);
        mainListPanel = new Panel(new Color(100, 50, 50),Color.BLACK, 0, titlePanel.getHeight(), 400, frame.getHeight() - titlePanel.getHeight(), true, null);

        //mainListPanel.add(addButton);

        searchModel = new DefaultListModel<>();
        searchStringList = new JList<>(searchModel);

        for(int i = 0; i < searchStrings.size(); i++){
            searchModel.addElement(searchStrings.get(i).getTitle());
        }
        searchStringList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        searchStringList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
            }
        });
        searchStringList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

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
        });

        //searchStringList.setBounds(0, 0, 200, 1000);
        searchStringList.setBackground(new Color(50, 50, 50));
        searchStringList.setForeground(Color.WHITE);


        JScrollPane test = new JScrollPane(searchStringList);
        test.setBounds(0, 0, 400, 763);
        //mainPanel.add(searchStringList);
        mainListPanel.add(test);
        //mainListPanel.setBackground(new Color(50, 50, 50));


        mainButtonPanel = new Panel(new Color(50, 50, 50), Color.BLACK, mainListPanel.getWidth(), mainListPanel.getY(), frame.getWidth() - mainListPanel.getWidth(), frame.getHeight() - titlePanel.getHeight(), true, null);
        addButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        editButton = new Button("Bearbeiten", new Color(0x767676), Color.WHITE, 0, 100, 200, 100);
        pokemonButton = new Button("Pokémon", new Color(0x767676), Color.WHITE, 0, 200, 200, 100);
        mainButtonPanel.add(addButton);
        mainButtonPanel.add(editButton);
        mainButtonPanel.add(pokemonButton);



        pokemonPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, titlePanel.getHeight(), 400, frame.getHeight() - titlePanel.getHeight(), false, null);


        //JList<String> pokemonJList = new JList<>(PokemonList.pokemonList.toArray(new String[0]));
        pokemonModel = new DefaultListModel<>();
        pokemonJList = new JList<>(pokemonModel);

        for(int i = 0; i < PokemonList.pokemonList.size(); i++){
            pokemonModel.addElement(PokemonList.pokemonList.get(i).getName());
        }
        pokemonJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
        scrollPane.setBounds(0, 20, 800, 650);


        // Create the text field for the search query
        JTextField searchField = new JTextField(20);
        searchField.setBounds(0, 0, 800, 20);

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
                                .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                                .toArray(String[]::new)
                );
            }
        });
        pokemonPanel.add(searchField, BorderLayout.NORTH);
        pokemonPanel.add(scrollPane, BorderLayout.CENTER);

        backPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 800, titlePanel.getHeight(), 200, frame.getHeight() - titlePanel.getHeight(), false, null);
        backButton = new Button("Zurück", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        backPanel.add(backButton);

        frame.getContentPane().add(titlePanel);
        frame.getContentPane().add(mainListPanel);
        frame.getContentPane().add(mainButtonPanel);
        frame.getContentPane().add(pokemonPanel);
        frame.getContentPane().add(backPanel);

        frame.revalidate();
        frame.repaint();







    }

    static void showPokemonScreen() {
        UI.mainListPanel.setVisible(false);
        UI.mainButtonPanel.setVisible(false);
        UI.pokemonPanel.setVisible(true);
        UI.backPanel.setVisible(true);
        UI.searchModel.addElement(new SearchString("1234", "5678").getTitle());
    }

    static void showMainScreen() {
        titleLabel.setText("Hauptmenü");
        pokemonPanel.setVisible(false);
        backPanel.setVisible(false);
        mainListPanel.setVisible(true);
        mainButtonPanel.setVisible(true);
    }


}
