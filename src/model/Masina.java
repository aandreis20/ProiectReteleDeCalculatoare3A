package model;

import java.io.Serializable;

public class Masina implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vin;
    private String marca;
    private String model;
    private int anFabricatie;

    public Masina(String vin, String marca, String model, int anFabricatie) {
        this.vin = vin;
        this.marca = marca;
        this.model = model;
        this.anFabricatie = anFabricatie;
    }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getAnFabricatie() { return anFabricatie; }
    public void setAnFabricatie(int anFabricatie) { this.anFabricatie = anFabricatie; }

    @Override
    public String toString() {
        return vin + " – " + marca + " " + model + " (" + anFabricatie + ")";
    }
}