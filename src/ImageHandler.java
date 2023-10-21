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
    private BufferedImage originalImage;
    private BufferedImage currentImage;
    private final JLabel leftImage;
    private final JLabel rightImage;

    public ImageHandler(JLabel leftImage, JLabel rightImage) {
        this.leftImage = leftImage;
        this.rightImage = rightImage;
    }

    public void updateBufferedImage(BufferedImage outputImage) {
        updateDisplayedImage(outputImage, RIGHT_IMAGE);
        currentImage = outputImage;
    }

    private void updateDisplayedImage(BufferedImage image, int leftOrRight) {
        if (leftOrRight == RIGHT_IMAGE) {
            rightImage.setIcon(new ImageIcon(image));
        } else {
            leftImage.setIcon(new ImageIcon(image)); // default
        }
    }

    private File lastOpenedDirectory;

    public void openImage() {
        JFileChooser fileChooser = new JFileChooser();

        if (lastOpenedDirectory != null) {
            fileChooser.setCurrentDirectory(lastOpenedDirectory);
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp", "tif");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            lastOpenedDirectory = selectedFile.getParentFile();
            try {
                originalImage = ImageIO.read(selectedFile);
                if (originalImage != null) {
                    updateDisplayedImage(originalImage, LEFT_IMAGE);
                    currentImage = originalImage;
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
        currentImage = originalImage;
        updateDisplayedImage(currentImage, RIGHT_IMAGE);
    }

    private File lastSavedDirectory;

    public void saveOutputImage() {
        JFileChooser fileChooser = new JFileChooser();

        if (lastSavedDirectory != null) {
            fileChooser.setCurrentDirectory(lastSavedDirectory);
        }

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg", "gif");

        fileChooser.setDialogTitle("Save Output Image File");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(null);
        if(userSelection == JFileChooser.APPROVE_OPTION) {
            File outputFile = fileChooser.getSelectedFile();
            String filePath = outputFile.getPath();

            lastSavedDirectory = outputFile.getParentFile();

            if(!filePath.endsWith(".png")) {
                filePath += ".png";
                outputFile = new File(filePath);
            }
            try {
                ImageIO.write(currentImage, "png", outputFile);
                JOptionPane.showMessageDialog(null, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Exception code: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean isInputNull() {
        return (currentImage == null);
    }

    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    public int getGrayDepth(BufferedImage img) {
        Raster raster = img.getData();
        int grayDepth = raster.getSampleModel().getSampleSize(0);
        return grayDepth;
    }

}
