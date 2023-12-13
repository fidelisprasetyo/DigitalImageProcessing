import java.awt.image.BufferedImage;

// some useful utility methods

public class ImageUtil {

    // return grayscale value from rgb bits
    public static int getGrayValue(BufferedImage image, int x, int y) {
        return image.getRGB(x,y) & 0xFF;
    }

    // convert gray value into rgb
    public static int convertGrayToRGB(int gray) {
        return (gray << 16) | (gray << 8) | gray;
    }

    public static int getRedValue(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x,y);
        return (rgb >> 16) & 0xFF;
    }

    public static int getGreenValue(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x,y);
        return (rgb >> 8) & 0xFF;
    }

    public static int getBlueValue(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x,y);
        return rgb & 0xFF;
    }

    public static int convertColorValuesToRGB(int r, int g, int b) {
        return (r << 16) | (g << 8) | b;
    }

    // fill outside borders with neighboring pixels (clamping)
    public static BufferedImage paddedImage(BufferedImage inputImage, int maskSize) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int newWidth = width + 2 * (maskSize / 2);
        int newHeight = height + 2 * (maskSize / 2);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < newHeight; y++) {
            for(int x = 0; x < newWidth; x++) {
                int originalX = Math.min(Math.max(x - (maskSize / 2), 0), width - 1);
                int originalY = Math.min(Math.max(y - (maskSize / 2), 0), height - 1);

                int rgb = inputImage.getRGB(originalX, originalY);
                newImage.setRGB(x,y,rgb);
            }
        }
        return newImage;
    }

    // get neighboring pixels according to maskSize
    public static int[][] extractNeighbors(BufferedImage paddedImage, int x, int y, int maskSize, int color) {
        int[][] pixels = new int[maskSize][maskSize];
        int padding = maskSize/2;

        for(int i = 0; i < maskSize; i++) {
            for(int j = 0; j < maskSize; j++) {
                if(color == 1) {
                    pixels[i][j] = getRedValue(paddedImage, x - padding + j, y - padding + i);
                } else if (color == 2) {
                    pixels[i][j] = getGreenValue(paddedImage, x - padding + j, y - padding + i);
                } else if (color == 3) {
                    pixels[i][j] = getBlueValue(paddedImage, x - padding + j, y - padding + i);
                } else {
                    pixels[i][j] = getGrayValue(paddedImage, x - padding + j, y - padding + i);
                }
            }
        }
        return pixels;
    }

    // convert 2d matrix of rgb values into BufferedImage
    public static BufferedImage writeRGBIntoBufferedImage(int[][] image) {
        int width = image.length;
        int height = image[0].length;

        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                outputImage.setRGB(x,y,image[x][y]);
            }
        }
        return outputImage;
    }

    // compute the pixel by doing mathematical operation on the kernel window (neighboring pixels)
    // PixelProcessor defines the desired calculation
    // is defined using lambda expression in Filter's class
    public static BufferedImage convolution(BufferedImage inputImage, int maskSize, PixelProcessor pixelProcessor) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        int[][] image = new int[width][height];

        int padding = maskSize/2;

        BufferedImage outputImage;
        BufferedImage paddedImage = ImageUtil.paddedImage(inputImage, maskSize);

        for(int y = padding; y < height + padding; y++) {
            for(int x = padding; x < width + padding; x++) {
                int newPixel = pixelProcessor.computeCenterPixel(paddedImage, x, y);
                image[x-padding][y-padding] = newPixel;
            }
        }

        outputImage = ImageUtil.writeRGBIntoBufferedImage(image);
        return outputImage;
    }


    // code snippet by Mario Dekena
    // url: https://stackoverflow.com/questions/36156543/reliable-way-to-check-if-image-is-grey-scale
    public static boolean isGreyscale(BufferedImage image)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        int pixel,red, green, blue;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                pixel = image.getRGB(i, j);
                red = (pixel >> 16) & 0xff;
                green = (pixel >> 8) & 0xff;
                blue = (pixel) & 0xff;
                if (red != green || green != blue ) return false;
            }
        }
        return true;
    }
}
