package modele;

import jakarta.persistence.*;
import org.hibernate.grammars.hql.HqlParser;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;

@Entity
public class Commande {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int Id;

    @Column()
    private int table;

    @Column(columnDefinition = "NUMERIC(7,2)",
            nullable = false,
            name = "prix_total")
    private double prixTotal;

    @Column(columnDefinition = "TIMESTAMP",
            nullable = false)
    private Timestamp horaire;

    @Column(length = 255)
    private String commentaire;


    public Commande() {
        this.prixTotal = 0;

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdHHmmss");
        LocalDateTime now = LocalDateTime.now();
        this.horaire = new Timestamp(Long.parseLong(now.format(format)));
    }
}
