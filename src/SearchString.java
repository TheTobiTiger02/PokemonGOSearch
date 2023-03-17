import java.util.ArrayList;

public class SearchString {
    private String title;
    private String query;
    private ArrayList<Pokemon> pokemonList = new ArrayList<>();

    public SearchString(String title, Pokemon[] pokemon, boolean asNumber){
        this.title = title;
        for(Pokemon p : pokemon){
            this.pokemonList.add(p);
            if(asNumber){
                this.query += p.getId() + ",";
            }
            else{
                this.query += p.getName() + ",";
            }
        }

    }

    public String getTitle() {
        return title;
    }

    public String getQuery() {
        return query;
    }
}
