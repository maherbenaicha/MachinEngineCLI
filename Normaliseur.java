package miniprojet;

public class Normaliseur implements Pretraiteur {
    @Override
    public String traiter(String nom) {
        return nom.toLowerCase();
    }
}
