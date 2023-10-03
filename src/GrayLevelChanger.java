import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class GrayLevelChanger {

    private BufferedImage inputImage;
    private int newDepth;

    public GrayLevelChanger(BufferedImage inputImage, int newDepth) {
        this.inputImage = inputImage;
        this.newDepth = newDepth;
    }

    public BufferedImage run() {
        int inputDepth = getGrayDepth(inputImage);
        int inputLevel = (int) Math.pow(2, inputDepth);
        int targetLevel = (int) Math.pow(2, newDepth);

        int ratio = inputLevel/ targetLevel;

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x,y) & 0xFF;
                int newPixelValue = (int) Math.floor((double)rgb/inputLevel * targetLevel) * ratio;
                int g = (newPixelValue << 16) | (newPixelValue << 8) | newPixelValue;
                outputImage.setRGB(x,y,g);
            }
        }

        return outputImage;
    }

    private int getGrayDepth(BufferedImage img) {
        Raster raster = img.getData();
        int grayDepth = raster.getSampleModel().getSampleSize(0);
        return grayDepth;
    }
}
