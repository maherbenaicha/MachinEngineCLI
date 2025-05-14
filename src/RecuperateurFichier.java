package miniprojet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecuperateurFichier {

    public static List<String> lireNomsDepuisCSV(String cheminFichier) {
        List<String> noms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            boolean premiereLigne = true;
            while ((ligne = br.readLine()) != null) {
                if (premiereLigne) {
                    premiereLigne = false;
                    continue; // Ignorer l'en-tête
                }
                String[] parties = ligne.split(",");
                if (parties.length >= 2) {
                    String nom = parties[1].trim();
                    if (!nom.isEmpty()) {
                        noms.add(nom);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
        return noms;
    }
}