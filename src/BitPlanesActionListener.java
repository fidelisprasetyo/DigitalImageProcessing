import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class BitPlanesActionListener implements ActionListener {

    ImageHandler imageHandler;
    JFrame frame;

    public BitPlanesActionListener(ImageHandler imageHandler, JFrame frame) {
        this.imageHandler = imageHandler;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JDialog popUpDialog = new JDialog(frame, "Remove selected bit plane", true);
        popUpDialog.setLayout(new GridLayout(3, 1));

        JPanel textPanel = new JPanel();
        JLabel title = new JLabel("Select bit plane");
        textPanel.add(title);

        JPanel sliderPanel = new JPanel();
        JSlider slider = new JSlider(0,7,0);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        sliderPanel.add(slider);

        JPanel buttonPanel = new JPanel();
        JButton button = new JButton("Remove");
        buttonPanel.add(button);

        popUpDialog.add(textPanel);
        popUpDialog.add(sliderPanel);
        popUpDialog.add(buttonPanel);

        button.addActionListener(e12 -> {
            int selectedValue = slider.getValue();
            BufferedImage outputImage = BitPlaneRemover.apply(imageHandler.getCurrentImage(), selectedValue);
            imageHandler.updateBufferedImage(outputImage);
            popUpDialog.dispose();
        });

        popUpDialog.setSize(250,200);
        popUpDialog.setLocationRelativeTo(frame);
        popUpDialog.setVisible(true);

    }

}
