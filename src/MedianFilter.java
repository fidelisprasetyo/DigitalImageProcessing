import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedianFilter {

    // -- public method

    // apply median filter
    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize) {

        PixelProcessor getMedian = (image, X, Y) -> {
            int median;
            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize, 0);
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
        };

        return ImageUtil.convolution(inputImage, maskSize, getMedian);
    }
}
