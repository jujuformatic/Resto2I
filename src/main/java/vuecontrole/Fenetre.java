package vuecontrole;

import javax.swing.*;
import java.awt.*;

public class Fenetre extends JFrame {
    private MainMenu menu;
    private ViewCarte viewCarte;
    private ViewCommande viewCommande;
    private ViewHistorique viewHistorique;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Fenetre() {
        super();
        this.setSize(1000, 600);
        this.setTitle("Resto2I");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Initialize CardLayout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        menu = new MainMenu();
        viewCarte = new ViewCarte(cardLayout, mainPanel);
        viewCommande = new ViewCommande(cardLayout, mainPanel, viewCarte, "MainMenu", null);
        viewHistorique = new ViewHistorique(cardLayout, mainPanel);

        // Add panels to mainPanel
        mainPanel.add(menu, "MainMenu");
        mainPanel.add(viewCarte, "Carte");
        mainPanel.add(viewCommande, "Commande");
        mainPanel.add(viewHistorique, "Historique");

        // Add action to switch to Carte panel
        menu.getEditerCarteButton().addActionListener(e -> cardLayout.show(mainPanel, "Carte"));
        menu.getCreerCommandeButton().addActionListener(e -> cardLayout.show(mainPanel, "Commande"));
        menu.getVoirCommandeButton().addActionListener(e -> cardLayout.show(mainPanel, "Historique"));

        // Add main panel to frame
        this.add(mainPanel);
        this.setVisible(true);
        device.setFullScreenWindow(this);
    }
}
