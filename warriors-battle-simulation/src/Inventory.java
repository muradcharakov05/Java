import java.util.HashMap;


public class Inventory {
    HashMap<String, Integer> attzel = new HashMap<>();
    HashMap<String, Integer> defzel = new HashMap<>();

    public Inventory() {
        attzel.put("Зелье Заморозки",1);
        attzel.put("Зелье Ярости",1);

        defzel.put("Зелье Защиты",1);
    }


}
