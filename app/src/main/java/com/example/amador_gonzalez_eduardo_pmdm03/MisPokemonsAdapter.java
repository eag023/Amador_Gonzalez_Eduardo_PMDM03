package com.example.amador_gonzalez_eduardo_pmdm03;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.amador_gonzalez_eduardo_pmdm03.ui.mis_pokemons.MisPokemonsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MisPokemonsAdapter extends RecyclerView.Adapter<MisPokemonsAdapter.MisPokemonsViewHolder> implements View.OnClickListener{
    public class MisPokemonsViewHolder extends RecyclerView.ViewHolder{
        TextView nombrePokemon, tipo;
        ImageView imagen, cvDelete;
        CardView cv;

        /**
         * Constructor que crea un MisPokemonsViewHolder
         * @param itemView View al que se le crea el ViewHolder
         */
        public MisPokemonsViewHolder(@NonNull View itemView) {
            super(itemView);

            nombrePokemon = itemView.findViewById(R.id.cvNombre);
            tipo = itemView.findViewById(R.id.cvType);
            imagen = itemView.findViewById(R.id.cvImagen);
            cv = itemView.findViewById(R.id.cvMisPokemons);
            cvDelete = itemView.findViewById(R.id.cvDelete);
        }
    }

    public List<Pokemon> pokeList;
    Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public MisPokemonsAdapter(List<Pokemon> pokeList, Context context) {
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
    public MisPokemonsAdapter.MisPokemonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        MisPokemonsAdapter.MisPokemonsViewHolder pvh = new MisPokemonsAdapter.MisPokemonsViewHolder(v);
        return pvh;
    }

    /**
     * Método para establecer la información en cada posición del RecyclerView
     * @param misPokemonsViewHolder ViewHolder
     * @param i Posición dentro del RecyclerView
     */
    @Override
    public void onBindViewHolder(MisPokemonsAdapter.MisPokemonsViewHolder misPokemonsViewHolder, int i) {
        misPokemonsViewHolder.nombrePokemon.setText(pokeList.get(i).getName());
        misPokemonsViewHolder.tipo.setText(pokeList.get(i).extraerTipos());

        SharedPreferences prefs = context.getSharedPreferences("app_preferences", MODE_PRIVATE);
        boolean switchEstado = prefs.getBoolean("switch_estado", false); // Recuperar el estado del Switch

        // Establecer la visibilidad del botón según el estado del Switch
        if (switchEstado) {
            misPokemonsViewHolder.cvDelete.setVisibility(View.VISIBLE); // Mostrar el botón borrar
            misPokemonsViewHolder.cvDelete.setOnClickListener(this);
            misPokemonsViewHolder.cvDelete.setTag(i);
        } else {
            misPokemonsViewHolder.cvDelete.setVisibility(View.INVISIBLE); // Ocultar el botón borrar
        }

        Glide.with(context).load(pokeList.get(i).getSprite()).diskCacheStrategy(DiskCacheStrategy.ALL).into(misPokemonsViewHolder.imagen);

        misPokemonsViewHolder.cv.setTag(i); //Se añade al CardView un tag para poder identificar a cada libro dentro del RecyclerView
        misPokemonsViewHolder.cv.setOnClickListener(this); //Asignamos un listener al CardView para cuando pulsamos
    }

    /**
     * Método que captura cuando pulsamos sobre un View
     * @param view View pulsado
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cvDelete) {
            int position = (Integer) view.getTag(); // Obtén la posición del elemento
            Pokemon pokemon = pokeList.get(position); // Obtén el Pokémon a eliminar

            // Eliminar de Firestore
            firestore.collection("Usuarios")
                    .document(auth.getCurrentUser().getEmail())
                    .collection("Pokemons Capturados")
                    .document(String.valueOf(pokemon.getId()))
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Si la eliminación en Firestore tiene éxito, actualiza la lista local
                        pokeList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, pokeList.size());
                    })
                    .addOnFailureListener(e -> {
                        // Maneja errores si la eliminación falla
                        e.printStackTrace();
                    });
        } else {
            // Cuando se pulsa el CardView (no el botón de borrar)
            Bundle result = new Bundle();
            result.putSerializable("Pokemon", pokeList.get((Integer) view.getTag()));

            Intent intent = new Intent(context, FichaPersonaje.class);
            intent.putExtras(result);
            context.startActivity(intent);
        }
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