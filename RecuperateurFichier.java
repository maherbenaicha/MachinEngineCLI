package miniprojet;


import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class RecuperateurFichier {

    // Méthode pour lire les noms depuis un fichier CSV
    public static List<String> lireNomsDepuisCSV(String cheminFichier) {
        List<String> noms = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                ligne = ligne.trim();
                if (!ligne.isEmpty()) {
                    noms.add(ligne);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }

        return noms;
    }
}

