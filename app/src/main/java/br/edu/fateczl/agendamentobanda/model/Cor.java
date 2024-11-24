package br.edu.fateczl.agendamentobanda.model;

public class Cor {
    private String name;
    private String hex;
    private String contrastHex;

    private String colorHash;
    private String contrastHexHash;

    public Cor(String name, String hex, String contrastHex) {
        this.name = name;
        this.hex = hex;
        this.contrastHex = contrastHex;
        this.colorHash = "#" + hex;
        this.contrastHexHash = "#" + contrastHex;
    }

    public String getName() {
        return name;
    }

    public String getHex() {
        return hex;
    }

    public String getContrastHex() {
        return contrastHex;
    }

    public String getColorHash() {
        return colorHash;
    }

    public String getContrastHexHash() {
        return contrastHexHash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHex(String hex) {
        this.hex = hex;
        this.colorHash = "#" + hex;
    }

    public void setContrastHex(String contrastHex) {
        this.contrastHex = contrastHex;
        this.contrastHexHash = "#" + contrastHex;
    }

    @Override
    public String toString() {
        return "name: " + name + " - " + " hex: " + hex + " - " + "contrastHex: " + contrastHex + " - " +
                "colorHash: " + colorHash + " - " + "contrastHexHash: " + contrastHexHash;
    }
}