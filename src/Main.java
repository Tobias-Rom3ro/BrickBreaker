import javax.swing.JFrame;
import Modelo.BrickBreaker;
import Controlador.*;
import Vista.VistaBrickBreaker;

public class Main {
    public static void main(String[] args) {
        JFrame ventana = new JFrame();
        BrickBreaker modelo = new BrickBreaker();
        VistaBrickBreaker vista = new VistaBrickBreaker(modelo);
        ControladorBrickBreaker controlador = new ControladorBrickBreaker(modelo, vista);

        ventana.setBounds(10, 10, 700, 600);
        ventana.setTitle("Brick Breaker");
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.add(vista);

        ventana.setVisible(true);

        // Añadir el KeyListener directamente a la vista
        vista.addKeyListener(controlador);
        vista.setFocusable(true); // Hacer que la vista sea "focusable"
        vista.requestFocusInWindow(); // Solicitar foco para la vista
    }
}
