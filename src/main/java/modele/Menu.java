package modele;

import jakarta.persistence.*;

import java.awt.*;
import java.util.*;

@Entity
@DiscriminatorValue("MENU")
public class Menu extends Item{
    @ManyToMany
    @JoinTable(name = "menu_item", // Nom de la table de jonction
               joinColumns = @JoinColumn(name = "menu_id"), // Colonne pour le Menu
               inverseJoinColumns = @JoinColumn(name = "item_id") // Colonne pour l'Item
    )
    private Set<Item> itemsMenu = new HashSet<>();

    public Menu() {
        super();
        this.setCategorie(Categorie.MENU);
    }

    public Menu(String nom, double prix, float TVA) {
        this();
        this.setNom(nom);
        this.setPrix(prix);
        this.setTVA(TVA);
    }

    public Menu(String nom, double prix, float TVA, Set<Item> items) {
        this(nom,prix,TVA);
        this.itemsMenu = items;
    }

    public void addItem(Item item) {
        // Vérifier si la collection items n'est pas null
        if (this.itemsMenu == null) {
            this.itemsMenu = new HashSet<>();
        }

        // Ajouter l'item au menu
        this.itemsMenu.add(item);

        // Assurer la cohérence bidirectionnelle
        if (item.getMenus() == null) {
            item.setMenus(new HashSet<>());
        }
        item.getMenus().add(this);
    }
}
