package miniprojet;

import java.util.*;
import java.io.*;
import miniprojet.RecuperateurFichier;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Configuration par défaut
        Pretraiteur pretraiteur = new Normaliseur();
        GenerateurCandidats generateur = new GenerateurSimple();
        Comparateur comparateur = new ComparateurEgalite();
        Selectionneur selectionneur = new SelectionneurSimple();

        MoteurdeMatching moteur = new MoteurdeMatching(pretraiteur, generateur, comparateur, selectionneur);

        while (true) {
            System.out.println("=== MENU ===");
            System.out.println("1. Effectuer une recherche");
            System.out.println("2. Quitter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            if (choix.equals("1")) {
                System.out.print("Saisir le nom à rechercher : ");
                String nomRequête = scanner.nextLine();

                System.out.print("Chemin du fichier CSV de base : ");
                String cheminFichier = scanner.nextLine();

                // Lecture des noms depuis le fichier CSV
                List<String> nomsBaseString = RecuperateurFichier.lireNomsDepuisCSV(cheminFichier);

                // Conversion de List<String> en List<Nom>
                List<Nom> nomsBase = new ArrayList<>();
                for (String nom : nomsBaseString) {
                    nomsBase.add(new Nom(nom));
                }

                // Ajout du seuil comme paramètre
                double seuil = 0.8; // Valeur par défaut
                List<NomScore> résultats = moteur.rechercher(nomRequête, nomsBase, seuil);

                System.out.println("\nRésultats :");
                for (NomScore ns : résultats) {
                    System.out.println(ns);
                }
                System.out.println();
            } else if (choix.equals("2")) {
                System.out.println("Fin du programme.");
                break;
            } else {
                System.out.println("Choix invalide.\n");
            }
        }

        scanner.close();
    }
}