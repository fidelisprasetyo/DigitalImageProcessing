import java.awt.image.BufferedImage;

public class SharpeningFilter {

    // ---public methods

    // apply laplacian sharpening
    public static BufferedImage laplacianFilter(BufferedImage inputImage, int maskSize) {
        double[][] filterMask = generateLaplacianFilter(maskSize);
        BufferedImage laplacianImage = convolution(inputImage, filterMask);

        return sumImages(inputImage, laplacianImage);
    }

    // apply highboost filtering
    public static BufferedImage highBoostFilter(BufferedImage inputImage, int maskSize, int A) {
        BufferedImage blurredImage = SmoothingFilter.applyFilter(inputImage,maskSize);
        BufferedImage maskImage = subtractImages(inputImage, blurredImage);
        BufferedImage multipliedMask = multiplyImageByInteger(maskImage, A);

        return sumImages(inputImage, multipliedMask);
    }

    // ---private utility methods

    // generate laplacian filter mask
    private static double[][] generateLaplacianFilter(int maskSize) {
        double[][] filterMask = new double[maskSize][maskSize];
        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < maskSize; j++) {
                filterMask[i][j] = -1.0;
            }
        }
        filterMask[maskSize/2][maskSize/2] = maskSize*maskSize - 1;

        return filterMask;
    }

    // returns image1 + image 2
    private static BufferedImage sumImages(BufferedImage image1, BufferedImage image2) {
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
    private static BufferedImage subtractImages(BufferedImage image1, BufferedImage image2) {
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
    private static BufferedImage multiplyImageByInteger(BufferedImage inputImage, int A) {
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

    // modified convolution with scaling
    private static BufferedImage convolution(BufferedImage inputImage, double[][] filterMask) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        int maskSize = filterMask.length;
        int padding = maskSize/2;

        int[][] unscaledGray = new int[width][height];

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage paddedImage = ImageUtil.extrapolateImage(inputImage, maskSize);

        for(int y = padding; y < height + padding; y++) {
            for(int x = padding; x < width + padding; x++) {
                int newValue = ImageUtil.getFilteredPixel(paddedImage, x, y, filterMask);
                unscaledGray[x-padding][y-padding] = newValue;
            }
        }

        int[][] scaledGray = normalizeValues(unscaledGray);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int rgb = ImageUtil.convertGrayToRGB(scaledGray[x][y]);
                outputImage.setRGB(x,y,rgb);
            }
        }
        return outputImage;
    }

    // scale negative/ larger than 255 values
    private static int[][] normalizeValues(int[][] unscaledGray) {
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

}
