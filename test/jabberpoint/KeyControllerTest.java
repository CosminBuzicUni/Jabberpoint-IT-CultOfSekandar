package jabberpoint;

import jabberpoint.command.Command;
import jabberpoint.controller.KeyController;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Component;
import java.awt.event.KeyEvent;

public class KeyControllerTest {

    static class RecordingCommand implements Command {
        public int called = 0;
        @Override public void execute() { called++; }
    }

    @Test
    public void testKeyControllerNextPrevExit() {
        RecordingCommand next = new RecordingCommand();
        RecordingCommand prev = new RecordingCommand();
        RecordingCommand exit = new RecordingCommand();
        KeyController kc = new KeyController(next, prev, exit);

        Component src = new Component() {};
        KeyEvent evNext = new KeyEvent(src, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, '\n');
        kc.keyPressed(evNext);
        assertEquals(1, next.called);

        KeyEvent evPrev = new KeyEvent(src, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, '\n');
        kc.keyPressed(evPrev);
        assertEquals(1, prev.called);

        KeyEvent evExit = new KeyEvent(src, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, 'Q', 'Q');
        kc.keyPressed(evExit);
        assertEquals(1, exit.called);
    }
}
