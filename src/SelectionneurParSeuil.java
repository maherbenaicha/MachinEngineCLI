package miniprojet;

import java.util.List;
import java.util.stream.Collectors;

public class SelectionneurParSeuil implements Selectionneur {
    private double seuil;
    private int maxResultats; // Added to support maximum number of results


    public SelectionneurParSeuil(double seuil) {
        this.seuil = seuil;
        this.maxResultats = Integer.MAX_VALUE; // Default to no limit
    }


    public SelectionneurParSeuil(double seuil, int maxResultats) {
        this.seuil = seuil;
        this.maxResultats = maxResultats > 0 ? maxResultats : Integer.MAX_VALUE;
    }

    @Override
    public List<CoupleScore> selectionner(List<CoupleScore> scores) {
        return scores.stream()
                .filter(cs -> cs.getScore() >= seuil)
                .sorted((cs1, cs2) -> Double.compare(cs2.getScore(), cs1.getScore())) // Sort in descending order
                .limit(maxResultats) // Limit to maxResultats
                .collect(Collectors.toList());
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
        this.maxResultats = maxResultats > 0 ? maxResultats : Integer.MAX_VALUE;
    }
}