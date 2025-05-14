package miniprojet;

import java.util.*;
import java.util.stream.Collectors;

public class TestDePerformance {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Fournir le chemin du fichier CSV : ");
        String cheminFichier = scanner.nextLine().trim();


        List<String> nomsBruts = RecuperateurFichier.lireNomsDepuisCSV(cheminFichier);
        if (nomsBruts.isEmpty()) {
            System.out.println("Erreur : Le fichier est vide ou introuvable.");
            scanner.close();
            return;
        }


        System.out.print("Entrez le nombre d'exécutions par test (par défaut 5) : ");
        String saisie = scanner.nextLine().trim();
        int nbExecutions;
        try {
            nbExecutions = Integer.parseInt(saisie);
            if (nbExecutions <= 0) {
                nbExecutions = 5;
                System.out.println("Nombre invalide, utilisation de la valeur par défaut: 5.");
            }
        } catch (NumberFormatException e) {
            nbExecutions = 5;
            System.out.println("Entrée invalide, utilisation de la valeur par défaut: 5.");
        }


        System.out.println("Lancement des tests de performance...");
        executerTestsPerformance(cheminFichier, nbExecutions);

        scanner.close();
        System.out.println("Tests de performance terminés.");
    }

    private static void executerTestsPerformance(String cheminFichier, int nbExecutions) {
        // Lire la liste de noms depuis le fichier
        List<String> nomsBruts = RecuperateurFichier.lireNomsDepuisCSV(cheminFichier);
        List<Nom> listeNoms = nomsBruts.stream()
                .map(n -> new Nom(n))
                .collect(Collectors.toList());

        System.out.println("\n=== Configuration du Test ===");
        System.out.println("Fichier utilisé: " + cheminFichier);
        System.out.println("Taille de la liste: " + listeNoms.size());
        System.out.println("Nombre d'exécutions par test: " + nbExecutions + " (pour moyenne)");


        System.out.println("\n=== Test des Prétraiteurs ===");
        testPretraiteurs(listeNoms, nbExecutions);


        System.out.println("\n=== Test des Générateurs de Candidats ===");
        testGenerateurs(listeNoms, nbExecutions);


        System.out.println("\n=== Test des Comparateurs ===");
        testComparateurs(nbExecutions);


        System.out.println("\n=== Test des Sélecteurs ===");
        testSelecteurs(nbExecutions);


        System.out.println("\n=== Test des Combinaisons ===");
        testCombinaisons(listeNoms, nbExecutions);


        System.out.println("\n=== Suggestion de la Meilleure Combinaison ===");
        System.out.println("La meilleure combinaison dépend de vos priorités (vitesse vs précision).");
        System.out.println("Basé sur les tests, la combinaison la plus rapide est généralement :");
        System.out.println("- Prétraiteur: Normaliseur (le plus rapide)");
        System.out.println("- Générateur: GenerateurCouplesTaille (moins de paires générées)");
        System.out.println("- Comparateur: ComparateurEgalite (le plus rapide, mais moins précis)");
        System.out.println("- Sélecteur: SelectionneurSimple (pas de filtrage)");
        System.out.println("Pour une meilleure précision, utilisez ComparateurJaroWinkler avec GenerateurCoupleIndex.");
    }

    private static long mesurerTemps(Runnable tache) {
        long debut = System.nanoTime();
        tache.run();
        long fin = System.nanoTime();
        return fin - debut; // Retourner en nanosecondes
    }

    private static double moyenneTemps(Runnable tache, int nbExecutions) {
        long total = 0;
        for (int i = 0; i < nbExecutions; i++) {
            total += mesurerTemps(tache);
        }
        return total / (double) nbExecutions / 1_000_000; // Convertir en millisecondes
    }

    private static void testPretraiteurs(List<Nom> listeNoms, int nbExecutions) {
        List<Pretraiteur> pretraiteurs = Arrays.asList(
                new Normaliseur(),
                new PretraiteurAccent(),
                new PretraiteurDiviserMotCompose()
        );

        for (Pretraiteur pretraiteur : pretraiteurs) {
            String nomClasse = pretraiteur.getClass().getSimpleName();
            double temps = moyenneTemps(() -> pretraiteur.traiter(listeNoms), nbExecutions);
            System.out.printf("Prétraiteur: %s -> Temps moyen: %.3f ms%n", nomClasse, temps);
        }
    }

    private static void testGenerateurs(List<Nom> listeNoms, int nbExecutions) {

        Pretraiteur pretraiteur = new PretraiteurAccent();
        List<Nom> listeTraitee = pretraiteur.traiter(listeNoms);

        List<GenerateurCandidats> generateurs = Arrays.asList(
                new GenerateurTousCouples(),
                new GenerateurCoupleIndex(),
                new GenerateurCouplesTaille()
        );

        for (GenerateurCandidats generateur : generateurs) {
            String nomClasse = generateur.getClass().getSimpleName();
            double temps = moyenneTemps(() -> generateur.genererCandidats(listeTraitee, listeTraitee), nbExecutions);
            System.out.printf("Générateur: %s -> Temps moyen: %.3f ms%n", nomClasse, temps);
        }
    }

    private static void testComparateurs(int nbExecutions) {

        List<CoupleNom> paires = new ArrayList<>();
        Random random = new Random();
        String[] bases = {"Jean-Pierre", "Marie-Claire", "François", "Éloïse", "Paul"};
        for (int i = 0; i < 10_000; i++) {
            String base = bases[random.nextInt(bases.length)];
            String variation = base.replace("é", "e").replace("-", " ") + (random.nextBoolean() ? i : "");
            paires.add(new CoupleNom(new Nom(base), new Nom(variation)));
        }

        List<Comparateur> comparateurs = Arrays.asList(
                new ComparateurEgalite(),
                new ComparateurLevenshtein(),
                new ComparateurJaroWinkler()
        );

        for (Comparateur comparateur : comparateurs) {
            String nomClasse = comparateur.getClass().getSimpleName();
            double temps = moyenneTemps(() -> {
                // Exécuter 10 fois pour amplifier
                for (int iter = 0; iter < 10; iter++) {
                    for (CoupleNom couple : paires) {
                        comparateur.comparer(couple.getNom1(), couple.getNom2());
                    }
                }
            }, nbExecutions);
            System.out.printf("Comparateur: %s -> Temps moyen pour 10 000 paires (x10 itérations): %.3f ms%n", nomClasse, temps);
        }
    }

    private static void testSelecteurs(int nbExecutions) {
        // Générer 100 000 scores
        List<CoupleScore> scores = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100_000; i++) {
            scores.add(new CoupleScore(new Nom("Nom" + i), new Nom("Nom" + (i + 1)), random.nextDouble()));
        }

        List<Selectionneur> selectionneurs = Arrays.asList(
                new SelectionneurSimple(),
                new SelectionneurParSeuil(0.7),
                new TopNSelectionneur(100)
        );

        for (Selectionneur selectionneur : selectionneurs) {
            String nomClasse = selectionneur.getClass().getSimpleName();
            double temps = moyenneTemps(() -> {
                // Exécuter 10 fois pour amplifier
                for (int iter = 0; iter < 10; iter++) {
                    selectionneur.selectionner(scores);
                }
            }, nbExecutions);
            System.out.printf("Sélecteur: %s -> Temps moyen pour 100 000 scores (x10 itérations): %.3f ms%n", nomClasse, temps);
        }
    }

    private static void testCombinaisons(List<Nom> listeNoms, int nbExecutions) {
        List<Pretraiteur> pretraiteurs = Arrays.asList(new Normaliseur(), new PretraiteurAccent());
        List<GenerateurCandidats> generateurs = Arrays.asList(
                new GenerateurTousCouples(),
                new GenerateurCoupleIndex(),
                new GenerateurCouplesTaille()
        );
        List<Comparateur> comparateurs = Arrays.asList(
                new ComparateurEgalite(),
                new ComparateurLevenshtein(),
                new ComparateurJaroWinkler()
        );
        List<Selectionneur> selectionneurs = Arrays.asList(
                new SelectionneurSimple(),
                new SelectionneurParSeuil(0.7)
        );

        double meilleurTemps = Double.MAX_VALUE;
        String meilleureCombinaison = "";

        for (Pretraiteur pretraiteur : pretraiteurs) {
            List<Nom> listeTraitee = pretraiteur.traiter(listeNoms);
            for (GenerateurCandidats generateur : generateurs) {
                List<CoupleNom> candidats = generateur.genererCandidats(listeTraitee, listeTraitee);
                for (Comparateur comparateur : comparateurs) {
                    List<CoupleScore> scores = new ArrayList<>();
                    for (CoupleNom couple : candidats) {
                        double score = comparateur.comparer(couple.getNom1(), couple.getNom2());
                        scores.add(new CoupleScore(couple.getNom1(), couple.getNom2(), score));
                    }
                    for (Selectionneur selectionneur : selectionneurs) {
                        String combinaison = pretraiteur.getClass().getSimpleName() + " + " +
                                generateur.getClass().getSimpleName() + " + " +
                                comparateur.getClass().getSimpleName() + " + " +
                                selectionneur.getClass().getSimpleName();
                        double temps = moyenneTemps(() -> {
                            List<Nom> lt = pretraiteur.traiter(listeNoms);
                            List<CoupleNom> c = generateur.genererCandidats(lt, lt);
                            List<CoupleScore> s = new ArrayList<>();
                            for (CoupleNom couple : c) {
                                double score = comparateur.comparer(couple.getNom1(), couple.getNom2());
                                s.add(new CoupleScore(couple.getNom1(), couple.getNom2(), score));
                            }
                            selectionneur.selectionner(s);
                        }, nbExecutions);
                        System.out.printf("Combinaison: %s -> Temps moyen: %.3f ms%n", combinaison, temps);
                        if (temps < meilleurTemps) {
                            meilleurTemps = temps;
                            meilleureCombinaison = combinaison;
                        }
                    }
                }
            }
        }

        System.out.printf("\nMeilleure combinaison: %s -> Temps moyen: %.3f ms%n", meilleureCombinaison, meilleurTemps);
    }
}