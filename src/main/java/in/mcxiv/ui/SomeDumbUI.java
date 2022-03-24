package in.mcxiv.ui;

import in.mcxiv.music.MusicBox;
import in.mcxiv.threads.ThreadMan;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SomeDumbUI {

    public static void main(String[] args) {
        MusicBox musicBox = new MusicBox();

        int arnm[] = new int[200];
        for (int i = 0; i < 200; i++) arnm[i++] = 100;
        AtomicInteger key = new AtomicInteger();
        musicBox.callBack(d -> arnm[key.getAndIncrement() % 200] = 100 + (int) (70 * d));

        JFrame jFrame = new JFrame() {
            {
                setSize(200, 200);
                setLocationRelativeTo(null);
                setVisible(true);
                setResizable(false);
                setDefaultCloseOperation(EXIT_ON_CLOSE);

                add(new JPanel() {
                    {
                        setBackground(Color.WHITE);
                        setBounds(0, 0, 200, 200);
                    }

                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.BLUE);
                        for (int i = 1; i < 200; i++)
                            g.drawLine(i - 1, arnm[i-1], i, arnm[i]);
                    }
                });
            }
        };

        ThreadMan.getInstance().scheduleAtFixedRate(jFrame::repaint, 0, 1000, TimeUnit.MILLISECONDS);
    }

}
