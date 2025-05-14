package miniprojet;
public class ComparateurEgalite implements Comparateur {
    public double comparer(Nom nom1, Nom nom2) {
        if (nom1 == null || nom2 == null) return 0.0;
        return nom1.getValeur().equals(nom2.getValeur()) ? 1.0 : 0.0;
    }
}