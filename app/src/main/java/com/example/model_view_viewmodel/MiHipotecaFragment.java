package com.example.model_view_viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.model_view_viewmodel.databinding.FragmentMiHipotecaBinding;

public class MiHipotecaFragment extends Fragment {
    private FragmentMiHipotecaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMiHipotecaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final MiHipotecaViewModel miHipotecaViewModel = new ViewModelProvider(this).get(MiHipotecaViewModel.class);

        binding.calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double capital = Double.parseDouble(binding.capital.getText().toString());
                int plazo = Integer.parseInt(binding.plazo.getText().toString());

                miHipotecaViewModel.calcular(capital, plazo);
            }
        });

        miHipotecaViewModel.cuota.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double cuota) {
                binding.cuota.setText(String.format("%.2f",cuota));
            }
        });

        miHipotecaViewModel.errorCapital.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double capitalMinimo) {
                if (capitalMinimo != null) {
                    binding.capital.setError("El capital no puede ser inferor a " + capitalMinimo + " euros");
                } else {
                    binding.capital.setError(null);
                }
            }
        });

        miHipotecaViewModel.errorPlazos.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer plazoMinimo) {
                if (plazoMinimo != null) {
                    binding.plazo.setError("El plazo no puede ser inferior a " + plazoMinimo + " años");
                } else {
                    binding.plazo.setError(null);
                }
            }
        });

        miHipotecaViewModel.calculando.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean calculando) {
                if (calculando) {
                    binding.calculando.setVisibility(View.VISIBLE);
                    binding.cuota.setVisibility(View.GONE);
                } else {
                    binding.calculando.setVisibility(View.GONE);
                    binding.cuota.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}