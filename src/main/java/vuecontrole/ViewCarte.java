// TODO : gérer la TVA --> bouton : à chaque clic, ca passe au taux suivant (cycle)

package vuecontrole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import modele.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewCarte extends JPanel {
    private ArrayList<Item> boissons;
    private ArrayList<Item> plats;
    private ArrayList<Item> menus;

    public ViewCarte(CardLayout cardLayout, JPanel mainPanel) {
        this.setLayout(new BorderLayout());

        JLabel title = new JLabel("Editer la Carte", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(title, BorderLayout.NORTH);

        // Initialize data
        boissons = new ArrayList<>();
        plats = new ArrayList<>();
        menus = new ArrayList<>();
        initializeData();

        JPanel categoriesPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        categoriesPanel.add(createEditableCategoryPanel("Boissons", boissons));
        categoriesPanel.add(createEditableCategoryPanel("Plats", plats));
        categoriesPanel.add(createEditableCategoryPanel("Menus", menus));

        this.add(categoriesPanel, BorderLayout.CENTER);

        JButton retourMenu = new JButton("Retour au Menu");
        retourMenu.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        this.add(retourMenu, BorderLayout.SOUTH);
    }

    private void initializeData() {
        RetrieveData data = new RetrieveData();
        java.util.List<Item> items = data.getItems();

        for(Item i : items) {
            if (!i.isHidden()) {
                if (i.getCategorie() == Item.Categorie.BOISSON) {
                    boissons.add(i);
                }
                if (i.getCategorie() == Item.Categorie.PLAT) {
                    plats.add(i);
                }
                if (i.getCategorie() == Item.Categorie.MENU) {
                    menus.add(i);
                }
            }
        }
    }

    private JPanel createEditableCategoryPanel(String title, ArrayList<Item> items) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        updateItemsPanel(itemsPanel, items);

        // Add item section
        JPanel addItemPanel = new JPanel(new BorderLayout());
        JTextField newItemField = new JTextField();
        JTextField newItemPrice = new JTextField();
        JButton addButton = new JButton("Add");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(newItemPrice, BorderLayout.CENTER);
        bottomPanel.add(addButton, BorderLayout.EAST);

        addButton.addActionListener(e -> {
            Item.Categorie cat;
            if(title=="Boissons") {
                cat = Item.Categorie.BOISSON;
            } else if(title == "Plats") {
                cat = Item.Categorie.PLAT;
            } else {
                cat = Item.Categorie.MENU;
            }

            String itemName = newItemField.getText().trim();
            double itemPrice = 0.0;
            try {
                itemPrice = Double.parseDouble(newItemPrice.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un prix valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Item newItem = new Item(itemName,cat,itemPrice,10);
            if (!itemName.isEmpty() && !items.contains(newItem)) {
                items.add(newItem);
                newItemField.setText("");
                newItemPrice.setText("");
                commit(newItem);
                updateItemsPanel(itemsPanel, items);
            }
        });



        addItemPanel.add(newItemField, BorderLayout.NORTH);
        addItemPanel.add(bottomPanel, BorderLayout.SOUTH);

        panel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
        panel.add(addItemPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateItemsPanel(JPanel itemsPanel, ArrayList<Item> items) {
        itemsPanel.removeAll();
        for (Item item : items) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            JLabel itemLabel = new JLabel(item.getNom());
            JButton removeButton = new JButton("-");
            removeButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to remove " + item.getNom() + "?",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    items.remove(item);
                    item.setHidden(true);
                    updateItemsPanel(itemsPanel, items);
                    merge(item);
                }
            });

            itemPanel.add(itemLabel, BorderLayout.CENTER);
            itemPanel.add(removeButton, BorderLayout.EAST);
            itemsPanel.add(itemPanel);
        }
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    public ArrayList<Item> getBoissons() {
        return boissons;
    }

    public ArrayList<Item> getPlats() {
        return plats;
    }

    public ArrayList<Item> getMenus() {
        return menus;
    }

    private void commit(Object o) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(o);

            et.commit();
        } catch (Exception ex){
            System.out.println(">>>>> Erreur !!" + "\n" + "         " + ex);
            ex.printStackTrace();
            if (et.isActive()) {
                et.rollback();
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    private void merge(Object o) {
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Resto2I");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(o);

            et.commit();
        } catch (Exception ex){
            System.out.println(">>>>> Erreur !!" + "\n" + "         " + ex);
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
