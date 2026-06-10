package jabberpoint;

import jabberpoint.model.Presentation;
import jabberpoint.persistence.Accessor;
import jabberpoint.persistence.DemoPresentation;
import jabberpoint.persistence.PresentationReader;
import jabberpoint.persistence.PresentationWriter;

import org.junit.Test;

import static org.junit.Assert.*;

public class AccessorDemoTest {

    @Test
    public void testGetDemoAccessorReturnsDemoPresentation() {
        PresentationReader accessor = Accessor.getDemoAccessor();

        assertNotNull(accessor);
        assertTrue(accessor instanceof DemoPresentation);
        assertEquals(".xml", Accessor.DEFAULT_EXTENSION);
    }

    @Test
    public void testDemoLoadFilePopulatesPresentation() throws Exception {
        Presentation presentation = new Presentation();

        Accessor.getDemoAccessor().loadFile(presentation, "");

        assertEquals("Demo Presentation", presentation.getTitle());
        assertTrue(presentation.getSize() >= 3);
        assertEquals("JabberPoint", presentation.getSlide(0).getTitle());
    }

    @Test
    public void testDemoPresentationIsReadOnly() {
        PresentationReader accessor = Accessor.getDemoAccessor();

        assertFalse(accessor instanceof PresentationWriter);
    }
}
