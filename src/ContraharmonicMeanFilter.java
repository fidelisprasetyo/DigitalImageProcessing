import java.awt.image.BufferedImage;

public class ContraharmonicMeanFilter {

    // -- public method
    // apply Harmonic Mean Filter

    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize, double orderValue) {

        PixelProcessor contraharmonicMean = (image, X, Y) -> {
            double sumNum = 0.0;
            double sumDenom = 0.0;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);

            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    sumNum += Math.pow(imageSegment[x][y],orderValue+1.0);
                    sumDenom += Math.pow(imageSegment[x][y], orderValue);
                }
            }

            double mean = sumNum/ sumDenom;
            return (int) Math.round(mean);
        };

        return ImageUtil.convolution(inputImage, maskSize, contraharmonicMean);
    }
}
