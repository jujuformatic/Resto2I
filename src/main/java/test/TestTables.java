package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import modele.*;

public class TestTables {
    public static void main(String[] args) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();

            Tables table1 = new Tables();
            Tables table2 = new Tables();
            Tables table3 = new Tables();
            Tables table4 = new Tables();
            Tables table5 = new Tables();

            em.persist(table1);
            em.persist(table2);
            em.persist(table3);
            em.persist(table4);
            em.persist(table5);

            et.commit();

        } catch (Exception ex){
            System.out.println(">>>>> Erreur !!");
            System.out.println(ex);
            et.rollback();
        }

    }

}
