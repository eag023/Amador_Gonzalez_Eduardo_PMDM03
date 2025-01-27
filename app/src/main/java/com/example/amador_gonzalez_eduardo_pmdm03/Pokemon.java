package com.example.amador_gonzalez_eduardo_pmdm03;

import java.io.Serializable;
import java.util.List;

public class Pokemon implements Serializable {
    private int id;
    private String name;
    private double height;
    private double weight;
    private List<String> types;
    private String sprite;

    public Pokemon(int id, String sprite, List<String> types, double weight, double height, String name) {
        this.id = id;
        this.sprite = sprite;
        this.types = types;
        this.weight = weight;
        this.height = height;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String extraerTipos() {
        String cad = "";
        for (int i=0; i<types.size(); i++){
            if (cad == "") {
                cad = types.get(i);
            } else {
                cad = cad + ", " + types.get(i);
            }
        }
        return cad;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
}
