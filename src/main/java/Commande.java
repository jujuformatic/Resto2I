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
            nullable = false)
    private double prixTotal;

    @Column(columnDefinition = "TIMESTAMP",
            nullable = false)
    private Timestamp horaire;

    @Column(length = 255)
    private String commentaire;

    public Commande() {
        this.prixTotal = 0;
    
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        //Timestamp now2 = new Timestamp();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdHHmmss");
        String nowaaa= now.format(format);
        System.out.println(now);
        System.out.println(nowaaa);
    }
}
