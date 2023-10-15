import java.awt.image.BufferedImage;

public class SharpeningFilter {

    // ---public methods

    // apply laplacian sharpening
    public static BufferedImage laplacianFilter(BufferedImage inputImage, int maskSize) {
        int[][] filterMask = generateLaplacianFilter(maskSize);

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[][] unscaledImage = new int[width][height];
        BufferedImage laplacianImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int padding = maskSize/2;
        BufferedImage paddedImage = ImageUtil.extrapolateImage(inputImage, maskSize);

        for(int y = padding; y < height + padding; y++) {
            for(int x = padding; x < width + padding; x++) {
                int newValue = getFilteredPixel(paddedImage, x, y, filterMask);
                unscaledImage[x-padding][y-padding] = newValue;
            }
        }

        int[][] scaledImage = normalizeValues(unscaledImage);
        laplacianImage = ImageUtil.writeGrayIntoBufferedImage(scaledImage);

        return ImageUtil.sumImages(inputImage, laplacianImage);
    }

    // apply highboost filtering
    public static BufferedImage highBoostFilter(BufferedImage inputImage, int maskSize, int A) {
        BufferedImage blurredImage = SmoothingFilter.applyFilter(inputImage,maskSize);
        BufferedImage maskImage = ImageUtil.subtractImages(inputImage, blurredImage);
        BufferedImage multipliedMask = ImageUtil.multiplyImageByInteger(maskImage, A);

        return ImageUtil.sumImages(inputImage, multipliedMask);
    }

    // ---private methods

    // generate laplacian filter mask
    private static int[][] generateLaplacianFilter(int maskSize) {
        int[][] filterMask = new int[maskSize][maskSize];
        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < maskSize; j++) {
                filterMask[i][j] = -1;
            }
        }
        filterMask[maskSize/2][maskSize/2] = maskSize*maskSize - 1;

        return filterMask;
    }

    private static int getFilteredPixel(BufferedImage image, int X, int Y, int[][] filterMask) {
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
