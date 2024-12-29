package modele;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class RetrieveData {
    public List<Commande> getCommandes() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        EntityManager em = emf.createEntityManager();

        try {
            // Récupérer toutes les commandes
            TypedQuery<Commande> query = em.createQuery("SELECT c FROM Commande c", Commande.class);
            List<Commande> commandes = query.getResultList();

            for (Commande commande : commandes) {
                System.out.println(commande);
            }
            return commandes;
        } finally {
            em.close();
            emf.close();
        }
    }

    public void getFactures() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        EntityManager em = emf.createEntityManager();

        try {
            // Récupérer toutes les factures
            TypedQuery<Facture> query = em.createQuery("SELECT f FROM Facture f", Facture.class);
            List<Facture> factures = query.getResultList();

            for (Facture facture : factures) {
                System.out.println(facture);
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    public List<Item> getItems() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        EntityManager em = emf.createEntityManager();

        try {
            // Récupérer tous les items
            TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i", Item.class);
            List<Item> items = query.getResultList();

            for (Item item : items) {
                System.out.println(item);
            }
            return items;
        } finally {
            em.close();
            emf.close();
        }
    }

    public void getMenus() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        EntityManager em = emf.createEntityManager();

        try {
            // Récupérer tous les menus
            TypedQuery<Menu> query = em.createQuery("SELECT m FROM Menu m", Menu.class);
            List<Menu> menus = query.getResultList();

            for (Menu menu : menus) {
                System.out.println(menu);
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    public void getTickets() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        EntityManager em = emf.createEntityManager();

        try {
            // Récupérer tous les tickets
            TypedQuery<Ticket> query = em.createQuery("SELECT t FROM Ticket t", Ticket.class);
            List<Ticket> tickets = query.getResultList();

            for (Ticket ticket : tickets) {
                System.out.println(ticket);
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    public List<Tables> getTables() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        EntityManager em = emf.createEntityManager();

        try {
            // Récupérer toutes les tables
            TypedQuery<Tables> query = em.createQuery("SELECT t FROM Tables t", Tables.class);
            List<Tables> tables = query.getResultList();

            for (Tables table : tables) {
                System.out.println(table);
            }
            return tables;
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void main(String[] args) {
        RetrieveData data = new RetrieveData();
        data.getCommandes();
    }
}
