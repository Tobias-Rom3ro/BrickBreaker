package Vista;

import javax.swing.*;
import java.awt.*;


public class Menu extends JPanel {
    private JButton start;
    private JButton exit;

    public Menu() {
        setLayout(null);
        start = new JButton();
        exit = new JButton();
        exit.setBackground(new Color(35,25,72,0));

        start.setBackground(new Color(35, 25, 72, 0));
        start.setForeground(new Color(255, 255, 255));

        // Cargar la imagen para el botón
        ImageIcon icono = new ImageIcon(getClass().getResource("/resources/menuImagenes/startPequenio.png"));
        ImageIcon icono2 = new ImageIcon(getClass().getResource("/resources/menuImagenes/exitPequenio.png"));
        // Asignar la imagen como icono del botón
        start.setIcon(icono);
        exit.setIcon(icono2);

        exit.setBorderPainted(false);
        exit.setContentAreaFilled(false);
        exit.setFocusPainted(false);

        start.setBorderPainted(false);
        start.setContentAreaFilled(false);
        start.setFocusPainted(false);

        // Establecer tamaño y posición del botón
        start.setBounds(290,320,200,58);
        exit.setBounds(290,370,200,58);

        this.add(start);
        this.add(exit);

    }
    public void paintComponent(Graphics g) {
        Dimension tamanio = getSize();
        ImageIcon imagen = new ImageIcon(new ImageIcon(getClass().getResource("/resources/menuImagenes/fondomMenu.png")).getImage());
        g.drawImage(imagen.getImage(), 0, 0, tamanio.width, tamanio.height, null);
    }
    public JButton getStartButton() {
        return start;
    }
    public JButton getExitButton() {
        return exit;
    }
}
