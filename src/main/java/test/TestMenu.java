package test;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import modele.*;

import java.util.HashSet;

public class TestMenu {
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

            Menu menu1 = new Menu("menu1");
            menu1.addItem(item1);
            Menu menu2 = new Menu("menu2",items);

            em.persist(item1);
            em.persist(item2);
            em.persist(menu1);
            em.persist(menu2);
            et.commit();
            System.out.println(menu1);
            System.out.println(menu2);
        } catch (Exception ex){
            System.out.println(">>>>> Erreur !!");
            et.rollback();
        }

    }

}
