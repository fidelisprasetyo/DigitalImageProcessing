import java.awt.image.BufferedImage;

public class HarmonicMeanFilter {

    // -- public method
    // apply Harmonic Mean Filter

    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {

        PixelProcessor harmonicMean = (image, X, Y) -> {
            double sum = 0.0;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);

            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    double fraction = 1/(double) imageSegment[x][y];
                    sum += fraction;
                }
            }
            double mean = (maskSize*maskSize)/sum;
            return (int) Math.round(mean);
        };

        return ImageUtil.convolution(inputImage, maskSize, harmonicMean);
    }
}
