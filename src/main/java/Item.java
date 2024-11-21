import jakarta.persistence.*;

import java.util.List;

@Entity
public class Item {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
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

    public Item(){}
    public Item(String nom, String categorie, double prix, float TVA) {
        this.nom = nom;
        this.categorie = categorie;
        this.prix = prix;
        this.TVA = TVA;
    }
}
