package vuecontrole;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ViewCarte extends JPanel {
    private ArrayList<String> boissons;
    private ArrayList<String> plats;
    private ArrayList<String> menus;

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
        boissons.add("Ice tea");
        boissons.add("Coca cola");
        boissons.add("Perrier");
        boissons.add("Fanta");
        boissons.add("Thé");
        boissons.add("Alcool");

        plats.add("Steak & frites");
        plats.add("Fish & chips");
        plats.add("Tartiflette");
        plats.add("Burger");
        plats.add("Quiche");

        menus.add("Menu midi");
        menus.add("Menu économique");
        menus.add("Menu étudiant");
        menus.add("Menu soir");
        menus.add("Menu enfant");
    }

    private JPanel createEditableCategoryPanel(String title, ArrayList<String> items) {
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
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String newItem = newItemField.getText().trim();
            if (!newItem.isEmpty() && !items.contains(newItem)) {
                items.add(newItem);
                newItemField.setText("");
                updateItemsPanel(itemsPanel, items);
            }
        });

        addItemPanel.add(newItemField, BorderLayout.CENTER);
        addItemPanel.add(addButton, BorderLayout.EAST);

        panel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
        panel.add(addItemPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateItemsPanel(JPanel itemsPanel, ArrayList<String> items) {
        itemsPanel.removeAll();
        for (String item : items) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            JLabel itemLabel = new JLabel(item);
            JButton removeButton = new JButton("-");
            removeButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to remove " + item + "?",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    items.remove(item);
                    updateItemsPanel(itemsPanel, items);
                }
            });

            itemPanel.add(itemLabel, BorderLayout.CENTER);
            itemPanel.add(removeButton, BorderLayout.EAST);
            itemsPanel.add(itemPanel);
        }
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    public ArrayList<String> getBoissons() {
        return boissons;
    }

    public ArrayList<String> getPlats() {
        return plats;
    }

    public ArrayList<String> getMenus() {
        return menus;
    }
}
