package miniprojet;

import java.util.List;
import java.util.stream.Collectors;


public class PretraiteurDiviserMotCompose implements Pretraiteur {
    @Override
    public List<Nom> traiter(List<Nom> noms) {
        return noms.stream()
                .map(nom -> new Nom(splitCompoundName(nom.getValeur())))
                .collect(Collectors.toList());
    }


    private String splitCompoundName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        String[] parts = name.split("[\\s'-]+");

        return String.join(" ", parts).toLowerCase().trim();
    }
}