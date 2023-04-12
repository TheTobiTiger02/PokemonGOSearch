import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class UI implements Runnable{


    static Frame frame;
    static JLabel usernameLabel, passwordLabel, addattributeLabel, removeattributeLabel;
    static Panel loginPanel, searchListPanel, mainMenuPanel, searchMenuPanel, checklistMenuPanel, checklistListPanel,
            pokemonPanel, addPokemonPanel, searchButtonPanel, searchPreviewPanel, addAttributePanel, removeAttributePanel,
            nationalChecklistPanel, shinyChecklistPanel, luckyChecklistPanel, shadowChecklistPanel, checklistButtonPanel;
    static Button loginButton, registerButton, searchButton, checkListButton, addSearchButton, editSearchButton, deleteSearchButton,
            searchMenuBackButton, importPokemonButton, logoutButton, checklistMenuBackButton, addChecklistButton, editChecklistButton,
            removeChecklistButton, addPokemonButton, removePokemonButton, searchBackButton, searchContinueButton, searchCompleteButton,
            checklistBackButton, checklistCompleteButton;
    static DefaultListModel<String> searchModel, checklistModel, pokemonModel, searchPreviewModel;
    static JList<String> searchStringList, checklistList, pokemonJList, searchPreviewList;
    static boolean queryAsNumber;
    static String query;
    static JTextField usernameTextField, searchField, searchPreviewSearchField;
    static JPasswordField passwordTextField;
    static JScrollPane pokemonScrollPane, searchListScrollPane, checklistListScrollPane, searchPreviewScrollPane, nationalChecklistScrollPane, shinyChecklistScrollPane, luckyChecklistScrollPane, shadowChecklistScrollPane;

    static Color checkedColor = new Color(0x25be3d);


    static ArrayList<String> attributes;
    static ArrayList<CheckBox> addAttributeCheckBoxes, removeAttributeCheckBoxes;
    static String pokemonQuery = "select * from pokemon";
    static String previewQuery = "";
    static String editingSearchTitle, editingChecklistTitle;
    static String pokemonFilter = "";
    static String previewFilter = "";
    static String checklistType;
    static ArrayList<String> checked;

    static int pokemonModelSize;
    static int searchModelSize = 0;

    static boolean isAdding, isEditingSearch, isEditingChecklist, nationalChecklistIsLoaded, shinyChecklistIsLoaded, luckyChecklistIsLoaded, shadowChecklistIsLoaded;

    static Thread testThread;

    static String currentState = "login";




    static Connection connection;
    static PreparedStatement statement;
    static ResultSet resultSet;

    static ArrayList<Button> nationalChecklistButtons, shinyChecklistButtons, luckyChecklistButtons, shadowChecklistButtons;
    static ArrayList<Pokemon> pokemonList;

    static User activeUser;





    public UI() {


        attributes = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7606827", "sql7606827", "uAPhstaBJb");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




        frame = new Frame("PokemonGoSearch", new Color(50, 50, 50), 1800, 1000, true);

        pokemonList = new ArrayList<>();

        try {
            statement = connection.prepareStatement("select * from pokemon");
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                pokemonList.add(new Pokemon(resultSet.getInt("number"), resultSet.getString("name"), resultSet.getBoolean("shiny"), resultSet.getBoolean("lucky"), resultSet.getBoolean("shadow"), resultSet.getString("title")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        loginPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 0, 0, frame.getWidth(), frame.getHeight(), false, null);
        loginPanel.setName("loginPanel");

        usernameTextField = new JTextField();
        usernameTextField.setBounds((loginPanel.getWidth() / 2) - 200, 300, 300, 25);
        passwordTextField = new JPasswordField();
        passwordTextField.setBounds((usernameTextField.getX()), usernameTextField.getY() + 50, usernameTextField.getWidth(), usernameTextField.getHeight());
        usernameLabel = new JLabel("Benutzername");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(usernameTextField.getX() - 100, 285, 100, 50);
        passwordLabel = new JLabel("Passwort");
        passwordLabel.setBounds(passwordTextField.getX() - 100, 335, 100, 50);
        passwordLabel.setForeground(Color.WHITE);
        loginButton = new Button("Anmelden", new Color(0x767676), Color.WHITE, usernameTextField.getX() + 50, 400 , 100, 50);
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        registerButton = new Button("Registrieren", new Color(0x767676), Color.WHITE, loginButton.getX() + loginButton.getWidth(), 400, 100, 50);
        registerButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        loginPanel.add(usernameTextField);
        loginPanel.add(passwordTextField);
        loginPanel.add(usernameLabel);
        loginPanel.add(passwordLabel);

        mainMenuPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 0, 0, frame.getWidth(), frame.getHeight() , false, null);
        mainMenuPanel.setName("MainMenuPanel");
        searchButton = new Button("Suche", new Color(0x767676), Color.WHITE, (mainMenuPanel.getWidth() / 2) - 200, (mainMenuPanel.getHeight() / 2) - 200 , 200, 100);
        checkListButton = new Button("Checkliste", new Color(0x767676), Color.WHITE, searchButton.getX() + searchButton.getWidth(), searchButton.getY() , searchButton.getWidth(), searchButton.getHeight());
        logoutButton = new Button("Abmelden", new Color(0x767676), Color.WHITE, mainMenuPanel.getWidth() - 215, 0, 200, 100);

        mainMenuPanel.add(searchButton);
        mainMenuPanel.add(checkListButton);
        mainMenuPanel.add(logoutButton);



        searchListPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, 0, 400, frame.getHeight(), false, null);
        searchListPanel.setName("searchListPanel");

        //mainListPanel.add(addButton);

        searchModel = new DefaultListModel<>();
        searchStringList = new JList<>(searchModel);


        searchStringList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //searchStringList.setBounds(0, 0, 200, 1000);
        searchStringList.setBackground(new Color(50, 50, 50));
        searchStringList.setForeground(Color.WHITE);
        searchStringList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && searchStringList.getSelectedIndex() != -1) {
                    try {
                        statement = connection.prepareStatement("select * from search where title = ? and username = ?");
                        statement.setString(1, searchModel.getElementAt(searchStringList.getSelectedIndex()));
                        statement.setString(2, activeUser.getUsername());
                        resultSet = statement.executeQuery();
                        if(resultSet.next()){
                            StringSelection stringSelection = new StringSelection(resultSet.getString("text"));
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(stringSelection, null);
                            JOptionPane.showMessageDialog(frame, "Suche wurde in die Zwischenablage kopiert");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
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


        searchListScrollPane = new JScrollPane(searchStringList);
        //searchListScrollPane.setBounds(0, 0, 400, 713);
        searchListScrollPane.setBounds(0, 0, searchListPanel.getWidth(), searchListPanel.getHeight() - 40);
        searchListScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        //mainPanel.add(searchStringList);
        searchListPanel.add(searchListScrollPane);
        //mainListPanel.setBackground(new Color(50, 50, 50));


        searchMenuPanel = new Panel(new Color(50, 50, 50), Color.BLACK, searchListPanel.getWidth(), searchListPanel.getY(), frame.getWidth() - searchListPanel.getWidth(), frame.getHeight(), false, null);
        searchMenuPanel.setName("searchMenuPanel");
        addSearchButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        editSearchButton = new Button("Bearbeiten", new Color(0x767676), Color.WHITE, 0, addSearchButton.getY() + 100, 200, 100);
        deleteSearchButton = new Button("Entfernen", new Color(0x767676), Color.WHITE, 0, editSearchButton.getY() + 100, 200, 100);
        importPokemonButton = new Button("Pokémon hinzufügen", new Color(0x767676), Color.WHITE, 0, deleteSearchButton.getY() + 100, 200, 100);
        importPokemonButton.setVisible(false);
        searchMenuBackButton = new Button("Zurück", new Color(0x767676), Color.WHITE, searchMenuPanel.getWidth() - 215, 0, 200, 100);
        searchMenuPanel.add(addSearchButton);
        searchMenuPanel.add(editSearchButton);
        searchMenuPanel.add(deleteSearchButton);
        searchMenuPanel.add(importPokemonButton);
        searchMenuPanel.add(searchMenuBackButton);




        pokemonPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, 0, 300, frame.getHeight(), false, null);
        pokemonPanel.setName("pokemonPanel");

        pokemonModel = new DefaultListModel<>();
        pokemonJList = new JList<>(pokemonModel);
        pokemonJList.setBackground(new Color(50, 50, 50));
        pokemonJList.setForeground(Color.WHITE);
        fillPokemonModel();
        pokemonModelSize = pokemonModel.getSize();



        pokemonJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        pokemonJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Get the selected item from the JList

            }
        });

        pokemonJList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2 ) {
                    addPokemonToPreview();
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




        pokemonScrollPane = new JScrollPane(pokemonJList);
        pokemonScrollPane.setBounds(0, 20, pokemonPanel.getWidth(), frame.getHeight() - 57);
        pokemonScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);





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


                String search = searchField.getText();

                if(search.equals("")){
                    pokemonFilter = "";
                }
                else{
                    pokemonFilter = String.format(" (numberChar like '%s' or name like '%s')", search + "%", search + "%");
                }
                fillPokemonModel();
            }
        });
        pokemonPanel.add(searchField);
        pokemonPanel.add(pokemonScrollPane);

        searchButtonPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 800, 0, 200, frame.getHeight(), false, null);
        searchButtonPanel.setName("searchButtonPanel");
        searchBackButton = new Button("Zurück", new Color(0x767676), Color.WHITE, 0, 0, 185, 100);
        searchContinueButton = new Button("Weiter", new Color(0x767676), Color.WHITE, 0, 100, 185, 100);
        searchCompleteButton = new Button("Fertig", new Color(0x767676), Color.WHITE, 0, 200, 185, 100);
        searchButtonPanel.add(searchBackButton);
        searchButtonPanel.add(searchContinueButton);
        searchButtonPanel.add(searchCompleteButton);
        
        checklistListPanel = new Panel(new Color(50, 50, 50), Color.BLACK, 0, 0, 400, frame.getHeight() , false, null);
        checklistListPanel.setName("checklistListPanel");
        
        checklistModel = new DefaultListModel<>();
        checklistList = new JList<>(checklistModel);

        checklistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        checklistList.setBackground(new Color(50, 50, 50));
        checklistList.setForeground(Color.WHITE);
        checklistList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && checklistList.getSelectedIndex() != -1) {
                    editChecklist();
                }
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

        checklistListScrollPane = new JScrollPane(checklistList);
        checklistListScrollPane.setBounds(0, 0, checklistListPanel.getWidth(), checklistListPanel.getHeight() - 40);
        checklistListScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        checklistListPanel.add(checklistListScrollPane);

        
        checklistMenuPanel = new Panel(new Color(50, 50, 50), Color.BLACK, checklistListPanel.getWidth(), checklistListPanel.getY(), frame.getWidth() - checklistListPanel.getWidth(), frame.getHeight() , false, null);
        checklistMenuPanel.setName("checklistMenuPanel");
        addChecklistButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        editChecklistButton = new Button("Bearbeiten", new Color(0x767676), Color.WHITE, 0, addChecklistButton.getY() + 100, 200, 100);
        removeChecklistButton = new Button("Entfernen", new Color(0x767676), Color.WHITE, 0, editChecklistButton.getY() + 100, 200, 100);
        checklistMenuBackButton = new Button("Zurück", new Color(0x767676), Color.WHITE, checklistMenuPanel.getWidth() - 215, 0, 200, 100);
        checklistMenuPanel.add(addChecklistButton);
        checklistMenuPanel.add(editChecklistButton);
        checklistMenuPanel.add(removeChecklistButton);
        checklistMenuPanel.add(checklistMenuBackButton);


        searchPreviewPanel = new Panel(new Color(50, 50, 50), Color.WHITE, pokemonPanel.getWidth() + 200, 0, pokemonPanel.getWidth(), frame.getHeight() , false, null);
        searchPreviewPanel.setName("searchPreviewPanel");

        searchPreviewModel = new DefaultListModel<>();
        searchPreviewList = new JList<>(searchPreviewModel);
        searchPreviewList.setBackground(new Color(50, 50, 50));
        searchPreviewList.setForeground(Color.WHITE);





        searchPreviewList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        searchPreviewList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Get the selected item from the JList

            }
        });
        searchPreviewList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    removePokemonFromPreview();
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




        searchPreviewScrollPane = new JScrollPane(searchPreviewList);
        searchPreviewScrollPane.setBounds(0, 20, searchPreviewPanel.getWidth(), frame.getHeight());
        searchPreviewScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);



        // Create the text field for the search query
        searchPreviewSearchField = new JTextField(20);
        searchPreviewSearchField.setBounds(0, 0, 400, 20);


        // Add a document listener to the text field to update the list of Pokemon
        searchPreviewSearchField.getDocument().addDocumentListener(new DocumentListener() {
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


                String search = searchPreviewSearchField.getText();




                if(search.equals("")){
                    previewFilter = "";
                }
                else {
                    previewFilter = String.format(" (numberChar like '%s' or name like '%s')", search + "%", search + "%");
                }

                fillPreviewModel();

            }
        });
        searchPreviewPanel.add(searchPreviewSearchField, BorderLayout.NORTH);
        searchPreviewPanel.add(searchPreviewScrollPane, BorderLayout.CENTER);

        addPokemonPanel = new Panel(new Color(50, 50, 50), Color.WHITE, pokemonPanel.getWidth(),0, 200, frame.getHeight() , false, null);
        addPokemonPanel.setName("addPokemonPanel");
        addPokemonButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 250, 200, 100);
        removePokemonButton = new Button("Entfernen", new Color(0x767676), Color.WHITE, 0, 350, 200, 100);
        addPokemonPanel.add(addPokemonButton);
        addPokemonPanel.add(removePokemonButton);

        Border blackline = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.WHITE);
        addAttributePanel = new Panel(new Color(50, 50, 50),Color.BLACK, 275, 0, 200, frame.getHeight(), false, null);
        addAttributePanel.setName("addAttributePanel");
        addattributeLabel = new JLabel("Hinzufügen", SwingConstants.CENTER);
        addattributeLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        addattributeLabel.setBounds(0, 0, addAttributePanel.getWidth(), 30);
        addattributeLabel.setForeground(Color.WHITE);
        addattributeLabel.setBackground(new Color(50, 50, 50));
        addattributeLabel.setBorder(blackline);
        addAttributePanel.setBorder(blackline);
        addAttributePanel.add(addattributeLabel);


        addAttributeCheckBoxes = new ArrayList<>();
        int y = 30;
        for(int i = 0; i < attributes.size(); i++){
            CheckBox c = new CheckBox(attributes.get(i));
            c.setBackground(new Color(50, 50, 50));
            c.setForeground(Color.WHITE);
            c.setBounds(2, y, 100, 20);
            addAttributeCheckBoxes.add(c);
            addAttributePanel.add(addAttributeCheckBoxes.get(i));
            y += 20;
        }


        removeAttributePanel = new Panel(new Color(50, 50, 50),Color.BLACK, 475,0, 200, frame.getHeight() , false, null);
        removeAttributePanel.setName("removeAttributePanel");
        removeattributeLabel = new JLabel("Entfernen", SwingConstants.CENTER);
        removeattributeLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        removeattributeLabel.setBounds(0, 0, removeAttributePanel.getWidth(), 30);
        removeattributeLabel.setForeground(Color.WHITE);
        removeattributeLabel.setBackground(new Color(50, 50, 50));
        removeattributeLabel.setBorder(blackline);
        removeAttributePanel.setBorder(blackline);
        removeAttributePanel.add(removeattributeLabel);



        removeAttributeCheckBoxes = new ArrayList<>();
        y = 30;
        for(int i = 0; i < attributes.size(); i++){
            CheckBox c = new CheckBox(attributes.get(i));
            c.setBackground(new Color(50, 50, 50));
            c.setForeground(Color.WHITE);
            c.setBounds(2, y, 100, 20);
            removeAttributeCheckBoxes.add(c);
            removeAttributePanel.add(removeAttributeCheckBoxes.get(i));
            y += 20;
        }

        nationalChecklistPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 0,0, frame.getWidth() - 210, frame.getHeight() - 38, false, null);
        nationalChecklistButtons = new ArrayList<>();


        //System.out.println(checkListButtons.get(1).getIcon());
        nationalChecklistPanel.setLayout(new GridLayout(0, 9));
        nationalChecklistPanel.setName("nationalChecklistPanel");
        nationalChecklistScrollPane = new JScrollPane(nationalChecklistPanel);
        nationalChecklistScrollPane.setName("nationalChecklistScrollPane");
        nationalChecklistScrollPane.setVisible(false);

        nationalChecklistScrollPane.setBounds(0, 0, nationalChecklistPanel.getWidth(), nationalChecklistPanel.getHeight());
        nationalChecklistScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        nationalChecklistScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

        nationalChecklistScrollPane.setBorder(null);
        nationalChecklistScrollPane.setViewportBorder(null);
        nationalChecklistScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));


        shinyChecklistPanel = new Panel(new Color(50, 50, 50), Color.WHITE, nationalChecklistPanel.getX(), nationalChecklistPanel.getY(), nationalChecklistPanel.getWidth(), nationalChecklistPanel.getHeight(), false, null);

        shinyChecklistButtons = new ArrayList<>();
        
        shinyChecklistPanel.setLayout(new GridLayout(0, 9));
        shinyChecklistPanel.setName("shinyChecklistPanel");
        shinyChecklistScrollPane = new JScrollPane(shinyChecklistPanel);
        shinyChecklistScrollPane.setName("shinyChecklistScrollPane");
        shinyChecklistScrollPane.setVisible(false);

        shinyChecklistScrollPane.setBounds(nationalChecklistPanel.getX(), nationalChecklistPanel.getY(), nationalChecklistPanel.getWidth(), nationalChecklistPanel.getHeight());

        shinyChecklistScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        shinyChecklistScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        shinyChecklistScrollPane.setBorder(null);
        shinyChecklistScrollPane.setViewportBorder(null);
        shinyChecklistScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        
        

        luckyChecklistPanel = new Panel(new Color(50, 50, 50), Color.WHITE, nationalChecklistPanel.getX(), nationalChecklistPanel.getY(), nationalChecklistPanel.getWidth(), nationalChecklistPanel.getHeight() , false, null);


        luckyChecklistButtons = new ArrayList<>();

        luckyChecklistPanel.setLayout(new GridLayout(0, 9));
        luckyChecklistPanel.setName("luckyChecklistPanel");
        luckyChecklistScrollPane = new JScrollPane(luckyChecklistPanel);
        luckyChecklistScrollPane.setName("luckyChecklistScrollPane");
        luckyChecklistScrollPane.setVisible(false);

        luckyChecklistScrollPane.setBounds(nationalChecklistPanel.getX(), nationalChecklistPanel.getY(), nationalChecklistPanel.getWidth(), nationalChecklistPanel.getHeight());

        luckyChecklistScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        luckyChecklistScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        luckyChecklistScrollPane.setBorder(null);
        luckyChecklistScrollPane.setViewportBorder(null);
        luckyChecklistScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));



        shadowChecklistPanel = new Panel(new Color(50, 50, 50), Color.WHITE, nationalChecklistPanel.getX(), nationalChecklistPanel.getY(), nationalChecklistPanel.getWidth(), nationalChecklistPanel.getHeight(), false, null);

        shadowChecklistButtons = new ArrayList<>();

        shadowChecklistPanel.setLayout(new GridLayout(0, 9));
        shadowChecklistPanel.setName("shadowChecklistPanel");
        shadowChecklistScrollPane = new JScrollPane(shadowChecklistPanel);
        shadowChecklistScrollPane.setName("shadowChecklistScrollPane");
        shadowChecklistScrollPane.setVisible(false);

        shadowChecklistScrollPane.setBounds(nationalChecklistPanel.getX(), nationalChecklistPanel.getY(), nationalChecklistPanel.getWidth(), nationalChecklistPanel.getHeight());

        shadowChecklistScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        shadowChecklistScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        shadowChecklistScrollPane.setBorder(null);
        shadowChecklistScrollPane.setViewportBorder(null);
        shadowChecklistScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        initChecklists();

        checklistButtonPanel = new Panel(new Color(50, 50, 50), Color.WHITE, nationalChecklistPanel.getWidth(), 0, frame.getWidth() - nationalChecklistPanel.getWidth(), frame.getHeight(), false, null);
        checklistButtonPanel.setName("checklistButtonPanel");
        checklistBackButton = new Button("Zurück", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        checklistCompleteButton = new Button("Fertig", new Color(0x767676), Color.WHITE, 0, checklistBackButton.getHeight(), 200, 100);
        checklistButtonPanel.add(checklistBackButton);
        checklistButtonPanel.add(checklistCompleteButton);







        frame.getContentPane().add(loginPanel);
        frame.getContentPane().add(mainMenuPanel);
        frame.getContentPane().add(searchListPanel);
        frame.getContentPane().add(checklistListPanel);
        frame.getContentPane().add(checklistMenuPanel);
        frame.getContentPane().add(searchMenuPanel);
        frame.getContentPane().add(pokemonPanel);
        frame.getContentPane().add(searchButtonPanel);
        frame.getContentPane().add(searchPreviewPanel);
        frame.getContentPane().add(addPokemonPanel);
        frame.getContentPane().add(addAttributePanel);
        frame.getContentPane().add(removeAttributePanel);
        frame.getContentPane().add(nationalChecklistScrollPane);
        frame.getContentPane().add(shinyChecklistScrollPane);
        frame.getContentPane().add(luckyChecklistScrollPane);
        frame.getContentPane().add(shadowChecklistScrollPane);
        frame.getContentPane().add(checklistButtonPanel);

        Thread printPanels = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    printVisibleComponents();
                    //System.out.println(luckyChecklistPanel.getWidth());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        printPanels.start();

        frame.revalidate();
        frame.repaint();

        showLoginScreen();

    }

    static void initChecklists() {

        Thread nationalChecklistThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PreparedStatement s;
                ResultSet r;
                int i;
                File imgFile;
                BufferedImage img;
                Image scaledImg;
                Icon icon;
                try {
                    s = connection.prepareStatement("select * from pokemon order by number,pokemonorder");
                    r = s.executeQuery();
                    i = 0;
                    while(r.next()){

                        try{
                            imgFile = new File("Addressable Assets\\" + r.getString("id") + ".icon.png");
                            img = ImageIO.read(imgFile);
                            scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(scaledImg);
                        }
                        catch(Exception e){
                            System.out.println(r.getString("number"));
                            continue;
                        }
                        //Icon icon = new ImageIcon("Images\\pm" + resultSet.getString("number") + ".icon.png");

                        Button button = new Button(r.getString("title"), Color.GRAY, Color.BLACK, 0, 0, 300, 200);
                        button.setPreferredSize(new Dimension(150, 150));
                        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
                        button.setVerticalAlignment(SwingConstants.BOTTOM);
                        button.setHorizontalAlignment(SwingConstants.CENTER);
                        button.setVerticalTextPosition(SwingConstants.NORTH);
                        button.setHorizontalTextPosition(SwingConstants.CENTER);
                        button.setIcon(icon);
                        nationalChecklistButtons.add(button);
                        //checkListPanel.add(checkListButtons.get(i));
                        nationalChecklistPanel.add(nationalChecklistButtons.get(i));

                        i++;
                    }
                }
                catch (SQLException e) {
                }



                nationalChecklistIsLoaded = true;

            }

        });
        nationalChecklistThread.start();

        Thread shinyChecklistThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PreparedStatement s;
                ResultSet r;
                int i;
                File imgFile;
                BufferedImage img;
                Image scaledImg;
                Icon icon;

                try {
                    s = connection.prepareStatement("select * from pokemon order by number,pokemonorder");
                    r = s.executeQuery();

                    i = 0;
                    while(r.next()){
                        try{
                            imgFile = new File("Addressable Assets\\" + r.getString("id") + ".s.icon.png");
                            img = ImageIO.read(imgFile);
                            scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(scaledImg);
                        }
                        catch(Exception e){
                            continue;
                        }
                        //Icon icon = new ImageIcon("Images\\pm" + resultSet.getString("number") + ".icon.png");

                        Button button = new Button(r.getString("title"), Color.GRAY, Color.BLACK, 0, 0, 300, 200);
                        button.setPreferredSize(new Dimension(150, 150));
                        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
                        button.setVerticalAlignment(SwingConstants.BOTTOM);
                        button.setHorizontalAlignment(SwingConstants.CENTER);
                        button.setVerticalTextPosition(SwingConstants.NORTH);
                        button.setHorizontalTextPosition(SwingConstants.CENTER);
                        button.setIcon(icon);
                        shinyChecklistButtons.add(button);
                        //checkListPanel.add(checkListButtons.get(i));
                        shinyChecklistPanel.add(shinyChecklistButtons.get(i));
                        i++;
                    }
                }
                catch (SQLException e) {
                }

                shinyChecklistIsLoaded = true;

            }
        });
        shinyChecklistThread.start();

        Thread luckyChecklistThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PreparedStatement s;
                ResultSet r;
                int i;
                File imgFile;
                BufferedImage img;
                Image scaledImg;
                Icon icon;
                i = 0;
                try {
                    s = connection.prepareStatement("select * from pokemon order by number,pokemonorder");
                    r = s.executeQuery();


                    while(r.next()){
                        try{
                            imgFile = new File("Addressable Assets\\" + r.getString("id") + ".icon.png");
                            img = ImageIO.read(imgFile);
                            scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(scaledImg);
                        }
                        catch(Exception e){
                            continue;
                        }

                        //Icon icon = new ImageIcon("Images\\pm" + resultSet.getString("number") + ".icon.png");

                        Button button = new Button(r.getString("title"), Color.GRAY, Color.BLACK, 0, 0, 300, 200);
                        button.setPreferredSize(new Dimension(150, 150));
                        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
                        button.setVerticalAlignment(SwingConstants.BOTTOM);
                        button.setHorizontalAlignment(SwingConstants.CENTER);
                        button.setVerticalTextPosition(SwingConstants.NORTH);
                        button.setHorizontalTextPosition(SwingConstants.CENTER);
                        button.setIcon(icon);
                        //button.setVerticalTextPosition(SwingConstants.NORTH);
                        //button.setHorizontalTextPosition(SwingConstants.TOP);
                        luckyChecklistButtons.add(button);
                        luckyChecklistPanel.add(luckyChecklistButtons.get(i));

                        i++;

                    }
                }
                catch (SQLException e) {
                }

                //luckyChecklistButtons.get(5).setVisible(false);
                //luckyChecklistPanel.remove(luckyChecklistButtons.get(5));


                luckyChecklistIsLoaded = true;
            }
        });
        luckyChecklistThread.start();

        Thread shadowChecklistThread = new Thread(new Runnable() {
            @Override
            public void run() {
                PreparedStatement s;
                ResultSet r;
                int i;
                File imgFile;
                BufferedImage img;
                Image scaledImg;
                Icon icon;
                try {
                    s = connection.prepareStatement("select * from pokemon order by number,pokemonorder");
                    r = s.executeQuery();

                    i = 0;
                    while(r.next()){
                        try{
                            imgFile = new File("Addressable Assets\\" + r.getString("id") + ".icon.png");
                            img = ImageIO.read(imgFile);
                            scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(scaledImg);
                        }
                        catch(Exception e){
                            continue;
                        }

                        //Icon icon = new ImageIcon("Images\\pm" + resultSet.getString("number") + ".icon.png");

                        Button button = new Button(r.getString("title"), Color.GRAY, Color.BLACK, 0, 0, 300, 200);
                        button.setPreferredSize(new Dimension(150, 150));
                        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
                        button.setVerticalAlignment(SwingConstants.BOTTOM);
                        button.setHorizontalAlignment(SwingConstants.CENTER);
                        button.setVerticalTextPosition(SwingConstants.NORTH);
                        button.setHorizontalTextPosition(SwingConstants.CENTER);
                        button.setIcon(icon);
                        shadowChecklistButtons.add(button);
                        //checkListPanel.add(checkListButtons.get(i));
                        shadowChecklistPanel.add(shadowChecklistButtons.get(i));
                        i++;
                    }
                }
                catch (SQLException e) {
                }
                shadowChecklistIsLoaded = true;

            }
        });
        shadowChecklistThread.start();
    }

    static void printVisibleComponents() {
        for(Component c: frame.getContentPane().getComponents()){
            if(c.isVisible() ){
                System.out.println(c.getName());
            }
        }
        System.out.println();
    }

    static void hideComponents() {
        for(Component c : frame.getContentPane().getComponents()){
            c.setVisible(false);
        }
    }

    static void importPokemon() {
        try{
            File file = new File(JOptionPane.showInputDialog(frame, "Gib den Dateipfad zur Textdatei mit den zu importierenden Pokemon an. (Ein Pokemon pro Zeile im Format name,nummer,shiny,lucky,shadow,titel,region,form,kostüm)"));
            Scanner sc = new Scanner(file);
            String [] line;
            while(sc.hasNextLine()){
                line = sc.nextLine().split(",");
                statement = connection.prepareStatement("insert into pokemon(number,numberChar,name,shiny,lucky,shadow,title,region,form,costume)values(?,?,?,?,?,?,?,?,?,?)");
                statement.setInt(1, Integer.parseInt(line[1]));
                statement.setString(2, line[1]);
                statement.setString(3, line[0]);
                statement.setBoolean(4, Boolean.parseBoolean(line[2]));
                statement.setBoolean(5, Boolean.parseBoolean(line[3]));
                statement.setBoolean(6,Boolean.parseBoolean(line[4]));
                statement.setString(7, line[5]);
                statement.setString(8, line[6]);
                if(line[7].equals("null")){
                    statement.setString(9, null);
                }
                else{
                    statement.setString(9, line[7]);
                }
                if(line[8].equals("null")){
                    statement.setString(10, null);
                }
                else{
                    statement.setString(10, line[8]);
                }
                statement.executeUpdate();
            }
        }
        catch(Exception e){

        }
    }

    static void showLoginScreen() {
        hideComponents();
        loginPanel.setVisible(true);
        //searchListPanel.setVisible(false);
        //mainButtonPanel.setVisible(false);
        //mainMenuPanel.setVisible(false);
        usernameTextField.setText("");
        passwordTextField.setText("");
        currentState = "login";
    }

    static void loginuser() {
        try {
            statement = connection.prepareStatement("select * from user where username = ? and password = ?");
            statement.setString(1, usernameTextField.getText());
            statement.setString(2, passwordTextField.getText());
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                JOptionPane.showMessageDialog(frame, "Willkommen " + resultSet.getString("username"));
                activeUser = new User(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("status"));
                if(activeUser.getStatus().equals("admin")){
                    importPokemonButton.setVisible(true);
                }
                //loginPanel.setVisible(false);
                hideComponents();
                showMainScreen();
            }
            else{
                JOptionPane.showMessageDialog(frame, "Account nicht gefunden");
            }
            //statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    static void registerUser() {
        try {
            statement = connection.prepareStatement("insert into user(username,password, status)values(?,?,?)");
            statement.setString(1, usernameTextField.getText());
            statement.setString(2, passwordTextField.getText());
            statement.setString(3, "user");
            statement.executeUpdate();
        }
        catch(SQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(frame, "Account mit Benutzername " + usernameTextField.getText() + " existiert bereits");
            return;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setActiveUser();
        loginPanel.setVisible(false);
        showMainScreen();

    }

    static void setActiveUser() {
        try {
            statement = connection.prepareStatement("select * from user where username = ? and password = ?");
            statement.setString(1, usernameTextField.getText());
            statement.setString(2, passwordTextField.getText());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                activeUser = new User(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("status"));
                //testThread.start();
                if(activeUser.getStatus().equals("admin")){
                    importPokemonButton.setVisible(true);
                }
                updateSearchList();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    static void showSearchMenu() {
        hideComponents();
        updateSearchList();
        searchMenuPanel.setVisible(true);
        searchListPanel.setVisible(true);
        //pokemonPanel.setVisible(false);
        //searchContinueButton.setVisible(false);
        //searchCompleteButton.setVisible(false);
        //searchButtonPanel.setVisible(false);
        //addPokemonPanel.setVisible(false);
        //searchPreviewPanel.setVisible(false);
        pokemonScrollPane.getVerticalScrollBar().setValue(0);
        searchPreviewScrollPane.getVerticalScrollBar().setValue(0);
        searchListScrollPane.getVerticalScrollBar().setValue(0);
        pokemonQuery = "select * from pokemon";
        previewQuery = "";
        searchField.setText("");
        searchPreviewSearchField.setText("");

        currentState = "searchMenu";

    }

    static void showChecklistMenu() {
        hideComponents();
        updateChecklistList();
        nationalChecklistScrollPane.getVerticalScrollBar().setValue(0);
        shinyChecklistScrollPane.getVerticalScrollBar().setValue(0);
        luckyChecklistScrollPane.getVerticalScrollBar().setValue(0);
        shadowChecklistScrollPane.getVerticalScrollBar().setValue(0);
        checklistMenuPanel.setVisible(true);
        checklistListPanel.setVisible(true);
        currentState = "checklistMenu";
    }

    static void addPokemonToPreview() {

        if(pokemonModelSize - pokemonJList.getSelectedIndices().length == 0){
            pokemonQuery = "";
            previewQuery = "select * from pokemon";
        }
        else{
            if(previewQuery.equals("")){
                previewQuery = "select * from pokemon where (numberChar = ";
                pokemonQuery += " where (numberChar != ";
            }
            if(pokemonQuery.contains(")")){
                pokemonQuery = pokemonQuery.replace(")", " and numberChar != ");
            }
            if(previewQuery.contains(")")){
                previewQuery = previewQuery.replace(")", " or numberChar = ");
            }
            for(int i = 0; i < pokemonJList.getSelectedIndices().length; i++){
                pokemonQuery += pokemonModel.getElementAt(pokemonJList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0];
                previewQuery += pokemonModel.getElementAt(pokemonJList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0];
                if(i + 1 < pokemonJList.getSelectedIndices().length){
                    pokemonQuery += " and numberChar != ";
                    previewQuery += " or numberChar = ";
                }
            }



            pokemonQuery += ")";
            previewQuery += ")";
        }

        fillPokemonModel();
        fillPreviewModel();
        pokemonModelSize = pokemonModel.getSize() - pokemonJList.getSelectedIndices().length;
        searchModelSize = searchPreviewModel.getSize() + pokemonJList.getSelectedIndices().length;

    }

    static void removePokemonFromPreview(){




        if(searchModelSize - searchPreviewList.getSelectedIndices().length == 0){
            pokemonQuery = "select * from pokemon";
            previewQuery = "";
        }
        else{
            if(pokemonQuery.equals("")){
                pokemonQuery = "select * from pokemon where (numberChar != ";
                previewQuery += "where (numberChar = ";
            }

            //pokemonQuery = pokemonQuery.replace(")", "");
            //previewQuery = previewQuery.replace(")", "");



            for(int i = 0; i < searchPreviewList.getSelectedIndices().length; i++){
                if(pokemonQuery.contains("and numberChar != " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + " ")){
                    pokemonQuery = pokemonQuery.replace("and numberChar != " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + " ", "");
                    //System.out.println(pokemonQuery);
                }
                else if(pokemonQuery.contains("and numberChar != " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + ")")){
                    pokemonQuery = pokemonQuery.replace("and numberChar != " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + ")", ")");
                }
                else{
                    pokemonQuery = pokemonQuery.replace("numberChar != " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + " ", "");
                    try{
                        pokemonQuery = pokemonQuery.replaceFirst("and", "");
                        pokemonQuery = pokemonQuery.replace("(  ", "(");
                    }
                    catch(Exception e){

                    }
                }
                if(pokemonQuery.contains("  ")){
                    pokemonQuery = pokemonQuery.replace("  ", " ");
                }

                if(previewQuery.contains("or numberChar = " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + " ")){
                    previewQuery = previewQuery.replace("or numberChar = " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + " ", "");
                }
                else if(previewQuery.contains("or numberChar = " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + ")")){
                    previewQuery = previewQuery.replace("or numberChar = " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + ")", ")");
                }
                else{
                    previewQuery = previewQuery.replace("numberChar = " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0] + " ", "");
                    try{
                        previewQuery = previewQuery.replaceFirst("or", "");
                        previewQuery = previewQuery.replace("(  ", "(");
                    }
                    catch(Exception e){

                    }
                }
                if(previewQuery.contains("  ")){
                    previewQuery = previewQuery.replace("  ", " ");
                }

            }

            /*for(int i = 0; i < pokemonModel.getSize(); i++){
                pokemonQuery += pokemonModel.getElementAt(i).split("#")[1].split("\\)")[0];
                pokemonQuery += " or numberChar = ";
                previewQuery += pokemonModel.getElementAt(i).split("#")[1].split("\\)")[0];
                previewQuery += " and numberChar != ";
            }
            for(int i = 0; i < searchPreviewList.getSelectedIndices().length; i++){
                pokemonQuery += searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0];
                previewQuery += searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0];
                if(i + 1 < searchPreviewList.getSelectedIndices().length){
                    pokemonQuery += " or numberChar = ";
                    previewQuery += " and numberChar != ";
                }
            }
             */
            //pokemonQuery += searchPreviewModel.getElementAt(pokemonJList.getSelectedIndex()).split("#")[1].split("\\)")[0] + ")";
            //searchQuery += searchPreviewModel.getElementAt(pokemonJList.getSelectedIndex()).split("#")[1].split("\\)")[0] + ")";
            //pokemonQuery += ")";
            //previewQuery += ")";


        }

        pokemonModelSize = pokemonModel.getSize() + searchPreviewList.getSelectedIndices().length;
        searchModelSize = searchPreviewModel.getSize() - searchPreviewList.getSelectedIndices().length;
        fillPokemonModel();
        fillPreviewModel();


    }

    static void editSearch() {
        try {
            statement = connection.prepareStatement("select * from search where title = ? and username = ?");
            statement.setString(1, searchModel.getElementAt(searchStringList.getSelectedIndex()));
            statement.setString(2, activeUser.getUsername());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                editingSearchTitle = resultSet.getString("title");
                String search = resultSet.getString("text");
                if(search.equals("")){
                    pokemonQuery = "select * from pokemon";
                    previewQuery = "";
                    showPokemonScreen();
                    return;
                }
                try{
                    Integer.parseInt(search.split(",")[0]);
                    queryAsNumber = true;
                }
                catch(NumberFormatException nfe){
                    queryAsNumber = false;
                }

                if(queryAsNumber){
                    pokemonQuery = "select * from pokemon where (numberChar != '" + search.replace(",", "' and numberChar != '");
                    pokemonQuery += "')";
                    previewQuery = pokemonQuery.replace("and", "or").replace("!", "");
                }
                else{
                    String query = "";
                    query += "select numberChar from pokemon where (name = '" + search.replace(",", "' or name = '");
                    query += "'" + ")";
                    statement = connection.prepareStatement(query);
                    ResultSet rs = statement.executeQuery();
                    pokemonQuery = "select * from pokemon where (numberChar != ";
                    ArrayList<String> data = new ArrayList<>();
                    while(rs.next()){
                        data.add(rs.getString("numberChar"));
                    }
                    for(int i = 0; i < data.size(); i++){
                        pokemonQuery += data.get(i);
                        if(i + 1 < data.size()){
                            pokemonQuery += " and numberChar != ";
                        }
                    }
                    pokemonQuery += ")";
                    previewQuery = pokemonQuery.replace("and", "or").replace("!", "");
                }

                try{
                    pokemonQuery = pokemonQuery.replaceAll("'", "");
                    previewQuery = previewQuery.replaceAll("'", "");
                }
                catch(Exception e){

                }
                //fillPokemonModel();
                //fillPreviewModel();
                pokemonModelSize = pokemonModel.getSize();
                searchModelSize = searchPreviewModel.getSize();
                isEditingSearch = true;
                //searchPreviewPanel.setVisible(true);
                //searchContinueButton.setVisible(true);
                //searchCompleteButton.setVisible(true);
                //addPokemonPanel.setVisible(true);
                showPokemonScreen();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void deleteSearch() {
        try {
            statement = connection.prepareStatement("delete from search where title = ? and username = ?");
            statement.setString(1, searchModel.getElementAt(searchStringList.getSelectedIndex()));
            statement.setString(2, activeUser.getUsername());
            statement.execute();
            updateSearchList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    static void showMainScreen() {
        /*updateSearchList(activeUser.getUsername());
        titleLabel.setText("Hauptmenü");
        pokemonPanel.setVisible(false);
        continueButton.setVisible(false);
        completeButton.setVisible(false);
        pokemonButtonPanel.setVisible(false);
        searchListPanel.setVisible(true);
        mainButtonPanel.setVisible(true);
        searchPreviewPanel.setVisible(false);
        addPokemonPanel.setVisible(false);
        pokemonScrollPane.getVerticalScrollBar().setValue(0);
        searchPreviewScrollPane.getVerticalScrollBar().setValue(0);
        searchListScrollPane.getVerticalScrollBar().setValue(0);
        pokemonQuery = "select * from pokemon";
        previewQuery = "";

         */
        hideComponents();
        mainMenuPanel.setVisible(true);
        currentState = "mainMenu";
    }

    static void showPokemonScreen() {
        //searchListPanel.setVisible(false);
        //searchMenuPanel.setVisible(false);
        hideComponents();
        pokemonPanel.setVisible(true);
        searchButtonPanel.setVisible(true);
        searchPreviewPanel.setVisible(true);
        addPokemonPanel.setVisible(true);
        currentState = "search";
        fillPokemonModel();
        fillPreviewModel();
    }

    static void updateSearchList(){
        searchModel.clear();
        try {
            statement = connection.prepareStatement("select * from search where username = ?");
            statement.setString(1, activeUser.getUsername());
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                searchModel.addElement(resultSet.getString("title"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    static void fillPokemonModel() {


        pokemonModel.clear();
        if(pokemonQuery.equals("")){
            return;
        }
        try {
            if(pokemonFilter.equals("")){
                statement = connection.prepareStatement(pokemonQuery);
            }
            else{
                if(pokemonQuery.contains("where")){
                    statement = connection.prepareStatement(pokemonQuery + " and" +  pokemonFilter);
                }
                else{
                    statement = connection.prepareStatement(pokemonQuery + " where" +  pokemonFilter);
                }

            }
            resultSet = statement.executeQuery();


            while(resultSet.next()){
                pokemonModel.addElement(resultSet.getString("name") + " (#" + resultSet.getString("number") + ")");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    static void fillPreviewModel(){

        searchPreviewModel.clear();
        if(previewQuery.equals("")){
            return;
        }
        try {
            if(previewFilter.equals("")){
                statement = connection.prepareStatement(previewQuery);
            }
            else{
                if(previewQuery.contains("where")) {
                    statement = connection.prepareStatement(previewQuery + " and" + previewFilter);
                }
                else{
                    statement = connection.prepareStatement(previewQuery + " where" +  previewFilter);
                }

            }
            resultSet = statement.executeQuery();


            while(resultSet.next()){
                searchPreviewModel.addElement(resultSet.getString("name") + " (#" + resultSet.getString("number") + ")");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void addSearch(){
        String search = "";

        if(queryAsNumber){
            int number;
            for(int i = 0; i < searchPreviewModel.getSize(); i++){
                number = Integer.parseInt(searchPreviewModel.getElementAt(i).split("#")[1].split("\\)")[0]);
                search += String.valueOf(number);
                if(i + 1 < searchPreviewModel.size()){
                    search += ",";
                }
            }
        }
        else{
            for(int i = 0; i < searchPreviewModel.size(); i++){
                search += searchPreviewModel.getElementAt(i).split(" ")[0];
                if(i + 1 < searchPreviewModel.size()){
                    search += ",";
                }
            }
        }
        String title;
        if(isEditingSearch){
            int reply = JOptionPane.showConfirmDialog(frame, "Möchtest du den Titel der Suche ändern?", "Neuer Titel", JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.YES_OPTION){
                title = JOptionPane.showInputDialog(frame, "Neuer Titel");
            }
            else{
                title = editingSearchTitle;
            }
            try {
                statement = connection.prepareStatement("update search set text = ?, title = ? where title = ? and username = ?");
                statement.setString(1, search);
                statement.setString(2, title);
                statement.setString(3, editingSearchTitle);
                statement.setString(4, activeUser.getUsername());
                statement.executeUpdate();
            } catch (SQLException e) {
                return;
            }
        }
        else {
            while (true) {
                try{
                    title = JOptionPane.showInputDialog(frame, "Gib einen Titel für deine Suche ein");
                    if (title.equals("")) {
                        JOptionPane.showMessageDialog(frame, "Du musst zuerst einen Titel eingeben");
                        continue;
                    }
                }
                catch(Exception e){
                    return;
                }


                try {
                    statement = connection.prepareStatement("select * from search where title = ? and username = ?");
                    statement.setString(1, title);
                    statement.setString(2, activeUser.getUsername());
                    resultSet = statement.executeQuery();
                    if (!resultSet.next()) {
                        break;
                    }
                    JOptionPane.showMessageDialog(frame, "Du hast diesen Titel bereits verwendet");

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            try {

                statement = connection.prepareStatement("insert into search(title,text,username)values(?,?,?)");
                statement.setString(1, title);
                statement.setString(2, search);
                statement.setString(3, activeUser.getUsername());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //showMainScreen();
        showSearchMenu();

    }

    static void deleteChecklist() {
        try {
            statement = connection.prepareStatement("delete from checklist where title = ? and username = ?");
            statement.setString(1, checklistModel.getElementAt(checklistList.getSelectedIndex()));
            statement.setString(2, activeUser.getUsername());
            statement.execute();
            updateChecklistList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static void showChecklist() {
        switch(currentState){
            case "nationalChecklist":
                nationalChecklistPanel.setVisible(true);
                nationalChecklistScrollPane.setVisible(true);

                //testThread.start();
                break;
            case "shinyChecklist":
                shinyChecklistPanel.setVisible(true);
                shinyChecklistScrollPane.setVisible(true);

                break;
            case "luckyChecklist":
                luckyChecklistPanel.setVisible(true);
                luckyChecklistScrollPane.setVisible(true);

                //testThread.start();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Scanner sc = new Scanner(System.in);
                        while(true){
                            int number = sc.nextInt() - 1;
                            luckyChecklistButtons.get(number).setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                            for(int i = 0; i < luckyChecklistButtons.size(); i++){
                                if(i != number){
                                    luckyChecklistButtons.get(i).setVisible(false);
                                }
                            }
                            luckyChecklistScrollPane.getVerticalScrollBar().setValue((number / 11) * 150);
                            //luckyChecklistButtons.get(number).setForeground(Color.GREEN);

                        }
                    }
                });
                //t.start();
                break;
            case "shadowChecklist":
                shadowChecklistPanel.setVisible(true);
                shadowChecklistScrollPane.setVisible(true);
                break;
            default:
                return;
        }
        checklistListPanel.setVisible(false);
        checklistMenuPanel.setVisible(false);
        checklistButtonPanel.setVisible(true);
    }

    static void addChecklist(ArrayList<Button> checklist) {
        String checked = "";
        for(int i = 0; i < checklist.size(); i++){
            if(checklist.get(i).getBackground() == checkedColor){
                checked += checklist.get(i).getText();
                if(i + 1 < checklist.size()){
                    checked += ",";
                }
            }
        }
        checked = checked.substring(0, checked.length() - 1);
        String title;
        if(isEditingChecklist){
            int reply = JOptionPane.showConfirmDialog(frame, "Möchtest du den Titel der Checkliste ändern?", "Neuer Titel?", JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.YES_OPTION){
                title = JOptionPane.showInputDialog(frame, "Neuer Titel");
            }
            else{
                title = editingChecklistTitle;
            }
            try {
                statement = connection.prepareStatement("update checklist set content = ?, title = ? where title = ? and username = ?");
                statement.setString(1, checked);
                statement.setString(2, title);
                statement.setString(3, editingChecklistTitle);
                statement.setString(4, activeUser.getUsername());
                statement.executeUpdate();
            } catch (SQLException e) {
                return;
            }
        }

        else{
            while(true) {
                try {
                    title = JOptionPane.showInputDialog(frame, "Gib einen Titel für diese Checkliste ein");
                    if (title.equals("")) {
                        JOptionPane.showMessageDialog(frame, "Du musst zuerst einen Titel eingeben");
                        continue;
                    }
                }
                catch(Exception e){
                    return;
                }

                try{
                    statement = connection.prepareStatement("select * from checklist where title = ? and username = ?");
                    statement.setString(1, title);
                    statement.setString(2, activeUser.getUsername());
                    resultSet = statement.executeQuery();
                    if (!resultSet.next()) {
                        break;
                    }
                    JOptionPane.showMessageDialog(frame, "Du hast diesen Titel bereits verwendet");
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            try {

                statement = connection.prepareStatement("insert into checklist(title,type,username, content)values(?,?,?,?)");
                statement.setString(1, title);
                statement.setString(2, checklistType);
                statement.setString(3, activeUser.getUsername());
                statement.setString(4, checked);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        showChecklistMenu();
    }

    static void editChecklist() {
        try {
            statement = connection.prepareStatement("select * from checklist where title = ? and username = ?");
            statement.setString(1, checklistModel.getElementAt(checklistList.getSelectedIndex()));
            statement.setString(2, activeUser.getUsername());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                checked = new ArrayList<>(Arrays.asList(resultSet.getString("content").split(",")));
                editingChecklistTitle = resultSet.getString("title");
                checklistType = resultSet.getString("type");
                currentState = checklistType + "Checklist";
                isEditingChecklist = true;
                fillChecklist();
                showChecklist();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    static void clearChecklists() {
        for(Button b : nationalChecklistButtons){
            b.setBackground(Color.GRAY);
        }
        for(Button b : shinyChecklistButtons){
            b.setBackground(Color.GRAY);
        }
        for(Button b : luckyChecklistButtons){
            b.setBackground(Color.GRAY);
        }
        for(Button b : shadowChecklistButtons){
            b.setBackground(Color.GRAY);
        }
    }

    static void fillChecklist() {
        clearChecklists();
        switch(checklistType){
            case "national":
                for(Button b : nationalChecklistButtons){
                    if(checked.contains(b.getText())){
                        b.setBackground(checkedColor);
                    }
                }
                break;
            case "shiny":
                for(Button b : shinyChecklistButtons){
                    if(checked.contains(b.getText())){
                        b.setBackground(checkedColor);
                    }
                }
                break;
            case "lucky":
                for(Button b : luckyChecklistButtons){
                    if(checked.contains(b.getText())){
                        b.setBackground(checkedColor);
                    }
                }
                break;
            case "shadow":
                for(Button b : shadowChecklistButtons){
                    if(checked.contains(b.getText())){
                        b.setBackground(checkedColor);
                    }
                }
                break;

        }
    }

    static void updateChecklistList() {
        checklistModel.clear();
        try {
            statement = connection.prepareStatement("select * from checklist where username = ?");
            statement.setString(1, activeUser.getUsername());
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                checklistModel.addElement(resultSet.getString("title"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    static void updateCheckList(String pokemon) {
        try {
            statement = connection.prepareStatement("update checklist set content = CONCAT(content, ?) where username = ?");
            statement.setString(1, pokemon + ",");
            statement.setString(2, activeUser.getUsername());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public void run() {

    }
}
