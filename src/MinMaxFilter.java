import java.awt.image.BufferedImage;

public class MinMaxFilter {

    public static BufferedImage applyMaxFilter(BufferedImage inputImage, int maskSize) {

        PixelProcessor maxPixel = (image, X, Y) -> {
            int max = 0;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);
            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    if(imageSegment[x][y] > max) {
                        max = imageSegment[x][y];
                    }
                }
            }
            return max;
        };

        return ImageUtil.convolution(inputImage, maskSize, maxPixel);
    }

    public static BufferedImage applyMinFilter(BufferedImage inputImage, int maskSize) {

        PixelProcessor minPixel = (image, X, Y) -> {
            int min = 255;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);
            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    if(imageSegment[x][y] < min) {
                        min = imageSegment[x][y];
                    }
                }
            }
            return min;
        };

        return ImageUtil.convolution(inputImage, maskSize, minPixel);
    }

    public static BufferedImage applyMidpointFilter(BufferedImage inputImage, int maskSize) {
        PixelProcessor midPixel = (image, X, Y) -> {
            int max = 0;
            int min = 255;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);
            for(int y = 0; y < maskSize; y++) {
                for(int x = 0; x < maskSize; x++) {
                    if(imageSegment[x][y] < min) {
                        min = imageSegment[x][y];
                    }
                    if(imageSegment[x][y] > max) {
                        max = imageSegment[x][y];
                    }
                }
            }
            return (int) Math.round((double) (min+max)/2);
        };

        return ImageUtil.convolution(inputImage, maskSize, midPixel);

    }
}
