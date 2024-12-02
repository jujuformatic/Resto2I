package vuecontrole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Commande extends JPanel {
    private JButton retourMenu;
    private JPanel cartePanel;
    private JPanel recapPanel;
    private JTextArea recapArea;
    private HashMap<String, Integer> recapItems;

    public Commande(CardLayout cardLayout, JPanel mainPanel, Carte carte) {
        this.setLayout(new BorderLayout());

        JLabel title = new JLabel("Créer une Commande", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(title, BorderLayout.NORTH);

        recapItems = new HashMap<>();

        // Recap Panel
        recapPanel = new JPanel(new BorderLayout());
        recapPanel.setBorder(BorderFactory.createTitledBorder("Résumé de la commande"));

        recapArea = new JTextArea();
        recapArea.setEditable(false);
        recapArea.setPreferredSize(new Dimension(200, 300)); // Ensure a minimum size for visibility
        recapPanel.add(new JScrollPane(recapArea), BorderLayout.CENTER);

        // Add buttons under the recap area
        JPanel recapButtonsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton validateButton = new JButton("Valider la Commande");
        validateButton.addActionListener(e -> validateCommande());

        JButton cancelButton = new JButton("Annuler la Commande");
        cancelButton.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        JButton printBillButton = new JButton("Imprimer la Facture");
        printBillButton.addActionListener(e -> printBill());

        recapButtonsPanel.add(validateButton);
        recapButtonsPanel.add(cancelButton);
        recapButtonsPanel.add(printBillButton);

        recapPanel.add(recapButtonsPanel, BorderLayout.SOUTH);

        this.add(recapPanel, BorderLayout.WEST);

        // Carte Panel
        cartePanel = new JPanel(new GridLayout(1, 3, 10, 10));
        cartePanel.add(createCarteColumn("Boissons", carte.getBoissons()));
        cartePanel.add(createCarteColumn("Plats", carte.getPlats()));
        cartePanel.add(createCarteColumn("Menus", carte.getMenus()));

        this.add(cartePanel, BorderLayout.CENTER);

        // Retour Menu Button
        retourMenu = new JButton("Retour au Menu");
        retourMenu.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        this.add(retourMenu, BorderLayout.SOUTH);
    }

    private JPanel createCarteColumn(String title, ArrayList<String> items) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

        for (String item : items) {
            JButton itemButton = new JButton(item);
            itemButton.addActionListener(e -> addItemToRecap(item));
            itemsPanel.add(itemButton);
        }

        panel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);

        return panel;
    }

    private void addItemToRecap(String item) {
        recapItems.put(item, recapItems.getOrDefault(item, 0) + 1);
        updateRecapArea();
    }

    private void removeItemFromRecap(String item) {
        if (recapItems.containsKey(item)) {
            int count = recapItems.get(item);
            if (count > 1) {
                recapItems.put(item, count - 1);
            } else {
                recapItems.remove(item);
            }
            updateRecapArea();
        }
    }

    private void updateRecapArea() {
        StringBuilder recapText = new StringBuilder();
        for (String item : recapItems.keySet()) {
            recapText.append(item).append(" x ").append(recapItems.get(item)).append("\n");
        }
        recapArea.setText(recapText.toString());
    }

    private void validateCommande() {
        System.out.println("Commande validée :");
        recapItems.forEach((item, count) -> System.out.println(item + " x " + count));
    }

    private void printBill() {
        System.out.println("Facture :");
        recapItems.forEach((item, count) -> System.out.println(item + " x " + count));
    }
}
