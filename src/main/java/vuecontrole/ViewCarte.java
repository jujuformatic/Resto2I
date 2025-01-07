package vuecontrole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import modele.*;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
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
        List<Item> items = data.getItems();

        for(Item i : items) {
            if (!i.isHidden()) {
                switch (i.getCategorie()) {
                    case BOISSON -> boissons.add(i);
                    case PLAT -> plats.add(i);
                    case MENU -> menus.add(i);
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
        JTextField newItemField = new JTextField("Nom de l'article");
        JTextField newItemPrice = new JTextField("Prix de l'article");

        setPlaceholderBehavior(newItemField, "Nom de l'article");
        setPlaceholderBehavior(newItemPrice, "Prix de l'article");

        String[] tvaOptions = {"TVA", "5.5%", "10%", "20%"};
        JComboBox<String> tvaDropdown = new JComboBox<>(tvaOptions);

        JButton addButton = new JButton("Ajouter");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(newItemField, BorderLayout.CENTER);
        topPanel.add(tvaDropdown, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(newItemPrice, BorderLayout.CENTER);
        bottomPanel.add(addButton, BorderLayout.EAST);

        addButton.addActionListener(e -> {
            Item.Categorie cat = switch (title) {
                case "Boissons" -> Item.Categorie.BOISSON;
                case "Plats" -> Item.Categorie.PLAT;
                default -> Item.Categorie.MENU;
            };

            String itemName = newItemField.getText().trim();
            if (itemName.isEmpty() || itemName.equals("Nom de l'article")) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nom valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double itemPrice = 0.0;
            try {
                itemPrice = Double.parseDouble(newItemPrice.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un prix valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (itemPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un prix supérieur à 0.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String tvaStr = (String) tvaDropdown.getSelectedItem();
            float TVA = (tvaStr != null && !tvaStr.equals("TVA")) ? Float.parseFloat(tvaStr.replace("%", "")) : 0.0f;
            if (TVA == 0.0f) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une TVA.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Item newItem = new Item(itemName,cat,itemPrice,TVA);
            if (!itemName.isEmpty() && !items.contains(newItem)) {
                items.add(newItem);
                newItemField.setText("Nom de l'article");
                newItemPrice.setText("Prix de l'article");
                commit(newItem);
                updateItemsPanel(itemsPanel, items);
            }
        });

        addItemPanel.add(topPanel, BorderLayout.NORTH);
        addItemPanel.add(bottomPanel, BorderLayout.SOUTH);

        panel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
        panel.add(addItemPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateItemsPanel(JPanel itemsPanel, ArrayList<Item> items) {
        itemsPanel.removeAll();
        for (Item item : items) {
            double prix = item.getPrix();
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String prixFormat = decimalFormat.format(prix);
            String label = prixFormat + "€ | " + item.getNom();

            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            JLabel itemLabel = new JLabel(label);
            itemLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            itemLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    int confirmation = JOptionPane.showConfirmDialog(
                            null,
                            "Supprimer " + item.getNom() + " ?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (confirmation == JOptionPane.YES_OPTION) {
                        items.remove(item);
                        item.setHidden(true);
                        updateItemsPanel(itemsPanel, items);
                        merge(item);
                    }
                }

                public void mouseEntered(java.awt.event.MouseEvent e) {
                    itemLabel.setText("<html><strike>" + label + "</strike></html>");
                }

                public void mouseExited(java.awt.event.MouseEvent e) {
                    itemLabel.setText(label);
                }
            });

            itemPanel.add(itemLabel);
            itemsPanel.add(itemPanel);
        }
        itemsPanel.revalidate();
        itemsPanel.repaint();
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

    private void setPlaceholderBehavior(JTextField field, String placeholder) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });
    }
}


