import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ImageHandler {

    public static final int LEFT_IMAGE = 0;
    public static final int RIGHT_IMAGE = 1;
    public static final int LINEAR = 0;
    public static final int BILINEAR = 1;
    private BufferedImage originalImage;
    private BufferedImage inputImage;
    private BufferedImage outputImage;
    private JLabel leftImage;
    private JLabel rightImage;

    public ImageHandler(JLabel leftImage, JLabel rightImage) {
        this.leftImage = leftImage;
        this.rightImage = rightImage;
    }

    public boolean isInputNull() {
        return (inputImage == null);
    }

    public BufferedImage getInputImage() {
        return inputImage;
    }

    public void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                originalImage = ImageIO.read(selectedFile);
                if (originalImage != null) {
                    updateDisplayedImage(originalImage, LEFT_IMAGE);
                    inputImage = originalImage;
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to open file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Exception code: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void revertToOriginal() {
        inputImage = originalImage;
        updateDisplayedImage(inputImage, RIGHT_IMAGE);
    }

    private void updateDisplayedImage(BufferedImage image, int leftOrRight) {
        if (leftOrRight == LEFT_IMAGE) {
            leftImage.setIcon(new ImageIcon(image));
        } else if (leftOrRight == RIGHT_IMAGE) {
            rightImage.setIcon(new ImageIcon(image));
        } else {
            leftImage.setIcon(new ImageIcon(image)); // default
        }
    }

    public int getGrayDepth(BufferedImage img) {
        Raster raster = img.getData();
        int grayDepth = raster.getSampleModel().getSampleSize(0);
        return grayDepth;
    }

    public void convertToPGM() {
        try {
            int grayDepth = getGrayDepth(inputImage);
            int maxGrayLevel = (int) Math.pow(2, grayDepth) - 1;
            int width = inputImage.getWidth();
            int height = inputImage.getHeight();

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PGM Files", "pgm");

            fileChooser.setDialogTitle("Save PGM File");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getPath();

                if(!filePath.endsWith(".pgm")) {
                    filePath += ".pgm";
                    selectedFile = new File(filePath);
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));

                // write the values into pgm file
                writer.write("P2\n"); // ASCII format
                writer.write("# Converted Image to PGM\n");    // comment
                writer.write(width + " " + height + "\n");  // number of rows and columns
                writer.write(maxGrayLevel + "\n");  // maximum gray level

                for(int y = 0; y < height; y++) {
                    for(int x = 0; x < width; x++) {
                        int pixel = inputImage.getRGB(x,y);
                        int g = pixel & 0xFF;
                        writer.write(g + " ");
                    }
                    writer.write('\n');
                }
                writer.close();
                JOptionPane.showMessageDialog(null, "File saved successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error! Exception code: " + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void nearestNeighbor(int newWidth, int newHeight) {
        int inputWidth = inputImage.getWidth();
        int inputHeight = inputImage.getHeight();
        outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

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
        updateDisplayedImage(outputImage, RIGHT_IMAGE);
        inputImage = outputImage; // update the input
    }

    public void imageInterpolation(int newWidth, int newHeight, int method) {
        int inputWidth = inputImage.getWidth();
        int inputHeight = inputImage.getHeight();
        outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

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
        updateDisplayedImage(outputImage, RIGHT_IMAGE);
        inputImage = outputImage; // update the input
    }

    public void changeGrayLevel(int newDepth) {
        int inputDepth = getGrayDepth(inputImage);
        int inputLevel = (int) Math.pow(2, inputDepth);
        int targetLevel = (int) Math.pow(2, newDepth);

        int ratio = inputLevel/ targetLevel;

        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int rgb = inputImage.getRGB(x,y) & 0xFF;
                int newPixelValue = (int) Math.floor((double)rgb/inputLevel * targetLevel) * ratio;
                int g = (newPixelValue << 16) | (newPixelValue << 8) | newPixelValue;
                outputImage.setRGB(x,y,g);
            }
        }
        updateDisplayedImage(outputImage, RIGHT_IMAGE);
        inputImage = outputImage; // update the input
    }
}
