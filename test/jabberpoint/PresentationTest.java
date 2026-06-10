package jabberpoint;

import jabberpoint.model.Presentation;
import jabberpoint.model.Slide;

import org.junit.Test;
import static org.junit.Assert.*;

public class PresentationTest {

    @Test
    public void testAppendAndSize() {
        Presentation p = new Presentation();
        assertEquals(0, p.getSize());
        Slide s = new Slide();
        p.append(s);
        assertEquals(1, p.getSize());
    }

    @Test
    public void testSlideNumberBounds() {
        Presentation p = new Presentation();
        Slide s1 = new Slide();
        Slide s2 = new Slide();
        p.append(s1);
        p.append(s2);
        p.setSlideNumber(0);
        assertEquals(0, p.getSlideNumber());
        p.nextSlide();
        assertEquals(1, p.getSlideNumber());
        p.nextSlide(); // should stay at last
        assertEquals(1, p.getSlideNumber());
        p.prevSlide();
        assertEquals(0, p.getSlideNumber());
        p.prevSlide(); // should stay at first
        assertEquals(0, p.getSlideNumber());
    }

    @Test
    public void testGetSlideAndCurrent() {
        Presentation p = new Presentation();
        Slide s = new Slide();
        s.setTitle("T");
        p.append(s);
        p.setSlideNumber(0);
        assertNotNull(p.getCurrentSlide());
        assertEquals("T", p.getCurrentSlide().getTitle());
    }
}
