import java.awt.image.BufferedImage;

public class GeometricMeanFilter {

    // -- public method
    // apply Geometric Mean Filter
    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {

        PixelProcessor geometricMean = (image, X, Y) -> {
            double prod = 1.0;
            int count = 0;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);

            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    if(imageSegment[x][y] != 0) {
                        prod *= imageSegment[x][y];
                        count++;
                    }
                }
            }
            double exp = 1.0 / count;
            double mean = Math.pow(prod, exp);
            return (int) Math.round(mean);
        };

        return ImageUtil.convolution(inputImage, maskSize, geometricMean);
    }
}
