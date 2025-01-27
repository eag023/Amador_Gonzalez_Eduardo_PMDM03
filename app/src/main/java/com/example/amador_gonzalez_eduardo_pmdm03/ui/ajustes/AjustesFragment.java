package com.example.amador_gonzalez_eduardo_pmdm03.ui.ajustes;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.amador_gonzalez_eduardo_pmdm03.LoginActivity;
import com.example.amador_gonzalez_eduardo_pmdm03.R;
import com.example.amador_gonzalez_eduardo_pmdm03.databinding.FragmentAjustesBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class AjustesFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private FragmentAjustesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AjustesViewModel ajustesViewModel =
                new ViewModelProvider(this).get(AjustesViewModel.class);

        binding = FragmentAjustesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    private static final String PREFS_NAME = "app_preferences";
    private static final String LANGUAGE_KEY = "language";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    SharedPreferences prefs;
    TextView tvIdioma, tvAcerca, tvCerrarSesion;
    Switch sEliminar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            tvIdioma = view.findViewById(R.id.tvIdioma);
            tvIdioma.setOnClickListener(this);

            tvAcerca = view.findViewById(R.id.tvAcerca);
            tvAcerca.setOnClickListener(this);

            tvCerrarSesion = view.findViewById(R.id.tvCerrarSesion);
            tvCerrarSesion.setOnClickListener(this);

            sEliminar = view.findViewById(R.id.sEliminar);
            sEliminar.setOnCheckedChangeListener(this);

            // Cargar las preferencias almacenadas
            prefs = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String savedLanguage = prefs.getString(LANGUAGE_KEY, "es");
            Boolean savedSwitch = prefs.getBoolean("switch_estado", false);

            // Configurar el idioma según las preferencias guardadas
            if (savedLanguage.equals("en")) {
                setLocale("en");
            } else {
                setLocale("es");
            }

            // Configurar el estado del Switch según las preferencias
            if (savedSwitch) {
                sEliminar.setChecked(true);
            } else {
                sEliminar.setChecked(false);
            }
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Método que se ejecuta cuando cambia el estado del Switch
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.sEliminar) {
            // Guardar el estado del Switch en SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("switch_estado", isChecked);
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tvIdioma){
            // Obtener el idioma actual
            String currentLanguage = prefs.getString(LANGUAGE_KEY, "es");

            // Alternar el idioma entre inglés y español
            if (currentLanguage.equals("en")) {
                // Cambiar a español
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(LANGUAGE_KEY, "es");
                editor.apply();
                setLocale("es");
            } else {
                // Cambiar a inglés
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(LANGUAGE_KEY, "en");
                editor.apply();
                setLocale("en");
            }

            // Recargar la actividad para aplicar el cambio de idioma
            getActivity().recreate();
        } else if (v.getId()==R.id.tvAcerca) {
            // Mostrar un diálogo con información sobre la aplicación
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.acceder);
            builder.setMessage(R.string.mensaje_acerca);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); // Cierra el diálogo
                }
            });
            builder.show();
        } else if (v.getId()==R.id.tvCerrarSesion) {
            // Mostrar un diálogo para confirmar el cierre de sesión
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.cerrar_sesion);
            builder.setMessage(R.string.mensaje_cerrar_sesion);
            builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Acción al hacer clic en "Aceptar"
                    firebaseAuth.signOut();
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    startActivity(intent);;
                    requireActivity().finish();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Acción al hacer clic en "Cancelar"
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
}