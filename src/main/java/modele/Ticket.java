package modele;

import jakarta.persistence.*;

@Entity
public class Ticket {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true,
            name = "numero_transaction")
    private int Id;

    @OneToOne
    @JoinColumn(name = "ID_COMMANDE")
    private Commande commande;

    public Ticket() {}

    public Ticket(Commande commande) {
        this.commande = commande;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "Id=" + Id +
                '}';
    }
}
