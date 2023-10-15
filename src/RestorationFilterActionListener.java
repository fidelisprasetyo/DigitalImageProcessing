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
        popUpDialog.setLayout(new GridLayout(3, 1));

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

        textFieldPanel.add(text);
        textFieldPanel.add(inputField);

        JPanel buttonPanel = new JPanel();
        JButton applyButton = new JButton("Apply");
        buttonPanel.add(applyButton);

        popUpDialog.add(comboBoxPanel);
        popUpDialog.add(textFieldPanel);
        popUpDialog.add(buttonPanel);

        applyButton.addActionListener(e1 -> {
            int inputValue = Integer.parseInt(inputField.getText());
            int selectedIndex = comboBox.getSelectedIndex();

            BufferedImage inputImage = imageHandler.getCurrentImage();
            BufferedImage outputImage;

            if(selectedIndex == 0) {
                outputImage = SmoothingFilter.applyFilter(inputImage, inputValue);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 1) {
                // TODO
                //imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 2) {
                // TODO
                //imageHandler.updateBufferedImage(outputImage);
            } else {
                // TODO
                //imageHandler.updateBufferedImage(outputImage);
            }

            popUpDialog.dispose();
        });

        popUpDialog.setSize(200,150);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);
    }
}
