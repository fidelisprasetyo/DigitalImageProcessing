import java.awt.image.BufferedImage;

public class HarmonicMeanFilter {

    // -- public method
    // apply Harmonic Mean Filter

    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {

        PixelProcessor harmonicMean = (image, X, Y) -> {
            double sum = 0.0;
            int count = 0;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);

            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    double fraction;
                    if(imageSegment[x][y] == 0) {
                        fraction = 1.0/ 0.01;
                    } else {
                        fraction = 1.0 /(double) imageSegment[x][y];
                    }
                    sum += fraction;
                    count++;
                }
            }
            double mean = count/sum;
            return (int) Math.round(mean);
        };

        return ImageUtil.convolution(inputImage, maskSize, harmonicMean);
    }
}
