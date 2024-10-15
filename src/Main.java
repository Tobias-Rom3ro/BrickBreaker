import javax.swing.JFrame;
import Modelo.BrickBreaker;
import Controlador.*;
import Vista.Menu;
import Vista.VistaBrickBreaker;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame ventana = new JFrame();
        Color color = Color.BLACK;
        BrickBreaker modelo = new BrickBreaker();
        VistaBrickBreaker vista = new VistaBrickBreaker(modelo);
        JFrame menu = new JFrame("Menu juego");

        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.getContentPane().setBackground(color);

        Vista.Menu meniu = new Menu();
        ControladorBrickBreaker controlador = new ControladorBrickBreaker(modelo, vista, menu, ventana);

        menu.add(meniu);
        menu.setSize(800, 600);
        menu.setVisible(true);
        ControladorMenu controladorMenu = new ControladorMenu(meniu,menu, ventana);
        ventana.setBounds(10, 10, 710, 600);
        ventana.setTitle("Brick Breaker");
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.add(vista);

        ventana.setVisible(false);

        // Añadir el KeyListener directamente a la vista
        vista.addKeyListener(controlador);
        vista.setFocusable(true); // Hacer que la vista sea "focusable"
        vista.requestFocusInWindow(); // Solicitar foco para la vista
    }
}
