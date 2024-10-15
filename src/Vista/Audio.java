package Vista;
import javax.sound.sampled.*;

public class Audio {
    private FloatControl volumeControl;

    public Audio() {
        // Aquí puedes configurar el volumen general por defecto si lo deseas
        try {
            Mixer.Info mixerInfo = AudioSystem.getMixerInfo()[0];
            Mixer audioMixer = AudioSystem.getMixer(mixerInfo);
            for (Line.Info lineInfo : audioMixer.getSourceLineInfo()) {
                if (lineInfo instanceof DataLine.Info) {
                    Line line = audioMixer.getLine(lineInfo);
                    if (line instanceof Clip) {
                        line.open();
                        volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                        break; // Salimos una vez que encontramos un clip
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void controlVolumen(int volumen) {
        // Asegurarse de que el volumen esté en el rango de 0 a 100
        if (volumen < 0) {
            volumen = 0;
        } else if (volumen > 100) {
            volumen = 100;
        }

        // Convertir el volumen (0-100) a decibelios (-80 a 6)
        float volume = (float) (volumen / 100.0 * 6.0) - 80.0f;
        if (volumeControl != null) {
            volumeControl.setValue(volume); // Establecer el nuevo volumen
        }

        System.out.println("Volumen ajustado a: " + volumen);
    }
}
