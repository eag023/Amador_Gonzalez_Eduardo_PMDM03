package com.example.amador_gonzalez_eduardo_pmdm03.ui.pokedex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amador_gonzalez_eduardo_pmdm03.PokedexAdapter;
import com.example.amador_gonzalez_eduardo_pmdm03.PokeApiService;
import com.example.amador_gonzalez_eduardo_pmdm03.PokemonListResponse;
import com.example.amador_gonzalez_eduardo_pmdm03.PokemonListResponse.Resultado;
import com.example.amador_gonzalez_eduardo_pmdm03.R;
import com.example.amador_gonzalez_eduardo_pmdm03.databinding.FragmentPokedexBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokedexFragment extends Fragment {

    RecyclerView rv;
    private FragmentPokedexBinding binding;
    List<Resultado> listResultado = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PokedexViewModel pokedexViewModel =
                new ViewModelProvider(this).get(PokedexViewModel.class);

        binding = FragmentPokedexBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar el RecyclerView
        rv = view.findViewById(R.id.rvPokedex);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
        rv.setHasFixedSize(true);

        // Crear una instancia de Retrofit para consumir la API de Pokémon
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear el servicio de la API de Pokémon
        PokeApiService pokeApiService = retrofit.create(PokeApiService.class);

        // Realizar la solicitud a la API para obtener la lista de Pokémon
        Call<PokemonListResponse> call = pokeApiService.getPokemonList(0, 50);

        call.enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Procesar los resultados obtenidos de la API
                    PokemonListResponse pokemonListResponse = response.body();
                    for (Resultado result : pokemonListResponse.results) {
                        listResultado.add(result);
                    }
                    // Establecer el adaptador para mostrar los datos en el RecyclerView
                    rv.setAdapter(new PokedexAdapter(listResultado, view.getContext()));
                } else {
                    // Mostrar un mensaje de error si la respuesta no fue exitosa
                    Toast.makeText(view.getContext(), R.string.error_respuesta, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                // Mostrar un mensaje de error si la solicitud falló
                Toast.makeText(view.getContext(), R.string.error_conexion + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}