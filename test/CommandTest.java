import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandTest {

    @Test
    public void testNextPrevCommands() {
        Presentation p = new Presentation();
        p.append(new Slide());
        p.append(new Slide());
        p.setSlideNumber(0);
        Command next = new NextSlideCommand(p);
        Command prev = new PrevSlideCommand(p);
        next.execute();
        assertEquals(1, p.getSlideNumber());
        prev.execute();
        assertEquals(0, p.getSlideNumber());
    }

    @Test
    public void testGotoSlideCommand() {
        Presentation p = new Presentation();
        p.append(new Slide());
        p.append(new Slide());
        Command goto1 = new GotoSlideCommand(p, 1);
        goto1.execute();
        assertEquals(1, p.getSlideNumber());
    }

    @Test
    public void testExitAppCommandRunsRunnable() {
        AtomicBoolean ran = new AtomicBoolean(false);
        Command exit = new ExitAppCommand(() -> ran.set(true));
        exit.execute();
        assertTrue(ran.get());
    }

    @Test
    public void testNewPresentationCommandClearsSlides() {
        Presentation p = new Presentation();
        p.append(new Slide());
        p.append(new Slide());
        p.setSlideNumber(1);

        Command newCmd = new NewPresentationCommand(p);
        newCmd.execute();

        assertEquals(0, p.getSize());
    }

    @Test
    public void testOpenFileCommandLoadsPresentation() throws Exception {
        Presentation p = new Presentation();
        p.append(new Slide());

        // Save a known presentation to a temp file, then open it via the command
        java.io.File tmp = java.io.File.createTempFile("jabberpoint-open-cmd", ".xml", new java.io.File("."));
        tmp.deleteOnExit();

        Presentation source = new Presentation();
        source.setTitle("Opened");
        Slide s = new Slide();
        s.setTitle("Slide One");
        s.append(new TextItem(1, "Hello"));
        source.append(s);
        new XMLAccessor().saveFile(source, tmp.getAbsolutePath());

        Command open = new OpenFileCommand(source, null, tmp.getAbsolutePath());
        open.execute();

        assertEquals("Opened", source.getTitle());
        assertEquals(1, source.getSize());
        assertEquals(0, source.getSlideNumber());
    }

    @Test
    public void testSaveFileCommandWritesFile() throws Exception {
        Presentation p = new Presentation();
        p.setTitle("Saved");
        Slide s = new Slide();
        s.setTitle("Only Slide");
        s.append(new TextItem(1, "Content"));
        p.append(s);

        java.io.File tmp = java.io.File.createTempFile("jabberpoint-save-cmd", ".xml", new java.io.File("."));
        tmp.deleteOnExit();

        Command save = new SaveFileCommand(p, null, tmp.getAbsolutePath());
        save.execute();

        String xml = new String(java.nio.file.Files.readAllBytes(tmp.toPath()), java.nio.charset.StandardCharsets.UTF_8);
        assertTrue(xml.contains("<showtitle>Saved</showtitle>"));
        assertTrue(xml.contains("<title>Only Slide</title>"));
    }
}
