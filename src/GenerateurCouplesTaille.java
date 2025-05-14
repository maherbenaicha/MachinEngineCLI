package miniprojet;

import java.util.*;


public class GenerateurCouplesTaille implements GenerateurCandidats {
    @Override
    public List<CoupleNom> genererCandidats(List<Nom> listeRecherche, List<Nom> listeBase) {

        Map<Integer, List<Nom>> tailleANoms = new HashMap<>();
        for (Nom nom : listeBase) {
            int taille = nom.getValeur().length();
            tailleANoms.computeIfAbsent(taille, k -> new ArrayList<>()).add(nom);
        }


        List<CoupleNom> couples = new ArrayList<>();
        for (Nom nom1 : listeRecherche) {
            int taille = nom1.getValeur().length();

            for (int delta = -1; delta <= 1; delta++) {
                int tailleCible = taille + delta;
                List<Nom> candidats = tailleANoms.getOrDefault(tailleCible, Collections.emptyList());
                for (Nom nom2 : candidats) {
                    couples.add(new CoupleNom(nom1, nom2));
                }
            }
        }

        return couples;
    }
}