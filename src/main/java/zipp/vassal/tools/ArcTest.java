package zipp.vassal.tools;


import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

class MyCanvas extends JComponent {
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(Color.gray);
        int x = 5;
        int y = 7;

        g2.draw(new Arc2D.Double(x, y, 200, 200, 0, 15,Arc2D.PIE));
        g2.drawString("Arc2D", x, 250);
    }
}

public class ArcTest {
    public static void main(String[] a) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(30, 30, 450, 450);
        window.getContentPane().add(new MyCanvas());
        window.setVisible(true);
    }
}