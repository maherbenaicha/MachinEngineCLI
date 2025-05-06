package MatchEngineCLI;
import java.util.*;
public class SelectionneurSimple implements Selectionneur {
    public List<NomScore> selectionner(List<NomScore> scores, double seuil) {
        return scores.stream()
                .filter(ns -> ns.getScore() >= seuil)
                .collect(Collectors.toList());
    }
}
