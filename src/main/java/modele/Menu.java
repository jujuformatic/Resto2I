package modele;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,
            nullable = false)
    private int Id;

    @Column(length = 255)
    private String nom;

    @ManyToMany
    @JoinTable(name = "menu_item", // Nom de la table de jonction
               joinColumns = @JoinColumn(name = "menu_id"), // Colonne pour le Menu
               inverseJoinColumns = @JoinColumn(name = "item_id") // Colonne pour l'Item
    )
    private Set<Item> itemsMenu = new HashSet<>();

    public Menu() {
    }

    public Menu(String nom) {
        this.nom = nom;
    }

    public Menu(String nom, Set<Item> items) {
        this.nom = nom;
        this.itemsMenu = items;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "Id=" + Id +
                ", items=" + itemsMenu +
                '}';
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
