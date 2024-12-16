package modele;

import jakarta.persistence.*;

@Entity
public class Facture {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,
            name = "numero_facture")
    private int Id;

    @OneToOne
    @JoinColumn(name = "ID_COMMANDE")
    private Commande commande;

    @Column(length = 255,
            name = "nom_client")
    private String nomClient;

    @Column(length = 255,
            name = "coordonnees_client")
    private String coordonneesClient;

    @Column(length = 255,
            name = "conditions_paiement")
    private String conditionsPaiement;

    @Column(name = "numero_TVA_client")
    private long numeroTVAClient;

    public Facture() {
    }

    public Facture(Commande commande, String nomClient, String coordonneesClient, String conditionsPaiement, long numeroTVAClient) {
        this.commande = commande;
        this.nomClient = nomClient;
        this.coordonneesClient = coordonneesClient;
        this.conditionsPaiement = conditionsPaiement;
        this.numeroTVAClient = numeroTVAClient;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "Id=" + Id +
                ", nomClient='" + nomClient + '\'' +
                ", coordonneesClient='" + coordonneesClient + '\'' +
                ", conditionsPaiement='" + conditionsPaiement + '\'' +
                ", numeroTVAClient=" + numeroTVAClient +
                '}';
    }
}
