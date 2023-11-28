import java.awt.image.BufferedImage;

public class SmoothingFilter {

    // -- public method
    // apply Mean Smoothing Filter
    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {

        PixelProcessor arithmeticMean = (image, X, Y) -> {
            double sum = 0.0;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize, 0);

            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    sum += imageSegment[x][y];
                }
            }
            double mean = sum/ (maskSize*maskSize);
            int gray = (int) Math.round(mean);
            return ImageUtil.convertGrayToRGB(gray);
        };

        return ImageUtil.convolution(inputImage, maskSize, arithmeticMean);
    }

}
