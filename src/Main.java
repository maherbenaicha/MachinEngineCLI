import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Exemple de liste de noms
        List<Nom> listeNoms = Arrays.asList(
                new Nom("Maher"),
                new Nom("Mahir"),
                new Nom("Marie"),
                new Nom("Mehdi"),
                new Nom("Marcel")
        );

        // Création des composants (avec Levenshtein ici)
        Pretraiteur pretraiteur = new Normaliseur();
        GenerateurCandidats generateur = new GenerateurSimple();
        Comparateur comparateur = new ComparateurLevenshtein();
        Selectionneur selectionneur = new SelectionneurSimple();

        // Création du moteur
        Moteur moteur = new Moteur(pretraiteur, generateur, comparateur, selectionneur);

        // Nom à rechercher
        String nomRecherche = "maher";

        // Seuil minimal (ex : 0.5 pour avoir des résultats assez proches)
        double seuil = 0.5;

        // Exécution de la recherche
        List<NomScore> resultats = moteur.rechercher(nomRecherche, listeNoms, seuil);

        // Affichage des résultats
        System.out.println("Résultats de la recherche pour : " + nomRecherche);
        for (NomScore ns : resultats) {
            System.out.printf("- %s (score : %.2f)%n", ns.getNom().getValeur(), ns.getScore());
        }
    }
}
