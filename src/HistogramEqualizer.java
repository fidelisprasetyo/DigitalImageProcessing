import java.awt.image.BufferedImage;

public class HistogramEqualizer {

    public int[] originalHist = new int[256];
    private final BufferedImage inputImage;
    private final int width;
    private final int height;

    public HistogramEqualizer(BufferedImage inputImage) {
        this.inputImage = inputImage;
        width = inputImage.getWidth();
        height = inputImage.getHeight();
        getHistogram();
    }

    public void getHistogram() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = inputImage.getRGB(x,y);
                int grayValue = (pixel) & 0xFF;
                originalHist[grayValue]++;
            }
        }
    }

    public BufferedImage globalEqualization() {
        double[] equalizedCdf = new double[256];
        int[] cdf = new int[256];
        double mn = width*height;

        cdf[0] = originalHist[0];
        for(int i = 1; i < 256; i++) {
            cdf[i] = cdf[i-1] + originalHist[i];
        }

        for(int i = 0; i<256; i++) {
            equalizedCdf[i] = (double) 255 * cdf[i] / mn;
        }

        BufferedImage equalizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixel = inputImage.getRGB(x,y) & 0xFF;
                int newPixel = (int) equalizedCdf[pixel];
                int rgb = (newPixel << 16) | (newPixel << 8) | newPixel;
                equalizedImage.setRGB(x,y,rgb);
            }
        }
        return equalizedImage;
    }
}
