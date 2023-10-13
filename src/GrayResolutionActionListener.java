import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class GrayResolutionActionListener implements ActionListener {

    private ImageHandler imageHandler;
    private JFrame frame;

    public GrayResolutionActionListener(ImageHandler imageHandler, JFrame frame) {
        this.imageHandler = imageHandler;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int inputGrayDepth = imageHandler.getGrayDepth(imageHandler.getCurrentImage());

        // pop up dialog gui to get user's desired value
        JDialog popUpDialog = new JDialog(frame, "Change gray level resolution ", true);
        popUpDialog.setLayout(new GridLayout(3, 1));

        JPanel textPanel = new JPanel();
        JPanel textFieldPanel = new JPanel();
        JLabel textLabel = new JLabel("Gray resolution");
        JLabel bitTextLabel = new JLabel("bits");
        JTextField grayTextField = new JTextField(String.valueOf(inputGrayDepth), 2);
        textPanel.add(textLabel);
        textFieldPanel.add(grayTextField);
        textFieldPanel.add(bitTextLabel);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        popUpDialog.add(textPanel);
        popUpDialog.add(textFieldPanel);
        popUpDialog.add(buttonPanel);

        cancelButton.addActionListener(e17 -> popUpDialog.dispose());
        okButton.addActionListener(e1 -> {
            String input = grayTextField.getText();
            int newDepth = Integer.parseInt(input);

            BufferedImage outputImage;
            outputImage = GrayLevelChanger.changeGrayLevel(imageHandler.getCurrentImage(), newDepth);

            imageHandler.updateBufferedImage(outputImage);
            popUpDialog.dispose();
        });

        popUpDialog.setSize(300,200);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);
    }
}