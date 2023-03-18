import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

public class PokemonList {

    Connection con1;
    PreparedStatement insert;


    //public static ArrayList<String> pokemonList = new ArrayList<>();
    public static ArrayList<Pokemon> pokemonList = new ArrayList<>();

    public PokemonList() {
        loadData();
    }

    public void loadData() {
        //pokemonList.add("");
        File file = new File("pokemonList.txt");
        try {
            Scanner sc = new Scanner(file);

            while(sc.hasNextLine()){
                String text = sc.nextLine();
                String pokemon = text.split(" ")[0];


                int id = Integer.parseInt(text.split("#")[1].substring(0, 3));

                pokemonList.add(new Pokemon(id, pokemon, text));

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertPokemon() {
        try {
            con1 = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7606827", "sql7606827",
                    "uAPhstaBJb");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(Pokemon p : pokemonList){
            try {
                insert = con1.prepareStatement("insert into pokemon(number,name)values(" +p.getId() + ",?)");
                insert.setString(1, p.getName());
                System.out.println(insert);
                insert.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void addPokemon(int id, String pokemon, String text) {
        pokemonList.add(new Pokemon(id, pokemon, text));
    }


}
