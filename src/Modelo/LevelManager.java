package Modelo;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private List<String> niveles;
    private int nivelActual;

    public LevelManager() {
        niveles = new ArrayList<>();
        niveles.add("nivel1.txt");
        niveles.add("nivel2.txt");
        niveles.add("nivel3.txt");
        nivelActual = 0;
    }

    public String getNivelActual() {
        if (nivelActual < niveles.size()) {
            return niveles.get(nivelActual);
        } else {
            return null; // No hay más niveles
        }
    }

    public void avanzarNivel() {
        nivelActual++;
    }

    public boolean hayMasNiveles() {
        return nivelActual < niveles.size() - 1;
    }

    public void reiniciarNiveles() {
        nivelActual = 0;
    }

    public int getNivelActualIndex() {
        return nivelActual;
    }
}
