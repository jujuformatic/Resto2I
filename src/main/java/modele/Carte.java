package modele;

import java.util.Set;

public class Carte {
    private Set<Item> items;
    private Set<Menu> menus;

    private static Carte instance;

    private Carte(){}

    public static Carte getInstance() {
        if(instance == null) {
            instance = new Carte();
        }
        return instance;
    }
}
