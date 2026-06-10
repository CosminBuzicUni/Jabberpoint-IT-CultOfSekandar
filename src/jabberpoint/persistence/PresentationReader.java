package jabberpoint.persistence;

import jabberpoint.model.Presentation;

import java.io.IOException;

/**
 * Reads a presentation from persistent storage.
 */
public interface PresentationReader {
    void loadFile(Presentation presentation, String filename) throws IOException;
}
