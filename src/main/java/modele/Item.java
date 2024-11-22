package modele;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,
            nullable = false)
    private int Id;

    @Column(nullable = false,
            length = 255)
    private String nom;

    @Column(nullable = false,
            length = 255)
    private String categorie;

    @Column(columnDefinition = "NUMERIC(7,2)",
            nullable = false)
    private double prix;

    @Column(columnDefinition = "NUMERIC(3,1)",
            nullable = false)
    private float TVA;


    @ManyToMany(mappedBy = "itemsMenu", cascade = CascadeType.PERSIST)
    private Set<Menu> menus = new HashSet<>();

    @ManyToMany(mappedBy = "itemsCommande", cascade = CascadeType.PERSIST)
    private Set<Commande> commandes = new HashSet<>();


    public Item(){}
    public Item(String nom, String categorie, double prix, float TVA) {
        this.nom = nom;
        this.categorie = categorie;
        this.prix = prix;
        this.TVA = TVA;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    @Override
    public String toString() {
        return "Item{" +
                "Id=" + Id +
                ", nom='" + nom + '\'' +
                ", categorie='" + categorie + '\'' +
                ", prix=" + prix +
                ", TVA=" + TVA +
                '}';
    }
}
