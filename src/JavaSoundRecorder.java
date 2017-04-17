import com.sun.media.sound.AudioFloatInputStream;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class JavaSoundRecorder {

    // record duration, in milliseconds
    static final long RECORD_TIME = 2000;
    public String path;
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine line;
    private boolean flag = false;
    private Thread stopper;
    ByteArrayOutputStream baos;
    AudioFormat format;

    public JavaSoundRecorder(String name) {
        try {
            print("ОЖИДАНИЕ ЗВУКА");
            this.path = name;
            baos = new ByteArrayOutputStream();
            float sampleRate = 5000;
            int sampleSizeInBits = 8;
            int channels = 2;
            boolean signed = true;
            boolean bigEndian = true;
            format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            int bufferSize = (int)format.getSampleRate() * format.getFrameSize();
            byte buffer[] = new byte[bufferSize];

            while (true) {
                int count = line.read(buffer, 0, buffer.length);
                if (count > 0) {
                    if(!flag) {
                        for (byte buf : buffer) {
                           // System.out.println(buf);
                            if (buf > 100) {
                                flag = true;
                                baos.write(buffer);
                                stopprStart();
                                print("ЗАПИСЬ АУДИО");
                                break;
                            }
                        }
                    }else
                        baos.write(buffer);
                }
            }


        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        System.out.println();
    }

    void finish() {
        try(AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(baos.toByteArray()),
                format, baos.toByteArray().length/(format.getSampleSizeInBits()/8*format.getChannels()))) {

            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(path));
            print("АУДО ЗАПИСАНО" + "\n" + path);
            flag = false;
            baos.close();
            baos = new ByteArrayOutputStream();
            callBack();
            print("ОЖИДАНИЕ ЗВУКА");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void print(String txt){
        System.out.println();
        System.out.println("****************************************");
        System.out.println(txt);
        System.out.println("****************************************");
    }

    void stopprStart() {

        stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                finish();

            }
        });

        stopper.start();

    }

    public abstract void callBack();
}