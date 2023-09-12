import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    private JLabel inputImage;
    private JLabel outputImage;
    private JButton browseButton = new JButton("Browse");
    private JButton applyButton = new JButton("Apply");

    public GUI() {

        // prepare GUI frame
        JFrame frame = new JFrame("Digital Image Processing");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // prepare image panels
        JLabel inputStaticText = new JLabel("Input Image");
        JLabel outputStaticText = new JLabel("Output Image");
        inputStaticText.setHorizontalAlignment(JLabel.CENTER);
        outputStaticText.setHorizontalAlignment(JLabel.CENTER);
        JPanel inputPanel = new JPanel();
        JPanel outputPanel = new JPanel();
        inputImage = new JLabel();
        outputImage = new JLabel();
        inputPanel.add(inputImage);
        outputPanel.add(outputImage);

        // put input and output panels into a container
        JPanel imagePanelContainer = new JPanel(new GridLayout(2,2));
        imagePanelContainer.add(inputStaticText);
        imagePanelContainer.add(outputStaticText);
        imagePanelContainer.add(inputPanel);
        imagePanelContainer.add(outputPanel);

        // button container (TEMP)
        JPanel buttonContainer = new JPanel(new GridLayout(1,2));
        buttonContainer.add(browseButton);
        buttonContainer.add(applyButton);

        // place button and container on the frame
        frame.add(imagePanelContainer, BorderLayout.CENTER);
        frame.add(buttonContainer, BorderLayout.SOUTH);

        // button actionListeners
        browseButton.addActionListener(new BrowseBtnActionListener());
        applyButton.addActionListener(new ApplyBtnActionListener());
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // not used
    }

    private class BrowseBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ImageHandler imageHandler = new ImageHandler(inputImage, outputImage);
            imageHandler.openImage();
        }
    }

    private class ApplyBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ImageHandler imageHandler = new ImageHandler(inputImage, outputImage);
            //ImageHandler.convertToPGM(); //TODO
        }
    }
}
