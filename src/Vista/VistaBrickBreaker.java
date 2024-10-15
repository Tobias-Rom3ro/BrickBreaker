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
import java.io.IOException;
import java.net.URL;
import Modelo.BrickBreaker;
import Utils.Sonido;

public class VistaBrickBreaker extends JPanel implements MouseMotionListener, KeyListener {
    private BrickBreaker modelo;
    private boolean isPaused = false;
    private Pausa pantallaPausa;
    private Image imagenPelota;
    private Image imagenBarra;
    private Image imagenCorazon;
    private Image imagenEstrella;
    private Font customFont, customFont2;
    private int anchoImg;
    private int altoImg;
    private boolean cambioMenu = false;
    private float volumen = 50;

    public VistaBrickBreaker(BrickBreaker modelo) {

        this.modelo = modelo;
        URL urlImagen = getClass().getClassLoader().getResource("resources/imagenes/bolitaMasPeque.png");
        URL urlImagen2 = getClass().getClassLoader().getResource("resources/imagenes/barraMoradaPeque.png");
        URL urlImagenCorazon = getClass().getClassLoader().getResource("resources/imagenes/corazon rojo neon.png");
        URL urlImagenEstrella = getClass().getClassLoader().getResource("resources/imagenes/estrella neon.png");

        if(urlImagen2 != null) {
            imagenBarra = new ImageIcon(urlImagen2).getImage();
            anchoImg = imagenBarra.getWidth(null);
            altoImg = imagenBarra.getHeight(null);
        }else{
            System.out.println("No se pudo cargar la imagen");
        }
        if (urlImagen != null) {
            imagenPelota = new ImageIcon(urlImagen).getImage();
            anchoImg = imagenPelota.getWidth(null);
            altoImg = imagenPelota.getHeight(null);
        } else {
            System.err.println("No se pudo cargar la imagen de la pelota");
        }

        if (urlImagenCorazon != null) {
            imagenCorazon = new ImageIcon(urlImagenCorazon).getImage();
        } else {
            System.err.println("No se pudo cargar la imagen del corazón");
        }

        if (urlImagenEstrella != null) {
            imagenEstrella = new ImageIcon(urlImagenEstrella).getImage();
        } else {
            System.err.println("No se pudo cargar la imagen de la estrella");
        }

        try {
            // Carga la fuente desde el archivo .ttf
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("resources/fonts/Android y.ttf"));
            customFont = customFont.deriveFont(25f); // Ajusta el tamaño de la fuente
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            customFont2 = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("resources/fonts/FUTRFW.TTF"));
            customFont2 = customFont2.deriveFont(15f); // Ajusta el tamaño de la fuente
            GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge2.registerFont(customFont2);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("No se pudo cargar la fuente externa");
        }

        setFocusable(true);
        requestFocusInWindow();
        addMouseMotionListener(this); // Añadir el listener de movimiento del ratón
        addKeyListener(this); // Añadir el listener de teclado
    }
    public void bajarVolumen(Sonido soni){
        soni.setVolumen(volumen);
    }
    public void pausarJuego() {
        isPaused = true;
        modelo.setPlay(false);
        mostrarPantallaPausa();
    }

    public void setEstadoPausa() {
        if(isPaused) {
            isPaused = false;
        }
        else{
            isPaused = true;
        }
    }

    public void cerrarPausa(){
        pantallaPausa.dispose();
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
                cambioMenu = true;
            }
        });

        pantallaPausa.getSliderVolumen().addChangeListener(e -> {
            int valorVolumen = pantallaPausa.getSliderVolumen().getValue();
            volumen = valorVolumen;
        });

        pantallaPausa.setVisible(true);
    }


    public void setCambioMenu(){
        cambioMenu=false;
    }
    public boolean isCambio(){
        return cambioMenu;
    }

    private void reanudarJuego() {
        isPaused = false;
        modelo.setPlay(true);
        pantallaPausa.dispose();
        repaint();
    }

    @Override
     // Para limpiar el fondo correctamente
    public void paint(Graphics g) {
        super.paint(g); // Llamar a la superclase para que dibuje los componentes (como el botón)

        // Fondo del juego
        if(modelo.getLevelManager().getNivelActualIndex() + 1 == 1){
            Dimension tamanio = getSize();
            ImageIcon imagen = new ImageIcon(getClass().getResource("/resources/imagenes/FondoRetroMov.gif"));
            g.drawImage(imagen.getImage(), 0, 0, tamanio.width, tamanio.height, this);
        }else if(modelo.getLevelManager().getNivelActualIndex() + 1 == 2){
            Dimension tamanio = getSize();
            ImageIcon imagen = new ImageIcon(getClass().getResource("/resources/imagenes/video-retrowave-videogame-800x600-px.gif"));
            g.drawImage(imagen.getImage(), 0, 0, tamanio.width, tamanio.height, this);
        }else if(modelo.getLevelManager().getNivelActualIndex() + 1 == 3){
            Dimension tamanio = getSize();
            ImageIcon imagen = new ImageIcon(getClass().getResource("/resources/imagenes/NivelTresFondo.gif"));
            g.drawImage(imagen.getImage(), 0, 0, tamanio.width, tamanio.height, this);
        }



        // Dibujar ladrillos
        modelo.getMap().draw((Graphics2D) g);

        // Bordes del frame
        g.setColor(new Color(0,0,0,0));
        g.fillRect(0, 0, 4, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Barra
        g.setColor(Color.BLUE);
        //g.fillRect(modelo.getPlayerX(), 550, 100, 8);
        if(imagenBarra != null){
            g.drawImage(imagenBarra, modelo.getPlayerX(), 535, null);
        }

        // Bola
        g.setColor(Color.GREEN);
        //g.fillOval(modelo.getBallposX(), modelo.getBallposY(), 20, 20);
        if (imagenPelota != null) {
            g.drawImage(imagenPelota, modelo.getBallposX(), modelo.getBallposY(), null);
        }

        // Dibujar vidas con corazón
        if (imagenCorazon != null) {
            g.drawImage(imagenCorazon, 300, 13, 30, 30, null); // Dibuja el corazón
            g.setColor(Color.WHITE);
            if (customFont != null) {
                g.setFont(customFont); // Establecer la fuente personalizada
            } else {
                g.setFont(new Font("serif", Font.BOLD, 25)); // Fuente por defecto en caso de error
            }
            g.drawString("x " + modelo.getVidas(), 340, 35); // Dibuja el número de vidas junto al corazón
        }

        // Dibujar puntaje con estrella
        if (imagenEstrella != null) {
            g.drawImage(imagenEstrella, 590, 10, 30, 30, null); // Dibuja la estrella
            g.setColor(Color.WHITE);
            if (customFont != null) {
                g.setFont(customFont); // Establecer la fuente personalizada
            } else {
                g.setFont(new Font("serif", Font.BOLD, 25)); // Fuente por defecto en caso de error
            }
            g.drawString("" + modelo.getScore(), 630, 35); // Dibuja el puntaje junto a la estrella
        }

        // Nivel Actual
        g.drawString("Nivel: " + (modelo.getLevelManager().getNivelActualIndex() + 1), 35, 35);
//        g.drawString("Vidas: "+ modelo.getVidas(), 310, 30);
        // Verificar si se ganó el juego o se pasó al siguiente nivel
        if (!modelo.isPlay()) {
            if (modelo.getTotalBricks() <= 0) {
                if (modelo.getLevelManager().hayMasNiveles()) {
                    mostrarMensajeFinJuego(g, "¡Nivel Completado! Puntaje: " + modelo.getScore(), "PRESIONA ENTER PARA EL SIGUIENTE NIVEL.");
                } else {
                    mostrarMensajeFinJuego(g, "¡Ganaste el Juego! Puntaje Total: " + modelo.getScore(), "PRESIONA ENTER PARA REINICIAR.");
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
                    mostrarMensajeFinJuego(g, "Fin del juego. Puntaje: " + modelo.getScore(), "PRESIONA ENTER PARA REINICIAR.");
                }

                mostrarMensajeFinJuego(g, "Fin del juego. Puntaje: " + modelo.getScore(), "PRESIONA ENTER PARA REINICIAR,");
            }
        }
    }


    private void mostrarMensajeFinJuego(Graphics g, String mensajePrincipal, String mensajeSecundario) {
        // Obtener el tamaño del panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Establecer el fondo semi-transparente para el mensaje
        g.setColor(new Color(0, 0, 0, 0)); // Negro con transparencia
        g.fillRect(panelWidth / 4, panelHeight / 3, 400, 150);

        // Configurar la fuente para el mensaje principal
        g.setColor(Color.WHITE);
        g.setFont(customFont);

        // Obtener FontMetrics para calcular el ancho del texto
        FontMetrics fmPrincipal = g.getFontMetrics(customFont);
        int textoAnchoPrincipal = fmPrincipal.stringWidth(mensajePrincipal);

        // Calcular la posición X para centrar el mensaje principal
        int xPrincipal = (panelWidth - textoAnchoPrincipal) / 2;
        int yPrincipal = panelHeight / 2 - 20; // Ajustar Y según el tamaño del rectángulo

        // Dibujar el mensaje principal centrado
        g.drawString(mensajePrincipal, xPrincipal, yPrincipal);

        // Configurar la fuente para el mensaje secundario
        g.setFont(customFont2);

        // Obtener FontMetrics para el mensaje secundario
        FontMetrics fmSecundario = g.getFontMetrics(customFont2);
        int textoAnchoSecundario = fmSecundario.stringWidth(mensajeSecundario);

        // Calcular la posición X para centrar el mensaje secundario
        int xSecundario = (panelWidth - textoAnchoSecundario) / 2;
        int ySecundario = yPrincipal + 50; // Colocar el mensaje secundario más abajo

        // Dibujar el mensaje secundario centrado
        g.drawString(mensajeSecundario, xSecundario, ySecundario);
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
                modelo.setBallposX(mouseX + 28); // Centrar la pelota sobre la barra (100 de ancho de la barra / 2 - 10 de diámetro de la pelota)
                modelo.setBallposY(550 - 40); // Posicionar justo encima de la barra
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
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

