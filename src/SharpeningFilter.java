import java.awt.image.BufferedImage;

public class SharpeningFilter {

    // ---public methods

    // apply laplacian sharpening
    public static BufferedImage laplacianFilter(BufferedImage inputImage, int maskSize) {
        int[][] filterMask = generateLaplacianFilter(maskSize);

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[][] unscaledGray = new int[width][height];

        int padding = maskSize/2;
        BufferedImage paddedImage = ImageUtil.paddedImage(inputImage, maskSize);

        for(int y = padding; y < height + padding; y++) {
            for(int x = padding; x < width + padding; x++) {
                int newValue = computePixel(paddedImage, x, y, filterMask);     // convolution
                unscaledGray[x-padding][y-padding] = newValue;
            }
        }

        int[][] scaledGray = ImageUtil.normalizeGrayLevels(unscaledGray);

        BufferedImage laplacianImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                laplacianImage.setRGB(x,y,ImageUtil.convertGrayToRGB(scaledGray[x][y]));
            }
        }

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

    private static int computePixel(BufferedImage image, int X, int Y, int[][] filterMask) {
        double sum = 0.0;
        int maskSize = filterMask.length;
        int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize, 0);

        for(int y = 0; y < filterMask.length; y++) {
            for(int x = 0; x < filterMask.length; x++) {
                sum += (double) imageSegment[x][y] * filterMask[x][y];
            }
        }
        return ImageUtil.convertGrayToRGB((int) Math.round(sum));
    }

}
