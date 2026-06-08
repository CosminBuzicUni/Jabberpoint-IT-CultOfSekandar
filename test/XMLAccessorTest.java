import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class XMLAccessorTest {

    @Test
    public void testSaveAndLoadRoundTrip() throws Exception {
        Presentation source = new Presentation();
        source.setTitle("My Show");

        Slide slide = new Slide();
        slide.setTitle("First");
        slide.append(new TextItem(1, "Hello"));
        source.append(slide);

        File tempXml = File.createTempFile("jabberpoint-roundtrip", ".xml", new File("."));
        tempXml.deleteOnExit();

        XMLAccessor accessor = new XMLAccessor();
        accessor.saveFile(source, tempXml.getAbsolutePath());

        String xml = new String(Files.readAllBytes(tempXml.toPath()), StandardCharsets.UTF_8);
        assertTrue(xml.contains("<showtitle>My Show</showtitle>"));
        assertTrue(xml.contains("<title>First</title>"));

        Presentation loaded = new Presentation();
        accessor.loadFile(loaded, tempXml.getAbsolutePath());

        assertEquals("My Show", loaded.getTitle());
        assertEquals(1, loaded.getSize());
        assertEquals("First", loaded.getSlide(0).getTitle());
        assertTrue(loaded.getSlide(0).getSlideItem(0) instanceof TextItem);
    }

    @Test
    public void testCompositeRoundTrip() throws Exception {
        Presentation source = new Presentation();
        source.setTitle("Composite Show");

        Slide slide = new Slide();
        slide.setTitle("Composite Slide");
        CompositeSlideItem group = new CompositeSlideItem(1);
        group.addChild(new TextItem(2, "Child One"));
        group.addChild(new TextItem(2, "Child Two"));
        slide.append(group);
        source.append(slide);

        File tempXml = File.createTempFile("jabberpoint-composite", ".xml", new File("."));
        tempXml.deleteOnExit();

        XMLAccessor accessor = new XMLAccessor();
        accessor.saveFile(source, tempXml.getAbsolutePath());

        Presentation loaded = new Presentation();
        accessor.loadFile(loaded, tempXml.getAbsolutePath());

        assertEquals(1, loaded.getSize());
        SlideItem item = loaded.getSlide(0).getSlideItem(0);
        assertTrue(item instanceof CompositeSlideItem);
        CompositeSlideItem loadedGroup = (CompositeSlideItem) item;
        assertEquals(2, loadedGroup.getChildCount());
        assertEquals("Child One", ((TextItem) loadedGroup.getChild(0)).getText());
        assertEquals("Child Two", ((TextItem) loadedGroup.getChild(1)).getText());
    }

    @Test
    public void testLoadMissingFileDoesNotThrow() throws Exception {
        XMLAccessor accessor = new XMLAccessor();
        Presentation presentation = new Presentation();

        accessor.loadFile(presentation, "definitely-not-a-file.xml");

        assertEquals(0, presentation.getSize());
    }
}
