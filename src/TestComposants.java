package miniprojet;

import java.util.*;


public class TestComposants {
    public static void main(String[] args) {
        // Liste de test initiale
        List<Nom> listeNoms = Arrays.asList(
                new Nom("Jean-Pierre"),
                new Nom("Jéan Pier"),
                new Nom("Marie Claire"),
                new Nom("Marie-Claire"),
                new Nom("Paul")
        );


        System.out.println("\n=== Liste Initiale ===");
        afficherListe(listeNoms);


        System.out.println("\n=== Étape 1 : Test des Prétraiteurs ===");
        System.out.println("Chaque prétraiteur est appliqué indépendamment à la liste initiale.");
        testPretraiteurs(listeNoms);


        System.out.println("\n=== Étape 2 : Test des Générateurs de Candidats ===");
        System.out.println("On recherche 'Jean-Pierre' dans la liste après pré-traitement.");
        List<Nom> listeRecherche = Arrays.asList(new Nom("Jean-Pierre"));
        testGenerateurs(listeRecherche, listeNoms);


        System.out.println("\n=== Étape 3 : Test des Comparateurs ===");
        System.out.println("Comparaison entre 'Jean-Pierre' et 'Jéan Pier' après pré-traitement.");
        testComparateurs();


        System.out.println("\n=== Étape 4 : Test des Sélecteurs ===");
        System.out.println("Filtrage des scores pour une liste de paires prédéfinies.");
        testSelecteurs();


        System.out.println("\n=== Étape 5 : Processus Complet ===");
        System.out.println("Exécution du processus complet : pré-traitement -> génération -> comparaison -> sélection.");
        testProcessusComplet(listeRecherche, listeNoms);
    }

    private static void afficherListe(List<Nom> liste) {
        if (liste.isEmpty()) {
            System.out.println("  (Liste vide)");
        } else {
            for (Nom nom : liste) {
                System.out.println("  " + nom.getValeur());
            }
        }
    }

    private static void testPretraiteurs(List<Nom> listeNoms) {
        List<Pretraiteur> pretraiteurs = Arrays.asList(
                new Normaliseur(),
                new PretraiteurAccent(),
                new PretraiteurDiviserMotCompose()
        );

        for (Pretraiteur pretraiteur : pretraiteurs) {
            String nomClasse = pretraiteur.getClass().getSimpleName();
            System.out.println("\nPrétraiteur: " + nomClasse);
            System.out.println("Description: " + getDescriptionPretraiteur(nomClasse));
            System.out.println("Liste après traitement:");
            List<Nom> resultat = pretraiteur.traiter(listeNoms);
            afficherListe(resultat);
        }
    }

    private static String getDescriptionPretraiteur(String nomClasse) {
        return switch (nomClasse) {
            case "Normaliseur" -> "Convertit tous les caractères en minuscules.";
            case "PretraiteurAccent" -> "Supprime les accents des caractères.";
            case "PretraiteurDiviserMotCompose" -> "Divise les mots composés en supprimant tirets, espaces, ou apostrophes.";
            default -> "Prétraiteur inconnu.";
        };
    }

    private static void testGenerateurs(List<Nom> listeRecherche, List<Nom> listeBase) {

        Pretraiteur pretraiteur = new PretraiteurAccent();
        List<Nom> listeRechercheTraitee = pretraiteur.traiter(listeRecherche);
        List<Nom> listeBaseTraitee = pretraiteur.traiter(listeBase);

        System.out.println("\nListe de recherche après pré-traitement (PretraiteurAccent):");
        afficherListe(listeRechercheTraitee);
        System.out.println("\nListe de base après pré-traitement (PretraiteurAccent):");
        afficherListe(listeBaseTraitee);

        List<GenerateurCandidats> generateurs = Arrays.asList(
                new GenerateurTousCouples(),
                new GenerateurCoupleIndex(),
                new GenerateurCouplesTaille()
        );

        for (GenerateurCandidats generateur : generateurs) {
            String nomClasse = generateur.getClass().getSimpleName();
            System.out.println("\nGénérateur: " + nomClasse);
            System.out.println("Description: " + getDescriptionGenerateur(nomClasse));
            System.out.println("Paires générées:");
            List<CoupleNom> candidats = generateur.genererCandidats(listeRechercheTraitee, listeBaseTraitee);
            if (candidats.isEmpty()) {
                System.out.println("  (Aucune paire générée)");
            } else {
                for (CoupleNom couple : candidats) {
                    System.out.println("  " + couple.getNom1().getValeur() + " <-> " + couple.getNom2().getValeur());
                }
            }
        }
    }

    private static String getDescriptionGenerateur(String nomClasse) {
        return switch (nomClasse) {
            case "GenerateurTousCouples" -> "Génère toutes les paires possibles entre recherche et base.";
            case "GenerateurCoupleIndex" -> "Génère des paires basées sur un index de bigrammes (sous-chaînes de 2 caractères).";
            case "GenerateurCouplesTaille" -> "Génère des paires avec des longueurs similaires (±1 caractère).";
            default -> "Générateur inconnu.";
        };
    }

    private static void testComparateurs() {
        // Pré-traitement des noms pour cohérence
        Pretraiteur pretraiteur = new PretraiteurAccent();
        Nom nom1 = pretraiteur.traiter(Arrays.asList(new Nom("Jean-Pierre"))).get(0);
        Nom nom2 = pretraiteur.traiter(Arrays.asList(new Nom("Jéan Pier"))).get(0);

        System.out.println("\nNoms comparés après pré-traitement (PretraiteurAccent):");
        System.out.println("  Nom 1: " + nom1.getValeur());
        System.out.println("  Nom 2: " + nom2.getValeur());

        List<Comparateur> comparateurs = Arrays.asList(
                new ComparateurEgalite(),
                new ComparateurLevenshtein(),
                new ComparateurJaroWinkler()
        );

        for (Comparateur comparateur : comparateurs) {
            String nomClasse = comparateur.getClass().getSimpleName();
            System.out.println("\nComparateur: " + nomClasse);
            System.out.println("Description: " + getDescriptionComparateur(nomClasse));
            double score = comparateur.comparer(nom1, nom2);
            System.out.println("Score: " + String.format("%.3f", score));
        }
    }

    private static String getDescriptionComparateur(String nomClasse) {
        return switch (nomClasse) {
            case "ComparateurEgalite" -> "Retourne 1.0 si les noms sont identiques, 0.0 sinon.";
            case "ComparateurLevenshtein" -> "Calcule la similarité basée sur le nombre de modifications nécessaires.";
            case "ComparateurJaroWinkler" -> "Utilise la mesure Jaro-Winkler, optimisée pour les noms avec un poids sur les préfixes.";
            default -> "Comparateur inconnu.";
        };
    }

    private static void testSelecteurs() {
        // Liste de scores prédéfinie
        List<CoupleScore> scores = Arrays.asList(
                new CoupleScore(new Nom("Jean-Pierre"), new Nom("Jéan Pier"), 0.95),
                new CoupleScore(new Nom("Jean-Pierre"), new Nom("Marie Claire"), 0.60),
                new CoupleScore(new Nom("Jean-Pierre"), new Nom("Paul"), 0.45),
                new CoupleScore(new Nom("Marie Claire"), new Nom("Marie-Claire"), 0.98)
        );

        System.out.println("\nListe des scores avant sélection:");
        for (CoupleScore couple : scores) {
            System.out.println("  " + couple.getNom1().getValeur() + " <-> " +
                    couple.getNom2().getValeur() + " (Score: " + String.format("%.3f", couple.getScore()) + ")");
        }

        List<Selectionneur> selectionneurs = Arrays.asList(
                new SelectionneurSimple(),
                new SelectionneurParSeuil(0.7),
                new TopNSelectionneur(2)
        );

        for (Selectionneur selectionneur : selectionneurs) {
            String nomClasse = selectionneur.getClass().getSimpleName();
            System.out.println("\nSélecteur: " + nomClasse);
            System.out.println("Description: " + getDescriptionSelectionneur(nomClasse));
            System.out.println("Résultats après sélection:");
            List<CoupleScore> resultats = selectionneur.selectionner(scores);
            if (resultats.isEmpty()) {
                System.out.println("  (Aucun résultat sélectionné)");
            } else {
                for (CoupleScore couple : resultats) {
                    System.out.println("  " + couple.getNom1().getValeur() + " <-> " +
                            couple.getNom2().getValeur() + " (Score: " + String.format("%.3f", couple.getScore()) + ")");
                }
            }
        }
    }

    private static String getDescriptionSelectionneur(String nomClasse) {
        return switch (nomClasse) {
            case "SelectionneurSimple" -> "Retourne tous les résultats sans filtrage.";
            case "SelectionneurParSeuil" -> "Ne conserve que les paires avec un score supérieur ou égal à 0.7.";
            case "TopNSelectionneur" -> "Ne conserve que les 2 meilleures paires (scores les plus élevés).";
            default -> "Sélecteur inconnu.";
        };
    }

    private static void testProcessusComplet(List<Nom> listeRecherche, List<Nom> listeBase) {
        System.out.println("\nConfiguration utilisée:");
        System.out.println("  Prétraiteur: PretraiteurAccent");
        System.out.println("  Générateur: GenerateurCouplesTaille");
        System.out.println("  Comparateur: ComparateurJaroWinkler");
        System.out.println("  Sélecteur: SelectionneurParSeuil (seuil = 0.7)");

        // Étape 1: Pré-traitement
        Pretraiteur pretraiteur = new PretraiteurAccent();
        System.out.println("\nÉtape 1: Pré-traitement");
        System.out.println("Liste de recherche avant:");
        afficherListe(listeRecherche);
        List<Nom> rechercheTraitee = pretraiteur.traiter(listeRecherche);
        System.out.println("Liste de recherche après:");
        afficherListe(rechercheTraitee);
        System.out.println("Liste de base avant:");
        afficherListe(listeBase);
        List<Nom> baseTraitee = pretraiteur.traiter(listeBase);
        System.out.println("Liste de base après:");
        afficherListe(baseTraitee);

        // Étape 2: Génération de candidats
        GenerateurCandidats generateur = new GenerateurCouplesTaille();
        System.out.println("\nÉtape 2: Génération de candidats");
        List<CoupleNom> candidats = generateur.genererCandidats(rechercheTraitee, baseTraitee);
        System.out.println("Paires générées:");
        if (candidats.isEmpty()) {
            System.out.println("  (Aucune paire générée)");
        } else {
            for (CoupleNom couple : candidats) {
                System.out.println("  " + couple.getNom1().getValeur() + " <-> " + couple.getNom2().getValeur());
            }
        }

        // Étape 3: Comparaison
        Comparateur comparateur = new ComparateurJaroWinkler();
        System.out.println("\nÉtape 3: Comparaison");
        List<CoupleScore> scores = new ArrayList<>();
        for (CoupleNom couple : candidats) {
            double score = comparateur.comparer(couple.getNom1(), couple.getNom2());
            scores.add(new CoupleScore(couple.getNom1(), couple.getNom2(), score));
        }
        System.out.println("Scores calculés:");
        if (scores.isEmpty()) {
            System.out.println("  (Aucun score calculé)");
        } else {
            for (CoupleScore couple : scores) {
                System.out.println("  " + couple.getNom1().getValeur() + " <-> " +
                        couple.getNom2().getValeur() + " (Score: " + String.format("%.3f", couple.getScore()) + ")");
            }
        }

        // Étape 4: Sélection
        Selectionneur selectionneur = new SelectionneurParSeuil(0.7);
        System.out.println("\nÉtape 4: Sélection");
        List<CoupleScore> resultats = selectionneur.selectionner(scores);
        System.out.println("Résultats après sélection:");
        if (resultats.isEmpty()) {
            System.out.println("  (Aucun résultat sélectionné)");
        } else {
            for (CoupleScore couple : resultats) {
                System.out.println("  " + couple.getNom1().getValeur() + " <-> " +
                        couple.getNom2().getValeur() + " (Score: " + String.format("%.3f", couple.getScore()) + ")");
            }
        }
    }
}