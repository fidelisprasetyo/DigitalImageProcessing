
import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            File imgPath = new File("./res/lena.png");
            BufferedImage img = ImageIO.read(imgPath);

            if (img != null) {
                int width = img.getWidth();
                int height = img.getHeight();

                File pgmImg = new File("output.pgm");
                BufferedWriter writer = new BufferedWriter(new FileWriter(pgmImg));

                writer.write("P2\n"); // ASCII format
                writer.write("# Test Png to PGM\n");    // comment
                writer.write(width + " " + height + "\n");  // number of rows and columns
                writer.write("255\n");

                for(int y = 0; y < height; y++) {
                    for(int x = 0; x < width; x++) {
                        int pixel = img.getRGB(x,y);
                        int g = pixel & 0xFF;
                        writer.write(g + " ");
                    }
                    writer.write('\n');
                }
                writer.close();

            } else {
                System.out.println("Failed loading image file!");
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


