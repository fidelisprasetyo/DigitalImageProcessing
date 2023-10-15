import java.awt.image.BufferedImage;

public class SmoothingFilter {

    // -- public method
    // apply smoothing filter
    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        int padding = maskSize/2;

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage paddedImage = ImageUtil.extrapolateImage(inputImage, maskSize);

        for(int y = padding; y < height + padding; y++) {
            for(int x = padding; x < width + padding; x++) {
                int newPixel = arithmeticMean(paddedImage, x, y, maskSize);
                int rgb = ImageUtil.convertGrayToRGB(newPixel);
                outputImage.setRGB(x - padding, y - padding, rgb);
            }
        }
        return outputImage;
    }

    private static int arithmeticMean(BufferedImage image, int X, int Y, int maskSize) {
        int sum = 0;
        int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);

        for(int y = 0; y < maskSize; y++) {
            for(int x = 0; x < maskSize; x++) {
                sum += imageSegment[x][y];
            }
        }
        double mean = (double) sum/ (maskSize*maskSize);
        return (int) Math.round(mean);
    }


//    private static double[][] generateWeightedMask(int maskSize) {
//        double[][] weightedMask = new double[maskSize][maskSize];
//        int center = maskSize/2;
//        int total = 0;
//
//        for(int i = 0; i < maskSize; i++) {
//            for(int j = 0; j < maskSize; j++) {
//                int distance = Math.abs(i - center) + Math.abs(j - center);
//                weightedMask[i][j] = Math.pow(2,(maskSize-1)-distance);
//                total += weightedMask[i][j];
//            }
//        }
//        for(int i = 0; i < maskSize; i++) {
//            for(int j = 0; j < maskSize; j++) {
//                weightedMask[i][j] = weightedMask[i][j] / total;
//            }
//        }
//        return weightedMask;
//    }

}
