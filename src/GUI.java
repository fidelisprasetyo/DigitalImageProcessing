import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    private JLabel inputImageIcon;
    private JLabel outputImageIcon;
    private JButton browseButton = new JButton("Browse");
    private JButton applyButton = new JButton("Apply");
    private JButton saveButton = new JButton("Save Output");
    static JToolBar toolBar;
    static JComboBox comboBox;
    private ImageHandler imageHandler;

    public GUI() {

        // prepare GUI frame
        JFrame frame = new JFrame("Digital Image Processing");
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // prepare toolbar and combobox
        toolBar = new JToolBar();
        comboBox = new JComboBox(new String[] {"Convert to PGM", "Scale spatial resolution", "Change level resolution"});

        // prepare image panels
        JPanel inputPanel = new JPanel();
        JPanel outputPanel = new JPanel();
        inputImageIcon = new JLabel();
        outputImageIcon = new JLabel();
        inputPanel.add(inputImageIcon);
        outputPanel.add(outputImageIcon);

        // put input and output panels into a container
        JPanel imagePanelContainer = new JPanel(new GridLayout(1,2));
        imagePanelContainer.add(inputPanel);
        imagePanelContainer.add(outputPanel);

        // place buttons and combobox into toolbar
        toolBar.add(browseButton);
        toolBar.add(applyButton);
        toolBar.add(saveButton);
        toolBar.add(comboBox);

        // place button and container on the frame
        frame.add(imagePanelContainer, BorderLayout.CENTER);
        frame.add(toolBar, BorderLayout.NORTH);

        // initiate imageHandler
        imageHandler = new ImageHandler(inputImageIcon, outputImageIcon);

        // button actionListeners
        comboBox.addActionListener(new ComboBoxActionListener());
        browseButton.addActionListener(new BrowseBtnActionListener());
        saveButton.addActionListener(new SaveBtnActionListener());
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // not used
    }

    private class ComboBoxActionListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedOption = (int) comboBox.getSelectedIndex();
            if (selectedOption == 0) {
                // TODO: convert to PGM option
                applyButton.addActionListener(new ConvertToPGMActionListener());
            } else if (selectedOption == 1) {
                // TODO: Scale spatial resolution option
            } else if (selectedOption == 2) {
                // TODO: Change level resolution option
            } else {

            }
        }
    }

    private class BrowseBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            imageHandler.openImage();
        }
    }

    private class ConvertToPGMActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            imageHandler.convertToPGM();
        }
    }

    private class SaveBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ImageHandler imageHandler = new ImageHandler(inputImageIcon, outputImageIcon);
            // TODO
        }
    }
}
