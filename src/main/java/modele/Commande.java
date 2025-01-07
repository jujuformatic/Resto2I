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

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commande_Item> itemsCommande = new HashSet<>();

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

    public Commande(Tables table, String commentaire) {
        this(table);
        this.commentaire = commentaire;
    }

    public void addItem(Item item) {
        // Vérifier si la collection itemsCommande n'est pas null
        if (this.itemsCommande == null) {
            this.itemsCommande = new HashSet<>();
        }

        Commande_Item commandeItem = null;

        // Vérifier si l'item existe déjà dans la commande
        for (Commande_Item ci : itemsCommande) {
            if (ci.getItem().equals(item)) {
                // Ajouter 1 à la quantité
                ci.setQuantite(ci.getQuantite() + 1);
                commandeItem = ci;
                break;
            }
        }

        // Si l'item n'existe pas déjà, créer un nouveau CommandeItem
        if (commandeItem == null) {
            commandeItem = new Commande_Item(this, item, 1);
            this.itemsCommande.add(commandeItem);
        }

        // Assurer la cohérence bidirectionnelle
        if (item.getCommandes() == null) {
            item.setCommandes(new HashSet<>());
        }
        item.getCommandes().add(commandeItem);

        // Calculer le prix total
        this.calculerPrixTotal();
    }

    private Commande_Item findCommandeItem(Item item) {
        for (Commande_Item ci : itemsCommande) {
            if (ci.getItem().equals(item)) {
                return ci;
            }
        }
        return null;
    }

    public void removeItem(Item item) {
        Commande_Item commandeItem = findCommandeItem(item);
        if (commandeItem != null) {
            if (commandeItem.getQuantite() > 1) {
                commandeItem.setQuantite(commandeItem.getQuantite() - 1);
            }
            else {
                itemsCommande.remove(commandeItem);
                item.getCommandes().remove(commandeItem);
            }
            calculerPrixTotal();
        }
    }

    public void validerCommande() {
        this.valide = true;
        this.table.setOccupe(false);
    }

    public void calculerPrixTotal() {
        this.prixTotal = 0;
        for (Commande_Item ci : itemsCommande) {
            this.prixTotal = this.prixTotal + (ci.getItem().getPrix() * ci.getQuantite());
        }
    }

    public int getId() {
        return Id;
    }

    public boolean isValide() {
        return valide;
    }

    public List<Commande_Item> getItemsCommande() {
        List<Commande_Item> comms = new ArrayList<>();
        for(Commande_Item ci : itemsCommande) {
            comms.add(ci);
        }
        return comms;
    }

    public double getPrixTotal() {
        //this.calculerPrixTotal();
        return prixTotal;
    }

    public Timestamp getHoraire() {
        return horaire;
    }

    public Tables getTable() {
        return table;
    }

    public void setTable(Tables table) {
        table.setOccupe(true);
        this.table = table;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "Id=" + Id +
                ", table=" + table.getId() +
                ", prixTotal=" + prixTotal +
                ", horaire=" + horaire +
                ", commentaire='" + commentaire + '\'' +
                ", itemsCommande=" + itemsCommande +
                ", ticket=" + ticket +
                ", facture=" + facture +
                '}';
    }
}
