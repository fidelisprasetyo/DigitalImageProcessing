import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedianFilter {

    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int padding = maskSize/2;

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage paddedImage = ImageUtil.extrapolateImage(inputImage, maskSize);

        for(int y = padding; y < height + padding; y++) {
            for(int x = padding; x < width + padding; x++) {
                int newPixel = getMedianPixel(paddedImage, x, y, maskSize);
                outputImage.setRGB(x - padding, y - padding, newPixel);
            }
        }
        return outputImage;
    }

    private static int getMedianPixel(BufferedImage image, int X, int Y, int maskSize) {
        int median;
        int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);
        List<Integer> pixelList = new ArrayList();

        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < maskSize; j++) {
                pixelList.add(imageSegment[i][j]);
            }
        }

        Collections.sort(pixelList);
        int n = pixelList.size();
        if(n%2 == 0) {
            median = (pixelList.get(n/2) + pixelList.get((n/2)-1))/2;
        } else {
            median = pixelList.get(n/2);
        }

        return ImageUtil.convertGrayToRGB(median);
    }
}
