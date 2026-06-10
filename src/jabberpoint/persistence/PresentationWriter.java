package jabberpoint.persistence;

import jabberpoint.model.Presentation;

import java.io.IOException;

/**
 * Writes a presentation to persistent storage.
 */
public interface PresentationWriter {
    void saveFile(Presentation presentation, String filename) throws IOException;
}
