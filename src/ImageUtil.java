import java.awt.image.BufferedImage;

// some useful utility methods

public class ImageUtil {

    // return grayscale value from rgb bits
    public static int getGrayValue(BufferedImage image, int x, int y) {
        int pixel = image.getRGB(x,y) & 0xFF;
        return pixel;
    }

    // convert gray value into rgb
    public static int convertGrayToRGB(int gray) {
        int rgb = (gray << 16) | (gray << 8) | gray;
        return rgb;
    }

    // get gray value and convert to rgb
    public static int getGrayValueInRGB(BufferedImage image, int x, int y) {
        int pixel = getGrayValue(image, x, y);
        int rgb = convertGrayToRGB(pixel);
        return rgb;
    }

    // fill outside borders with neighboring pixels (clamping)
    public static BufferedImage extrapolateImage(BufferedImage inputImage, int maskSize) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int newWidth = width + 2 * (maskSize / 2);
        int newHeight = height + 2 * (maskSize / 2);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < newHeight; y++) {
            for(int x = 0; x < newWidth; x++) {
                int originalX = Math.min(Math.max(x - (maskSize / 2), 0), width - 1);
                int originalY = Math.min(Math.max(y - (maskSize / 2), 0), height - 1);

                int rgb = getGrayValueInRGB(inputImage, originalX, originalY);
                newImage.setRGB(x,y,rgb);
            }
        }
        return newImage;
    }

    // get neighboring pixels according to maskSize
    public static int[][] extractNeighbors(BufferedImage paddedImage, int x, int y, int maskSize) {
        int[][] pixels = new int[maskSize][maskSize];
        int padding = maskSize/2;

        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < maskSize; j++) {
                pixels[i][j] = getGrayValue(paddedImage, x - padding + j, y - padding + i);
            }
        }
        return pixels;
    }

    // convolution operation
    public static BufferedImage convolution(BufferedImage inputImage, double[][] filterMask) {
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

    // get the new center pixel's value
    public static int getFilteredPixel(BufferedImage image, int X, int Y, double[][] filterMask) {
        double sum = 0.0;
        int maskSize = filterMask.length;
        int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);

        for(int y = 0; y < filterMask.length; y++) {
            for(int x = 0; x < filterMask.length; x++) {
                sum += (double) imageSegment[x][y] * filterMask[x][y];
            }
        }
        return convertGrayToRGB((int) Math.round(sum));
    }

}
