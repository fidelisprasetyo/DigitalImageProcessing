import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CompressionActionListener implements ActionListener {

    ImageHandler imageHandler;
    JFrame frame;

    public CompressionActionListener(ImageHandler imageHandler, JFrame frame) {
        this.imageHandler = imageHandler;
        this.frame = frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        JDialog popUpDialog = new JDialog(frame, "Select Compression Method", true);
        popUpDialog.setLayout(new GridLayout(2, 1));

        JPanel comboBoxPanel = new JPanel();
        JComboBox<String> comboBox = new JComboBox(new String[] {
                "Run Length Coding - Grayscale",
                "Run Length Coding - Bit Planes",
                "Huffman Coding"
        });
        comboBoxPanel.add(comboBox);

        JPanel buttonPanel = new JPanel();
        JButton encodeBtn = new JButton("Encode");
        JButton decodeBtn = new JButton("Decode");
        buttonPanel.add(encodeBtn);
        buttonPanel.add(decodeBtn);

        popUpDialog.add(comboBoxPanel);
        popUpDialog.add(buttonPanel);

        encodeBtn.addActionListener(e1 -> {
            int selectedIndex = comboBox.getSelectedIndex();
            if(selectedIndex == 0) {
                try {
                    RunLengthCoding.encode(imageHandler.getCurrentImage(), "bmp");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (selectedIndex == 1) {
                try {
                    BitPlaneCoding.encode(imageHandler.getCurrentImage());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (selectedIndex == 2) {
                System.out.println("HUFFMAN");
            }
            popUpDialog.dispose();
        });

        decodeBtn.addActionListener(e12 -> {
            int selectedIndex = comboBox.getSelectedIndex();
            if(selectedIndex == 0) {
                try {
                    imageHandler.updateBufferedImage(RunLengthCoding.decode());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else if (selectedIndex == 1) {
                try {
                    imageHandler.updateBufferedImage(BitPlaneCoding.decode(imageHandler.getCurrentImage()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            popUpDialog.dispose();
        });

        popUpDialog.setSize(300,200);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);

    }
}
