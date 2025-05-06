package MatchEngineCLI;
import java.util.*;
public interface Selectionneur {
List<NomScore> selectionner(List<NomScore> scores, double seuil);
    }