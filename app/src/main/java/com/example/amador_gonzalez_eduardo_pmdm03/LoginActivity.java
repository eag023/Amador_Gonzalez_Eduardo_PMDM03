package com.example.amador_gonzalez_eduardo_pmdm03;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnCompleteListener<AuthResult> {

    Button btAcceder;
    EditText etEmail, etPassword;
    TextView tvOpcion;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    SharedPreferences prefs;
    private static final String PREFS_NAME = "app_preferences";
    private static final String LANGUAGE_KEY = "language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Carga del idioma guardado en las preferencias compartidas
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedLanguage = prefs.getString(LANGUAGE_KEY, "es");

        // Establece el idioma según las preferencias
        if (savedLanguage.equals("en")) {
            setLocale("en");
        } else {
            setLocale("es");
        }

        // Si el usuario ya está autenticado, redirige a la actividad principal
        if (auth.getCurrentUser() != null) {
            Intent activity = new Intent(this, MainActivity.class);
            startActivity(activity);
            finish();
        }

        btAcceder = findViewById(R.id.bAcceder);
        btAcceder.setOnClickListener(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        tvOpcion = findViewById(R.id.tvOpcion);
        tvOpcion.setOnClickListener(this);
        tvOpcion.setTag(0);
    }

    // Método para cambiar el idioma de la aplicación
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View view){
        // Alterna entre registro e inicio de sesión al hacer clic en tvOpcion
        if (view.getId()==R.id.tvOpcion){
            if((Integer)tvOpcion.getTag() == 0){
                tvOpcion.setText(R.string.inicio_sesion);
                btAcceder.setText(R.string.registrar);
                tvOpcion.setTag(1);
            } else {
                tvOpcion.setText(R.string.registro);
                btAcceder.setText(R.string.acceder);
                tvOpcion.setTag(0);
            }
        } else {
            if (!etEmail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                if((Integer)tvOpcion.getTag() == 0) {
                    auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this);
                } else {
                    auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this);
                }
            } else {
                Toast.makeText(this, R.string.campos_vacios, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (etPassword.length()<8){
            Toast.makeText(this, R.string.contraseña_corta, Toast.LENGTH_LONG).show();
        } else {
            if(!task.isSuccessful()) {
                Toast.makeText(this, R.string.campos_error, Toast.LENGTH_SHORT).show();
            } else {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection("Usuarios").document(etEmail.getText().toString()).set(new HashMap<>());

                Intent activity = new Intent(this, MainActivity.class);
                startActivity(activity);
                finish();
            }
        }
    }
}