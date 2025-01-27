package com.example.amador_gonzalez_eduardo_pmdm03;

public class PokemonListResponse {
    public Resultado[] results;

    public class Resultado {
        private int id;
        private String name;

        public Resultado(int id, String name) {
            this.id = id;
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
    }
}
