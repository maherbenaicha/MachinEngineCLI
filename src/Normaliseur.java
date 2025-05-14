package miniprojet;

import java.util.List;
import java.util.stream.Collectors;

public class Normaliseur implements Pretraiteur {
    @Override
    public List<Nom> traiter(List<Nom> noms) {
        return noms.stream()
                .map(nom -> new Nom(nom.getValeur().toLowerCase()))
                .collect(Collectors.toList());
    }
}