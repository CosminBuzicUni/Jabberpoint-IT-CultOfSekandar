import java.awt.Frame;
import java.io.IOException;
import javax.swing.JOptionPane;

public class OpenFileCommand implements Command {
    private final Presentation presentation;
    private final Frame parent;
    private final String filename;

    public OpenFileCommand(Presentation presentation, Frame parent, String filename) {
        this.presentation = presentation;
        this.parent = parent;
        this.filename = filename;
    }

    @Override
    public void execute() {
        presentation.clear();
        PresentationReader reader = Accessor.getAccessor(filename);
        try {
            reader.loadFile(presentation, filename);
            presentation.setSlideNumber(0);
        } catch (IOException exc) {
            JOptionPane.showMessageDialog(parent, "IO Exception: " + exc, "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
