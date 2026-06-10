package jabberpoint;

import jabberpoint.model.SlideItem;
import jabberpoint.model.Style;

import org.junit.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import static org.junit.Assert.*;

public class SlideItemTest {

    private static class DummySlideItem extends SlideItem {
        DummySlideItem(int level) {
            super(level);
        }

        @Override
        public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style style) {
            return new Rectangle(0, 0, 1, 1);
        }

        @Override
        public void draw(int x, int y, float scale, Graphics g, Style style, ImageObserver observer) {
            // no-op for testing abstract base behavior
        }
    }

    @Test
    public void testLevelIsStored() {
        SlideItem slideItem = new DummySlideItem(3);

        assertEquals(3, slideItem.getLevel());
        assertNotNull(slideItem.getBoundingBox(null, null, 1.0f, null));
    }
}
