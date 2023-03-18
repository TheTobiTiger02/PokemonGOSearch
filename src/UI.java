import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UI {


static Frame frame;
static JLabel titleLabel;
static Panel mainListPanel, mainButtonPanel, pokemonPanel, pokemonButtonPanel, titlePanel, queryPreviewPanel;
static Button addButton,editButton, pokemonButton, backButton;
static DefaultListModel<String> searchModel, pokemonModel;
static JList<String> searchStringList, pokemonJList;
static int selectedSearch;
static boolean queryAsNumber;
static String query;
static JTextArea queryPreviewTextField;
static JTextField searchField;

static ArrayList<Pokemon> pokemon;
static ArrayList<SearchString> searchStrings = new ArrayList<>();

static Connection connection;
static PreparedStatement statement;





    public UI() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7606827", "sql7606827", "uAPhstaBJb");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        new PokemonList();

        for(int i = 0; i < 100; i++){
            searchStrings.add(new SearchString(PokemonList.pokemonList.get(0).getName(), new Pokemon[] {PokemonList.pokemonList.get(0)}, false));
        }




        frame = new Frame("Test", Color.WHITE, 1000, 800, true);
        titlePanel = new Panel(Color.WHITE,Color.BLACK, 0, 0, 1000, 50, true, null);
        titleLabel = new JLabel("Hauptmenü", SwingConstants.CENTER);
        titleLabel.setBounds(10, 0, 1000, 50);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);
        mainListPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, titlePanel.getHeight(), 400, frame.getHeight() - titlePanel.getHeight(), true, null);

        //mainListPanel.add(addButton);

        searchModel = new DefaultListModel<>();
        searchStringList = new JList<>(searchModel);

        for(int i = 0; i < searchStrings.size(); i++){
            searchModel.addElement(searchStrings.get(i).getTitle());
        }
        searchStringList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        test.setBounds(0, 0, 400, 713);
        //mainPanel.add(searchStringList);
        mainListPanel.add(test);
        //mainListPanel.setBackground(new Color(50, 50, 50));


        mainButtonPanel = new Panel(new Color(100, 50, 50), Color.BLACK, mainListPanel.getWidth(), mainListPanel.getY(), frame.getWidth() - mainListPanel.getWidth(), frame.getHeight() - titlePanel.getHeight(), true, null);
        addButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        editButton = new Button("Bearbeiten", new Color(0x767676), Color.WHITE, 0, 100, 200, 100);
        pokemonButton = new Button("Pokémon", new Color(0x767676), Color.WHITE, 0, 200, 200, 100);
        mainButtonPanel.add(addButton);
        mainButtonPanel.add(editButton);
        mainButtonPanel.add(pokemonButton);



        pokemonPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, titlePanel.getHeight(), 400, frame.getHeight() - titlePanel.getHeight(), false, null);


        //JList<String> pokemonJList = new JList<>(PokemonList.pokemonList.toArray(new String[0]));
        pokemonModel = new DefaultListModel<>();
        fillPokemonModel();
        pokemonJList = new JList<>(pokemonModel);
        pokemonJList.setBackground(new Color(50, 50, 50));
        pokemonJList.setForeground(Color.WHITE);




        pokemonJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        pokemonJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Get the selected item from the JList
                String selectedItem = pokemonJList.getSelectedValue();


                // Print the selected item to the console
                //System.out.println("Selected item: " + selectedItem);
            }
        });
        pokemonJList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Get the selected item from the JList
                    queryPreviewTextField.setText("");
                    pokemon.add(PokemonList.pokemonList.get(pokemonJList.getSelectedIndex()));
                    Collections.sort(pokemon, new Comparator<Pokemon>() {
                        @Override
                        public int compare(Pokemon p1, Pokemon p2) {
                            return Integer.compare(p1.getId(), p2.getId());
                        }
                    });
                    for(Pokemon p : pokemon){
                        //System.out.println(p.getId());
                    }
                    //query+= pokemonJList.getSelectedValue() + ",";
                    if(queryAsNumber){
                        for(Pokemon p : pokemon){
                            queryPreviewTextField.setText(queryPreviewTextField.getText() + p.getId() + ",");
                        }
                    }
                    else {
                        for(Pokemon p : pokemon){
                            queryPreviewTextField.setText(queryPreviewTextField.getText() + p.getName() + ",");
                        }
                    }



                    String selectedItem = pokemonJList.getSelectedValue();
                    //System.out.println(pokemonJList.getSelectedIndex());

                    // Print the selected item to the console
                    //System.out.println("Double-clicked item: " + selectedItem);
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
        scrollPane.setBounds(0, 20, 400, 693);


        // Create the text field for the search query
        searchField = new JTextField(20);
        searchField.setBounds(0, 0, 400, 20);


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
                System.out.println(query);

                // Update the list of Pokemon to only include those that match the search query
                pokemonJList.setListData(
                        PokemonList.pokemonList.stream()
                                .filter(p -> p.getText().toLowerCase().contains(query.toLowerCase()))
                                .map(Pokemon::getText)
                                .toArray(String[]::new)
                );
            }
        });
        pokemonPanel.add(searchField, BorderLayout.NORTH);
        pokemonPanel.add(scrollPane, BorderLayout.CENTER);

        pokemonButtonPanel = new Panel(new Color(100, 50, 50), Color.WHITE, 800, titlePanel.getHeight(), 200, frame.getHeight() - titlePanel.getHeight(), false, null);
        backButton = new Button("Zurück", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        pokemonButtonPanel.add(backButton);

        queryPreviewPanel = new Panel(new Color(50, 50, 50), Color.WHITE, pokemonPanel.getWidth(), titlePanel.getHeight(), frame.getWidth() - pokemonPanel.getWidth() - pokemonButtonPanel.getWidth(), frame.getHeight() - titlePanel.getHeight(), false, null);
        queryPreviewTextField = new JTextArea();
        queryPreviewTextField.setBounds(0, 0, queryPreviewPanel.getWidth(), queryPreviewPanel.getHeight());
        queryPreviewTextField.setEditable(false);
        queryPreviewTextField.setLineWrap(true);
        queryPreviewTextField.setWrapStyleWord(true);
        queryPreviewPanel.add(queryPreviewTextField);



        frame.getContentPane().add(titlePanel);
        frame.getContentPane().add(mainListPanel);
        frame.getContentPane().add(mainButtonPanel);
        frame.getContentPane().add(pokemonPanel);
        frame.getContentPane().add(pokemonButtonPanel);
        frame.getContentPane().add(queryPreviewPanel);

        frame.revalidate();
        frame.repaint();







    }

    static void showPokemonScreen() {
        UI.mainListPanel.setVisible(false);
        UI.mainButtonPanel.setVisible(false);
        UI.pokemonPanel.setVisible(true);
        UI.pokemonButtonPanel.setVisible(true);
        //UI.searchModel.addElement(new SearchString("1234", "5678").getTitle());
    }

    static void showMainScreen() {
        titleLabel.setText("Hauptmenü");
        pokemonPanel.setVisible(false);
        pokemonButtonPanel.setVisible(false);
        mainListPanel.setVisible(true);
        mainButtonPanel.setVisible(true);
    }

    static void fillPokemonModel() {

        System.out.println("Test");
        try {
            statement = connection.prepareStatement("select * from pokemon");
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                pokemonModel.addElement(rs.getString("name") + " (#" + String.format("%04d", Integer.parseInt(rs.getString("number"))) + ")");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
