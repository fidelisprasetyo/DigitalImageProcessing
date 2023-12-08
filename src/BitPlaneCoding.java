import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BitPlaneCoding {

    public static void encode(BufferedImage image, String format) throws IOException {
        long startTime = System.nanoTime();
        List<byte[]> bitPlanes = bufferedImageToBitPlanes(image);
        ByteArrayOutputStream output = new ByteArrayOutputStream();


        for (int i = 0; i < 8; i++) {
            encodeBitPlane(bitPlanes.get(i), output);
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total encode runtime: " + totalTime);

        saveDialog(output, "bin");
        output.close();

    }

    public static BufferedImage decode() throws IOException {
        List<byte[]> bitPlanes = new ArrayList<>();


        for (int i = 0; i < 8; i++) {
            String filePath = loadDialog();
            bitPlanes.add(getByteArrayFromFile(filePath));
        }

        long startTime = System.nanoTime();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (int i = 0; i < bitPlanes.get(0).length; i++) {
            byte[] pixel = new byte[8];
            for (int j = 0; j < 8; j++) {
                pixel[j] = bitPlanes.get(j)[i];
            }
            output.write(getByteFromBitPlane(pixel));
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total encode runtime: " + totalTime);

        saveDialog(output, "bmp");
        output.close();

        byte[] savedImage = output.toByteArray();
        return ImageUtil.byteToBufferedImage(savedImage);
    }

    public static List<byte[]> bufferedImageToBitPlanes(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);

        List<byte[]> bitPlanes = new ArrayList<>();
        for (int i = 7; i >= 0; i--) {
            byte[] bitPlane = new byte[width * height];

            for (int j = 0; j < pixels.length; j++) {
                int pixelValue = (pixels[j] >> i) & 0x01; // Extract the i-th bit
                bitPlane[j] = (byte) pixelValue;
            }
            bitPlanes.add(bitPlane);
        }

        return bitPlanes;
    }

    private static void encodeBitPlane(byte[] bitPlane, ByteArrayOutputStream output) throws IOException {
        byte cur = bitPlane[0];
        int count = 1;

        for (int i = 1; i < bitPlane.length; i++) {
            byte next = bitPlane[i];
            if (next == cur) {
                count++;
            } else if (count == 255) {
                output.write((byte) count);
                output.write(cur);
                count = 1;
                cur = next;
            } else {
                output.write((byte) count);
                output.write(cur);
                count = 1;
                cur = next;
            }
        }
        output.write((byte) count);
        output.write(cur);
    }

    private static byte[] getByteArrayFromFile(String filePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    private static String loadDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load a file");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Bit Plane files", "bin");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
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

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            if (!filePath.endsWith("." + format)) {
                filePath += "." + format;
            }

            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                output.writeTo(outputStream);
                JOptionPane.showMessageDialog(null, "File saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage());
            }
        }
    }

    private static byte getByteFromBitPlane(byte[] bits) {
        byte result = 0;
        for (int i = 0; i < 8; i++) {
            result |= (bits[i] & 1) << (7 - i);
        }
        return result;
    }
}