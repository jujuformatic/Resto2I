package vuecontrole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Fenetre extends JFrame {
    private MainMenu menu;
    private Carte carte;
    private Commande commande;
    private Historique historique;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Fenetre() {
        super();
        this.setSize(1000, 600);
        this.setTitle("Resto2I");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize CardLayout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        menu = new MainMenu();
        carte = new Carte(cardLayout, mainPanel);
        commande = new Commande(cardLayout, mainPanel, carte);
        historique = new Historique(cardLayout, mainPanel);

        // Add panels to mainPanel
        mainPanel.add(menu, "MainMenu");
        mainPanel.add(carte, "Carte");
        mainPanel.add(commande, "Commande");
        mainPanel.add(historique, "Historique");

        // Add action to switch to Carte panel
        menu.getEditerCarteButton().addActionListener(e -> cardLayout.show(mainPanel, "Carte"));
        menu.getCreerCommandeButton().addActionListener(e -> cardLayout.show(mainPanel, "Commande"));
        menu.getVoirCommandeButton().addActionListener(e -> cardLayout.show(mainPanel, "Historique"));

        // Add main panel to frame
        this.add(mainPanel);
        this.setVisible(true);
    }
}
