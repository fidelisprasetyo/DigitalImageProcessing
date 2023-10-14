import java.awt.image.BufferedImage;

public class SpatialResolutionChanger {

    public static final int LINEAR = 0;
    public static final int BILINEAR = 1;

    // --- public methods

    public static BufferedImage nearestNeighbor(BufferedImage inputImage, int newWidth, int newHeight) {
        int inputWidth = inputImage.getWidth();
        int inputHeight = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        if (newWidth > inputWidth) {        // zoom image
            double ratio = newWidth/ (double)inputWidth;
            for(int y = 0; y < newHeight; y++) {
                for(int x = 0; x < newWidth; x++) {
                    int X = (int) Math.floor(x/ratio);
                    int Y = (int) Math.floor(y/ratio);

                    int g = inputImage.getRGB(X,Y);
                    outputImage.setRGB(x,y,g);
                }
            }
        } else {    // shrink image
            double ratio = inputWidth/ (double)newWidth;
            for(int y = 0; y < newHeight; y++) {
                for(int x = 0; x < newWidth; x++) {
                    int X = (int) Math.floor(x*ratio);
                    int Y = (int) Math.floor(y*ratio);
                    int g = inputImage.getRGB(X,Y);
                    outputImage.setRGB(x,y,g);
                }
            }
        }
        return outputImage;
    }

    public static BufferedImage imageInterpolation(BufferedImage inputImage, int newWidth, int newHeight, int method) {
        int inputWidth = inputImage.getWidth();
        int inputHeight = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        double ratio = (double)inputWidth/ newWidth;

        for(int y = 0; y < newHeight; y++) {
            for(int x = 0; x < newWidth; x++) {
                double X = x * ratio;
                double Y = y * ratio;

                int x0 = (int) Math.floor(X);
                int x1 = (int) Math.ceil(X);
                int y0 = (int) Math.floor(Y);
                int y1 = (int) Math.ceil(Y);

                // bound check
                x0 = Math.max(x0, 0);
                x1 = Math.min(x1, inputWidth - 1);
                y0 = Math.max(y0, 0);
                y1 = Math.min(y1, inputHeight -1);

                // get gray values
                int F00 = inputImage.getRGB(x0,y0) & 0xFF;
                int F01 = inputImage.getRGB(x0,y1) & 0xFF;
                int F10 = inputImage.getRGB(x1,y0) & 0xFF;
                int F11 = inputImage.getRGB(x1,y1) & 0xFF;

                double a = X - x0;
                double b = Y - y0;
                double p;

                if(method == LINEAR) {
                    p = (F00 * ( 1 - a )) + (F11 * a);  // Linear interpolation
                } else {
                    p = ((1 - a) * (1 - b) * F00) + (a * (1 - b) * F10) + (a * b * F11) + ((1 - a) * b * F01);  // Bilinear interpolation
                }

                int pixel = (int) Math.min(255, Math.max(0, p));
                int rgb = (pixel << 16) | (pixel << 8) | pixel;   // construct the rgb value

                outputImage.setRGB(x,y,rgb);
            }
        }
        return outputImage;
    }

}
