package Vista;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

import Modelo.BrickBreaker;

public class VistaBrickBreaker extends JPanel implements MouseMotionListener {
    private BrickBreaker modelo;

    public VistaBrickBreaker(BrickBreaker modelo) {
        this.modelo = modelo;
        setFocusable(true);
        requestFocusInWindow();
        addMouseMotionListener(this); // Añadir el listener de movimiento del ratón
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Para limpiar el fondo correctamente

        // Fondo del juego
        Dimension tamanio = getSize();
        ImageIcon imagen = new ImageIcon(getClass().getResource("/resources/imagenes/FondoRetroMov.gif"));
        g.drawImage(imagen.getImage(), 0, 0, tamanio.width, tamanio.height, this);


        // Dibujar ladrillos
        modelo.getMap().draw((Graphics2D) g);

        // Bordes del frame
        g.setColor(Color.RED);
        g.fillRect(0, 0, 4, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Barra
        g.setColor(Color.BLUE);
        g.fillRect(modelo.getPlayerX(), 550, 100, 8);

        // Bola
        g.setColor(Color.GREEN);
        g.fillOval(modelo.getBallposX(), modelo.getBallposY(), 20, 20);

        // Puntaje
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Puntaje: " + modelo.getScore(), 590, 30);

        // Nivel Actual
        g.drawString("Nivel: " + (modelo.getLevelManager().getNivelActualIndex() + 1), 50, 30);
        g.drawString("Vidas: " + modelo.getVidas(), 320, 30);

        // Verificar si se ganó el juego o se pasó al siguiente nivel
        if (!modelo.isPlay()) {
            if (modelo.getTotalBricks() <= 0) {
                if (modelo.getLevelManager().hayMasNiveles()) {
                    mostrarMensajeFinJuego(g, "¡Nivel Completado! Puntaje: " + modelo.getScore(), "Presiona Enter para el siguiente nivel.");
                } else {
                    mostrarMensajeFinJuego(g, "¡Ganaste el Juego! Puntaje Total: " + modelo.getScore(), "Presiona Enter para reiniciar.");
                }
            }

            // Verificar si se perdió el juego
            if (modelo.getBallposY() > 570) {
                if (modelo.getVidas() > 0) {
                    modelo.decrementarVidas();
                    modelo.reiniciarPelota();
                    repaint();
                }
                if (modelo.getVidas() == 0) {
                    modelo.setPlay(false);
                    mostrarMensajeFinJuego(g, "Fin del juego. Puntaje: " + modelo.getScore(), "Presiona Enter para reiniciar.");
                }
            }
        }
    }


    private void mostrarMensajeFinJuego(Graphics g, String mensajePrincipal, String mensajeSecundario) {
        // Dibujar un rectángulo semi-transparente como fondo del mensaje
        g.setColor(new Color(0, 0, 0, 150)); // Negro con transparencia
        g.fillRect(150, 250, 400, 150);

        // Mensaje principal
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 30));
        g.drawString(mensajePrincipal, 200, 300);

        // Mensaje secundario
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString(mensajeSecundario, 200, 350);
    }

    // Implementación de MouseMotionListener
    @Override
    public void mouseDragged(MouseEvent e) {
        // No se utiliza, pero debe implementarse
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        // Limitar el movimiento de la barra dentro de los límites del panel
        if (mouseX < 10) {
            mouseX = 10;
        } else if (mouseX > 600) {
            mouseX = 600;
        }
        modelo.setPlayerX(mouseX);

        // Si la pelota no ha sido lanzada, actualizar su posición para seguir la barra
        if (!modelo.isBallLanzada()) {
            modelo.setBallposX(mouseX + 40); // Centrar la pelota sobre la barra (100 de ancho de la barra / 2 - 10 de diámetro de la pelota)
            modelo.setBallposY(550 - 20); // Posicionar justo encima de la barra
        }

        repaint();
    }

    // Método opcional para actualizar la vista con más detalles si es necesario
    public void actualizarVistaJuego(int[][] mapaLadrillos, int posicionJugador, int posicionBolaX, int posicionBolaY, int puntaje, int ladrillosRestantes, boolean juegoGanado, boolean juegoTerminado) {
        repaint();
    }
}
