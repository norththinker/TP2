package org.example;

public enum TypeProjectile {
    ETOILE("etoile.png"),
    SARDINES("sardines.png"),
    HIPPOCAMPE("Hippocampe.png");

    private final String imagePath;

    TypeProjectile(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImage() {
        return imagePath;
    }
}