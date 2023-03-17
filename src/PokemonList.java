import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PokemonList {

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

    public static void addPokemon(int id, String pokemon, String text) {
        pokemonList.add(new Pokemon(id, pokemon, text));
    }


}
