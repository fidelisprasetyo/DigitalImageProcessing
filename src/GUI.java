import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    private final JButton executeButton = new JButton("Execute");
    static JToolBar toolBar;
    static JComboBox comboBox;
    private final ImageHandler imageHandler;
    JFrame frame = new JFrame("Digital Image Processing");

    public GUI() {

        // prepare GUI frame
        frame.setSize(1000, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // prepare toolbar and combobox
        toolBar = new JToolBar();
        comboBox = new JComboBox(new String[] {
                "Select an action",
                "Convert to PGM",
                "Change spatial resolution",
                "Change gray level resolution",
                "Histogram equalization",
                "Image enhancement spatial filters",
                "Remove bit-planes",
                "Image restoration spatial filters",
                "Kuwahara Filter"});

        // prepare image panels
        JLabel leftImage = new JLabel();
        JLabel rightImage = new JLabel();
        JScrollPane inputScrollPane = new JScrollPane(leftImage);
        JScrollPane outputScrollPane = new JScrollPane(rightImage);

        // put input and output panels into a container
        JPanel imagePanelContainer = new JPanel(new GridLayout(1,2));
        imagePanelContainer.add(inputScrollPane);
        imagePanelContainer.add(outputScrollPane);

        // place buttons and combobox into toolbar
        JButton browseButton = new JButton("Browse");
        toolBar.add(browseButton);
        toolBar.add(executeButton);
        JButton saveButton = new JButton("Save Output");
        toolBar.add(saveButton);
        toolBar.add(comboBox);
        JButton undoButton = new JButton("Undo All");
        toolBar.add(undoButton);

        // place gui components
        frame.add(imagePanelContainer, BorderLayout.CENTER);
        frame.add(toolBar, BorderLayout.NORTH);
        //frame.add(logPanel, BorderLayout.SOUTH);

        // initiate imageHandler
        imageHandler = new ImageHandler(leftImage, rightImage);

        // button actionListeners
        comboBox.addActionListener(new ComboBoxActionListener());
        browseButton.addActionListener(new BrowseFileActionListener());
        saveButton.addActionListener(new SaveBtnActionListener());
        undoButton.addActionListener(new UndoBtnActionListener());
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // not used
    }

    // main combo box that provides all image processing options
    private class ComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedOption = (int) comboBox.getSelectedIndex();
            ActionListener[] listeners = executeButton.getActionListeners();
            for(ActionListener listener : listeners) {
                executeButton.removeActionListener(listener);
            }
            if(imageHandler.isInputNull()) { //DEBUG MODEDEDEDE
                executeButton.addActionListener(e1 -> JOptionPane.showMessageDialog(null, "No input file!", "Error", JOptionPane.ERROR_MESSAGE));
            } else {
                if (selectedOption == 0) {  // nothing

                } else if (selectedOption == 1) {
                    executeButton.addActionListener(new ConvertToPGMActionListener(imageHandler));  // convert to PGM
                } else if (selectedOption == 2) {
                    executeButton.addActionListener(new SpatialResolutionActionListener(imageHandler, frame)); // change spatial resolution
                } else if (selectedOption == 3) {
                    executeButton.addActionListener(new GrayResolutionActionListener(imageHandler, frame));    // change gray level resolution
                } else if (selectedOption == 4) {
                    executeButton.addActionListener(new HistogramEqualizationActionListener(imageHandler, frame));
                } else if (selectedOption == 5) {
                    executeButton.addActionListener(new SpatialFilterActionListener(imageHandler, frame));
                } else if (selectedOption == 6) {
                    executeButton.addActionListener(new BitPlanesActionListener(imageHandler, frame));
                } else if (selectedOption == 7) {
                    executeButton.addActionListener(new RestorationFilterActionListener(imageHandler, frame));
                } else if (selectedOption == 8) {
                    executeButton.addActionListener(new KuwaharaFilterActionListener(imageHandler, frame));
                }
            }
        }
    }

    // undo all button which reverts right image to its original file
    private class UndoBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            imageHandler.revertToOriginal();
        }
    }

    public class BrowseFileActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            imageHandler.openImage();
            // trigger comboBox to update its if state
            ActionEvent comboBoxEvent = new ActionEvent(comboBox, ActionEvent.ACTION_PERFORMED, "");
            for(ActionListener listener : comboBox.getActionListeners()) {
                listener.actionPerformed(comboBoxEvent);
            }
        }
    }

    private class SaveBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            imageHandler.saveOutputImage();
        }
    }
}
