package modele;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "item_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ITEM")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(nullable = false, length = 255)
    private String nom;

    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private Categorie categorie;

    @Column(columnDefinition = "NUMERIC(7,2)", nullable = false)
    private double prix;

    @Column(columnDefinition = "NUMERIC(3,1)", nullable = false)
    private float TVA;

    @Column(nullable = false)
    private boolean hidden;

    @ManyToMany(mappedBy = "itemsMenu", cascade = CascadeType.PERSIST)
    private Set<Menu> menus = new HashSet<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commande_Item> commandes = new HashSet<>();

    public Item(){
        this.hidden = false;
    }

    public Item(String nom) {
        this();
        this.nom = nom;
    }

    public Item(String nom, Categorie categorie, double prix, float TVA) {
        this();
        this.nom = nom;
        this.categorie = categorie;
        this.prix = prix;
        this.TVA = TVA;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public double getPrix() {
        return prix;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public Set<Commande_Item> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande_Item> commandes) {
        this.commandes = commandes;
    }

    public float getTVA() {
        return TVA;
    }

    public void hide() {
        this.hidden = true;
    }

    public void show() {
        this.hidden = false;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setTVA(float TVA) {
        this.TVA = TVA;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", categorie='" + categorie + '\'' +
                ", prix=" + prix +
                ", TVA=" + TVA +
                ", hidden=" + hidden +
                ", menus=" + menus +
                ", commandes=" + commandes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && Double.compare(prix, item.prix) == 0 && Float.compare(TVA, item.TVA) == 0 && hidden == item.hidden && Objects.equals(nom, item.nom) && categorie == item.categorie && Objects.equals(menus, item.menus) && Objects.equals(commandes, item.commandes);
    }

    public enum Categorie {
        MENU,
        PLAT,
        BOISSON
    }
}