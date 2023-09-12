import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImageHandler {
    private JLabel inputImage;
    private JLabel outputImage;

    public ImageHandler(JLabel inputImage, JLabel outputImage) {
        this.inputImage = inputImage;
        this.outputImage = outputImage;
    }

    public void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                if (image != null) {
                    inputImage.setIcon(new ImageIcon(image));
                } else {

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
