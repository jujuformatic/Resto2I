package vuecontrole;
import modele.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewHistorique extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel commandesPanel;
    private List<Commande> commandes = new ArrayList<>();
    private JTextArea topVentesArea;
    private JTable topVentesTable;
    private List<TopVentes> topJour;
    private List<TopVentes> topSemaine;
    private List<TopVentes> topMois;

    public ViewHistorique(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.setLayout(new BorderLayout());

        JLabel title = new JLabel("Historique des commandes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(title, BorderLayout.NORTH);

        commandesPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        updateCommandesPanel();

        this.add(new JScrollPane(commandesPanel), BorderLayout.CENTER);

        // Initialiser les tops des ventes
        topJour = new ArrayList<>();
        topSemaine = new ArrayList<>();
        topMois = new ArrayList<>();

        RetrieveData data = new RetrieveData();
        topJour = data.getTop("DAY");
        topSemaine = data.getTop("WEEK");
        topMois = data.getTop("MONTH");

        // Top des ventes
        JPanel topVentesPanel = new JPanel(new BorderLayout());
        topVentesPanel.setPreferredSize(new Dimension(250, 0));

        JLabel topVentesTitle = new JLabel("Top des ventes :", SwingConstants.CENTER);
        topVentesTitle.setFont(new Font("Arial", Font.BOLD, 14));
        topVentesPanel.add(topVentesTitle, BorderLayout.NORTH);

        // Menu déroulant pour choisir le top des ventes
        String[] options = {"Jour", "Semaine", "Mois"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.addActionListener(e -> updateTopVentes(comboBox.getSelectedItem().toString()));
        topVentesPanel.add(comboBox, BorderLayout.SOUTH);

        // Créer le modèle de table
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Nom de l'item", "Prix total"}, 0);
        topVentesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(topVentesTable);
        topVentesPanel.add(scrollPane, BorderLayout.CENTER);

        this.add(topVentesPanel, BorderLayout.EAST);

        // Bouton de retour au menu principal
        JButton retourMenu = new JButton("Retour au Menu");
        retourMenu.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        this.add(retourMenu, BorderLayout.SOUTH);

        // Initialiser avec le top du jour
        updateTopVentes("Jour");
    }

    private void updateTopVentes(String periode) {
        List<TopVentes> topVentes;
        switch (periode) {
            case "Semaine":
                topVentes = topSemaine;
                break;
            case "Mois":
                topVentes = topMois;
                break;
            case "Jour":
            default:
                topVentes = topJour;
                break;
        }

        DefaultTableModel tableModel = (DefaultTableModel) topVentesTable.getModel();
        tableModel.setRowCount(0); // Clear existing rows

        for (TopVentes vente : topVentes) {
            tableModel.addRow(new Object[]{vente.getItemNom(), vente.getTotalRevenu() + "€"});
        }
    }

    private void updateCommandesPanel() {
        commandesPanel.removeAll();
        RetrieveData dataCommandes = new RetrieveData();
        commandes = dataCommandes.getCommandes();



        for (Commande comm : commandes) {
            String id = String.valueOf(comm.getId());
            String horaire = String.valueOf(comm.getHoraire());
            horaire = horaire.substring(8,10) + "/" + horaire.substring(5,7) + "/" + horaire.substring(0,4);
            String prix = String.valueOf(comm.getPrixTotal());
            JButton cmdButton = new JButton("<html>• n°" + id + "<br>• " + horaire + "<br>• " + prix + "€" + "</html>");
            if(comm.isValide()) {
                cmdButton.setBackground(new Color(164,230,138));
                cmdButton.setOpaque(true);
            }
            cmdButton.setPreferredSize(new Dimension(100, 80));
            cmdButton.addActionListener(e -> openCommandeDetail(comm));
            commandesPanel.add(cmdButton);
        }

        commandesPanel.revalidate();
        commandesPanel.repaint();
    }

    private void
    openCommandeDetail(Commande commande) {
        ViewCommande commandeView = new ViewCommande(cardLayout, mainPanel, new ViewCarte(cardLayout, mainPanel), "Historique",commande);
        mainPanel.add(commandeView, "CommandeDetail");
        cardLayout.show(mainPanel, "CommandeDetail");
        if(commande.isValide()) {
            commandeView.setButtonsEnabled(false);
        } else {
            commandeView.setButtonsEnabled(true);
        }
    }
}
