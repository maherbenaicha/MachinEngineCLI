package miniprojet;

import java.util.List;

public interface GenerateurCandidats {
    List<CoupleNom> genererCandidats(List<Nom> listeRecherche, List<Nom> listeBase);
}