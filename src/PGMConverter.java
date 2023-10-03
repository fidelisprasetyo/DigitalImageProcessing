import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PGMConverter {

    private int getGrayDepth(BufferedImage img) {
        Raster raster = img.getData();
        int grayDepth = raster.getSampleModel().getSampleSize(0);
        return grayDepth;
    }

    public void convertToPGM(BufferedImage image) {
        try {
            int grayDepth = getGrayDepth(image);
            int maxGrayLevel = (int) Math.pow(2, grayDepth) - 1;
            int width = image.getWidth();
            int height = image.getHeight();

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
                        int pixel = image.getRGB(x,y);
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



    public PGMConverter() {

    }
}
