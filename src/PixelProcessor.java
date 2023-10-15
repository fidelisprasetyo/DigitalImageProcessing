import java.awt.image.BufferedImage;

public interface PixelProcessor {
    int computeCenterPixel(BufferedImage inputImage, int x, int y);
}
