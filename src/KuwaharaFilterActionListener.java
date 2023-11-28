import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class KuwaharaFilterActionListener implements ActionListener {

    ImageHandler imageHandler;
    JFrame frame;

    public KuwaharaFilterActionListener(ImageHandler imageHandler, JFrame frame) {
        this.imageHandler = imageHandler;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int defaultSectors = 8;
        double defaultSigma = 3.0;
        int defaultQ = 3;

        JDialog popUpDialog = new JDialog(frame, "Apply Kuwahara Filter", true);
        popUpDialog.setLayout(new GridLayout(5, 1));

        JPanel comboBoxPanel = new JPanel();
        JComboBox<String> comboBox = new JComboBox(new String[] {
                "Conventional",
                "Proposed"
        });
        comboBoxPanel.add(comboBox);

        JPanel textFieldPanel = new JPanel();
        JLabel textSectors = new JLabel("Kernel Size");
        JTextField textField = new JTextField(String.valueOf(defaultSectors), 3);
        textFieldPanel.add(textSectors);
        textFieldPanel.add(textField);

        JPanel textFieldPanel2 = new JPanel();
        JLabel textSigma = new JLabel("Sigma");
        JTextField textField2 = new JTextField(String.valueOf(defaultSigma), 3);
        textFieldPanel2.add(textSigma);
        textFieldPanel2.add(textField2);

        JPanel textFieldPanel3 = new JPanel();
        JLabel textQ = new JLabel("q");
        JTextField textField3 = new JTextField(String.valueOf(defaultQ), 3);
        textFieldPanel2.add(textQ);
        textFieldPanel2.add(textField3);

        JPanel buttonPanel = new JPanel();
        JButton applyButton = new JButton("Apply");
        buttonPanel.add(applyButton);

        popUpDialog.add(comboBoxPanel);
        popUpDialog.add(textFieldPanel);
        popUpDialog.add(textFieldPanel2);
        popUpDialog.add(textFieldPanel3);
        popUpDialog.add(buttonPanel);

        textFieldPanel2.setVisible(false);
        textFieldPanel3.setVisible(false);

        comboBox.addActionListener(e1 -> {
            int selectedIndex = comboBox.getSelectedIndex();
            if(selectedIndex == 1) {
                textFieldPanel2.setVisible(true);
                textFieldPanel3.setVisible(true);
                textSectors.setText("Sectors");
            } else {
                textFieldPanel2.setVisible(false);
                textFieldPanel3.setVisible(true);
                textSectors.setText("Kernel Size");
            }

        });

        applyButton.addActionListener(e12 -> {
            BufferedImage inputImage = imageHandler.getCurrentImage();
            BufferedImage outputImage;

            int selectedIndex = comboBox.getSelectedIndex();
            int kernelSize = Integer.parseInt(textField.getText());
            double sigma = Double.parseDouble(textField2.getText());
            int q = Integer.parseInt(textField3.getText());

            if(selectedIndex == 0) {
                outputImage = KuwaharaFilter.applyFilter(inputImage, kernelSize);
                imageHandler.updateBufferedImage(outputImage);
            } else if (selectedIndex == 1) {
                outputImage = KuwaharaFilter.proposedKuwahara(inputImage, sigma, kernelSize, q);
                imageHandler.updateBufferedImage(outputImage);
            }
            popUpDialog.dispose();

        });

        popUpDialog.setSize(200,200);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);
    }
}
