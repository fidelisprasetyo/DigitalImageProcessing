import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.*;

public class BitPlaneCoding {

    private static int[][][] decomposeImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[][][] bitPlane = new int[8][height][width];

        for(int i = 0; i < 8; i++) {

            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    int gray = ImageUtil.getGrayValue(image, x, y);
                    int bit = ((gray & (1 << i)) >> i);
                    bitPlane[i][y][x] = bit;
                }
            }
        }
        return bitPlane;
    }

    public static void encode(BufferedImage image) throws IOException {
        int[][][] bitPlane = decomposeImage(image);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int height = image.getHeight();
        int width = image.getWidth();

        long startTime = System.nanoTime();

        for(int i = 0; i < 8; i++) {
            for(int y = 0; y < height; y++) {

                int cur = bitPlane[i][y][0];
                int count = 1;

                if(cur == 1) {
                    output.write((byte) 0);
                }

                for(int x = 1; x < width; x++) {
                    int next = bitPlane[i][y][x];
                    if(cur == next) {
                        count++;
                    } else {
                        output.write((byte) count);
                        count = 1;
                        cur = next;
                    }
                }
                output.write((byte) count);
                if(y == height - 1) continue;
                output.write((byte)254); // change row
            }
            if(i == 7) continue;
            output.write((byte)255); // change bit plane
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total encode runtime: " + totalTime);

        saveDialog(output, "bin");
    }

    public static BufferedImage decode(BufferedImage image) throws IOException {

        String filePath = loadDialog();
        byte[] imageBytes = getByteArrayFromFile(filePath);

        int height = image.getHeight();
        int width = image.getWidth();

        int[][][] bitPlane = new int[8][height][width];
        int x = 0;
        int y = 0;
        int plane = 0;
        boolean zero = true;

        long startTime = System.nanoTime();

        for(byte b : imageBytes) {
            int count = b & 0xFF;

            if(count == 254) {
                y++;
                x = 0;
                zero = true;
            } else if (count == 255) {
                plane++;
                y = 0;
                x = 0;
                zero = true;
            } else {
                for(int i = 0; i < count; i++) {
                    if (zero) {
                        bitPlane[plane][y][x] = 0;
                    } else {
                        bitPlane[plane][y][x] = 1;
                    }
                    x++;
                }
                zero = !zero;
            }
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total encode runtime: " + totalTime);

        return bitPlaneToBufferedimage(bitPlane, width, height);
    }

    private static BufferedImage bitPlaneToBufferedimage(int[][][] bitPlane, int width, int height) {
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int value = 0;
                for(int i = 0; i < 8; i++) {
                    value += (bitPlane[i][y][x] << i);
                }
                int pixel = ImageUtil.convertGrayToRGB(value);
                outputImage.setRGB(x,y, pixel);
            }
        }
        return outputImage;
    }

    private static byte[] getByteArrayFromFile(String filePath) throws IOException {
        try(InputStream inputStream = new FileInputStream(filePath)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    private static String loadDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load a file");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("bin files", "bin");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);

        if(result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    private static void saveDialog(ByteArrayOutputStream output, String format) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(format + " Files", format);

        fileChooser.setDialogTitle("Save " + format + " file");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(null);

        if(userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            if(!filePath.endsWith("." + format)) {
                filePath += "." + format;
            }

            OutputStream outputStream;
            try {
                outputStream = new FileOutputStream(filePath);
                output.writeTo(outputStream);
                JOptionPane.showMessageDialog(null, "File saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage());
            }
        }
    }

}
