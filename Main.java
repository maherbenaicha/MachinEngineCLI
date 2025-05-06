package moteur;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Nom> listeNoms = Arrays.asList(
                new Nom("Maher"),
                new Nom("Marie"),
                new Nom("Marcel"),
                new Nom("Mahir")
        );

        Pretraiteur pretraiteur = new PretraiteurIdentite();
        GenerateurCandidats generateur = new GenerateurTous();
        Comparateur comparateur = new ComparateurEquals();
        Selectionneur selectionneur = new SelectionneurTous();

        Moteur moteur = new Moteur(pretraiteur, generateur, comparateur, selectionneur);

        String nomRecherche = "Maher";
        double seuil = 0.0;

        List<NomScore> resultats = moteur.rechercher(nomRecherche, listeNoms, seuil);

        System.out.println("Résultats pour : " + nomRecherche);
        for (NomScore ns : resultats) {
            System.out.printf("- %s (score : %.2f)%n", ns.getNom().getValeur(), ns.getScore());
        }
    }
}
