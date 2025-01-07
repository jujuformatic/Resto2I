package modele;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "num_table")
    private int id;

    @Column()
    private int nbPersonnes;

    @Column()
    private boolean occupe;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commande> commandes = new HashSet<>();

    public Tables() {
        this.occupe = false;
        this.nbPersonnes = 2;
    }

    public Tables(int nbPersonnes) {
        this.occupe = false;
        this.nbPersonnes = nbPersonnes;
    }

    public void setOccupe(boolean occupe) {
        this.occupe = occupe;
    }

    public boolean isOccupe() {
        return occupe;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Tables{" +
                "id=" + id +
                ", nbPersonnes=" + nbPersonnes +
                ", occupe=" + occupe +
                '}';
    }
}
