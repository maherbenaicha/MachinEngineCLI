package miniprojet;

public class CoupleScore {
    private Nom nom1;
    private Nom nom2;
    private double score;

    public CoupleScore(Nom nom1, Nom nom2, double score) {
        this.nom1 = nom1;
        this.nom2 = nom2;
        this.score = score;
    }

    // Optionnel : Ajouter des getters pour accéder aux champs
    public Nom getNom1() {
        return nom1;
    }

    public Nom getNom2() {
        return nom2;
    }

    public double getScore() {
        return score;
    }
}