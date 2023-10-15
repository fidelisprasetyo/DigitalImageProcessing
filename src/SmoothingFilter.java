import java.awt.image.BufferedImage;
import java.util.Arrays;

public class SmoothingFilter {

    // -- public method
    // apply smoothing filter
    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {
        double filterCoef = (double) 1/(maskSize*maskSize);
        double[][] filterMask = new double[maskSize][maskSize];

//        for(double[] row: filterMask) {
//            Arrays.fill(row,filterCoef);
//        }

        filterMask = generateWeightedMask(maskSize);
        return ImageUtil.convolution(inputImage, filterMask);
    }

    private static double[][] generateWeightedMask(int maskSize) {
        double[][] weightedMask = new double[maskSize][maskSize];
        int center = maskSize/2;
        int total = 0;

        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < maskSize; j++) {
                int distance = Math.abs(i - center) + Math.abs(j - center);
                weightedMask[i][j] = Math.pow(2,(maskSize-1)-distance);
                total += weightedMask[i][j];
            }
        }
        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < maskSize; j++) {
                weightedMask[i][j] = weightedMask[i][j] / total;
            }
        }
        return weightedMask;
    }

}
