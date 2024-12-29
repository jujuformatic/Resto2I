package modele;

import jakarta.persistence.*;

@Entity
public class Commande_Item {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column()
    private int quantite;

    // Constructeurs
    public Commande_Item() {
    }

    public Commande_Item(Commande commande, Item item, int quantite) {
        this.commande = commande;
        this.item = item;
        this.quantite = quantite;
    }

    // Méthode statique pour vérifier si l'association existe déjà
    public static boolean associationExists(Commande commande, Item item) {
        for (Commande_Item ci : commande.getItemsCommande()) {
            if (ci.getItem().equals(item)) {
                return true;
            }
        }
        return false;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "Commande_Item{" +
                "id=" + id +
                ", commande=" + commande.getId() +
                ", item=" + item.getNom() +
                ", quantite=" + quantite +
                '}';
    }
}