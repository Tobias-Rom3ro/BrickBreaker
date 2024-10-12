package Modelo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MapGenerator {

    private int[][] map;
    private int brickWidth;
    private int brickHeight;

    public MapGenerator() {
        // Inicialización vacía; se cargará mediante cargarNivel
    }

    // Método para cargar un nivel desde un archivo
    public void cargarNivel(String archivoNivel) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoNivel))) {
            String linea;
            int filas = 0;
            // Primer pase para contar filas y columnas
            while ((linea = br.readLine()) != null) {
                filas++;
            }
            br.close();

            // Asumimos que todas las filas tienen el mismo número de columnas
            BufferedReader br2 = new BufferedReader(new FileReader(archivoNivel));
            String primeraLinea = br2.readLine();
            int columnas = primeraLinea.trim().split("\\s+").length;
            map = new int[filas][columnas];
            br2.close();

            // Segundo pase para llenar el mapa
            BufferedReader br3 = new BufferedReader(new FileReader(archivoNivel));
            int fila = 0;
            while ((linea = br3.readLine()) != null) {
                String[] tokens = linea.trim().split("\\s+");
                for (int col = 0; col < tokens.length; col++) {
                    map[fila][col] = Integer.parseInt(tokens[col]);
                }
                fila++;
            }
            br3.close();

            // Calcular dimensiones de los ladrillos
            brickWidth = 540 / columnas;
            brickHeight = 150 / filas;

        } catch (IOException e) {
            e.printStackTrace();
            // En caso de error, inicializar un mapa por defecto
            map = new int[3][7];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    map[i][j] = 1;
                }
            }
            brickWidth = 540 / 7;
            brickHeight = 150 / 3;
        }
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // Color basado en la resistencia
                    switch (map[i][j]) {
                        case 1:
                            g.setColor(Color.GREEN);
                            break;
                        case 2:
                            g.setColor(Color.ORANGE);
                            break;
                        case 3:
                            g.setColor(Color.RED);
                            break;
                        default:
                            g.setColor(Color.GRAY);
                            break;
                    }
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.WHITE);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int row, int col) {
        if (map[row][col] > 0) {
            map[row][col]--; // Reducir la resistencia
            // No llamar a BrickBreaker.incrementTotalBricks()
        }
    }


    // Getters
    public int getBrickWidth() {
        return brickWidth;
    }

    public int getBrickHeight() {
        return brickHeight;
    }

    public int[][] getMap() {
        return map;
    }

    public int getTotalBricks() {
        int total = 0;
        for (int[] fila : map) {
            for (int bloque : fila) {
                if (bloque > 0) {
                    total++;
                }
            }
        }
        return total;
    }
}
