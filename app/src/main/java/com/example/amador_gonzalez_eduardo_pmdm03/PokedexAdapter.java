package com.example.amador_gonzalez_eduardo_pmdm03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> implements View.OnClickListener{
    public class PokedexViewHolder extends RecyclerView.ViewHolder{
        TextView nombrePokemon;
        CardView cv;

        /**
         * Constructor que crea un PokedexViewHolder
         * @param itemView View al que se le crea el ViewHolder
         */
        public PokedexViewHolder(@NonNull View itemView) {
            super(itemView);

            nombrePokemon = itemView.findViewById(R.id.rvNombre);
            cv = itemView.findViewById(R.id.cvPokedex);
        }
    }

    public List<PokemonListResponse.Resultado> pokeList;
    Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public PokedexAdapter(List<PokemonListResponse.Resultado> pokeList, Context context) {
        this.pokeList = pokeList;
        this.context=context;
    }

    /**
     * Método que llama el RecyclerView
     * @param recyclerView RecyclerView al que se le asigna el adapter
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Método que crea un ViewHolder
     * @param parent Donde el View se añadirá
     * @param i Tipo del nuevo View
     * @return BooksViewHolder creado
     */
    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_pokedex, parent, false);
        PokedexViewHolder pvh = new PokedexViewHolder(v);
        return pvh;
    }

    /**
     * Método para establecer la información en cada posición del RecyclerView
     * @param pokedexViewHolder ViewHolder
     * @param i Posición dentro del RecyclerView
     */
    @Override
    public void onBindViewHolder(PokedexViewHolder pokedexViewHolder, int i) {
        pokedexViewHolder.nombrePokemon.setText(pokeList.get(i).getName());

        pokedexViewHolder.cv.setTag(i); //Se añade al CardView un tag para poder identificar a cada libro dentro del RecyclerView
        pokedexViewHolder.cv.setOnClickListener(this); //Asignamos un listener al CardView para cuando pulsamos
    }

    /**
     * Método que captura cuando pulsamos sobre un View
     * @param view View pulsado
     */
    @Override
    public void onClick(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokeApiService pokeApiService = retrofit.create(PokeApiService.class);

        // Hacer la solicitud con Retrofit
        Call<PokemonResponse> call = pokeApiService.getPokemon(((Integer) view.getTag() + 1));

        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Procesar los resultados
                    PokemonResponse pokemon = response.body();
                    if (pokemon != null) {
                        HashMap<String, Object> campos = new HashMap<>();
                        campos.put("Nombre", pokemon.getName());
                        campos.put("Tipo", pokemon.getTypes());
                        campos.put("Altura", pokemon.getHeight());
                        campos.put("Peso", pokemon.getWeight());
                        campos.put("Imagen", pokemon.getSprites().getFrontDefault());

                        firestore.collection("Usuarios").document(auth.getCurrentUser().getEmail()).collection("Pokemons Capturados")
                                .document(String.valueOf(pokemon.getId())).set(campos);

                        Toast.makeText(context, R.string.capturado, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), R.string.error_respuesta, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Toast.makeText(view.getContext(), R.string.error_conexion + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Método que cuenta la cantidad de elementos de la lista
     * @return Número de elementos de la lista
     */
    @Override
    public int getItemCount() {
        return pokeList.size();
    }
}