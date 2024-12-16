package modele;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.*;
import java.time.*;

@Entity
public class Commande {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int Id;

    @Column(columnDefinition = "NUMERIC(7,2)",
            nullable = false,
            name = "prix_total")
    private double prixTotal;

    @Column(columnDefinition = "TIMESTAMP",
            nullable = false)
    private Timestamp horaire;

    @Column(length = 255)
    private String commentaire;

    @Column()
    private boolean valide;

    @ManyToOne
    @JoinColumn(name = "no_table")
    private Tables table;

    @ManyToMany
    @JoinTable(name = "commande_item", // Nom de la table de jonction
            joinColumns = @JoinColumn(name = "commande_id"), // Colonne pour la commande
            inverseJoinColumns = @JoinColumn(name = "item_id") // Colonne pour l'Item
    )
    private Set<Item> itemsCommande = new HashSet<>();

    @OneToOne(mappedBy = "commande")
    private Ticket ticket;

    @OneToOne(mappedBy = "commande")
    private Facture facture;

    public Commande() {
        this.prixTotal = 0;
        this.valide = false;

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear() - 1900;
        int month = now.getMonthValue() - 1;
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int nano = now.getNano();
        this.horaire = new Timestamp(year,month,day,hour,minute,second,nano);
    }

    public Commande(Tables table) {
        this();

        this.table = table;
        table.setOccupe(true);
    }

    public Commande(Tables table, String commentaire, Set<Item> items) {
        this(table);
        this.commentaire = commentaire;
        this.itemsCommande = items;
    }

    public void addItem(Item item) {
        // Vérifier si la collection items n'est pas null
        if (this.itemsCommande == null) {
            this.itemsCommande = new HashSet<>();
        }

        // Ajouter l'item au menu
        this.itemsCommande.add(item);

        // Assurer la cohérence bidirectionnelle
        if (item.getCommandes() == null) {
            item.setCommandes(new HashSet<>());
        }
        item.getCommandes().add(this);
        this.calculerPrixTotal();
    }

    public void validerCommande() {
        this.valide = true;
        this.table.setOccupe(false);
    }

    public void calculerPrixTotal() {
        this.prixTotal = 0;
        for(Item i : this.itemsCommande) {
            this.prixTotal = this.prixTotal + i.getPrix();
        }
    }

    @Override
    public String toString() {
        return "Commande{" +
                "Id=" + Id +
                ", table=" + table.getId() +
                ", prixTotal=" + prixTotal +
                ", horaire=" + horaire +
                ", commentaire='" + commentaire + '\'' +
                ", itemsCommande=" +
                ", ticket=" + ticket +
                ", facture=" + facture +
                '}';
    }
}
