package miniprojet;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private List<Pretraiteur> pretraiteurs = new ArrayList<>(List.of(
            new Normaliseur(),
            new PretraiteurAccent(),
            new PretraiteurDiviserMotCompose()
    ));
    private GenerateurCandidats generateur = new GenerateurCouplesTaille();
    private Comparateur comparateur = new ComparateurJaroWinkler();
    private Selectionneur selectionneur = new SelectionneurParSeuil(0.6);
    private double seuil = 0.6;
    private int maxResultats = 10;

    public List<Pretraiteur> getPretraiteurs() {
        return pretraiteurs;
    }

    public void setPretraiteurs(List<Pretraiteur> pretraiteurs) {
        this.pretraiteurs = pretraiteurs;
    }

    public GenerateurCandidats getGenerateur() {
        return generateur;
    }

    public void setGenerateur(GenerateurCandidats generateur) {
        this.generateur = generateur;
    }

    public Comparateur getComparateur() {
        return comparateur;
    }

    public void setComparateur(Comparateur comparateur) {
        this.comparateur = comparateur;
    }

    public Selectionneur getSelectionneur() {
        return selectionneur;
    }

    public void setSelectionneur(Selectionneur selectionneur) {
        this.selectionneur = selectionneur;
    }

    public double getSeuil() {
        return seuil;
    }

    public void setSeuil(double seuil) {
        this.seuil = seuil;
    }

    public int getMaxResultats() {
        return maxResultats;
    }

    public void setMaxResultats(int maxResultats) {
        this.maxResultats = maxResultats;
    }
}