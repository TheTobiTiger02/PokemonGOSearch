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
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UI implements Runnable{


    static Frame frame;
    static JLabel usernameLabel, passwordLabel, titleLabel, addattributeLabel, removeattributeLabel;
    static Panel loginPanel, searchListPanel, mainButtonPanel, pokemonPanel, addPokemonPanel, pokemonButtonPanel, titlePanel, searchPreviewPanel, addAttributePanel, removeAttributePanel, checkListPanel;
    static Button loginButton, registerButton, addButton, editButton, deleteButton, pokemonButton, importPokemonButton, logoutButton, addPokemonButton, removePokemonButton, backButton, continueButton, completeButton;
    static DefaultListModel<String> searchModel, pokemonModel, searchPreviewModel;
    static JList<String> searchStringList, pokemonJList, searchPreviewList;
    static boolean queryAsNumber;
    static String query;
    static JTextField usernameTextField, searchField, searchPreviewSearchField;
    static JPasswordField passwordTextField;
    static JScrollPane pokemonScrollPane, searchListScrollPane, searchPreviewScrollPane, checkListScrollPane;


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

    static Connection connection;
    static PreparedStatement statement;
    static ResultSet resultSet;

    static User activeUser;





    public UI() {


        attributes = new ArrayList<>();



        try {
            connection = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7606827", "sql7606827", "uAPhstaBJb");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




        frame = new Frame("PokemonGoSearch", new Color(50, 50, 50), 1000, 800, true);
        titlePanel = new Panel(Color.WHITE,Color.BLACK, 0, 0, 1000, 50, true, null);
        titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setBounds(10, 0, 1000, 50);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);

        checkListPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 0, titlePanel.getHeight(), frame.getWidth(), frame.getHeight() - titlePanel.getHeight(), false, null);

        checkListButtons = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            checkListButtons.add(new Button("Button " + i, Color.GREEN, Color.WHITE, 0, 0, 200, 200));
            checkListPanel.add(checkListButtons.get(i));
        }
        checkListPanel.setLayout(new GridLayout(checkListButtons.size() / 5, 5));
        checkListScrollPane = new JScrollPane(checkListPanel);
        checkListScrollPane.setVisible(false);
        checkListScrollPane.setBounds(50, 50, 900, 700);

        checkListScrollPane.getVerticalScrollBar().setUnitIncrement(16);


        loginPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 0, titlePanel.getHeight(), frame.getWidth(), frame.getHeight() - titlePanel.getHeight(), true, null);
        loginButton = new Button("Anmelden", new Color(0x767676), Color.WHITE, 390, 400 , 100, 50);
        loginButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        registerButton = new Button("Registrieren", new Color(0x767676), Color.WHITE, 490, 400, 100, 50);
        registerButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        usernameTextField = new JTextField();
        usernameTextField.setBounds(350, 300, 300, 25);
        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(350, 350, 300, 25);
        usernameLabel = new JLabel("Benutzername");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(250, 285, 100, 50);
        passwordLabel = new JLabel("Passwort");
        passwordLabel.setBounds(250, 335, 100, 50);
        passwordLabel.setForeground(Color.WHITE);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        loginPanel.add(usernameTextField);
        loginPanel.add(passwordTextField);
        loginPanel.add(usernameLabel);
        loginPanel.add(passwordLabel);


        searchListPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, titlePanel.getHeight(), 400, frame.getHeight() - titlePanel.getHeight(), false, null);

        //mainListPanel.add(addButton);

        searchModel = new DefaultListModel<>();
        searchStringList = new JList<>(searchModel);


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
        searchListScrollPane.setBounds(0, 0, 400, 713);
        //mainPanel.add(searchStringList);
        searchListPanel.add(searchListScrollPane);
        //mainListPanel.setBackground(new Color(50, 50, 50));


        mainButtonPanel = new Panel(new Color(50, 50, 50), Color.BLACK, searchListPanel.getWidth(), searchListPanel.getY(), frame.getWidth() - searchListPanel.getWidth(), frame.getHeight() - titlePanel.getHeight(), false, null);
        addButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 0, 200, 100);
        editButton = new Button("Bearbeiten", new Color(0x767676), Color.WHITE, 0, 100, 200, 100);
        deleteButton = new Button("Entfernen", new Color(0x767676), Color.WHITE, 0, 200, 200, 100);
        pokemonButton = new Button("Pokémon", new Color(0x767676), Color.WHITE, 0, 300, 200, 100);
        importPokemonButton = new Button("Pokémon hinzufügen", new Color(0x767676), Color.WHITE, 0, 400, 200, 100);
        logoutButton = new Button("Abmelden", new Color(0x767676), Color.WHITE, mainButtonPanel.getWidth() - 215, 0, 200, 100);
        importPokemonButton.setVisible(false);
        mainButtonPanel.add(addButton);
        mainButtonPanel.add(editButton);
        mainButtonPanel.add(deleteButton);
        mainButtonPanel.add(pokemonButton);
        mainButtonPanel.add(importPokemonButton);
        mainButtonPanel.add(logoutButton);



        pokemonPanel = new Panel(new Color(50, 50, 50),Color.BLACK, 0, titlePanel.getHeight(), 300, frame.getHeight() - titlePanel.getHeight(), false, null);


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

        pokemonButtonPanel = new Panel(new Color(50, 50, 50), Color.WHITE, 800, titlePanel.getHeight(), 200, frame.getHeight() - titlePanel.getHeight(), false, null);
        backButton = new Button("Zurück", new Color(0x767676), Color.WHITE, 0, 0, 185, 100);
        continueButton = new Button("Weiter", new Color(0x767676), Color.WHITE, 0, 100, 185, 100);
        continueButton.setVisible(false);
        completeButton = new Button("Fertig", new Color(0x767676), Color.WHITE, 0, 200, 185, 100);
        completeButton.setVisible(false);
        pokemonButtonPanel.add(backButton);
        pokemonButtonPanel.add(continueButton);
        pokemonButtonPanel.add(completeButton);

        searchPreviewPanel = new Panel(new Color(50, 50, 50), Color.WHITE, pokemonPanel.getWidth() + 200, titlePanel.getHeight(), pokemonPanel.getWidth(), frame.getHeight() - titlePanel.getHeight(), false, null);

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
        addPokemonButton = new Button("Hinzufügen", new Color(0x767676), Color.WHITE, 0, 250, 200, 100);
        removePokemonButton = new Button("Entfernen", new Color(0x767676), Color.WHITE, 0, 350, 200, 100);
        addPokemonPanel.add(addPokemonButton);
        addPokemonPanel.add(removePokemonButton);

        Border blackline = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.WHITE);
        addAttributePanel = new Panel(new Color(50, 50, 50),Color.BLACK, 275, titlePanel.getHeight(), 200, frame.getHeight() - titlePanel.getHeight(), false, null);
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









        frame.getContentPane().add(loginPanel);
        frame.getContentPane().add(titlePanel);
        frame.getContentPane().add(searchListPanel);
        frame.getContentPane().add(mainButtonPanel);
        frame.getContentPane().add(pokemonPanel);
        frame.getContentPane().add(pokemonButtonPanel);
        frame.getContentPane().add(searchPreviewPanel);
        frame.getContentPane().add(addPokemonPanel);
        frame.getContentPane().add(addAttributePanel);
        frame.getContentPane().add(removeAttributePanel);
        frame.getContentPane().add(checkListScrollPane);

        frame.revalidate();
        frame.repaint();







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
        searchListPanel.setVisible(false);
        mainButtonPanel.setVisible(false);
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
                return;
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
                if(activeUser.getStatus().equals("admin")){
                    importPokemonButton.setVisible(true);
                }
                updateSearchList(activeUser.getUsername());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            /*pokemonQuery = "select * from pokemon where (numberChar != ";
            previewQuery = "select * from pokemon where (numberChar = ";
            for(int i = 0; i < searchPreviewModel.getSize(); i++){
                pokemonQuery += searchPreviewModel.getElementAt(i).split("#")[1].split("\\)")[0];
                pokemonQuery += " and numberChar != ";

                previewQuery += searchPreviewModel.getElementAt(i).split("#")[1].split("\\)")[0];
                previewQuery += " or numberChar = ";
            }
            for(int i = 0; i < pokemonJList.getSelectedIndices().length; i++){
                pokemonQuery += pokemonModel.getElementAt(pokemonJList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0];
                previewQuery += pokemonModel.getElementAt(pokemonJList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0];
                if(i + 1 < pokemonJList.getSelectedIndices().length){
                    pokemonQuery += " and numberChar != ";
                    previewQuery += " or numberChar = ";
                }
            }

             */
            pokemonQuery += ")";
            previewQuery += ")";
        }


        fillPokemonModel();
        fillPreviewModel();
        pokemonModelSize = pokemonModel.getSize();
        searchModelSize = searchPreviewModel.getSize();
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

            pokemonQuery = pokemonQuery.replace(")", "");
            previewQuery = previewQuery.replace(")", "");



            for(int i = 0; i < searchPreviewList.getSelectedIndices().length; i++){
                if(pokemonQuery.contains("and numberChar != " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0])){
                    pokemonQuery = pokemonQuery.replace("and numberChar != " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0], "");
                }
                else{
                    pokemonQuery = pokemonQuery.replace("numberChar != " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0], "");
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

                if(previewQuery.contains("or numberChar = " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0])){
                    previewQuery = previewQuery.replace("or numberChar = " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0], "");
                }
                else{
                    previewQuery = previewQuery.replace("numberChar = " + searchPreviewModel.getElementAt(searchPreviewList.getSelectedIndices()[i]).split("#")[1].split("\\)")[0], "");
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
            pokemonQuery += ")";
            previewQuery += ")";


        }


        fillPokemonModel();
        fillPreviewModel();
        pokemonModelSize = pokemonModel.getSize();
        searchModelSize = searchPreviewModel.getSize();

    }

    static void editSearch() {
        try {
            statement = connection.prepareStatement("select * from search where title = ? and username = ?");
            statement.setString(1, searchModel.getElementAt(searchStringList.getSelectedIndex()));
            statement.setString(2, activeUser.getUsername());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                String search = resultSet.getString("text");
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
                    previewQuery = pokemonQuery.replace("and", "or");
                    previewQuery = previewQuery.replace("!", "");
                }
                else{
                    pokemonQuery = "select * from pokemon where (name != '" + search.replace(",", "' and name != '");
                    pokemonQuery += "')";
                    previewQuery = pokemonQuery.replace("and", "or");
                    previewQuery = previewQuery.replace("!", "");
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
                continueButton.setVisible(true);
                completeButton.setVisible(true);
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
        updateSearchList(activeUser.getUsername());
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
    }

    static void showPokemonScreen() {
        UI.searchListPanel.setVisible(false);
        UI.mainButtonPanel.setVisible(false);
        UI.pokemonPanel.setVisible(true);
        UI.pokemonButtonPanel.setVisible(true);
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
        showMainScreen();


    }

    static void test() {
        try {
            statement = connection.prepareStatement("insert into pokemon(number,name)values(" +152 + ",?)");
            statement.setString(1, "Chikorita");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {

    }
}
