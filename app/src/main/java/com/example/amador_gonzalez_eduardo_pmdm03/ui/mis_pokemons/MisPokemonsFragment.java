package com.example.amador_gonzalez_eduardo_pmdm03.ui.mis_pokemons;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amador_gonzalez_eduardo_pmdm03.MisPokemonsAdapter;
import com.example.amador_gonzalez_eduardo_pmdm03.PokedexAdapter;
import com.example.amador_gonzalez_eduardo_pmdm03.Pokemon;
import com.example.amador_gonzalez_eduardo_pmdm03.R;
import com.example.amador_gonzalez_eduardo_pmdm03.databinding.FragmentMisPokemonsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MisPokemonsFragment extends Fragment implements OnCompleteListener<QuerySnapshot> {

    private FragmentMisPokemonsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MisPokemonsViewModel misPokemonsViewModel =
                new ViewModelProvider(this).get(MisPokemonsViewModel.class);

        binding = FragmentMisPokemonsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<Pokemon> pokemons = new ArrayList<>();
    RecyclerView rvPokemons;
    Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Realizar una consulta a Firestore para obtener los Pokémon capturados por el usuario actual
        firestore.collection("Usuarios").document(auth.getCurrentUser().getEmail()).collection("Pokemons Capturados").get().addOnCompleteListener(this);

        // Configurar el RecyclerView
        rvPokemons= view.findViewById(R.id.rvMisPokemons);
        rvPokemons.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
        rvPokemons.setHasFixedSize(true);

        context = getContext();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        // Obtener el resultado de la consulta a Firestore
        QuerySnapshot result = task.getResult(); //Obtenemos el resultado de la tarea

        // Limpiar la lista de Pokémon antes de llenarla nuevamente
        pokemons.clear();

        // Procesar cada documento obtenido en la consulta
        for (DocumentSnapshot document : result.getDocuments()) {
            ArrayList<String> listTipos = new ArrayList<>();
            ArrayList tipos = (ArrayList) document.get("Tipo");

            // Extraer y procesar los tipos del Pokémon
            for(int i=0; i<tipos.size(); i++){
                HashMap hm = (HashMap) tipos.get(i);
                HashMap hm2 = (HashMap) hm.get("type");
                listTipos.add((String) hm2.get("name"));
            }

            // Crear una instancia de la clase Pokémon con los datos obtenidos
            Pokemon pokemon= new Pokemon(Integer.parseInt(document.getId()), document.getString("Imagen"), listTipos, document.getDouble("Peso"), document.getDouble("Altura"), document.getString("Nombre"));

            // Agregar el Pokémon a la lista
            pokemons.add(pokemon);
        }

        // Configurar el adaptador del RecyclerView para mostrar los Pokémon
        rvPokemons.setAdapter(new MisPokemonsAdapter(pokemons, context));
    }
}