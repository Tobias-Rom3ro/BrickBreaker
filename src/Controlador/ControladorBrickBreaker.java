package Controlador;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import Modelo.BrickBreaker;
import Vista.VistaBrickBreaker;
import java.util.Random;
import javax.swing.JFrame;
import javax.sound.sampled.*;
import Vista.Sonido;

public class ControladorBrickBreaker implements KeyListener, ActionListener {
    private BrickBreaker modelo;
    private VistaBrickBreaker vista;
    private Timer timer;
    private final int delay = 3;
    private JFrame frameMenu;
    private JFrame frameJuego;
    private Sonido sonidoBloque;
    private Sonido sonidoPanel;
    private Sonido sonidoBarra;
    private Sonido sonidoVictoria;
    private Sonido musicaMenu;
    public ControladorBrickBreaker(BrickBreaker modelo, VistaBrickBreaker vista, JFrame frameMenu, JFrame frameJuego) {
        this.modelo = modelo;
        this.vista = vista;
        this.frameMenu = frameMenu;
        this.frameJuego = frameJuego;
        sonidoBloque = new Sonido("/multimedia/SonidoChoqueBloque.wav");
        sonidoPanel = new Sonido("/multimedia/SonidoChoquePanel.wav");
        sonidoBarra = new Sonido("/multimedia/SonidoPuntos.wav");
        sonidoVictoria = new Sonido("/multimedia/SonidoVictoriaNivel.wav");
        musicaMenu = new Sonido("/multimedia/sonidoFondoPrincipal.wav");
        musicaMenu.reproducirEnBucle();
        timer = new Timer(delay, this);
        timer.start();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!modelo.isPlay()) {
                if (!modelo.isBallLanzada()) {
                    iniciarLanzamiento();
                } else {
                    modelo.resetGame();
                    vista.repaint();
                }
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_P) {
            if (modelo.isBallLanzada()) {
                vista.pausarJuego(); // Pausar el juego si no está pausado
            }
        }
    }

    public void volverMenu(){
        vista.cerrarPausa();
        frameMenu.setVisible(true);  // Ocultar el frame actual
        frameJuego.setVisible(false);
        modelo.reiniciarPelota();
        vista.setEstadoPausa();
        modelo.recoveryLive();
        vista.setCambioMenu();
    }
    private void iniciarLanzamiento() {
        // Decidir dirección inicial aleatoriamente hacia la izquierda o derecha, asegurando que no sea cero
        Random rand = new Random();
        int dirX = 0;
        while (dirX == 0) {
            dirX = rand.nextInt(3) - 1; // -1, 0, 1
        }
        modelo.setBallXdir(dirX); // -1 o 1
        modelo.setBallYdir(-2); // Siempre hacia arriba
        modelo.setPlay(true);
        modelo.setBallLanzada(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No es necesario para el control por ratón
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No es necesario para el control por ratón
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (modelo.isPlay()) {
// Actualizar posición de la pelota con velocidad moderada
            modelo.setBallposX(modelo.getBallposX() + modelo.getBallXdir() * 2); // Multiplicar por 2 o un valor constante pequeño
            modelo.setBallposY(modelo.getBallposY() + modelo.getBallYdir() * 2); // Multiplicar por 2 o un valor constante pequeño



            // Verificar colisiones con los bordes
            if (modelo.getBallposX() < 0 || modelo.getBallposX() > 670) {
                modelo.setBallXdir(-modelo.getBallXdir());
                sonidoBloque.reproducir();
            }
            if (modelo.getBallposY() < 0) {
                modelo.setBallYdir(-modelo.getBallYdir());
                sonidoBloque.reproducir();
            }

            if (modelo.getBallposY() > 570) {
                modelo.setPlay(false);
                sonidoBloque.reproducir();
            }

            // Rebote contra la barra
            Rectangle ballRect = new Rectangle(modelo.getBallposX(), modelo.getBallposY(), 20, 20);
            Rectangle playerRect = new Rectangle(modelo.getPlayerX(), 535, 100, 8);
            if (ballRect.intersects(playerRect)) {
                modelo.setBallYdir(-modelo.getBallYdir());
                sonidoPanel.reproducir();

                // Ajustar la dirección horizontal de la pelota según dónde golpea la barra
                int paddleCenter = modelo.getPlayerX() + 50; // 50 es la mitad del ancho de la barra (100)
                int ballCenter = modelo.getBallposX() + 10; // 10 es la mitad del diámetro de la pelota (20)
                int delta = ballCenter - paddleCenter;

                // Ajustar la dirección X basándose en la posición de impacto
                int newBallXdir = delta / 10; // Esto da una dirección entre -5 y +5

                // Asegurar que la dirección horizontal no sea cero
                if (newBallXdir == 0) {
                    newBallXdir = (delta > 0) ? 1 : -1;
                } else if (newBallXdir > 3) { // Limitar la dirección horizontal
                    newBallXdir = 3;
                } else if (newBallXdir < -3) {
                    newBallXdir = -3;
                }

                modelo.setBallXdir(newBallXdir);
            }

            // Verificar colisiones con los ladrillos
            for (int i = 0; i < modelo.getMap().getMap().length; i++) {
                for (int j = 0; j < modelo.getMap().getMap()[0].length; j++) {
                    if (modelo.getMap().getMap()[i][j] > 0) {

                        int brickX = j * modelo.getMap().getBrickWidth() + 80;
                        int brickY = i * modelo.getMap().getBrickHeight() + 50;
                        int brickWidth = modelo.getMap().getBrickWidth();
                        int brickHeight = modelo.getMap().getBrickHeight();

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);

                        if (ballRect.intersects(brickRect)) {
                            modelo.getMap().setBrickValue(i, j); // Reducir resistencia
                            sonidoBarra.reproducir(); // Reproducir sonido de la barra

                            // Verificar si el bloque fue destruido
                            if (modelo.getMap().getMap()[i][j] == 0) {
                                modelo.decrementTotalBricks();
                                // Incrementar puntaje basado en la resistencia original
                                modelo.incrementScore(5); // Puedes ajustar según la resistencia original
                            }

                            // Rebote de la pelota
                            if (modelo.getBallposX() + 19 <= brickRect.x || modelo.getBallposX() + 1 >= brickRect.x + brickRect.width) {
                                modelo.setBallXdir(-modelo.getBallXdir());
                            } else {
                                modelo.setBallYdir(-modelo.getBallYdir());
                            }

                            // Si todos los ladrillos han sido destruidos, avanzar al siguiente nivel
                            if (modelo.getTotalBricks() <= 0) {
                                sonidoVictoria.reproducir();
                                modelo.avanzarNivel();
                                modelo.setPlay(false);
                                vista.repaint();
                                return;
                            }
                        }
                    }
                }
            }
            vista.bajarVolumen(sonidoBarra);
            vista.bajarVolumen(sonidoVictoria);
            vista.bajarVolumen(musicaMenu);
            vista.bajarVolumen(sonidoBarra);
            vista.bajarVolumen(sonidoPanel);
            vista.repaint(); // Redibujar la vista para actualizar el juego

        }
        if(vista.isCambio()){
            volverMenu();
        }
    }
}
