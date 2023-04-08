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
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UI implements Runnable{


    static Frame frame;
    static JLabel usernameLabel, passwordLabel, titleLabel, addattributeLabel, removeattributeLabel;
    static Panel loginPanel, searchListPanel, mainMenuPanel, searchMenuPanel, checklistMenuPanel, checklistListPanel, pokemonPanel, addPokemonPanel, searchButtonPanel, titlePanel, searchPreviewPanel, addAttributePanel, removeAttributePanel, checkListPanel;
    static Button loginButton, registerButton, searchButton, checkListButton, addSearchButton, editSearchButton, deleteSearchButton, pokemonButton, searchMenuBackButton, importPokemonButton, logoutButton, checklistMenuBackButton, addChecklistButton, editChecklistButton, removeChecklistButton, addPokemonButton, removePokemonButton, searchBackButton, searchContinueButton, searchCompleteButton;
    static DefaultListModel<String> searchModel, checklistModel, pokemonModel, searchPreviewModel;
    static JList<String> searchStringList, checklistList, pokemonJList, searchPreviewList;
    static boolean queryAsNumber;
    static String query;
    static JTextField usernameTextField, searchField, searchPreviewSearchField;
    static JPasswordField passwordTextField;
    static JScrollPane pokemonScrollPane, searchListScrollPane, checklistListScrollPane, searchPreviewScrollPane, checkListScrollPane;


    static ArrayList<String> pokemonList, attributes;
    static ArrayList<CheckBox> addAttributeCheckBoxes, removeAttributeCheckBoxes;
    static ArrayList<Button> checkListButtons;
    static String pokemonQuery = "select * from pokemon";
    static String previewQuery = "";
    static String editingTitle;
    static String pokemonFilter = "";
    static String previewFilter = "";

    static int pokemonModelSize;
    static int searchModelSize = 0;

    static boolean isAdding, isEditing;

    static Thread testThread;

    static Connection connection;
    static PreparedStatement statement;
    static ResultSet resultSet;

    static HashMap<String, Button> test;

    static User activeUser;





    public UI() {


        attributes = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7606827", "sql7606827", "uAPhstaBJb");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




        frame = new Frame("PokemonGoSearch", new Color(50, 50, 50), 1800, 1000, true);
        titlePanel = new Panel(Color.WHITE,Color.BLACK, 0, 0, frame.getWidth(), 50, true, null);
        titlePanel.setName("TitlePanel");
        titleLabel = new JLabel("Login");
        titleLabel.setBounds(frame.getWidth() / 2, 0, 1000, 50);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel, SwingConstants.CENTER);

        checkListPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 0, titlePanel.getHeight(), frame.getWidth() - 50, frame.getHeight() - 100, false, null);

        checkListButtons = new ArrayList<>();
        test = new HashMap<>();

        testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement s = connection.prepareStatement("select number, name from pokemon");
                    ResultSet r = s.executeQuery();

                    int x = 0;
                    int y = 0;
                    while(r.next()){

                        System.out.println(r.getString("name"));
                        File imgFile = new File("Addressable Assets\\pm" + r.getString("number") + ".icon.png");
                        BufferedImage img = ImageIO.read(imgFile);
                        Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        Icon icon = new ImageIcon(scaledImg);
                        //Icon icon = new ImageIcon("Images\\pm" + resultSet.getString("number") + ".icon.png");

                        Button button = new Button(r.getString("name"), Color.GRAY, Color.BLACK, x, y, 300, 200);
                        test.put(button.getText(), button);
                        button.setPreferredSize(new Dimension(100, 200));
                        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
                        button.setVerticalAlignment(SwingConstants.BOTTOM);
                        button.setVerticalTextPosition(SwingConstants.CENTER);
                        button.setIcon(icon);
                        checkListButtons.add(button);
                        //checkListPanel.add(checkListButtons.get(i));
                        checkListPanel.add(test.get(button.getText()));

                    }

                }
                catch (SQLException e) {
                }
                catch (IOException e) {
                }


                try {
                    PreparedStatement s = connection.prepareStatement("select * from checklist where username = ?");
                    s.setString(1, activeUser.getUsername());
                    ResultSet r = s.executeQuery();

                    if(r.next()){
                        String [] content = r.getString("content").split(",");
                        for(String c : content){
                            test.get(c).setBackground(Color.GREEN);
                        }
                    }
                } catch (SQLException e) {

                }


            }
        });




        //System.out.println(checkListButtons.get(1).getIcon());
        checkListPanel.setLayout(new GridLayout(0, 8));
        checkListPanel.setName("checkListPanel");
        checkListScrollPane = new JScrollPane(checkListPanel);
        checkListScrollPane.setName("checkListScrollPane");
        checkListScrollPane.setVisible(false);

        checkListScrollPane.setBounds(0, titlePanel.getHeight(), frame.getWidth() , frame.getHeight() - 100);

        checkListScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        checkListScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);


        loginPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 0, titlePanel.getHeight(), frame.getWidth(), frame.getHeight() - titlePanel.getHeight(), true, null);
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

        mainMenuPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 0, titlePanel.getHeight(), frame.getWidth(), frame.getHeight() - titlePanel.getHeight(), false, null);
        mainMenuPanel.setName("MainMenuPanel");
        searchButton = new Button("Suche", new Color(0x767676), Color.WHITE, (mainMenuPanel.getWidth() / 2) - 200, (mainMenuPanel.getHeight() / 2) - 200 , 200, 100);
        checkListButton = new Button("Checkliste", new Color(0x767676), Color.WHITE, searchButton.getX() + searchButton.getWidth(), searchButton.getY() , searchButton.getWidth(), searchButton.getHeight());
        logoutButton = new Button("Abmelden", new Color(0x767676), Color.WHITE, mainMenuPanel.getWidth() - 215, 0, 200, 100);

        mainMenuPanel.add(searchButton);
        mainMenuPanel.add(checkListButton);
        mainMenuPanel.add(logoutButton);



        searchListPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, titlePanel.getHeight(), 400, frame.getHeight() - titlePanel.getHeight(), false, null);
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
        //mainPanel.add(searchStringList);
        searchListPanel.add(searchListScrollPane);
        //mainListPanel.setBackground(new Color(50, 50, 50));


        searchMenuPanel = new Panel(new Color(50, 50, 50), Color.BLACK, searchListPanel.getWidth(), searchListPanel.getY(), frame.getWidth() - searchListPanel.getWidth(), frame.getHeight() - titlePanel.getHeight(), false, null);
        searchMenuPanel.setName("searchMenuPanel");
        addSearchButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        editSearchButton = new Button("Bearbeiten", new Color(0x767676), Color.WHITE, 0, addSearchButton.getY() + 100, 200, 100);
        deleteSearchButton = new Button("Entfernen", new Color(0x767676), Color.WHITE, 0, editSearchButton.getY() + 100, 200, 100);
        pokemonButton = new Button("Pokémon", new Color(0x767676), Color.WHITE, 0, deleteSearchButton.getY() + 100,200, 100);
        importPokemonButton = new Button("Pokémon hinzufügen", new Color(0x767676), Color.WHITE, 0, pokemonButton.getY() + 100, 200, 100);
        importPokemonButton.setVisible(false);
        searchMenuBackButton = new Button("Zurück", new Color(0x767676), Color.WHITE, searchMenuPanel.getWidth() - 215, 0, 200, 100);
        searchMenuPanel.add(addSearchButton);
        searchMenuPanel.add(editSearchButton);
        searchMenuPanel.add(deleteSearchButton);
        searchMenuPanel.add(pokemonButton);
        searchMenuPanel.add(importPokemonButton);
        searchMenuPanel.add(searchMenuBackButton);




        pokemonPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, titlePanel.getHeight(), 300, frame.getHeight() - titlePanel.getHeight(), false, null);
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

                if (e.getClickCount() == 2 && (isAdding || isEditing)) {
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
        pokemonScrollPane.setBounds(0, 20, pokemonPanel.getWidth(), 693);



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

        searchButtonPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 800, titlePanel.getHeight(), 200, frame.getHeight() - titlePanel.getHeight(), false, null);
        searchButtonPanel.setName("searchButtonPanel");
        searchBackButton = new Button("Zurück", new Color(0x767676), Color.WHITE, 0, 0, 185, 100);
        searchContinueButton = new Button("Weiter", new Color(0x767676), Color.WHITE, 0, 100, 185, 100);
        searchContinueButton.setVisible(false);
        searchCompleteButton = new Button("Fertig", new Color(0x767676), Color.WHITE, 0, 200, 185, 100);
        searchCompleteButton.setVisible(false);
        searchButtonPanel.add(searchBackButton);
        searchButtonPanel.add(searchContinueButton);
        searchButtonPanel.add(searchCompleteButton);
        
        checklistListPanel = new Panel(new Color(50, 50, 50), Color.BLACK, 0, titlePanel.getHeight(), 400, frame.getHeight() - titlePanel.getHeight(), false, null);
        checklistListPanel.setName("checklistListPanel");
        
        checklistModel = new DefaultListModel<>();
        checklistList = new JList<>(checklistModel);

        checklistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        checklistList.setBackground(new Color(50, 50, 50));
        checklistList.setForeground(Color.WHITE);
        checklistList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && searchStringList.getSelectedIndex() != -1) {

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
        checklistListPanel.add(checklistListScrollPane);

        
        checklistMenuPanel = new Panel(new Color(50, 50, 50), Color.BLACK, checklistListPanel.getWidth(), checklistListPanel.getY(), frame.getWidth() - checklistListPanel.getWidth(), frame.getHeight() - titlePanel.getHeight(), false, null);
        checklistMenuPanel.setName("checklistMenuPanel");
        addChecklistButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        editChecklistButton = new Button("Bearbeiten", new Color(0x767676), Color.WHITE, 0, addChecklistButton.getY() + 100, 200, 100);
        removeChecklistButton = new Button("Entfernen", new Color(0x767676), Color.WHITE, 0, editChecklistButton.getY() + 100, 200, 100);
        checklistMenuBackButton = new Button("Zurück", new Color(0x767676), Color.WHITE, checklistMenuPanel.getWidth() - 215, 0, 200, 100);
        checklistMenuPanel.add(addChecklistButton);
        checklistMenuPanel.add(editChecklistButton);
        checklistMenuPanel.add(removeChecklistButton);
        checklistMenuPanel.add(checklistMenuBackButton);


        searchPreviewPanel = new Panel(new Color(50, 50, 50), Color.WHITE, pokemonPanel.getWidth() + 200, titlePanel.getHeight(), pokemonPanel.getWidth(), frame.getHeight() - titlePanel.getHeight(), false, null);
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
        searchPreviewScrollPane.setBounds(0, 20, searchPreviewPanel.getWidth(), 693);



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

        addPokemonPanel = new Panel(new Color(50, 50, 50), Color.WHITE, pokemonPanel.getWidth(), titlePanel.getHeight(), 200, frame.getHeight() - titlePanel.getHeight(), false, null);
        addPokemonPanel.setName("addPokemonPanel");
        addPokemonButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 250, 200, 100);
        removePokemonButton = new Button("Entfernen", new Color(0x767676), Color.WHITE, 0, 350, 200, 100);
        addPokemonPanel.add(addPokemonButton);
        addPokemonPanel.add(removePokemonButton);

        Border blackline = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.WHITE);
        addAttributePanel = new Panel(new Color(50, 50, 50),Color.BLACK, 275, titlePanel.getHeight(), 200, frame.getHeight() - titlePanel.getHeight(), false, null);
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


        removeAttributePanel = new Panel(new Color(50, 50, 50),Color.BLACK, 475, titlePanel.getHeight(), 200, frame.getHeight() - titlePanel.getHeight(), false, null);
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








        frame.getContentPane().add(titlePanel);
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
        frame.getContentPane().add(checkListScrollPane);

        frame.revalidate();
        frame.repaint();

        Thread printPanels = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    printVisibleComponents();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        //printPanels.start();

    }

    static void printVisibleComponents() {
        for(Component c: frame.getContentPane().getComponents()){
            if(c.isVisible() ){
                System.out.println(c.getName());
            }
        }
        System.out.println();
    }

    static void importPokemon() {
        try{
            File file = new File(JOptionPane.showInputDialog(frame, "Gib den Dateipfad zur Textdatei mit den zu importierenden Pokemon an. (Ein Pokemon pro Zeile im Format name,nummer)"));
            System.out.println(file.getName());
            Scanner sc = new Scanner(file);
            String line;
            while(sc.hasNextLine()){
                line = sc.nextLine();
                statement = connection.prepareStatement("insert into pokemon(number,numberChar,name)values(" + Integer.parseInt(line.split(",")[1]) + ",?,?)");
                statement.setString(1, line.split(",")[1]);
                statement.setString(2, line.split(",")[0]);
                statement.executeUpdate();

            }
        }
        catch(Exception e){
            
        }
    }

    static void showLoginScreen() {
        loginPanel.setVisible(true);
        //searchListPanel.setVisible(false);
        //mainButtonPanel.setVisible(false);
        mainMenuPanel.setVisible(false);
        titleLabel.setText("Login");
        usernameTextField.setText("");
        passwordTextField.setText("");
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
                loginPanel.setVisible(false);
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
                testThread.start();
                if(activeUser.getStatus().equals("admin")){
                    importPokemonButton.setVisible(true);
                }
                updateSearchList(activeUser.getUsername());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    static void showSearchMenu() {
        updateSearchList(activeUser.getUsername());
        searchMenuPanel.setVisible(true);
        searchListPanel.setVisible(true);
        pokemonPanel.setVisible(false);
        searchContinueButton.setVisible(false);
        searchCompleteButton.setVisible(false);
        searchButtonPanel.setVisible(false);
        addPokemonPanel.setVisible(false);
        searchPreviewPanel.setVisible(false);
        pokemonScrollPane.getVerticalScrollBar().setValue(0);
        searchPreviewScrollPane.getVerticalScrollBar().setValue(0);
        searchListScrollPane.getVerticalScrollBar().setValue(0);
        pokemonQuery = "select * from pokemon";
        previewQuery = "";
        searchField.setText("");
        searchPreviewSearchField.setText("");

        titleLabel.setText("Suche");

    }

    static void showChecklistMenu() {
        checklistMenuPanel.setVisible(true);
        checklistListPanel.setVisible(true);
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
                System.out.println(pokemonQuery);
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
                        System.out.println("Test");
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
                String search = resultSet.getString("text");
                System.out.println(search);
                try{
                    Integer.parseInt(search.split(",")[0]);
                    queryAsNumber = true;
                }
                catch(NumberFormatException nfe){
                    queryAsNumber = false;
                }
                editingTitle = resultSet.getString("title");

                if(queryAsNumber){
                    pokemonQuery = "select * from pokemon where (numberChar != '" + search.replace(",", "' and numberChar != '");
                    pokemonQuery += "')";
                    previewQuery = pokemonQuery.replace("and", "or").replace("!", "");
                }
                else{
                    String query = "";
                    query += "select numberChar from pokemon where (name = '" + search.replace(",", "' or name = '");
                    query += "'" + ")";
                    System.out.println(query);
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
                fillPokemonModel();
                fillPreviewModel();
                pokemonModelSize = pokemonModel.getSize();
                searchModelSize = searchPreviewModel.getSize();
                isEditing = true;
                searchPreviewPanel.setVisible(true);
                searchContinueButton.setVisible(true);
                searchCompleteButton.setVisible(true);
                addPokemonPanel.setVisible(true);
                UI.titleLabel.setText("Wähle alle Pokemon aus, die zur Suche hinzugefügt werden sollen");
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
            updateSearchList(activeUser.getUsername());
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
        titleLabel.setText("Hauptmenü");
        mainMenuPanel.setVisible(true);
    }

    static void showPokemonScreen() {
        UI.searchListPanel.setVisible(false);
        UI.searchMenuPanel.setVisible(false);
        UI.pokemonPanel.setVisible(true);
        UI.searchButtonPanel.setVisible(true);
        fillPokemonModel();
    }

    static void updateSearchList(String username){
        searchModel.clear();
        try {
            statement = connection.prepareStatement("select * from search where username = ?");
            statement.setString(1, username);
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
            System.out.println(statement);
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
            System.out.println(statement);
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
        if(isEditing){
            int reply = JOptionPane.showConfirmDialog(frame, "Möchtest du den Titel der Suche ändern?", "Neuer Titel", JOptionPane.YES_NO_OPTION);
            if(reply == JOptionPane.YES_OPTION){
                title = JOptionPane.showInputDialog(frame, "Neuer Titel");
            }
            else{
                title = editingTitle;
            }
            try {
                statement = connection.prepareStatement("update search set text = ?, title = ? where title = ? and username = ?");
                statement.setString(1, search);
                statement.setString(2, title);
                statement.setString(3, editingTitle);
                statement.setString(4, activeUser.getUsername());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {


            while (true) {
                try{
                    title = JOptionPane.showInputDialog(frame, "Gib einen Titel für deine Suche ein");
                    if (title.equals("")) {
                        JOptionPane.showMessageDialog(frame, "Du musst zuerst einen Titel hinzufügen");
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

    static void updateCheckList(String pokemon) {
        try {
            statement = connection.prepareStatement("update checklist set content = CONCAT(content, ?) where username = ?");
            statement.setString(1, pokemon + ",");
            statement.setString(2, activeUser.getUsername());
            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public void run() {

    }
}
