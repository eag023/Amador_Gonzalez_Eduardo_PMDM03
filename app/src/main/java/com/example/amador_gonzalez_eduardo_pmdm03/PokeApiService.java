package com.example.amador_gonzalez_eduardo_pmdm03;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PokeApiService {
    // Endpoint para obtener un rango de Pokémon
    @GET("pokemon")
    Call<PokemonListResponse> getPokemonList(@Query("offset") int offset, @Query("limit") int limit);

    @GET("pokemon/{id}")
    Call<PokemonResponse> getPokemon(@Path("id") int id);
}
