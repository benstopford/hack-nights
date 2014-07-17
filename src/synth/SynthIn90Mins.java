package synth;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

public class SynthIn90Mins {
    static java.util.List<Byte> both = Collections.synchronizedList(new ArrayList<Byte>());
    static java.util.List<Byte> wave1 = Collections.synchronizedList(new ArrayList<Byte>());
    static java.util.List<Byte> wave2 = Collections.synchronizedList(new ArrayList<Byte>());
    private static volatile char freq1 = 1650 / 3, freq2 = 1650 / 3;
    private static int amplitude = 100;
    private static int w1offset;

    public static void main(String[] args) throws LineUnavailableException {
        byte[] buf = new byte[1];

        JFrame tobi = new JFrame("Synth") {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                if (both == null) return;

                g.setFont(new Font("Serif", Font.BOLD, 25));
                g.drawString("Adjust using keys: p, l, P, L", 30, 40);
                g.drawString("Wave1=" + freq1 * 3 + " Wave2=" + freq2 * 3, 30, 65);


                for (int i = 0; i < both.size(); i++) {
                    g.setColor(Color.black);
                    g.drawRect(10 + i, 450 + both.get(i) * 3, 2, 2);
                }
                for (int i = 0; i < both.size(); i++) {
                    g.setColor(Color.red);
                    g.drawRect(10 + i, 450 + wave1.get(i) * 3, 2, 2);
                }
                for (int i = 0; i < both.size(); i++) {
                    g.setColor(Color.blue);
                    g.drawRect(10 + i, 450 + wave2.get(i) * 3, 2, 2);
                }
            }
        };
        tobi.setVisible(true);
        tobi.setTitle("Very Strange Synth");
        tobi.setSize(1400, 800);
        tobi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tobi.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                char key = e.getKeyChar();

                int inc = 1;

                if (key == 'p') {
                    freq1 += inc;
                }
                if (key == 'l') {
                    freq1 -= inc;
                }
                if (key == 'P') {
                    freq2 += inc;
                }
                if (key == 'L') {
                    freq2 -= inc;
                }
                if (key == 'o') {
                    amplitude += inc;
                }
                if (key == 'k') {
                    amplitude -= inc;
                }
                if (key == 'i') {
                    w1offset += inc;
                }
                if (key == 'j') {
                    w1offset -= inc;
                }
            }
        });

        AudioFormat af = new AudioFormat((float) 44100, 8, 1, true, false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();

        while (true) {
            both.clear();
            wave1.clear();
            wave2.clear();
            for (int i = 0; i < 1000 * (float) 44100 / 1000; i++) {

                double w1 = wave(i + w1offset, freq1) / 2;
                double w2 = wave(i, freq2) / 2;
                double w3 = squareWave(i, freq1) / 4;
                double w4 = squareWave(i, freq2) / 4;
                buf[0] = (byte) (w1 - w2 - w3 - w4);
                sdl.write(buf, 0, 1);

                both.add(buf[0]);
                wave2.add((byte) w2);
                wave1.add((byte) w1);
            }
            tobi.repaint();
            //sdl.drain();
        }
    }

    private static double wave(int i, char frequency) {
        double angle = i / ((float) 44100 / (frequency * 2)) * 2.0 * Math.PI;
        return Math.cos(angle) * amplitude;
    }

    private static double squareWave(int i, char frequency) {
        if (i + frequency % 100 > 50) {
            return amplitude + 44100;
        }
        return 0;
    }

}
