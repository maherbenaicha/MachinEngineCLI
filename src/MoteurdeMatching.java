package miniprojet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MoteurdeMatching {
    private List<Pretraiteur> pretraiteurs;
    private GenerateurCandidats generateur;
    private Comparateur comparateur;
    private Selectionneur selectionneur;

    public MoteurdeMatching(List<Pretraiteur> pretraiteurs, GenerateurCandidats generateur,
                            Comparateur comparateur, Selectionneur selectionneur) {
        this.pretraiteurs = pretraiteurs;
        this.generateur = generateur;
        this.comparateur = comparateur;
        this.selectionneur = selectionneur;
    }

    public List<NomScore> rechercher(String nomRecherche, List<Nom> listeNoms, double seuil) {
        if (nomRecherche == null || listeNoms == null) {
            return new ArrayList<>();
        }
        Nom nomRechercheNom = new Nom(nomRecherche);
        List<Nom> nomTraite = List.of(nomRechercheNom);
        List<Nom> listeNomsTraites = new ArrayList<>(listeNoms);

        for (Pretraiteur pretraiteur : pretraiteurs) {
            nomTraite = pretraiteur.traiter(nomTraite);
            listeNomsTraites = pretraiteur.traiter(listeNomsTraites);
        }

        nomTraite = nomTraite.stream().filter(nom -> nom != null).collect(Collectors.toList());
        listeNomsTraites = listeNomsTraites.stream().filter(nom -> nom != null).collect(Collectors.toList());

        if (nomTraite.isEmpty()) {
            return new ArrayList<>();
        }

        List<CoupleNom> couples = generateur.genererCandidats(nomTraite, listeNomsTraites);
        List<CoupleScore> scores = new ArrayList<>();
        for (CoupleNom couple : couples) {
            double score = comparateur.comparer(couple.getNom1(), couple.getNom2());
            scores.add(new CoupleScore(couple.getNom1(), couple.getNom2(), score));
        }

        List<CoupleScore> selected = selectionneur.selectionner(scores);
        return selected.stream()
                .map(cs -> new NomScore(cs.getNom2(), cs.getScore()))
                .collect(Collectors.toList());
    }

    public List<CoupleScore> comparerListes(List<Nom> liste1, List<Nom> liste2, double seuil) {
        if (liste1 == null || liste2 == null) {
            return new ArrayList<>();
        }
        List<Nom> liste1Traitee = new ArrayList<>(liste1);
        List<Nom> liste2Traitee = new ArrayList<>(liste2);

        for (Pretraiteur pretraiteur : pretraiteurs) {
            liste1Traitee = pretraiteur.traiter(liste1Traitee);
            liste2Traitee = pretraiteur.traiter(liste2Traitee);
        }

        liste1Traitee = liste1Traitee.stream().filter(nom -> nom != null).collect(Collectors.toList());
        liste2Traitee = liste2Traitee.stream().filter(nom -> nom != null).collect(Collectors.toList());

        List<CoupleNom> couples = generateur.genererCandidats(liste1Traitee, liste2Traitee);
        List<CoupleScore> scores = new ArrayList<>();
        for (CoupleNom couple : couples) {
            double score = comparateur.comparer(couple.getNom1(), couple.getNom2());
            scores.add(new CoupleScore(couple.getNom1(), couple.getNom2(), score));
        }
        return selectionneur.selectionner(scores);
    }

    public List<CoupleScore> dedupliquer(List<Nom> liste, double seuil) {
        if (liste == null) {
            return new ArrayList<>();
        }
        List<Nom> listeTraitee = new ArrayList<>(liste);
        for (Pretraiteur pretraiteur : pretraiteurs) {
            listeTraitee = pretraiteur.traiter(listeTraitee);
        }
        listeTraitee = listeTraitee.stream().filter(nom -> nom != null).collect(Collectors.toList());


        System.out.println("Comparateur utilisé pour la déduplication : " + comparateur.getClass().getSimpleName());


        Map<Integer, List<Nom>> index = new HashMap<>();
        for (Nom nom : listeTraitee) {
            int len = nom.getValeur().length();
            index.computeIfAbsent(len, k -> new ArrayList<>()).add(nom);
        }

        List<CoupleScore> similarPairs = new ArrayList<>();
        List<Nom> processed = new ArrayList<>();

        for (Nom nom : listeTraitee) {
            if (processed.contains(nom)) {
                continue;
            }
            processed.add(nom);
            int len = nom.getValeur().length();
            for (int checkLen = len - 1; checkLen <= len + 1; checkLen++) {
                List<Nom> candidats = index.getOrDefault(checkLen, new ArrayList<>());
                for (Nom candidat : candidats) {
                    if (processed.contains(candidat) || nom == candidat) {
                        continue;
                    }
                    double score = comparateur.comparer(nom, candidat);
                    // Log every score calculated
                    System.out.println("Score pour " + nom.getValeur() + " vs " + candidat.getValeur() + ": " + score);
                    if (score >= seuil) {
                        similarPairs.add(new CoupleScore(nom, candidat, score));
                        processed.add(candidat);
                    }
                }
            }
        }


        System.out.println("Paires similaires avant sélection : " + similarPairs.size());
        List<CoupleScore> selected = selectionneur.selectionner(similarPairs);

        System.out.println("Paires similaires après sélection : " + selected.size());

        return selected;
    }
}