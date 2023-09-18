import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    private JButton browseButton = new JButton("Browse");
    private JButton executeButton = new JButton("Execute");
    private JButton saveButton = new JButton("Save Output");
    private JButton undoButton = new JButton("Undo All");
    static JToolBar toolBar;
    static JComboBox comboBox;
    private ImageHandler imageHandler;
    JFrame frame = new JFrame("Digital Image Processing");

    public GUI() {

        // prepare GUI frame
        frame.setSize(1600, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // prepare toolbar and combobox
        toolBar = new JToolBar();
        comboBox = new JComboBox(new String[] {"Select an action", "Convert to PGM", "Change spatial resolution", "Change gray level resolution"});

        // prepare image panels
        JLabel leftImage = new JLabel();
        JLabel rightImage = new JLabel();
        JScrollPane inputScrollPane = new JScrollPane(leftImage);
        JScrollPane outputScrollPane = new JScrollPane(rightImage);

        // put input and output panels into a container
        JPanel imagePanelContainer = new JPanel(new GridLayout(1,2));
        imagePanelContainer.add(inputScrollPane);
        imagePanelContainer.add(outputScrollPane);

//        // Log bar
//        JPanel logPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JLabel logText = new JLabel();
//        logPanel.add(logText);

        // place buttons and combobox into toolbar
        toolBar.add(browseButton);
        toolBar.add(executeButton);
        toolBar.add(saveButton);
        toolBar.add(comboBox);
        toolBar.add(undoButton);

        // place gui components
        frame.add(imagePanelContainer, BorderLayout.CENTER);
        frame.add(toolBar, BorderLayout.NORTH);
        //frame.add(logPanel, BorderLayout.SOUTH);

        // initiate imageHandler
        imageHandler = new ImageHandler(leftImage, rightImage);

        // button actionListeners
        comboBox.addActionListener(new ComboBoxActionListener());
        browseButton.addActionListener(new BrowseBtnActionListener());
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

    private class UndoBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            imageHandler.revertToOriginal();
        }
    }

    private class ComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedOption = (int) comboBox.getSelectedIndex();
            ActionListener[] listeners = executeButton.getActionListeners();
            for(ActionListener listener : listeners) {
                executeButton.removeActionListener(listener);
            }
            if (selectedOption == 0) {
                // do nothing
            } else if (selectedOption == 1) {
                // convert to PGM
                executeButton.addActionListener(new ConvertToPGMActionListener());
            } else if (selectedOption == 2) {
                // Scale spatial resolution option
                executeButton.addActionListener(new SpatialResolutionActionListener());
            } else if (selectedOption == 3) {
                executeButton.addActionListener(new GrayResolutionActionListener());
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
            if(imageHandler.isInputNull()) {
                JOptionPane.showMessageDialog(null, "No input file!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                imageHandler.convertToPGM();
            }
        }
    }

    private class SpatialResolutionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(imageHandler.isInputNull()) {
                JOptionPane.showMessageDialog(null, "No input file!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int inputWidth = imageHandler.getInputImage().getWidth();
                int inputHeight = imageHandler.getInputImage().getHeight();

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

                widthTextField.addActionListener(e16 -> {
                    String widthInput = widthTextField.getText();
                    try {
                        int curWidth = inputWidth;
                        int newWidth = Integer.parseInt(widthInput);

                        int newHeight = inputHeight * newWidth / curWidth;
                        heightTextField.setText(Integer.toString(newHeight));

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter an integer!", "Error", JOptionPane.ERROR_MESSAGE);
                        widthTextField.setText(Integer.toString(inputWidth));
                    }
                });

                widthTextField.addActionListener(e16 -> {
                    String widthInput = widthTextField.getText();
                    try {
                        int curWidth = inputWidth;
                        int newWidth = Integer.parseInt(widthInput);

                        int newHeight = inputHeight * newWidth / curWidth;
                        heightTextField.setText(Integer.toString(newHeight));

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter an integer!", "Error", JOptionPane.ERROR_MESSAGE);
                        widthTextField.setText(Integer.toString(inputWidth));
                    }
                });

                heightTextField.addActionListener(e15 -> {
                    String heightInput = heightTextField.getText();
                    try {
                        int curHeight = inputHeight;
                        int newHeight = Integer.parseInt(heightInput);

                        int newWidth = inputWidth * newHeight / curHeight;
                        widthTextField.setText(Integer.toString(newWidth));

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter an integer!", "Error", JOptionPane.ERROR_MESSAGE);
                        heightTextField.setText(Integer.toString(inputHeight));
                    }
                });

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
                            int newWidth = Integer.parseInt(widthTextField.getText());
                            int newHeight = Integer.parseInt(heightTextField.getText());
                            imageHandler.nearestNeighbor(newWidth, newHeight);
                            popUpDialog.dispose();
                        });
                    } else if (srOption == 2) {     // Linear Interpolation
                        okButton.addActionListener(e12 -> {
                            int newWidth = Integer.parseInt(widthTextField.getText());
                            int newHeight = Integer.parseInt(heightTextField.getText());
                            imageHandler.imageInterpolation(newWidth, newHeight, ImageHandler.LINEAR);
                            popUpDialog.dispose();
                        });
                    } else {            // Bilinear Interpolation
                        okButton.addActionListener(e1 -> {
                            int newWidth = Integer.parseInt(widthTextField.getText());
                            int newHeight = Integer.parseInt(heightTextField.getText());
                            imageHandler.imageInterpolation(newWidth, newHeight, ImageHandler.BILINEAR);
                            popUpDialog.dispose();
                        });
                    }
                });

                popUpDialog.setSize(300,200);
                popUpDialog.setLocationRelativeTo(frame);
                popUpDialog.setVisible(true);
            }
        }
    }

    private class GrayResolutionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(imageHandler.isInputNull()) {
                JOptionPane.showMessageDialog(null, "No input file!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int inputGrayDepth = imageHandler.getGrayDepth(imageHandler.getInputImage());
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
                    imageHandler.changeGrayLevel(newDepth);
                    popUpDialog.dispose();
                });

                popUpDialog.setSize(300,200);
                popUpDialog.setLocationRelativeTo(frame);
                popUpDialog.setVisible(true);
            }
        }
    }
    // TODO!!!
    private class SaveBtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            imageHandler.saveOutputImage();
        }
    }
}
