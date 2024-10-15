package Controlador;

import Vista.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import Vista.Sonido;

public class ControladorMenu implements ActionListener {
    private Menu menu;
    private JFrame frameMenu;
    private JFrame frameJuego;
    private Sonido musicaMenu;
    public ControladorMenu(Menu menu, JFrame frameMenu, JFrame frameJuego) {
        this.menu = menu;
        this.frameMenu = frameMenu;
        this.frameJuego = frameJuego;

        musicaMenu = new Sonido("/multimedia/sonidoFondoPrincipal.wav");
        musicaMenu.reproducirEnBucle();
        // Registrar los escuchadores de los botones
        this.menu.getStartButton().addActionListener(this);
        this.menu.getExitButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.getStartButton()) {
            musicaMenu.detener();
            // Acción para el botón "Start"
            frameMenu.setVisible(false);  // Ocultar el frame actual
            frameJuego.setVisible(true);// Mostrar el nuevo frame
        }
        if(e.getSource() == menu.getExitButton()) {
            musicaMenu.detener();
            System.exit(0);
        }
    }
}
