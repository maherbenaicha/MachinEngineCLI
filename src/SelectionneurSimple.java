package miniprojet;
import java.util.List;

public class SelectionneurSimple implements Selectionneur {
    @Override
    public List<CoupleScore> selectionner(List<CoupleScore> resultatsAvecScore) {
        return resultatsAvecScore;
    }
}