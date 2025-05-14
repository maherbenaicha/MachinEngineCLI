package miniprojet;

public class ComparateurLevenshtein implements Comparateur {

    @Override
    public double comparer(Nom nom1, Nom nom2) {
        // Extraire les chaînes de caractères des objets Nom
        String s1 = nom1.getValeur();
        String s2 = nom2.getValeur();
        int distance = levenshtein(s1, s2);
        return 1.0 / (1 + distance); // plus proche de 1 si proche
    }

    private int levenshtein(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= s1.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= s2.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        s1.charAt(i - 1) == s2.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[s2.length()];
    }
}