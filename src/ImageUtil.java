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

    // convert 2d matrix of grayscale values into BufferedImage
    public static BufferedImage writeGrayIntoBufferedImage(int[][] image) {
        int width = image.length;
        int height = image[0].length;

        BufferedImage outputImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int rgb = ImageUtil.convertGrayToRGB(image[x][y]);
                outputImage.setRGB(x,y,rgb);
            }
        }
        return outputImage;
    }

    // returns image1 + image 2
    public static BufferedImage sumImages(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image2.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int gray1 = ImageUtil.getGrayValue(image1,x,y);
                int gray2 = ImageUtil.getGrayValue(image2,x,y);
                int sumGray = Math.min(255, gray1+gray2);
                int rgb = ImageUtil.convertGrayToRGB(sumGray);
                result.setRGB(x,y,rgb);
            }
        }
        return result;
    }

    // returns image1 - image 2
    public static BufferedImage subtractImages(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image2.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int gray1 = ImageUtil.getGrayValue(image1,x,y);
                int gray2 = ImageUtil.getGrayValue(image2,x,y);
                int sumGray = Math.max(0, gray1-gray2);
                int rgb = ImageUtil.convertGrayToRGB(sumGray);
                result.setRGB(x,y,rgb);
            }
        }
        return result;
    }

    // returns A * inputImage
    public static BufferedImage multiplyImageByInteger(BufferedImage inputImage, int A) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int gray = ImageUtil.getGrayValue(inputImage, x, y);
                int multipliedGray = Math.min(255, A*gray);
                int rgb = ImageUtil.convertGrayToRGB(multipliedGray);
                result.setRGB(x,y,rgb);
            }
        }
        return result;
    }

    // scale negative/ larger than 255 values
    public static int[][] normalizeGrayLevels(int[][] unscaledGray) {
        int width = unscaledGray.length;
        int height = unscaledGray[0].length;

        int[][] scaledGray = new int[width][height];

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixel = unscaledGray[x][y];
                if(pixel < min) {
                    min = pixel;
                }
                if(pixel > max) {
                    max = pixel;
                }
            }
        }
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixel = unscaledGray[x][y];
                int normalizedPixel = (int) Math.round((pixel - min) * (255.0/(max-min)));
                scaledGray[x][y] = normalizedPixel;
            }
        }
        return scaledGray;
    }

    // compute the pixel by doing mathematical operation on the kernel window (neighboring pixels)
    // PixelProcessor defines the desired calculation
    // is defined using lambda expression in Filter's class
    public static BufferedImage convolution(BufferedImage inputImage, int maskSize, PixelProcessor pixelProcessor) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        int padding = maskSize/2;

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage paddedImage = ImageUtil.extrapolateImage(inputImage, maskSize);

        for(int y = padding; y < height + padding; y++) {
            for(int x = padding; x < width + padding; x++) {
                int newPixel = pixelProcessor.computeCenterPixel(paddedImage, x, y);
                int rgb = ImageUtil.convertGrayToRGB(newPixel);
                outputImage.setRGB(x - padding, y - padding, rgb);
            }
        }
        return outputImage;
    }
}
