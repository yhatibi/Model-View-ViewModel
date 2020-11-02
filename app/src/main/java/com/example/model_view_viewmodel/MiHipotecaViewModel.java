package com.example.model_view_viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MiHipotecaViewModel extends AndroidViewModel {
    MutableLiveData<Boolean> calculando = new MutableLiveData<>();

    Executor executor;

    SimuladorHipoteca simulador;

    MutableLiveData<Double> cuota = new MutableLiveData<>();
    MutableLiveData<Double> errorCapital = new MutableLiveData<>();
    MutableLiveData<Integer> errorPlazos = new MutableLiveData<>();

    public MiHipotecaViewModel(@NonNull Application application) {
        super(application);

        executor = Executors.newSingleThreadExecutor();
        simulador = new SimuladorHipoteca();
    }


    public void calcular(double capital, int plazo) {

        final SimuladorHipoteca.Solicitud solicitud = new SimuladorHipoteca.Solicitud(capital, plazo);

        executor.execute(new Runnable() {
            @Override
            public void run() {

                simulador.calcular(solicitud, new SimuladorHipoteca.Callback() {
                    MutableLiveData<Boolean> calculando = new MutableLiveData<>();

                    @Override
                    public void cuandoEsteCalculadaLaCuota(double cuotaResultante) {
                        errorCapital.postValue(null);
                        errorPlazos.postValue(null);
                        cuota.postValue(cuotaResultante);
                    }

                    @Override
                    public void cuandoHayaErrorDeCapitalInferiorAlMinimo(double capitalMinimo) {
                        errorCapital.postValue(capitalMinimo);
                    }

                    @Override
                    public void cuandoHayaErrorDePlazoInferiorAlMinimo(int plazoMinimo) {
                        errorPlazos.postValue(plazoMinimo);
                    }

                    @Override
                    public void cuandoEmpieceElCalculo() {
                        calculando.postValue(true);
                    }

                    @Override
                    public void cuandoFinaliceElCalculo() {
                        calculando.postValue(false);
                    }
                });
            }
        });
    }
}