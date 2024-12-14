package vuecontrole;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ViewHistorique extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel commandesPanel;

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

        // Top des ventes
        JPanel topVentesPanel = new JPanel(new BorderLayout());
        topVentesPanel.setPreferredSize(new Dimension(150, 0));

        JLabel topVentesTitle = new JLabel("Top des ventes :", SwingConstants.CENTER);
        topVentesTitle.setFont(new Font("Arial", Font.BOLD, 14));
        topVentesPanel.add(topVentesTitle, BorderLayout.NORTH);

        JTextArea topVentesArea = new JTextArea();
        topVentesArea.setEditable(false);
        topVentesArea.setText("1. Steak & frites - 500€\n2. Thé - 300€\n3. Menu enfant - 250€\n...");
        topVentesPanel.add(new JScrollPane(topVentesArea), BorderLayout.CENTER);

        this.add(topVentesPanel, BorderLayout.EAST);

        // Bouton de retour au menu principal
        JButton retourMenu = new JButton("Retour au Menu");
        retourMenu.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        this.add(retourMenu, BorderLayout.SOUTH);
    }

    private void updateCommandesPanel() {
        commandesPanel.removeAll();

        // Données temporaires : liste de commandes avec numéro, date, et prix total
        List<String[]> commandes = Arrays.asList(
                new String[]{"100", "20/11/2024", "55.55€"},
                new String[]{"99", "20/11/2024", "55.55€"},
                new String[]{"98", "20/11/2024", "55.55€"},
                new String[]{"101", "20/11/2024", "55.55€"}
        );

        for (String[] cmd : commandes) {
            JButton cmdButton = new JButton("<html>• n°" + cmd[0] + "<br>• " + cmd[1] + "<br>• " + cmd[2] + "</html>");
            cmdButton.setPreferredSize(new Dimension(100, 80));
            cmdButton.addActionListener(e -> openCommandeDetail());
            commandesPanel.add(cmdButton);
        }

        commandesPanel.revalidate();
        commandesPanel.repaint();
    }

    private void openCommandeDetail() {
        ViewCommande commandeView = new ViewCommande(cardLayout, mainPanel, new ViewCarte(cardLayout, mainPanel), "Historique");
        mainPanel.add(commandeView, "CommandeDetail");
        cardLayout.show(mainPanel, "CommandeDetail");
        commandeView.setButtonsEnabled(false);
    }
}
