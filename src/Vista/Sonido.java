package Vista;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Sonido {
    private Clip clip;
    private FloatControl volumenControl;

    public Sonido(String rutaArchivo) {
        try {
            // Usar getResourceAsStream para cargar el archivo desde el classpath
            InputStream audioSrc = getClass().getResourceAsStream(rutaArchivo);
            if (audioSrc == null) {
                throw new IllegalArgumentException("No se encontró el archivo de sonido: " + rutaArchivo);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            volumenControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    // Reproduce el sonido en bucle (usado para música de fondo)
    public void reproducirEnBucle() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    // Reproduce el sonido una vez (para efectos como rebotes)
    public void reproducir() {
        if (clip != null) {
            clip.setFramePosition(0);  // Para reiniciar el clip
            clip.start();
        }
    }

    // Detener el sonido
    public void detener() {
        if (clip != null) {
            clip.stop();
        }
    }

    // Ajustar el volumen (el rango va de 0 a 100, donde 0 es -80.0 y 100 es 6.0)
    public void setVolumen(float valor) {
        if (volumenControl != null) {
            // Mapear el rango de 0 a 100 al rango de -80.0 a 6.0
            float nuevoVolumen = (valor / 100.0f) * (volumenControl.getMaximum() - volumenControl.getMinimum()) + volumenControl.getMinimum();
            volumenControl.setValue(nuevoVolumen);
        }
    }

    // Obtener el volumen como un valor entre 0 y 100
    public float getVolumen() {
        if (volumenControl != null) {
            // Mapear el valor actual del volumen al rango de 0 a 100
            return ((volumenControl.getValue() - volumenControl.getMinimum()) /
                    (volumenControl.getMaximum() - volumenControl.getMinimum())) * 100;
        }
        return 0;
    }
}
