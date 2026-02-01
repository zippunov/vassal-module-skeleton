package zipp.vassal.tools;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ScreenDPI {
    public static boolean isRetinaDisplay() {
        final GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final AffineTransform transform = gfxConfig.getDefaultTransform();
        return !transform.isIdentity();
    }

    public static double getPixelDensity() {
        final GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return gfxConfig.getDefaultTransform().getScaleX();
    }

    public static int getPixelDensityInt() {
        final GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return (int) gfxConfig.getDefaultTransform().getScaleX();
    }
}
