package miniprojet;

import java.util.List;

public interface Selectionneur {
    List<NomScore> selectionner(List<NomScore> scores, double seuil);
}
