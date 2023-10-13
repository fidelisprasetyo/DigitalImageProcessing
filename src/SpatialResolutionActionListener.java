import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class SpatialResolutionActionListener implements ActionListener {

    private ImageHandler imageHandler;
    private JFrame frame;

    public SpatialResolutionActionListener(ImageHandler imageHandler, JFrame frame) {
        this.imageHandler = imageHandler;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int inputWidth = imageHandler.getCurrentImage().getWidth();
        int inputHeight = imageHandler.getCurrentImage().getHeight();

        // pop up dialog gui
        JDialog popUpDialog = new JDialog(frame, "Change spatial resolution", true);
        popUpDialog.setLayout(new GridLayout(4, 1));

        JPanel textPanel = new JPanel();
        JLabel widthText = new JLabel("Width");
        JLabel heightText = new JLabel("Height");
        textPanel.add(widthText);
        textPanel.add(heightText);

        JPanel spatResPanel = new JPanel();
        JTextField widthTextField = new JTextField(String.valueOf(inputWidth), 4);
        JTextField heightTextField = new JTextField(String.valueOf(inputHeight), 4);
        spatResPanel.add(widthTextField);
        spatResPanel.add(heightTextField);

        JPanel comboBoxPanel = new JPanel();
        String[] options = {"Select an algorithm", "Nearest Neighbor", "Linear", "Bilinear"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBoxPanel.add(comboBox);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        popUpDialog.add(textPanel);
        popUpDialog.add(spatResPanel);
        popUpDialog.add(comboBoxPanel);
        popUpDialog.add(buttonPanel);

        cancelButton.addActionListener(e17 -> popUpDialog.dispose());

        // text field to get new width from user, automatically scales the height value by pressing enter
        widthTextField.addActionListener(e16 -> {
            String widthInput = widthTextField.getText();
            try {
                int newWidth = Integer.parseInt(widthInput);
                int newHeight = inputHeight * newWidth / inputWidth;
                heightTextField.setText(Integer.toString(newHeight));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter an integer!", "Error", JOptionPane.ERROR_MESSAGE);
                widthTextField.setText(Integer.toString(inputWidth));
            }
        });

        // text field to get new height from user, automatically scales the width value by pressing enter
        heightTextField.addActionListener(e15 -> {
            String heightInput = heightTextField.getText();
            try {
                int newHeight = Integer.parseInt(heightInput);
                int newWidth = inputWidth * newHeight / inputHeight;
                widthTextField.setText(Integer.toString(newWidth));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter an integer!", "Error", JOptionPane.ERROR_MESSAGE);
                heightTextField.setText(Integer.toString(inputHeight));
            }
        });

        // combo box that lists scaling methods (linear/ interpolation)
        comboBox.addActionListener(e14 -> {
            int srOption = comboBox.getSelectedIndex();

            ActionListener[] listeners = okButton.getActionListeners();
            for(ActionListener listener : listeners) {
                okButton.removeActionListener(listener);
            }

            if (srOption == 0) {
                // do nothing
            }
            else if (srOption == 1) {        // Nearest Neighbor
                okButton.addActionListener(e13 -> {
                    BufferedImage outputImage;
                    int newWidth = Integer.parseInt(widthTextField.getText());
                    int newHeight = Integer.parseInt(heightTextField.getText());

                    outputImage = SpatialResolutionChanger.nearestNeighbor(imageHandler.getCurrentImage(), newWidth, newHeight);
                    imageHandler.updateBufferedImage(outputImage);
                    popUpDialog.dispose();
                });
            } else if (srOption == 2) {     // Linear Interpolation
                okButton.addActionListener(e12 -> {
                    BufferedImage outputImage;
                    int newWidth = Integer.parseInt(widthTextField.getText());
                    int newHeight = Integer.parseInt(heightTextField.getText());

                    outputImage = SpatialResolutionChanger.imageInterpolation(imageHandler.getCurrentImage(), newWidth, newHeight, 0);
                    imageHandler.updateBufferedImage(outputImage);
                    popUpDialog.dispose();
                });
            } else {            // Bilinear Interpolation
                okButton.addActionListener(e1 -> {
                    BufferedImage outputImage;
                    int newWidth = Integer.parseInt(widthTextField.getText());
                    int newHeight = Integer.parseInt(heightTextField.getText());

                    outputImage = SpatialResolutionChanger.imageInterpolation(imageHandler.getCurrentImage(), newWidth, newHeight, 1);
                    imageHandler.updateBufferedImage(outputImage);
                    popUpDialog.dispose();
                });
            }
        });
        popUpDialog.setSize(300,200);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);
    }
}