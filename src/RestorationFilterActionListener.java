import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class RestorationFilterActionListener implements ActionListener {

    ImageHandler imageHandler;
    JFrame frame;

    public RestorationFilterActionListener(ImageHandler imageHandler, JFrame frame) {
        this.imageHandler = imageHandler;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int defaultValue = 3;

        JDialog popUpDialog = new JDialog(frame, "Apply Selected Filter", true);
        popUpDialog.setLayout(new GridLayout(4, 1));

        JPanel comboBoxPanel = new JPanel();
        JComboBox<String> comboBox = new JComboBox(new String[] {
                "Arithmetic mean filter",
                "Geometric mean filter",
                "Harmonic mean filter",
                "Contraharmonic mean filter",
                "Max filter",
                "Min filter",
                "Midpoint filter",
                "Alpha-trimmed mean filter"
        });
        comboBoxPanel.add(comboBox);

        JPanel textFieldPanel = new JPanel();
        JLabel text = new JLabel("Mask resolution");
        JTextField inputField = new JTextField(String.valueOf(defaultValue), 3);

        JPanel textFieldPanel2 = new JPanel();
        JLabel text2 = new JLabel();
        JTextField inputField2 = new JTextField("0", 3);

        textFieldPanel.add(text);
        textFieldPanel.add(inputField);
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
            if (selectedIndex == 3) {
                text2.setText("Order");
                textFieldPanel2.setVisible(true);
            } else if (selectedIndex == 7) {
                text2.setText("d value");
                textFieldPanel2.setVisible(true);
            } else {
                textFieldPanel2.setVisible(false);
            }
        });

        applyButton.addActionListener(e1 -> {
            int inputValue = Integer.parseInt(inputField.getText());
            int selectedIndex = comboBox.getSelectedIndex();

            BufferedImage inputImage = imageHandler.getCurrentImage();
            BufferedImage outputImage;

            if(selectedIndex == 0) {
                outputImage = SmoothingFilter.applyFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 1) {
                outputImage = GeometricMeanFilter.applyFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 2) {
                outputImage = HarmonicMeanFilter.applyFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 3) {
                double orderValue = Double.parseDouble(inputField2.getText());
                outputImage = ContraharmonicMeanFilter.applyFilter(inputImage, inputValue, orderValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 4) {
                outputImage = MinMaxFilter.applyMaxFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 5) {
                outputImage = MinMaxFilter.applyMinFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 6) {
                outputImage = MinMaxFilter.applyMidpointFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 7) {
                int dValue = Integer.parseInt(inputField2.getText());
                outputImage = AlphaTrimmedMeanFilter.applyFilter(inputImage, inputValue, dValue);
                imageHandler.updateBufferedImage(outputImage);
            }


            popUpDialog.dispose();
        });

        popUpDialog.setSize(200,200);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);
    }
}
