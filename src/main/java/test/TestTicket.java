package test;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import modele.*;

import java.util.HashSet;

public class TestTicket {
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

            Commande commande1 = new Commande(table1);
            Commande commande2 = new Commande(table2,"RAS",items);

            Ticket t1 = new Ticket(commande1);
            Ticket t2 = new Ticket(commande2);

            commande1.addItem(item1);

            em.persist(item1);
            em.persist(item2);
            em.persist(commande1);
            em.persist(commande2);
            em.persist(t1);
            em.persist(t2);
            et.commit();
        } catch (Exception ex){
            System.out.println(">>>>> Erreur !!");
            System.out.println(ex);
            et.rollback();
        }

    }

}
