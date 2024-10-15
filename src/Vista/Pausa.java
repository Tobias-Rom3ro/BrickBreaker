package Vista;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class Pausa extends JFrame {
    private JButton botonReanudar;
    private JButton botonVolumen;
    private JSlider sliderVolumen;

    public Pausa(ActionListener reanudarAction, ActionListener menuPrincipalAction ) {
        setTitle("Pantalla de Pausa");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);
        ImageIcon icono = new ImageIcon(getClass().getResource("/resources/imagenes/4K-39.png"));
        ImageIcon icono2 = new ImageIcon(getClass().getResource("/resources/imagenes/Menu.png"));
        botonReanudar = createButton("Reanudar", reanudarAction, Color.BLACK);
        botonReanudar.setBounds(120, 20, 150, 80); // Asignar posición y tamaño
        botonReanudar.setIcon(icono);
        botonVolumen = createButton("Menú Principal", menuPrincipalAction, Color.BLACK);
        botonVolumen.setBounds(120, 110, 100, 50); // Asignar posición y tamaño
        botonVolumen.setIcon(icono2);
        botonReanudar.setHorizontalTextPosition(SwingConstants.CENTER); // Texto centrado horizontalmente
        botonReanudar.setVerticalTextPosition(SwingConstants.BOTTOM);
        botonVolumen.setHorizontalTextPosition(SwingConstants.CENTER); // Texto centrado horizontalmente
        botonVolumen.setVerticalTextPosition(SwingConstants.BOTTOM);
        sliderVolumen = new JSlider(0, 100, 50);
        sliderVolumen.setMajorTickSpacing(20);
        sliderVolumen.setPaintTicks(true);
        sliderVolumen.setPaintLabels(true);
        sliderVolumen.setForeground(Color.BLACK);
        sliderVolumen.setBackground(Color.BLACK);
        sliderVolumen.setBounds(100, 230, 200, 50); // Asignar posición y tamaño
        add(sliderVolumen);

        JLabel volumenLabel = new JLabel("Volumen:");
        volumenLabel.setForeground(Color.WHITE);
        volumenLabel.setFont(new Font("Pixel", Font.BOLD, 16));
        volumenLabel.setBounds(100, 200, 200, 20); // Asignar posición y tamaño
        add(volumenLabel);

        add(botonReanudar);
        add(botonVolumen);
    }

    private JButton createButton(String text, ActionListener actionListener, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Pixel", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 50));
        button.addActionListener(actionListener);
        button.setBorderPainted(true);
        return button;
    }

    public JSlider getSliderVolumen() {
        return sliderVolumen; // Método para obtener el slider
    }
}
