package miniprojet;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
// Tai Lee Siang        C:\Users\Maher\Desktop\testouet\peps_names_1k.csv
public class Main {
    static MoteurdeMatching moteur;
    static Configuration config = new Configuration();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        moteur = new MoteurdeMatching(
                config.getPretraiteurs(),
                config.getGenerateur(),
                config.getComparateur(),
                config.getSelectionneur()
        );

        while (true) {
            afficherMenuPrincipal();
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> effectuerRecherche();
                case "2" -> comparerDeuxListes();
                case "3" -> effectuerDedupliquerListe();
                case "4" -> configurerParametres();
                case "5" -> {
                    System.out.println("Fin du programme.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    static void afficherMenuPrincipal() {
        System.out.println("\n===== MENU PRINCIPAL =====\n");
        System.out.println("1. Rechercher un nom");
        System.out.println("2. Comparer deux listes de noms");
        System.out.println("3. Dédupliquer une liste de noms");
        System.out.println("4. Configurer les paramètres");
        System.out.println("5. Quitter");
        System.out.print("Votre choix : ");
    }

    static void afficherMenuConfiguration() {
        System.out.println("\n===== CONFIGURATION DES PARAMÈTRES =====");
        System.out.println("1. Gérer les pré-traitements des noms");
        System.out.println("2. Sélectionner une mesure de comparaison");
        System.out.println("3. Choisir un générateur de candidats");
        System.out.println("4. Définir un selectionneur de résultats");
        System.out.println("5. Retour au menu principal");
        System.out.print("Votre choix : ");
    }

    static void configurerParametres() {
        while (true) {
            afficherMenuConfiguration();
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> choixPretraiteurs();
                case "2" -> choixComparateur();
                case "3" -> choixGenerateur();
                case "4" -> choixSelectionneur();
                case "5" -> {
                    moteur = new MoteurdeMatching(
                            config.getPretraiteurs(),
                            config.getGenerateur(),
                            config.getComparateur(),
                            config.getSelectionneur()
                    );
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    static void afficherMenuPretraiteur() {
        System.out.println("\n===== GESTION DES PRÉ-TRAITEMENTS =====");
        System.out.println("Pré-traitements actuellement actifs :");
        List<Pretraiteur> pretraiteurs = config.getPretraiteurs();
        if (pretraiteurs.isEmpty()) {
            System.out.println("  Aucun pré-traitement activé.");
        } else {
            for (int i = 0; i < pretraiteurs.size(); i++) {
                String description = switch (pretraiteurs.get(i).getClass().getSimpleName()) {
                    case "Normaliseur" -> "Convertir en minuscules";
                    case "PretraiteurAccent" -> "Supprimer les accents";
                    case "PretraiteurDiviserMotCompose" -> "Séparer les mots composés";
                    default -> "Pré-traitement inconnu";
                };
                System.out.println("  " + (i + 1) + ". " + description);
            }
        }
        System.out.println("\nOptions :");
        System.out.println("1. Ajouter un pré-traitement");
        System.out.println("2. Supprimer un pré-traitement");
        System.out.println("3. Retour");
        System.out.print("Votre choix : ");
    }

    static void choixPretraiteurs() {
        while (true) {
            afficherMenuPretraiteur();
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> ajouterPretraiteur();
                case "2" -> supprimerPretraiteur();
                case "3" -> {
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    static void ajouterPretraiteur() {
        System.out.println("\n===== AJOUTER UN PRÉ-TRAITEMENT =====");
        System.out.println("Pré-traitements disponibles :");
        List<String> options = new ArrayList<>();
        if (config.getPretraiteurs().stream().noneMatch(p -> p instanceof Normaliseur)) {
            options.add("1. Convertir en minuscules");
        }
        if (config.getPretraiteurs().stream().noneMatch(p -> p instanceof PretraiteurAccent)) {
            options.add("2. Supprimer les accents");
        }
        if (config.getPretraiteurs().stream().noneMatch(p -> p instanceof PretraiteurDiviserMotCompose)) {
            options.add("3. Séparer les mots composés");
        }
        if (options.isEmpty()) {
            System.out.println("Aucun pré-traitement supplémentaire disponible.");
            return;
        }
        options.forEach(System.out::println);
        System.out.println("4. Retour");
        System.out.print("Votre choix : ");

        String choix = scanner.nextLine();
        switch (choix) {
            case "1" -> {
                if (config.getPretraiteurs().stream().noneMatch(p -> p instanceof Normaliseur)) {
                    config.getPretraiteurs().add(new Normaliseur());
                    System.out.println("Pré-traitement 'Convertir en minuscules' ajouté.");
                } else {
                    System.out.println("Ce pré-traitement est déjà actif.");
                }
            }
            case "2" -> {
                if (config.getPretraiteurs().stream().noneMatch(p -> p instanceof PretraiteurAccent)) {
                    config.getPretraiteurs().add(new PretraiteurAccent());
                    System.out.println("Pré-traitement 'Supprimer les accents' ajouté.");
                } else {
                    System.out.println("Ce pré-traitement est déjà actif.");
                }
            }
            case "3" -> {
                if (config.getPretraiteurs().stream().noneMatch(p -> p instanceof PretraiteurDiviserMotCompose)) {
                    config.getPretraiteurs().add(new PretraiteurDiviserMotCompose());
                    System.out.println("Pré-traitement 'Séparer les mots composés' ajouté.");
                } else {
                    System.out.println("Ce pré-traitement est déjà actif.");
                }
            }
            case "4" -> {
                return;
            }
            default -> System.out.println("Choix invalide.");
        }
    }

    static void supprimerPretraiteur() {
        List<Pretraiteur> pretraiteurs = config.getPretraiteurs();
        if (pretraiteurs.isEmpty()) {
            System.out.println("Aucun pré-traitement à supprimer.");
            return;
        }

        System.out.println("\n===== SUPPRIMER UN PRÉ-TRAITEMENT =====");
        System.out.println("Sélectionnez le pré-traitement à supprimer :");
        for (int i = 0; i < pretraiteurs.size(); i++) {
            String description = switch (pretraiteurs.get(i).getClass().getSimpleName()) {
                case "Normaliseur" -> "Convertir en minuscules";
                case "PretraiteurAccent" -> "Supprimer les accents";
                case "PretraiteurDiviserMotCompose" -> "Séparer les mots composés";
                default -> "Pré-traitement inconnu";
            };
            System.out.println((i + 1) + ". " + description);
        }
        System.out.println((pretraiteurs.size() + 1) + ". Retour");
        System.out.print("Votre choix : ");

        String choix = scanner.nextLine();
        try {
            int index = Integer.parseInt(choix) - 1;
            if (index >= 0 && index < pretraiteurs.size()) {
                String description = switch (pretraiteurs.get(index).getClass().getSimpleName()) {
                    case "Normaliseur" -> "Convertir en minuscules";
                    case "PretraiteurAccent" -> "Supprimer les accents";
                    case "PretraiteurDiviserMotCompose" -> "Séparer les mots composés";
                    default -> "Pré-traitement inconnu";
                };
                pretraiteurs.remove(index);
                System.out.println("Pré-traitement '" + description + "' supprimé.");
            } else if (index == pretraiteurs.size()) {
                return;
            } else {
                System.out.println("Choix invalide.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Choix invalide.");
        }
    }

    static void afficherMenuComparateur() {
        System.out.println("\n===== SÉLECTION DE LA MESURE DE COMPARAISON =====");
        System.out.println("1. Égalité exacte (comparaison stricte)");
        System.out.println("2. Distance de Levenshtein (basée sur les modifications)");
        System.out.println("3. Jaro-Winkler (optimisée pour les noms)");
        System.out.println("4. Retour");
        System.out.print("Votre choix : ");
    }

    static void choixComparateur() {
        while (true) {
            afficherMenuComparateur();
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> {
                    config.setComparateur(new ComparateurEgalite());
                    System.out.println("Mesure définie sur Égalité exacte.");
                    return;
                }
                case "2" -> {
                    config.setComparateur(new ComparateurLevenshtein());
                    System.out.println("Mesure définie sur Distance de Levenshtein.");
                    return;
                }
                case "3" -> {
                    config.setComparateur(new ComparateurJaroWinkler());
                    System.out.println("Mesure définie sur Jaro-Winkler.");
                    return;
                }
                case "4" -> {
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    static void afficherMenuGenerateur() {
        System.out.println("\n===== SÉLECTION DU GÉNÉRATEUR DE CANDIDATS =====");
        System.out.println("1. Tous les couples ");
        System.out.println("2. Par index");
        System.out.println("3. Par longueur");
        System.out.println("4. Retour");
        System.out.print("Votre choix : ");
    }

    static void choixGenerateur() {
        while (true) {
            afficherMenuGenerateur();
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> {
                    config.setGenerateur(new GenerateurTousCouples());
                    System.out.println("Générateur défini sur Tous les couples.");
                    return;
                }
                case "2" -> {
                    config.setGenerateur(new GenerateurCoupleIndex());
                    System.out.println("Générateur défini sur Index.");
                    return;
                }
                case "3" -> {
                    config.setGenerateur(new GenerateurCouplesTaille());
                    System.out.println("Générateur défini sur Longueur.");
                    return;
                }
                case "4" -> {
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    static void afficherMenuSelectionneur() {
        System.out.println("\n===== SÉLECTION DU selectionneur DE RÉSULTATS =====");
        System.out.println("1. Tous les résultats (aucun filtrage)");
        System.out.println("2. Par seuil de similarité");
        System.out.println("3. Les N premiers résultats");
        System.out.println("4. Retour");
        System.out.print("Votre choix : ");
    }

    static void choixSelectionneur() {
        while (true) {
            afficherMenuSelectionneur();
            String choix = scanner.nextLine();

            switch (choix) {
                case "1" -> {
                    config.setSelectionneur(new SelectionneurSimple());
                    System.out.println("selectionneur défini sur Tous les résultats.");
                    return;
                }
                case "2" -> {
                    System.out.print("Entrez le seuil de similarité (0 à 1) : ");
                    String saisie = scanner.nextLine().trim().replace(',', '.');
                    try {
                        double seuil = Double.parseDouble(saisie);
                        if (seuil >= 0 && seuil <= 1) {
                            config.setSelectionneur(new SelectionneurParSeuil(seuil));
                            System.out.println("selectionneur défini sur Seuil : " + seuil);
                            System.out.println("Seuil configuré dans Main : " + seuil);
                        } else {
                            System.out.println("Seuil invalide. Doit être entre 0 et 1.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : valeur non valide !");
                    }
                    return;
                }
                case "3" -> {
                    System.out.print("Entrez le nombre de résultats à sélectionner : ");
                    String saisie = scanner.nextLine();
                    try {
                        int n = Integer.parseInt(saisie);
                        if (n > 0) {
                            config.setSelectionneur(new TopNSelectionneur(n));
                            System.out.println("selectionneur défini sur " + n + " premiers résultats.");
                        } else {
                            System.out.println("Nombre invalide. Doit être supérieur à 0.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : valeur non valide !");
                    }
                    return;
                }
                case "4" -> {
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    static void effectuerRecherche() {
        System.out.print("Entrez le nom à rechercher : ");
        String nomRecherche = scanner.nextLine().trim();
        if (nomRecherche.isEmpty()) {
            System.out.println("Le nom ne peut pas être vide.");
            return;
        }
        System.out.print("Fournir le chemin du fichier CSV : ");
        String cheminFichier = scanner.nextLine().trim();
        List<Nom> listeNoms = RecuperateurFichier.lireNomsDepuisCSV(cheminFichier)
                .stream().map(n -> new Nom(n)).collect(Collectors.toList());
        if (listeNoms.isEmpty()) {
            System.out.println("Le fichier est vide ou introuvable.");
            return;
        }
        long start = System.currentTimeMillis();
        List<NomScore> resultats = moteur.rechercher(nomRecherche, listeNoms, config.getSeuil());
        long end = System.currentTimeMillis();
        System.out.println("\nRésultats pour : " + nomRecherche);
        if (resultats.isEmpty()) {
            System.out.println("Aucune correspondance trouvée.");
        } else {
            for (NomScore ns : resultats) {
                String id = ns.getNom().getId().isEmpty() ? "N/A" : ns.getNom().getId();
                System.out.printf("ID: %s, Nom: %s (Score: %.2f)%n", id, ns.getNom().getValeur(), ns.getScore());
            }
        }
        System.out.println("Temps d'exécution : " + (end - start) + " ms");
    }

    static void comparerDeuxListes() {
        System.out.print("Fournir le chemin du premier fichier CSV : ");
        String cheminFichier1 = scanner.nextLine().trim();
        List<Nom> liste1 = RecuperateurFichier.lireNomsDepuisCSV(cheminFichier1)
                .stream().map(n -> new Nom(n)).collect(Collectors.toList());
        if (liste1.isEmpty()) {
            System.out.println("Le premier fichier est vide ou introuvable.");
            return;
        }
        System.out.print("Fournir le chemin du second fichier CSV : ");
        String cheminFichier2 = scanner.nextLine().trim();
        List<Nom> liste2 = RecuperateurFichier.lireNomsDepuisCSV(cheminFichier2)
                .stream().map(n -> new Nom(n)).collect(Collectors.toList());
        if (liste2.isEmpty()) {
            System.out.println("Le second fichier est vide ou introuvable.");
            return;
        }
        long start = System.currentTimeMillis();
        List<CoupleScore> resultats = moteur.comparerListes(liste1, liste2, config.getSeuil());
        long end = System.currentTimeMillis();
        if (resultats.isEmpty()) {
            System.out.println("Aucune correspondance trouvée.");
        } else {
            for (CoupleScore couple : resultats) {
                String id1 = couple.getNom1().getId().isEmpty() ? "N/A" : couple.getNom1().getId();
                String id2 = couple.getNom2().getId().isEmpty() ? "N/A" : couple.getNom2().getId();
                System.out.printf("%s:%s <-> %s:%s (Score: %.2f)%n",
                        id1, couple.getNom1().getValeur(), id2, couple.getNom2().getValeur(), couple.getScore());
            }
        }
        System.out.println("Temps d'exécution : " + (end - start) + " ms");
    }

    static void effectuerDedupliquerListe() {
        System.out.print("Fournir le chemin du fichier CSV à dédupliquer : ");
        String cheminFichier = scanner.nextLine().trim();
        List<Nom> liste = RecuperateurFichier.lireNomsDepuisCSV(cheminFichier)
                .stream().map(n -> new Nom(n)).collect(Collectors.toList());
        if (liste.isEmpty()) {
            System.out.println("Le fichier est vide ou introuvable.");
            return;
        }
        long start = System.currentTimeMillis();
        List<CoupleScore> dedupliquee = moteur.dedupliquer(liste, config.getSeuil());
        long end = System.currentTimeMillis();
        if (dedupliquee.isEmpty()) {
            System.out.println("Aucune correspondance similaire trouvée.");
        } else {
            System.out.println("Paires similaires trouvées (" + dedupliquee.size() + " paires) :");
            for (CoupleScore couple : dedupliquee) {
                String id1 = couple.getNom1().getId().isEmpty() ? "N/A" : couple.getNom1().getId();
                String id2 = couple.getNom2().getId().isEmpty() ? "N/A" : couple.getNom2().getId();
                System.out.printf("%s:%s <-> %s:%s (Score: %.2f)%n",
                        id1, couple.getNom1().getValeur(), id2, couple.getNom2().getValeur(), couple.getScore());
            }
        }
        System.out.println("Temps d'exécution : " + (end - start) + " ms");
    }
} // Tai Lee Siang        C:\Users\Maher\Desktop\testouet\peps_names_1k.csv
