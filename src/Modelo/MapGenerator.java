package Modelo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.ImageIcon;

public class MapGenerator {

    private int[][] map;
    private int brickWidth;
    private int brickHeight;
    private Image[] imagenesBloques;

    public MapGenerator() {
        // Cargar las imágenes de los bloques
        imagenesBloques = new Image[4]; // 0 no se usa, ya que las resistencias van de 1 a 3
//        imagenesBloques[1] = new ImageIcon(getClass().getResource("/resources/imagenes/BloqueResistencia1.png")).getImage();
//        imagenesBloques[2] = new ImageIcon(getClass().getResource("/resources/imagenes/BloqueResistencia2.png")).getImage();
//        imagenesBloques[3] = new ImageIcon(getClass().getResource("/resources/imagenes/BloqueResistencia3.png")).getImage();
        URL url = getClass().getResource("/resources/imagenes/BloqueResistencia1.png");
        if (url != null) {
            imagenesBloques[1] = new ImageIcon(url).getImage();
        } else {
            System.err.println("No se encontró la imagen para BloqueResistencia1");
        }
        URL url2 = getClass().getResource("/resources/imagenes/bloqueResistencia2.png");
        if (url2 != null) {
            imagenesBloques[2] = new ImageIcon(url2).getImage();
        } else {
            System.err.println("No se encontró la imagen para BloqueResistencia2");
        }
        URL url3 = getClass().getResource("/resources/imagenes/bloqueResistencia3.png");
        if (url3 != null) {
            imagenesBloques[3] = new ImageIcon(url3).getImage();
        } else {
            System.err.println("No se encontró la imagen para bloqueResistencia3");
        }

    }

    public void cargarNivel(String archivoNivel) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(archivoNivel);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                throw new IOException("No se pudo encontrar el archivo: " + archivoNivel);
            }

            String linea;
            int filas = 0;
            // Primer pase para contar filas
            while ((linea = br.readLine()) != null) {
                filas++;
            }
            br.close();

            // Asumimos que todas las filas tienen el mismo número de columnas
            BufferedReader br2 = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(archivoNivel)));
            String primeraLinea = br2.readLine();
            int columnas = primeraLinea.trim().split("\\s+").length;
            map = new int[filas][columnas];
            br2.close();

            // Segundo pase para llenar el mapa
            BufferedReader br3 = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(archivoNivel)));
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
            // Inicializar un mapa por defecto en caso de error
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
                    // Seleccionar la imagen basada en la resistencia
                    Image imagenBloque = imagenesBloques[map[i][j]];
                    if (imagenBloque != null) {
                        g.drawImage(imagenBloque, j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight, null);
                    }
                }
            }
        }
    }

    public void setBrickValue(int row, int col) {
        if (map[row][col] > 0) {
            map[row][col]--; // Reducir la resistencia del bloque
        }
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

    public int getBrickWidth() {
        return brickWidth;
    }

    public int getBrickHeight() {
        return brickHeight;
    }
}
