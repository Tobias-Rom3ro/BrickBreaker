package Vista;


import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

import Modelo.BrickBreaker;

public class VistaBrickBreaker extends JPanel implements MouseMotionListener, KeyListener {
    private BrickBreaker modelo;
    private boolean isPaused = false;
    private Pausa pantallaPausa;

    public VistaBrickBreaker(BrickBreaker modelo) {
        this.modelo = modelo;
        setFocusable(true);
        requestFocusInWindow();
        addMouseMotionListener(this); // Añadir el listener de movimiento del ratón
        addKeyListener(this); // Añadir el listener de teclado
    }

    private void pausarJuego() {
        isPaused = true;
        modelo.setPlay(false);
        mostrarPantallaPausa();
    }

    private void mostrarPantallaPausa() {
        pantallaPausa = new Pausa(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reanudarJuego();
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aquí puedes implementar la lógica para controlar el volumen
                JOptionPane.showMessageDialog(pantallaPausa, "Control de volumen no implementado", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        pantallaPausa.setVisible(true);
    }

    private void reanudarJuego() {
        isPaused = false;
        modelo.setPlay(true);
        pantallaPausa.dispose(); // Cerrar la pantalla de pausa
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Para limpiar el fondo correctamente
    public void paint(Graphics g) {
        super.paint(g); // Llamar a la superclase para que dibuje los componentes (como el botón)

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

                mostrarMensajeFinJuego(g, "Fin del juego. Puntaje: " + modelo.getScore(), "Presiona Enter para reiniciar.");
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

    @Override
    public void mouseDragged(MouseEvent e) {
        // No se utiliza, pero debe implementarse
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isPaused) {
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (!isPaused) {
                pausarJuego(); // Pausar el juego si no está pausado
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se utiliza, pero debe implementarse
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No se utiliza, pero debe implementarse
    }
}

