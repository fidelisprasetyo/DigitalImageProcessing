import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ImageHandler {
    private JLabel inputImageIcon;
    private JLabel outputImageIcon;
    private BufferedImage inputImage;
    private BufferedImage outputImage;

    public ImageHandler(JLabel inputImageIcon, JLabel outputImageIcon) {
        this.inputImageIcon = inputImageIcon;
        this.outputImageIcon = outputImageIcon;
    }

    public void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                inputImage = ImageIO.read(selectedFile);
                if (inputImage != null) {
                    inputImageIcon.setIcon(new ImageIcon(inputImage));
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to open file", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Exception code: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void convertToPGM() {
        if (inputImage != null) {
            try {
                int height = inputImage.getHeight();
                int width = inputImage.getWidth();

                File pgmImg = new File("./output/test.pgm");
                BufferedWriter writer = new BufferedWriter(new FileWriter(pgmImg));

                writer.write("P2\n"); // ASCII format
                writer.write("# Converted Image to PGM\n");    // comment
                writer.write(width + " " + height + "\n");  // number of rows and columns
                writer.write("255\n");

                for(int y = 0; y < height; y++) {
                    for(int x = 0; x < width; x++) {
                        int pixel = inputImage.getRGB(x,y);
                        int g = pixel & 0xFF;
                        writer.write(g + " ");
                    }
                    writer.write('\n');
                }
                writer.close();
                JOptionPane.showMessageDialog(null, "File saved to /output directory", "Success!", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error! Exception code: " + e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No input file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveOutputImage() {

    }
}
