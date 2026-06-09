import java.awt.Frame;
import java.io.IOException;
import javax.swing.JOptionPane;

public class SaveFileCommand implements Command {
    private final Presentation presentation;
    private final Frame parent;
    private final String filename;

    public SaveFileCommand(Presentation presentation, Frame parent, String filename) {
        this.presentation = presentation;
        this.parent = parent;
        this.filename = filename;
    }

    @Override
    public void execute() {
        PresentationWriter writer = Accessor.getAccessor(filename);
        try {
            writer.saveFile(presentation, filename);
        } catch (IOException exc) {
            JOptionPane.showMessageDialog(parent, "IO Exception: " + exc, "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
