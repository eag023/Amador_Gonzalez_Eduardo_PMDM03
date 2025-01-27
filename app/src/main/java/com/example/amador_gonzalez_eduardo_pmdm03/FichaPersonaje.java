package com.example.amador_gonzalez_eduardo_pmdm03;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class FichaPersonaje extends AppCompatActivity {

    TextView nombre, tipo, peso, altura, id;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ficha_personaje);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle bundle = getIntent().getExtras();
        Pokemon pokemon = (Pokemon) bundle.getSerializable("Pokemon");

        nombre = findViewById(R.id.txNombrePk);
        nombre.setText(pokemon.getName());

        tipo = findViewById(R.id.txTipos);
        tipo.setText(pokemon.extraerTipos());

        peso = findViewById(R.id.txPeso);
        peso.setText(pokemon.getWeight()/10 + " kg");

        altura = findViewById(R.id.txAltura);
        altura.setText(pokemon.getHeight()/10 + " m");

        id = findViewById(R.id.txIndicePk);
        id.setText(pokemon.getId()+"");

        imagen = findViewById(R.id.imageView);
        Glide.with(this).load(pokemon.getSprite()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imagen);
    }
}