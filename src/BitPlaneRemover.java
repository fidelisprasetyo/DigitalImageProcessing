import java.awt.image.BufferedImage;

public class BitPlaneRemover {

    public static BufferedImage apply(BufferedImage inputImage, int selectedBit) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        int bitMask = getBitMask(selectedBit);

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int gray = ImageUtil.getGrayValue(inputImage, x, y);
                int slicedGray = gray & bitMask;
                int rgb = ImageUtil.convertGrayToRGB(slicedGray);
                outputImage.setRGB(x,y,rgb);
            }
        }
        return outputImage;
    }

    private static int getBitMask(int selectedBit) {
        // if 0 -> 0xFF & ~(1) -> 0xFF & 0 -> 11111110
        // if 1 -> 0xFF & ~(10) -> 0xFF & 01 -> 11111101
        // if 2 -> 0xFF & ~(100) -> 0xFF & 011 -> 11111011
        // and so on
        return 0xFF & ~(1 << selectedBit);
    }
}
