package zipp.vassal.tools;


import javax.swing.*;
import java.awt.*;

class MyCanvas1 extends JComponent {
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(Color.gray);
        int x1 = 50;
        int y1 = 70;

        int x2 = 250;
        int y2 = 180;

        int dx = x2 - x1;
        int dy = y2 - y1;


        double k = ((double) (y2 - y1)) / ((double) (x2 - x1));



        int x3 = x1 + dx / 2;
        int y3 = y1 + dy / 2;


        double alpha = Math.atan(-1/k);
        System.out.println(alpha);
        int dx2 = (int) (30 * Math.cos(alpha));

        int x4 = x3 + dx2;
        int y4 = y3 - (int)(dx2 / k);

        g2.drawLine(x1, y1, x2, y2);
        g2.drawLine(x3, y3, x4, y4);
    }
}

public class OrthoTest {
    public static void main(String[] a) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(30, 30, 450, 450);
        window.getContentPane().add(new MyCanvas1());
        window.setVisible(true);
    }
}