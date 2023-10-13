import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class HistogramEqualizationActionListener implements ActionListener {

    private final ImageHandler imageHandler;
    private final JFrame frame;

    public HistogramEqualizationActionListener(ImageHandler imageHandler, JFrame frame) {
        this.imageHandler = imageHandler;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int inputVal = 3;

        JDialog popUpDialog = new JDialog(frame, "Equalize Histogram", true);
        popUpDialog.setLayout(new GridLayout(1, 1));

        // upper panel
        JPanel popUpPanel = new JPanel();
        popUpPanel.setLayout(new GridLayout(2,1));

        JRadioButton option1Btn = new JRadioButton("Global");
        JRadioButton option2Btn = new JRadioButton("Local");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(option1Btn);
        buttonGroup.add(option2Btn);
        option1Btn.setSelected(true);

        JPanel textFieldPanel = new JPanel();
        JTextField input = new JTextField(String.valueOf(inputVal), 3);
        JLabel text = new JLabel("px");
        textFieldPanel.add(input);
        textFieldPanel.add(text);

        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.add(option1Btn);
        radioButtonPanel.add(option2Btn);
        radioButtonPanel.add(textFieldPanel);

        JPanel buttonPanel = new JPanel();
        JButton executeBtn = new JButton("Apply");
        buttonPanel.add(executeBtn);

        popUpPanel.add(radioButtonPanel);
        popUpPanel.add(buttonPanel);

        popUpDialog.add(popUpPanel);


        executeBtn.addActionListener(e1 -> {
            BufferedImage outputImage;
            if(option1Btn.isSelected()) {
                outputImage = HistogramEqualizer.globalEqualization(imageHandler.getCurrentImage());
                imageHandler.updateBufferedImage(outputImage);
            } else {
                int inputValue = Integer.parseInt(input.getText());
                outputImage = HistogramEqualizer.localEqualization(imageHandler.getCurrentImage(), inputValue);
                imageHandler.updateBufferedImage(outputImage);
            }
            popUpDialog.dispose();
        });

        popUpDialog.setSize(300,200);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);
    }
}