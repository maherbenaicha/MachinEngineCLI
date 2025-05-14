package miniprojet;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;


public class PretraiteurAccent implements Pretraiteur {
    @Override
    public List<Nom> traiter(List<Nom> noms) {
        return noms.stream()
                .map(nom -> new Nom(removeAccents(nom.getValeur())))
                .collect(Collectors.toList());
    }

    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[\\p{M}]", "")
                .toLowerCase();
    }
}