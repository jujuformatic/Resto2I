// TODO : Trouver un moyen pour actualiser toutes les pages à chaque retour au menu principal (cf. ViewHistorique)
// TODO --> cf lien envoyé discord (observer)


package vuecontrole;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import modele.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ViewCommande extends JPanel {
    private JButton retourMenu;
    private JButton validateButton;
    private JButton cancelButton;

    private JPanel cartePanel;
    private JPanel recapPanel;
    private JTextArea recapArea;
    private JLabel totalPriceLabel;
    private JLabel tableNumberLabel;
    private HashMap<String, Item> itemMap; // Map pour lier les noms des items aux objets Item
    private HashMap<Item, Integer> recapItems;
    private ArrayList<JButton> carteButtons; // Liste pour stocker les boutons de la carte
    private String previousView;
    private Commande commande;
    private Tables table = new Tables();

    public ViewCommande(CardLayout cardLayout, JPanel mainPanel, ViewCarte carte, String previousView, Commande command) {
        this.setLayout(new BorderLayout());
        this.previousView = previousView;
        carteButtons = new ArrayList<>();

        JLabel title = new JLabel("Créer une Commande", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(title, BorderLayout.NORTH);

        recapItems = new HashMap<>();
        itemMap = new HashMap<>();

        // Recap Panel
        recapPanel = new JPanel(new BorderLayout());
        recapPanel.setBorder(BorderFactory.createTitledBorder("Résumé de la commande"));

        // Total price and table number panel
        if (command == null) {
            this.commande = new Commande();
            getTableDispo();
            commande.setTable(table);
        }
        else {
            this.commande = command;
            this.table = command.getTable();
        }


        JPanel recapHeader = new JPanel(new GridLayout(1, 2));
        totalPriceLabel = new JLabel("Total: " + this.commande.getPrixTotal() + "€", SwingConstants.LEFT);
        tableNumberLabel = new JLabel("Table: " + table.getId(), SwingConstants.RIGHT);
        recapHeader.add(totalPriceLabel);
        recapHeader.add(tableNumberLabel);

        recapPanel.add(recapHeader, BorderLayout.NORTH);

        recapArea = new JTextArea();
        recapArea.setEditable(false);
        recapArea.setMinimumSize(new Dimension(200, 300));

        // Add MouseListener to handle clicks on the recap items
        recapArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int clickOffset = recapArea.viewToModel2D(evt.getPoint());
                try {
                    int lineStart = recapArea.getLineStartOffset(recapArea.getLineOfOffset(clickOffset));
                    int lineEnd = recapArea.getLineEndOffset(recapArea.getLineOfOffset(clickOffset));
                    String clickedLine = recapArea.getText().substring(lineStart, lineEnd).trim();

                    if (!clickedLine.isEmpty()) {
                        String itemName = clickedLine.split(" x ")[0];
                        Item clickedItem = itemMap.get(itemName);
                        if(clickedItem != null) {
                            decrementItemInRecap(clickedItem);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        recapPanel.add(new JScrollPane(recapArea), BorderLayout.CENTER);

        // Boutons sous le récapitulatif
        JPanel recapButtons = new JPanel(new GridLayout(1, 3, 10, 10));
        validateButton = new JButton("Valider la Commande");
        validateButton.addActionListener(e -> validateCommand(cardLayout, mainPanel));
        cancelButton = new JButton("Annuler la Commande");
        cancelButton.addActionListener(e -> cancelCommand(cardLayout, mainPanel));
        JButton printBillButton = new JButton("Imprimer le Ticket");
        printBillButton.addActionListener(e -> displayTicket(commande));

        recapButtons.add(validateButton);
        recapButtons.add(cancelButton);
        recapButtons.add(printBillButton);
        recapPanel.add(recapButtons, BorderLayout.SOUTH);

        this.add(recapPanel, BorderLayout.WEST);

        // Get items BDD
        RetrieveData data = new RetrieveData();
        List<Item> items = data.getItems();
        List<Item> boissons = new ArrayList<>();
        List<Item> plats = new ArrayList<>();
        List<Item> menus = new ArrayList<>();

        for(Item i : items) {
            if (!i.isHidden()) {
                if (i.getCategorie() == Item.Categorie.BOISSON) {
                    boissons.add(i);
                }
                if (i.getCategorie() == Item.Categorie.PLAT) {
                    plats.add(i);
                }
                if (i.getCategorie() == Item.Categorie.MENU) {
                    menus.add(i);
                }
            }
        }



        // Carte Panel
        cartePanel = new JPanel(new GridLayout(1, 3, 10, 10));
        cartePanel.add(createCarteColumn("Boissons", boissons));
        cartePanel.add(createCarteColumn("Plats", plats));
        cartePanel.add(createCarteColumn("Menus", menus));

        initRecap();

        this.add(cartePanel, BorderLayout.CENTER);

        // Retour Button
        retourMenu = new JButton("Retour");
        retourMenu.addActionListener(e -> retourMenu(cardLayout,mainPanel));
        this.add(retourMenu, BorderLayout.SOUTH);
    }

    private JPanel createCarteColumn(String title, List<Item> items) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(label, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

        for (Item item : items) {
            JButton itemButton = new JButton(item.getNom());
            itemButton.addActionListener(e -> addItemToRecap(item));
            carteButtons.add(itemButton); // Ajouter le bouton à la liste
            itemsPanel.add(itemButton);
        }

        panel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);

        return panel;
    }

    private void addItemToRecap(Item item) {
        recapItems.put(item, recapItems.getOrDefault(itemMap.get(item.getNom()), 0) + 1);
        commande.addItem(item);
        itemMap.put(item.getNom(),item);
        updateRecapArea();
    }

    private void initRecap() {
        System.out.println(commande);
        List<Commande_Item> itemsCommande = commande.getItemsCommande();

        for (Commande_Item ci : itemsCommande) {
            recapItems.put(ci.getItem(),ci.getQuantite());
            itemMap.put(ci.getItem().getNom(), ci.getItem());
            updateRecapArea();
        }
    }

    private void decrementItemInRecap(Item item) {
        if (recapItems.containsKey(item)) {
            int count = recapItems.get(item);
            if (count > 1) {
                recapItems.put(item, count - 1);
            } else {
                recapItems.remove(item);
                itemMap.remove(item);
            }
            commande.removeItem(item);
            updateRecapArea();
        }
    }

    private void updateRecapArea() {
        recapArea.setText(""); // Clear the recap area

        StringBuilder recapText = new StringBuilder();

        for (Item item : recapItems.keySet()) {
            int count = recapItems.get(item);

            recapText.append(item.getNom()).append(" x ").append(count).append("\n");
        }


        recapArea.setText(recapText.toString());
        totalPriceLabel.setText("Total: " + commande.getPrixTotal() + "€");
    }

    private void validateCommand(CardLayout cardLayout, JPanel mainPanel) {
        int response = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir valider la commande ?",
                "Validation de la commande", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            System.out.println("Commande validée :");
            recapItems.forEach((item, count) -> System.out.println(item + " x " + count));
            cardLayout.show(mainPanel, "MainMenu");
        }
        resetCommande();
        commande.validerCommande();
        commit(commande);
    }

    private void cancelCommand(CardLayout cardLayout, JPanel mainPanel) {
        int response = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir annuler la commande ?",
                "Annulation de la commande", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            recapItems.clear();
            updateRecapArea();
            cardLayout.show(mainPanel, "MainMenu");
        }
        resetCommande();
    }

    private void retourMenu(CardLayout cardLayout, JPanel mainPanel) {

        System.out.println("Commande enregistrée :");
        recapItems.forEach((item, count) -> System.out.println(item + " x " + count));
        cardLayout.show(mainPanel, "MainMenu");

        resetCommande();
        commit(commande);
    }

    private void resetCommande() {
        recapItems.clear();
        updateRecapArea();
        totalPriceLabel.setText("Total: 0€");
        tableNumberLabel.setText("Table: " + this.table.getId()); // Tu peux ajuster le numéro de table si nécessaire
    }

    // Méthode pour bloquer ou débloquer les boutons
    public void setButtonsEnabled(boolean enabled) {
        // Bloquer/débloquer les boutons de la carte
        for (JButton button : carteButtons) {
            button.setEnabled(enabled);
        }

        // Bloquer/débloquer les boutons de validation et d'annulation
        validateButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
    }

    private JPanel createTicketPanel() {
        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Utilisation d'une police à largeur fixe
        Font monospacedFont = new Font("Monospaced", Font.PLAIN, 12);

        // En-tête
        ticketPanel.add(new JLabel("Resto2I")).setFont(monospacedFont);
        ticketPanel.add(new JLabel("Tel : 01 23 45 67 89")).setFont(monospacedFont);
        ticketPanel.add(new JLabel("Mail : resto2i@ig2i.centralelille.fr")).setFont(monospacedFont);
        ticketPanel.add(new JLabel("Adresse : 21 rue Jean Souvraz 62300 Lens")).setFont(monospacedFont);
        ticketPanel.add(Box.createVerticalStrut(10));

        // Détails de la transaction
        ticketPanel.add(new JLabel("N° de transaction : 215154")).setFont(monospacedFont);
        ticketPanel.add(new JLabel("Table n°3")).setFont(monospacedFont);
        ticketPanel.add(new JLabel("Payé le 20/11/2024 à 19h00")).setFont(monospacedFont);
        ticketPanel.add(Box.createVerticalStrut(10));

        // Entête des colonnes
        JLabel header = new JLabel(String.format("%-20s %5s %10s", "Article", "Qté", "Prix"));
        header.setFont(monospacedFont);
        ticketPanel.add(header);

        // Détails des articles
        for (Item item : recapItems.keySet()) {
            int count = recapItems.get(item);
            String itemLine = String.format("%-20s %5d %10s", item, count, "10€ + 10%");
            JLabel itemLabel = new JLabel(itemLine);
            itemLabel.setFont(monospacedFont);
            ticketPanel.add(itemLabel);
        }

        ticketPanel.add(Box.createVerticalStrut(10));

        // Totaux
        ticketPanel.add(new JLabel("Total HT : 55.55€")).setFont(monospacedFont);
        ticketPanel.add(new JLabel("Avec TVA : 10€")).setFont(monospacedFont);

        return ticketPanel;
    }

    private void displayTicket(Commande commande) {
        JDialog ticketDialog = new JDialog();
        ticketDialog.setTitle("Ticket de Caisse");
        ticketDialog.setSize(300, 400);
        ticketDialog.setLocationRelativeTo(this);

        JPanel ticketPanel = createTicketPanel();
        ticketDialog.add(ticketPanel);
        ticketDialog.setVisible(true);

        Ticket t1 = new Ticket(commande);
        commit(commande);
        commit(t1);
    }

    private void getTableDispo() {
        RetrieveData data = new RetrieveData();
        List<Tables> tables = data.getTables();
        for (Tables t : tables) {
            if (!t.isOccupe()) {
                this.table = t;
                return;
            }
        }
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
            System.out.println(">>>>> Erreur !!");
            ex.printStackTrace();
            if (et.isActive()) {
                et.rollback();
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}
