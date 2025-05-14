package miniprojet;

/**
 * Comparator based on the Jaro-Winkler similarity measure.
 */
public class ComparateurJaroWinkler implements Comparateur {
    private static final double PREFIX_SCALE = 0.1; // Prefix adjustment factor
    private static final int MAX_PREFIX_LENGTH = 4; // Maximum prefix length

    @Override
    public double comparer(Nom nom1, Nom nom2) {
        if (nom1 == null || nom2 == null) return 0.0;
        if (nom1.getValeur().equals(nom2.getValeur())) return 1.0;

        // Calculate Jaro similarity
        double jaroScore = calculateJaro(nom1.getValeur(), nom2.getValeur());

        // Apply Winkler adjustment for common prefixes
        int prefixLength = getCommonPrefixLength(nom1.getValeur(), nom2.getValeur());
        return jaroScore + (prefixLength * PREFIX_SCALE * (1.0 - jaroScore));
    }

    private double calculateJaro(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 == 0 || len2 == 0) return 0.0;

        // Define search window
        int maxDistance = Math.max(len1, len2) / 2 - 1;

        // Find matching characters
        boolean[] matched1 = new boolean[len1];
        boolean[] matched2 = new boolean[len2];
        int matches = 0;

        for (int i = 0; i < len1; i++) {
            int start = Math.max(0, i - maxDistance);
            int end = Math.min(len2, i + maxDistance + 1);
            for (int j = start; j < end; j++) {
                if (!matched2[j] && s1.charAt(i) == s2.charAt(j)) {
                    matched1[i] = matched2[j] = true;
                    matches++;
                    break;
                }
            }
        }

        if (matches == 0) return 0.0;

        // Calculate transpositions
        int transpositions = 0;
        int k = 0;
        for (int i = 0; i < len1; i++) {
            if (matched1[i]) {
                while (!matched2[k]) k++;
                if (s1.charAt(i) != s2.charAt(k)) transpositions++;
                k++;
            }
        }

        // Jaro formula
        return ((double) matches / len1 + (double) matches / len2 +
                (double) (matches - transpositions / 2) / matches) / 3.0;
    }

    private int getCommonPrefixLength(String s1, String s2) {
        int maxPrefix = Math.min(Math.min(s1.length(), s2.length()), MAX_PREFIX_LENGTH);
        for (int i = 0; i < maxPrefix; i++) {
            if (s1.charAt(i) != s2.charAt(i)) return i;
        }
        return maxPrefix;
    }
}