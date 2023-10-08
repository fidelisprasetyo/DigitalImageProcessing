import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class HistogramEqualizationActionListener implements ActionListener {

    private ImageHandler imageHandler;
    private JFrame frame;
    private int[] histogram;

    public HistogramEqualizationActionListener(ImageHandler imageHandler, JFrame frame) {
        this.imageHandler = imageHandler;
        this.frame = frame;
        HistogramEqualizer histogramEqualizer = new HistogramEqualizer(imageHandler.getCurrentImage());
        //this.histogram = histogramEqualizer.getHistogram();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int inputVal = imageHandler.getCurrentImage().getHeight();

        JDialog popUpDialog = new JDialog(frame, "Equalize Histogram", true);
        popUpDialog.setLayout(new GridLayout(2, 1));

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
        JButton executeBtn = new JButton("Execute");
        buttonPanel.add(executeBtn);

        popUpPanel.add(radioButtonPanel);
        popUpPanel.add(buttonPanel);

        // lower panel

//        class HistogramPlot extends JPanel {
//            private int[] histogram;
//
//            public HistogramPlot(int[] histogram) {
//                this.histogram = histogram;
//                setPreferredSize(new Dimension(300,200));
//            }
//
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//
//                int width = getWidth();
//                int height = getHeight();
//
//                int maxCount = 0;
//                for(int count : histogram) {
//                    if(count > maxCount) {
//                        maxCount = count;
//                    }
//                }
//
//                g.setColor(Color.BLACK);
//                for(int i = 0; i < 256; i++) {
//                    int barHeight= (int) ((double) histogram[i]/maxCount*(height));
//                    int x = i * (width/256);
//                    int y = height - barHeight;
//                    g.fillRect(x,y,width/256, barHeight);
//                }
//            }
//        }

        JPanel histPanel = new JPanel();
        //histPanel.add(new HistogramPlot(histogram));
        histPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        popUpDialog.add(popUpPanel);
        popUpDialog.add(histPanel);


        executeBtn.addActionListener(e1 -> {
            if(option1Btn.isSelected()) {
                BufferedImage outputImage;
                HistogramEqualizer histogramEqualizer = new HistogramEqualizer(imageHandler.getCurrentImage());
                outputImage = histogramEqualizer.globalEqualization();
                imageHandler.updateBufferedImage(outputImage);
            } else {
                int inputValue = Integer.parseInt(input.getText());
                System.out.println("Local is selected");
                System.out.println(inputValue*10);
            }
            popUpDialog.dispose();
        });

        popUpDialog.setSize(300,200);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);
    }
}