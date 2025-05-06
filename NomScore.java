package miniprojet;

public class NomScore {
    private Nom nom;
    private double score;

    public NomScore(Nom nom, double score) {
        this.nom = nom;
        this.score = score;
    }

    public Nom getNom() {
        return nom;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Nom: " + nom.getValeur() + ", Score: " + score;
    }
}