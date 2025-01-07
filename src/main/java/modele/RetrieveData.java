package modele;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

public class RetrieveData {
    public List<Commande> getCommandes() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        EntityManager em = emf.createEntityManager();

        try {
            // Récupérer toutes les commandes
            TypedQuery<Commande> query = em.createQuery("SELECT c FROM Commande c ORDER BY c.horaire DESC", Commande.class);
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

    public List<TopVentes> getTop(String interval) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        EntityManager em = emf.createEntityManager();

        String sql = "SELECT i.nom AS item_nom, " +
                     "SUM(ci.quantite) AS total_quantite, " +
                     "SUM(ci.quantite * i.prix) AS total_revenu " +
                     "FROM commande c " +
                     "JOIN commande_item ci ON c.Id = ci.commande_id " +
                     "JOIN item i ON ci.item_id = i.id " +
                     "WHERE c.horaire >= (NOW() - INTERVAL 1 " + interval + ") " + // Filtrer les commandes selon l'intervalle
                     "GROUP BY i.nom " +
                     "ORDER BY total_revenu DESC";

        Query query = em.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(result -> new TopVentes(
                        (String) result[0],
                        ((Number) result[1]).longValue(),
                        ((Number) result[2]).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        RetrieveData data = new RetrieveData();
        data.getCommandes();
    }
}
