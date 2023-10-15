import java.awt.image.BufferedImage;

public class PixelProcessor {

    public static int arithmeticMean(BufferedImage image, int X, int Y, int[][] filterMask) {
        int sum = 0;
        int maskSize = filterMask.length;
        int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);

        for(int y = 0; y < maskSize; y++) {
            for(int x = 0; x < maskSize; x++) {
                sum += imageSegment[x][y];
            }
        }

        double mean = (double) sum/ (maskSize*maskSize);
        return (int) Math.round(mean);
    }
}