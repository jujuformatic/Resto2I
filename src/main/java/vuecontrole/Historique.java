package vuecontrole;

import javax.swing.*;
import java.awt.*;

class Historique extends JPanel {
    private JButton retourMenu;
    private JTextArea historiqueDetails;

    public Historique(CardLayout cardLayout, JPanel mainPanel) {
        this.setLayout(new BorderLayout());

        JLabel title = new JLabel("Historique des Commandes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(title, BorderLayout.NORTH);

        historiqueDetails = new JTextArea("Historique des commandes...");
        historiqueDetails.setEditable(false);
        this.add(new JScrollPane(historiqueDetails), BorderLayout.CENTER);

        retourMenu = new JButton("Retour au Menu");
        retourMenu.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        this.add(retourMenu, BorderLayout.SOUTH);
    }

    public void addHistoriqueItem(String item) {
        historiqueDetails.append("\n" + item);
    }
}