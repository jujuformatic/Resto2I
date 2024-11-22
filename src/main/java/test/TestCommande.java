package test;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import modele.*;

import java.util.HashSet;

public class TestCommande {
    public static void main(String[] args) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Item item1 = new Item("Pizza","Plat",15.50,10);
            Item item2 = new Item("Patates","Plat",10.00,10);

            HashSet<Item> items = new HashSet<>();
            items.add(item1);
            items.add(item2);

            Commande commande1 = new Commande();
            Commande commande2 = new Commande(1,"RAS",items);

            commande1.addItem(item1);

            em.persist(item1);
            em.persist(item2);
            em.persist(commande1);
            em.persist(commande2);
            et.commit();
        } catch (Exception ex){
            System.out.println(">>>>> Erreur !!");
            System.out.println(ex);
            et.rollback();
        }

    }

}
