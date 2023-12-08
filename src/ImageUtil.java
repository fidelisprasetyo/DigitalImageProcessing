import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

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

    public static int[][] convertBufferedImageToMatrix(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] imageMatrix = new int[height][width];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                imageMatrix[y][x] = getGrayValue(image, x, y);
            }
        }
        return imageMatrix;
    }

    public static byte[] bufferedImageToByte(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public static BufferedImage byteToBufferedImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage output = ImageIO.read(is);
        return output;
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

    // returns image1 + image 2
    public static BufferedImage sumImages(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image2.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int gray1 = ImageUtil.getGrayValue(image1,x,y);
                int gray2 = ImageUtil.getGrayValue(image2,x,y);
                int sumGray = Math.min(255, gray1+gray2);
                int rgb = ImageUtil.convertGrayToRGB(sumGray);
                result.setRGB(x,y,rgb);
            }
        }
        return result;
    }

    // returns image1 - image 2
    public static BufferedImage subtractImages(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image2.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int gray1 = ImageUtil.getGrayValue(image1,x,y);
                int gray2 = ImageUtil.getGrayValue(image2,x,y);
                int sumGray = Math.max(0, gray1-gray2);
                int rgb = ImageUtil.convertGrayToRGB(sumGray);
                result.setRGB(x,y,rgb);
            }
        }
        return result;
    }

    // returns A * inputImage
    public static BufferedImage multiplyImageByInteger(BufferedImage inputImage, int A) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int gray = ImageUtil.getGrayValue(inputImage, x, y);
                int multipliedGray = Math.min(255, A*gray);
                int rgb = ImageUtil.convertGrayToRGB(multipliedGray);
                result.setRGB(x,y,rgb);
            }
        }
        return result;
    }

    // scale negative/ larger than 255 values
    public static int[][] normalizeGrayLevels(int[][] unscaledGray) {
        int width = unscaledGray.length;
        int height = unscaledGray[0].length;

        int[][] scaledGray = new int[width][height];

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixel = unscaledGray[x][y];
                if(pixel < min) {
                    min = pixel;
                }
                if(pixel > max) {
                    max = pixel;
                }
            }
        }
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int pixel = unscaledGray[x][y];
                int normalizedPixel = (int) Math.round((pixel - min) * (255.0/(max-min)));
                scaledGray[x][y] = normalizedPixel;
            }
        }
        return scaledGray;
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
