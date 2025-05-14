package miniprojet;

import java.util.ArrayList;
import java.util.List;

public class GenerateurTousCouples implements GenerateurCandidats {
    @Override
    public List<CoupleNom> genererCandidats(List<Nom> listeRecherche, List<Nom> listeBase) {
        List<CoupleNom> couples = new ArrayList<>();
        for (Nom nom1 : listeRecherche) {
            for (Nom nom2 : listeBase) {
                couples.add(new CoupleNom(nom1, nom2));
            }
        }
        return couples;
    }
}