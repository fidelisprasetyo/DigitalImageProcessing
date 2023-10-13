import java.awt.image.BufferedImage;

public class HistogramEqualizer {

    public static int[] getHistogram(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        int[] histogram = new int[256];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int grayValue = ImageUtil.getGrayValue(inputImage,x,y);
                histogram[grayValue]++;
            }
        }
        return histogram;
    }

    public static BufferedImage globalEqualization(BufferedImage inputImage) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        int[] originalHist = getHistogram(inputImage);

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
                int pixel = ImageUtil.getGrayValue(inputImage,x,y);
                int newPixel = (int) equalizedCdf[pixel];
                int rgb = ImageUtil.convertGrayToRGB(newPixel);
                equalizedImage.setRGB(x,y,rgb);
            }
        }
        return equalizedImage;
    }

    private static int getEqualizedPixel(int X, int Y, BufferedImage paddedImage, int maskSize) {
        int[] cdf = new int[256];
        int[] histogram = new int[256];
        double[] equalizedCdf = new double[256];

        for(int y = Y - maskSize/2; y < Y + maskSize/2; y++) {
            for(int x = X - maskSize/2; x < X + maskSize/2; x++) {
                int grayValue = paddedImage.getRGB(x,y) & 0xFF;
                histogram[grayValue]++;
            }
        }

        cdf[0] = histogram[0];
        for(int i = 1; i < 256; i++) {
            cdf[i] = cdf[i-1] + histogram[i];
        }

        for(int i = 0; i < 256; i++) {
            equalizedCdf[i] = (double) 255 * cdf[i]/(maskSize*maskSize);
        }

        int pixel = ImageUtil.getGrayValue(paddedImage, X, Y);
        int newPixel = (int) equalizedCdf[pixel];
        int rgb = ImageUtil.convertGrayToRGB(newPixel);

        return rgb;
    }

    public static BufferedImage localEqualization(BufferedImage inputImage, int maskSize) {
        int height = inputImage.getHeight();
        int width = inputImage.getWidth();
        BufferedImage equalizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage paddedImage = ImageUtil.extrapolateImage(inputImage, maskSize);

        int padding = maskSize/2;
        for(int y = padding ; y < height + padding ; y++) {
            for(int x = padding ; x < width + padding ; x++) {
                int rgb = getEqualizedPixel(x,y, paddedImage, maskSize);
                equalizedImage.setRGB(x - padding, y - padding, rgb);
            }
        }
        return equalizedImage;
    }
}
