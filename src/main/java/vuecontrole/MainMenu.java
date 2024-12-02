package vuecontrole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private JButton editerCarte;
    private JButton creerCommande;
    private JButton voirCommande;

    public MainMenu() {
        this.setLayout(new GridLayout(3, 1, 10, 10));

        this.editerCarte = new JButton("Editer Carte");
        this.creerCommande = new JButton("Créer une commande");
        this.voirCommande = new JButton("Voir l'historique des commandes");

        this.add(editerCarte);
        this.add(creerCommande);
        this.add(voirCommande);
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
}
