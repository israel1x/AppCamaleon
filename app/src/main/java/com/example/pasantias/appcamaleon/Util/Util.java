package com.example.pasantias.appcamaleon.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.pasantias.appcamaleon.R;

public class Util {

    public static class Constantes {
        public static final String[] CVISITA = {" Cerrado ", " No esta el dueño ", " No tiene dinero "};

        public static final String FRAGRUTACLIENTE = "RUTACLIENTE";

        public static final int ESTADO_ACTIVO = 1;
        public static final int ESTADO_INACTIVO = 2;

        public static final String ESTADO_ACTIVO_T = "Estado Activo";
        public static final String ESTADO_INACTIVO_T = "Estado Inactivo";

        public static final int ESTADO_NO_VISITADO = 1;
        public static final int ESTADO_VISITADO = 2;
        public static final int ESTADO_CERRADO = 3;
        public static final int ESTADO_NO_DUENIO = 4;
        public static final int ESTADO_NO_DINERO = 5;

        public static final int ESTADO_ENVIADO=1;
        public static final int ESTADO_ENVIADO_OFFLINE=2;
        public static final int ESTADO_PENDIENTE=3;
        public static final int ESTADO_EN_PROCESO_ENVIO=4;


        public static final String ESTADO_VISITADO_T = "Visitado";
        public static final String ESTADO_CERRADO_T = "Cerrado";
        public static final String ESTADO_NO_DUENIO_T = "No esta el dueño";
        public static final String ESTADO_NO_DINERO_T = "No tiene dinero";

        public static final String ESTADO_ENVIADO_T="Enviado";
        public static final String ESTADO_ENVIADO_OFFLINE_T="Envio Pendiente";
        public static final String ESTADO_PENDIENTE_T="Pendiente";

        public static String estadoPedido(int estado) {
            switch (estado) {
                case ESTADO_ENVIADO:
                    return ESTADO_ENVIADO_T;
                case ESTADO_ENVIADO_OFFLINE:
                    return ESTADO_ENVIADO_OFFLINE_T;
                case ESTADO_PENDIENTE:
                    return ESTADO_PENDIENTE_T;
                default:
                    return "";
            }
        }


        public static String estadoVisita(int estado) {
            switch (estado) {
                case ESTADO_VISITADO:
                    return ESTADO_VISITADO_T;
                case ESTADO_CERRADO:
                    return ESTADO_CERRADO_T;
                case ESTADO_NO_DUENIO:
                    return ESTADO_NO_DUENIO_T;
                case ESTADO_NO_DINERO:
                    return ESTADO_NO_DINERO_T;
                default:
                    return "";
            }
        }

        public static int estadoVisitaColor(int estado) {
            switch (estado) {
                case ESTADO_NO_VISITADO:
                    return R.color.colorSeparador;
                case ESTADO_VISITADO:
                    return R.color.colorVisitada;
                case ESTADO_CERRADO:
                    return R.color.colorAusente;
                case ESTADO_NO_DUENIO:
                    return R.color.colorAusente;
                case ESTADO_NO_DINERO:
                    return R.color.colorAusente;
                default:
                    return R.color.colorSeparador;
            }
        }


    }

    public static void callFragment(int containerViewId, Fragment fragment, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

}
