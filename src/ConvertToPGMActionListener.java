import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConvertToPGMActionListener implements ActionListener {

    private final ImageHandler imageHandler;

    public ConvertToPGMActionListener(ImageHandler imageHandler) {
        this.imageHandler = imageHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PGMConverter.convertToPGM(imageHandler.getCurrentImage());
    }
}
