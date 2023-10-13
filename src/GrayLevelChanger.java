import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class GrayLevelChanger {

    public static BufferedImage changeGrayLevel(BufferedImage inputImage, int newDepth) {
        int inputDepth = getGrayDepth(inputImage);
        int inputLevel = (int) Math.pow(2, inputDepth);
        int targetLevel = (int) Math.pow(2, newDepth);

        int ratio = inputLevel/ targetLevel;

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int g = inputImage.getRGB(x,y) & 0xFF;
                int newPixelValue = (int) Math.floor((double)g/inputLevel * targetLevel) * ratio;
                int rgb = (newPixelValue << 16) | (newPixelValue << 8) | newPixelValue;
                outputImage.setRGB(x,y,rgb);
            }
        }

        return outputImage;
    }

    private static int getGrayDepth(BufferedImage img) {
        Raster raster = img.getData();
        return raster.getSampleModel().getSampleSize(0);
    }
}
