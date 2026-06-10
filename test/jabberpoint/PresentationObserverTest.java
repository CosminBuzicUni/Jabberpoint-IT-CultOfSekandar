package jabberpoint;

import jabberpoint.model.Presentation;
import jabberpoint.observer.Observer;

import org.junit.Test;

import static org.junit.Assert.*;

public class PresentationObserverTest {

    private static class RecordingObserver implements Observer {
        int called;
        Presentation lastPresentation;

        @Override
        public void update(Presentation presentation) {
            called++;
            lastPresentation = presentation;
        }
    }

    @Test
    public void testAddAndNotifyObserverViaSetSlideNumber() {
        Presentation presentation = new Presentation();
        RecordingObserver observer = new RecordingObserver();

        presentation.addObserver(observer);
        presentation.setSlideNumber(2);

        assertEquals(1, observer.called);
        assertSame(presentation, observer.lastPresentation);
        assertEquals(2, presentation.getSlideNumber());
    }

    @Test
    public void testRemoveObserverStopsNotifications() {
        Presentation presentation = new Presentation();
        RecordingObserver observer = new RecordingObserver();

        presentation.addObserver(observer);
        presentation.removeObserver(observer);
        presentation.setSlideNumber(1);

        assertEquals(0, observer.called);
    }
}
