package MatchEngineCLI;
import java.util.*;
package MatchEngineCLI;

public  class Normaliseur implements Pretraiteur {
    public String traiter(String nom) {
        return nom.toLowerCase();
    }
}