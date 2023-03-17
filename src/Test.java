
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Test {
    public static void main(String[] args) {
        new PokemonList();
        ArrayList<Pokemon> pokemon = new ArrayList<>();
        pokemon.add(PokemonList.pokemonList.get(5));
        pokemon.add(PokemonList.pokemonList.get(10));
        pokemon.add(PokemonList.pokemonList.get(53));
        pokemon.add(PokemonList.pokemonList.get(7));
        pokemon.add(PokemonList.pokemonList.get(13));
        pokemon.add(PokemonList.pokemonList.get(103));
        pokemon.add(PokemonList.pokemonList.get(40));
        pokemon.add(PokemonList.pokemonList.get(1));

        Collections.sort(pokemon, new Comparator<Pokemon>() {
            @Override
            public int compare(Pokemon o1, Pokemon o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
        pokemon.add(PokemonList.pokemonList.get(3));
        Collections.sort(pokemon, new Comparator<Pokemon>() {
            @Override
            public int compare(Pokemon o1, Pokemon o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });


        for(Pokemon p : pokemon){
            System.out.println(p.getId());
        }

    }
}
