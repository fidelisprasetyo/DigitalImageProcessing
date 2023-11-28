import java.awt.image.BufferedImage;

public class GeometricMeanFilter {

    // -- public method
    // apply Geometric Mean Filter
    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {

        PixelProcessor geometricMean = (image, X, Y) -> {
            double prod = 1.0;
            int count = 0;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize, 0);

            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    if(imageSegment[x][y] == 0) {
                        prod *= 1;
                    } else {
                        prod *= imageSegment[x][y];
                    }
                    count++;
                }
            }
            double exp = 1.0 / count;
            double mean = Math.pow(prod, exp);

            int gray = (int) Math.round(mean);
            return ImageUtil.convertGrayToRGB(gray);
        };

        return ImageUtil.convolution(inputImage, maskSize, geometricMean);
    }
}
