import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SpatialFilterActionListener implements ActionListener {

    ImageHandler imageHandler;
    JFrame frame;

    public SpatialFilterActionListener(ImageHandler imageHandler, JFrame frame) {
        this.imageHandler = imageHandler;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int defaultValue = 3;
        int defaultValue2 = 2;

        JDialog popUpDialog = new JDialog(frame, "Apply Selected Filter", true);
        popUpDialog.setLayout(new GridLayout(4, 1));

        JPanel comboBoxPanel = new JPanel();
        JComboBox<String> comboBox = new JComboBox(new String[] {
                "Smoothing Filter",
                "Median Filter",
                "Sharpening Laplacian Filter",
                "High-boosting Filter"
        });
        comboBoxPanel.add(comboBox);

        JPanel textFieldPanel = new JPanel();
        JLabel text = new JLabel("Mask resolution");
        JTextField inputField = new JTextField(String.valueOf(defaultValue), 3);

        textFieldPanel.add(text);
        textFieldPanel.add(inputField);

        JPanel textFieldPanel2 = new JPanel();
        JLabel text2 = new JLabel("A value");
        JTextField inputField2 = new JTextField(String.valueOf(defaultValue2), 3);

        textFieldPanel2.add(text2);
        textFieldPanel2.add(inputField2);

        JPanel buttonPanel = new JPanel();
        JButton applyButton = new JButton("Apply");

        buttonPanel.add(applyButton);
        popUpDialog.add(comboBoxPanel);
        popUpDialog.add(textFieldPanel);
        popUpDialog.add(textFieldPanel2);
        popUpDialog.add(buttonPanel);

        textFieldPanel2.setVisible(false);

        comboBox.addActionListener(e12 -> {
            int selectedIndex = comboBox.getSelectedIndex();
            if(selectedIndex == 3) {
                textFieldPanel2.setVisible(true);
            } else {
                textFieldPanel2.setVisible(false);
            }
        });

        applyButton.addActionListener(e1 -> {
            int inputValue = Integer.parseInt(inputField.getText());
            int aValue = Integer.parseInt(inputField2.getText());
            int selectedIndex = comboBox.getSelectedIndex();
            BufferedImage inputImage = imageHandler.getCurrentImage();
            BufferedImage outputImage;

            if(selectedIndex == 0) {
                outputImage = SmoothingFilter.applyFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 1) {
                outputImage = MedianFilter.applyFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 2) {
                outputImage = SharpeningFilter.laplacianFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else {
                outputImage = SharpeningFilter.highBoostFilter(inputImage, inputValue, aValue);
                imageHandler.updateBufferedImage(outputImage);
            }

            popUpDialog.dispose();

        });

        popUpDialog.setSize(200,200);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);

    }
}
