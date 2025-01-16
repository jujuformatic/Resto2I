package vuecontrole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private JButton editerCarte;
    private JButton creerCommande;
    private JButton voirCommande;
    private JButton quitterApplication;

    public MainMenu() {
        this.setLayout(new GridLayout(4, 1, 10, 10));

        this.editerCarte = new JButton("Editer Carte");
        this.creerCommande = new JButton("Créer une commande");
        this.voirCommande = new JButton("Voir l'historique des commandes");
        this.quitterApplication = new JButton("Quitter l'application");

        this.add(editerCarte);
        this.add(creerCommande);
        this.add(voirCommande);

        // Ajuster la taille du bouton Quitter Application
        quitterApplication.setPreferredSize(new Dimension(200, 60));
        this.add(quitterApplication);

        quitterApplication.addActionListener(e -> System.exit(0));
    }

    public JButton getEditerCarteButton() {
        return editerCarte;
    }

    public JButton getCreerCommandeButton() {
        return creerCommande;
    }

    public JButton getVoirCommandeButton() {
        return voirCommande;
    }

    public JButton getQuitterApplicationButton() {
        return quitterApplication;
    }
}
