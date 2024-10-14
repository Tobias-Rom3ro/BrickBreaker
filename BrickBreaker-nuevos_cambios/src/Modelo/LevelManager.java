package Modelo;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private List<String> niveles;
    private int nivelActual;

    public LevelManager() {
        niveles = new ArrayList<>();
        // Añade las rutas a tus archivos de nivel aquí
        niveles.add("C:\\Users\\Daniel\\Documents\\Semestre 7\\POO\\Proyectos\\BrickBreaker-nuevos_cambios\\BrickBreaker-nuevos_cambios\\src\\resources\\nivel1.txt");
        niveles.add("C:\\Users\\Daniel\\Documents\\Semestre 7\\POO\\Proyectos\\BrickBreaker-nuevos_cambios\\BrickBreaker-nuevos_cambios\\src\\resources\\nivel2.txt");
        // Agrega más niveles según sea necesario
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
