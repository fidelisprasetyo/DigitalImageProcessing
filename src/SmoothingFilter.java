import java.awt.image.BufferedImage;
import java.util.Arrays;

public class SmoothingFilter {

    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {
        double filterCoef = (double) 1/(maskSize*maskSize);
        double[][] filterMask = new double[maskSize][maskSize];

        for(double[] row: filterMask) {
            Arrays.fill(row,filterCoef);
        }
        return correlation(inputImage, filterMask);
    }

    public static BufferedImage correlation(BufferedImage inputImage, double[][] filterMask) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        int maskSize = filterMask.length;
        int padding = maskSize/2;

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage paddedImage = ImageUtil.extrapolateImage(inputImage, maskSize);

        for(int y = padding; y < height + padding; y++) {
            for(int x = padding; x < width + padding; x++) {
                int newPixel = getFilteredPixel(paddedImage, x, y, filterMask);
                outputImage.setRGB(x - padding, y - padding, newPixel);
            }
        }
        return outputImage;
    }

    private static int getFilteredPixel(BufferedImage image, int X, int Y, double[][] filterMask) {
        double sum = 0.0;
        int maskSize = filterMask.length;
        int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);

        for(int y = 0; y < filterMask.length; y++) {
            for(int x = 0; x < filterMask.length; x++) {
                sum += (double) imageSegment[x][y] * filterMask[x][y];
            }
        }
        return ImageUtil.convertGrayToRGB((int) Math.round(sum));
    }
}
