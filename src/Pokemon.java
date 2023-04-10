
public class Pokemon {
    private int number;
    private String name;
    private boolean shiny;
    private boolean lucky;
    private boolean shadow;
    private String title;

    public Pokemon(int number, String name, boolean shiny, boolean lucky, boolean shadow, String title) {
        this.number = number;
        this.name = name;
        this.shiny = shiny;
        this.lucky = lucky;
        this.shadow = shadow;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", shiny=" + shiny +
                ", lucky=" + lucky +
                ", shadow=" + shadow +
                ", title='" + title + '\'' +
                '}';
    }
}
