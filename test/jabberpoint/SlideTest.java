package jabberpoint;

import jabberpoint.model.Slide;
import jabberpoint.model.Style;
import jabberpoint.model.TextItem;

import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class SlideTest {

    @Test
    public void testTitleAndAppendOperations() {
        Slide slide = new Slide();
        slide.setTitle("Intro");

        slide.append(1, "Hello");
        TextItem second = new TextItem(2, "World");
        slide.append(second);

        assertEquals("Intro", slide.getTitle());
        assertEquals(2, slide.getSize());
        assertSame(second, slide.getSlideItem(1));
        assertEquals(2, slide.getSlideItems().size());
    }

    @Test
    public void testDrawWithGraphicsContext() {
        Style.createStyles();
        Slide slide = new Slide();
        slide.setTitle("Draw Test");
        slide.append(1, "Item text");

        BufferedImage canvas = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();
        try {
            slide.draw(g2d, new Rectangle(0, 0, 1200, 800), null);
        } finally {
            g2d.dispose();
        }

        assertEquals(1, slide.getSize());
    }
}
