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

public class ControladorBrickBreaker implements KeyListener, ActionListener {
    private BrickBreaker modelo;
    private VistaBrickBreaker vista;
    private Timer timer;
    private final int delay = 10;

    public ControladorBrickBreaker(BrickBreaker modelo, VistaBrickBreaker vista) {
        this.modelo = modelo;
        this.vista = vista;

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
            modelo.setBallposX(modelo.getBallposX() + modelo.getBallXdir());
            modelo.setBallposY(modelo.getBallposY() + modelo.getBallYdir());

            // Verificar colisiones con los bordes
            if (modelo.getBallposX() < 0 || modelo.getBallposX() > 670) {
                modelo.setBallXdir(-modelo.getBallXdir());
            }
            if (modelo.getBallposY() < 0) {
                modelo.setBallYdir(-modelo.getBallYdir());
            }

            if (modelo.getBallposY() > 570) {
                modelo.setPlay(false);
            }

            // Rebote contra la barra
            Rectangle ballRect = new Rectangle(modelo.getBallposX(), modelo.getBallposY(), 20, 20);
            Rectangle playerRect = new Rectangle(modelo.getPlayerX(), 535, 100, 8);
            if (ballRect.intersects(playerRect)) {
                modelo.setBallYdir(-modelo.getBallYdir());

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
                                modelo.avanzarNivel();
                                modelo.setPlay(false);
                                vista.repaint();
                                return;
                            }
                        }
                    }
                }
            }

            vista.repaint(); // Redibujar la vista para actualizar el juego
        }
    }
}
