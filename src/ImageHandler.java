import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
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

    private void updateDisplayedImage(BufferedImage image, int leftOrRight) {
        if (leftOrRight == RIGHT_IMAGE) {
            rightImage.setIcon(new ImageIcon(image));
        } else {
            leftImage.setIcon(new ImageIcon(image)); // default
        }
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

    public void saveOutputImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg", "gif");

        fileChooser.setDialogTitle("Save Output Image File");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(null);
        if(userSelection == JFileChooser.APPROVE_OPTION) {
            File outputFile = fileChooser.getSelectedFile();
            String filePath = outputFile.getPath();

            if(!filePath.endsWith(".png")) {
                filePath += ".png";
                outputFile = new File(filePath);
            }

            try {
                ImageIO.write(outputImage, "png", outputFile);
                JOptionPane.showMessageDialog(null, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Exception code: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isInputNull() {
        return (inputImage == null);
    }

    public BufferedImage getInputImage() {
        return inputImage;
    }

    public int getGrayDepth(BufferedImage img) {
        Raster raster = img.getData();
        int grayDepth = raster.getSampleModel().getSampleSize(0);
        return grayDepth;
    }

    public void convertToPGM() {
        PGMConverter pgmConverter = new PGMConverter();
        pgmConverter.convertToPGM(inputImage);
    }

    public void nearestNeighbor(int newWidth, int newHeight) {
        SpatialResolutionChanger spatialResolutionChanger = new SpatialResolutionChanger(inputImage, newWidth, newHeight);
        outputImage = spatialResolutionChanger.nearestNeighbor();

        updateDisplayedImage(outputImage, RIGHT_IMAGE);
        inputImage = outputImage; // update the input
    }

    public void imageInterpolation(int newWidth, int newHeight, int method) {
        SpatialResolutionChanger spatialResolutionChanger = new SpatialResolutionChanger(inputImage, newWidth, newHeight);
        outputImage = spatialResolutionChanger.imageInterpolation(method);

        updateDisplayedImage(outputImage, RIGHT_IMAGE);
        inputImage = outputImage; // update the input
    }

    public void changeGrayLevel(int newDepth) {
        GrayLevelChanger grayLevelChanger = new GrayLevelChanger(inputImage, newDepth);
        outputImage = grayLevelChanger.run();

        updateDisplayedImage(outputImage, RIGHT_IMAGE);
        inputImage = outputImage; // update the input
    }
}
