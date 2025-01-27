package com.example.amador_gonzalez_eduardo_pmdm03;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonResponse {
    private int id;
    private String name;
    private double height;
    private double weight;
    private List<TypeWrapper> types;
    private Sprites sprites;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public List<TypeWrapper> getTypes() {
        return types;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public static class TypeWrapper {
        private Type type;

        public Type getType() {
            return type;
        }

        public static class Type {
            private String name;

            public String getName() {
                return name;
            }
        }
    }

    public static class Sprites {
        @SerializedName("front_default")
        private String frontDefault;

        public String getFrontDefault() {
            return frontDefault;
        }
    }
}