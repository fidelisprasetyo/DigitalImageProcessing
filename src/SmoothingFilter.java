import java.awt.image.BufferedImage;
import java.util.Arrays;

public class SmoothingFilter {

    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {
        double filterCoef = (double) 1/(maskSize*maskSize);
        double[][] filterMask = new double[maskSize][maskSize];

        for(double[] row: filterMask) {
            Arrays.fill(row,filterCoef);
        }
        return ImageUtil.convolution(inputImage, filterMask);
    }

}
