package miniprojet;

import java.util.List;
import java.util.stream.Collectors;

public class SelectionneurSimple implements Selectionneur {
    
    public List<NomScore> selectionner(List<NomScore> scores, double seuil) {
        return scores.stream()
                .filter(ns -> ns.getScore() >= seuil)
                .collect(Collectors.toList());
    }
}
