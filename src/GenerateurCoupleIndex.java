package miniprojet;

import java.util.*;

public class GenerateurCoupleIndex implements GenerateurCandidats {
    private Map<String, Set<Nom>> bigramIndex;

    public GenerateurCoupleIndex() {
        this.bigramIndex = new HashMap<>();
    }

    public void indexer(List<Nom> listeBase) {
        bigramIndex.clear();
        for (Nom nom : listeBase) {
            String nomValeur = nom.getValeur().toLowerCase();
            Set<String> bigrammes = extraireBigrammes(nomValeur);
            for (String bigramme : bigrammes) {
                bigramIndex.computeIfAbsent(bigramme, k -> new HashSet<>()).add(nom);
            }
        }
    }

    @Override
    public List<CoupleNom> genererCandidats(List<Nom> listeRecherche, List<Nom> listeBase) {
        indexer(listeBase);
        List<CoupleNom> candidats = new ArrayList<>();
        for (Nom nom1 : listeRecherche) {
            String nomValeur = nom1.getValeur().toLowerCase();
            Set<String> bigrammes = extraireBigrammes(nomValeur);
            Set<Nom> nomsCandidats = new HashSet<>();
            for (String bigramme : bigrammes) {
                Set<Nom> nomsAvecBigramme = bigramIndex.getOrDefault(bigramme, new HashSet<>());
                nomsCandidats.addAll(nomsAvecBigramme);
            }
            for (Nom nom2 : nomsCandidats) {
                candidats.add(new CoupleNom(nom1, nom2));
            }
        }
        return candidats;
    }

    private Set<String> extraireBigrammes(String input) {
        Set<String> bigrammes = new HashSet<>();
        if (input == null || input.length() < 2) {
            return bigrammes;
        }
        for (int i = 0; i < input.length() - 1; i++) {
            bigrammes.add(input.substring(i, i + 2));
        }
        return bigrammes;
    }
}