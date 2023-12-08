import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class RunLengthCoding {

    public static void encode(BufferedImage image, String format) throws IOException {
        byte[] imageBytes = ImageUtil.bufferedImageToByte(image, format);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long startTime = System.nanoTime();
        byte cur = imageBytes[0];
        int count = 1;
        for(int i = 1; i < imageBytes.length; i++) {
            byte next = imageBytes[i];
            if(next == cur) {
                count++;
            } else if (count == 255) {
                output.write((byte)count);
                output.write(cur);
                count = 1;
                cur = next;
            }
            else {
                output.write((byte)count);
                output.write(cur);
                count = 1;
                cur = next;
            }
        }
        output.write((byte)count);
        output.write(cur);

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total encode runtime: " + totalTime);

        saveDialog(output, "bin");
        output.close();


    }

    public static BufferedImage decode() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String filePath = loadDialog();

        long startTime = System.nanoTime();
        byte[] imageBytes = getByteArrayFromFile(filePath);
        for(int i = 0; i < imageBytes.length; i+=2) {
            int count = imageBytes[i] & 0xFF;
            byte value = imageBytes[i+1];
            for(int j = 0; j < count; j++) {
                output.write(value);
            }
        }
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Total decode runtime: " + totalTime);

        saveDialog(output, "bmp");
        output.close();

        byte[] savedImage = output.toByteArray();
        return ImageUtil.byteToBufferedImage(savedImage);
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
