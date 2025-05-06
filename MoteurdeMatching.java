package miniprojet;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MoteurdeMatching {
    private Pretraiteur pretraiteur;
    private GenerateurCandidats generateur;
    private Comparateur comparateur;
    private Selectionneur selectionneur;

    public MoteurdeMatching(Pretraiteur pretraiteur, GenerateurCandidats generateur,
                            Comparateur comparateur, Selectionneur selectionneur) {
        this.pretraiteur = pretraiteur;
        this.generateur = generateur;
        this.comparateur = comparateur;
        this.selectionneur = selectionneur;
    }

    public List<NomScore> rechercher(String nomRecherche, List<Nom> listeNoms, double seuil) {
        String nomTraite = pretraiteur.traiter(nomRecherche);
        List<String> candidats = generateur.genererCandidats(nomTraite,
                listeNoms.stream().map(Nom::getValeur).collect(Collectors.toList()));

        List<NomScore> scores = new ArrayList<>();
        for (String candidat : candidats) {
            double score = comparateur.comparer(nomTraite, candidat);
            scores.add(new NomScore(new Nom(candidat), score));
        }

        return selectionneur.selectionner(scores, seuil);
    }
}
