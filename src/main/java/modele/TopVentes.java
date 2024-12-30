package modele;

public class TopVentes {
    private String itemNom;
    private Long totalQuantite;
    private Double totalRevenu;

    public TopVentes(String itemNom, Long totalQuantite, Double totalRevenu) {
        this.itemNom = itemNom;
        this.totalQuantite = totalQuantite;
        this.totalRevenu = totalRevenu;
    }

    public String getItemNom() {
        return itemNom;
    }

    public Long getTotalQuantite() {
        return totalQuantite;
    }

    public Double getTotalRevenu() {
        return totalRevenu;
    }
}
