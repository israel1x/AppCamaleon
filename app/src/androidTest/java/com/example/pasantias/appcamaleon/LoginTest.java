package com.example.pasantias.appcamaleon;

import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginTest {


    @Test
    public void testuserAndPassword() {

        // Ingreso datos de prueba en los editexts del login
        onView(withId(R.id.et_dataUsuario)).perform(typeText("Wilson"));
        onView(withId(R.id.et_dataPassword)).perform(typeText("1"));

        // Accion sobre el boton
        onView(withId(R.id.bt_login)).perform(click());
    }

}
