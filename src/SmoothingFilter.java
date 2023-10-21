import java.awt.image.BufferedImage;

public class SmoothingFilter {

    // -- public method
    // apply Mean Smoothing Filter
    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {

        PixelProcessor arithmeticMean = (image, X, Y) -> {
            double sum = 0.0;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);

            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    sum += imageSegment[x][y];
                }
            }
            double mean = sum/ (maskSize*maskSize);
            return (int) Math.round(mean);
        };

        return ImageUtil.convolution(inputImage, maskSize, arithmeticMean);
    }

    // not used: weighted mask
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
