package miniprojet;

public class Nom {
    private String id;
    private String valeur;

    public Nom(String id, String valeur) {
        this.id = id;
        this.valeur = valeur != null ? valeur : "";
    }

    public Nom(String valeur) {
        this.id = "";
        this.valeur = valeur != null ? valeur : "";
    }

    public String getId() {
        return id;
    }

    public String getValeur() {
        return valeur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nom nom = (Nom) o;
        return valeur.equals(nom.valeur);
    }

    @Override
    public int hashCode() {
        return valeur.hashCode();
    }
}