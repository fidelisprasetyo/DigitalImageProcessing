import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConvertToPGMActionListener implements ActionListener {

    private ImageHandler imageHandler;

    public ConvertToPGMActionListener(ImageHandler imageHandler) {
        this.imageHandler = imageHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ImageOperator imageOperator = new ImageOperator();
        imageOperator.convertToPGM(imageHandler.getCurrentImage());
    }
}
