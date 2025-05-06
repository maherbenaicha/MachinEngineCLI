package MatchEngineCLI;
import java.util.*;
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
}