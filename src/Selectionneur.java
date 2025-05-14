package miniprojet;

import java.util.List;

public interface Selectionneur {
    List<CoupleScore> selectionner(List<CoupleScore> resultatsAvecScore);
}