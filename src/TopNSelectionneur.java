package miniprojet;

import java.util.List;
import java.util.stream.Collectors;

public class TopNSelectionneur implements Selectionneur {
    private final int n;

    public TopNSelectionneur(int n) {
        this.n = n;
    }

    @Override
    public List<CoupleScore> selectionner(List<CoupleScore> resultatsAvecScore) {
        return resultatsAvecScore.stream()
                .sorted((cs1, cs2) -> Double.compare(cs2.getScore(), cs1.getScore())) // Tri décroissant
                .limit(n) // Limite à n
                .collect(Collectors.toList());
    }
}