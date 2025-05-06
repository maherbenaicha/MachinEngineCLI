package miniprojet;

import java.util.List; 

public class GenerateurSimple implements GenerateurCandidats {
    @Override
    public List<String> genererCandidats(String nom, List<String> liste) {
        return liste; 
    }
}
