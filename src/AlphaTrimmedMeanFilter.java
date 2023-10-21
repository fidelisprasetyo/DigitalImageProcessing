import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlphaTrimmedMeanFilter {

    // -- public method

    // apply median filter
    public static BufferedImage applyFilter(BufferedImage inputImage, int maskSize, int d) {

        PixelProcessor alphaTrimmedMean = (image, X, Y) -> {

            int[][] imageSegment = ImageUtil.extractNeighbors(image, X, Y, maskSize);
            List<Integer> pixelList = new ArrayList();

            for(int i = 0; i < maskSize; i++) {
                for(int j = 0; j < maskSize; j++) {
                    pixelList.add(imageSegment[i][j]);
                }
            }
            Collections.sort(pixelList);

            int sum = 0;
            int count = 0;
            int n = pixelList.size();
            for(int i = d/2; i < n - d/2; i++) {
                sum += pixelList.get(i);
                count++;
            }

            return (int) Math.round((double) sum/count);

        };

        return ImageUtil.convolution(inputImage, maskSize, alphaTrimmedMean);
    }
}
