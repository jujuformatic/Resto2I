package test;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import modele.*;

import java.util.HashSet;

public class TestMain {
    public static void main(String[] args) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction et = em.getTransaction();
            try {
            et.begin();
            Item item1 = new Item("Pizza","Plat",15.50,10);
            Item item2 = new Item("Patates","Plat",10.00,10);

            Tables table1 = new Tables();
            Tables table2 = new Tables();

            HashSet<Item> items = new HashSet<>();
            items.add(item1);
            items.add(item2);

            Menu menu1 = new Menu("menu1",20.00,10);
            menu1.addItem(item1);
            Menu menu2 = new Menu("menu2",30.00,10,items);

            Commande commande1 = new Commande(table1);
            Commande commande2 = new Commande(table2,"RAS",items);

            commande1.addItem(item1);
            commande2.addItem(menu1);

            Facture f1 = new Facture(commande1,"Michel","10 rue ...","paiement immédiat",150);

            Ticket t1 = new Ticket(commande1);
            Ticket t2 = new Ticket(commande2);

            commande1.validerCommande();

            em.persist(item1);
            em.persist(item2);

            em.persist(table1);
            em.persist(table2);

            em.persist(menu1);
            em.persist(menu2);

            em.persist(commande1);
            em.persist(commande2);

            em.persist(t1);
            em.persist(t2);

            em.persist(f1);

            et.commit();
        } catch (Exception ex){
            System.out.println(">>>>> Erreur !!");
            ex.printStackTrace();
            if (et.isActive()) {
                et.rollback();
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}
