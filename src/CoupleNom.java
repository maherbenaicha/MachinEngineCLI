package miniprojet;

import java.util.Objects;

public class CoupleNom {
    private Nom nom1;
    private Nom nom2;

    public CoupleNom(Nom nom1, Nom nom2) {
        this.nom1 = nom1;
        this.nom2 = nom2;
    }

    public Nom getNom1() {
        return nom1;
    }

    public Nom getNom2() {
        return nom2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoupleNom)) return false;
        CoupleNom other = (CoupleNom) o;
        return Objects.equals(nom1, other.nom1) && Objects.equals(nom2, other.nom2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom1, nom2);
    }
}