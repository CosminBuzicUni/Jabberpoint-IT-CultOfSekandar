package jabberpoint;

import jabberpoint.model.Style;
import jabberpoint.model.TextItem;

import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

import static org.junit.Assert.*;

public class TextItemTest {

    @Test
    public void testDefaultTextAndLevel() {
        TextItem item = new TextItem();

        assertEquals(0, item.getLevel());
        assertEquals("No Text Given", item.getText());
    }

    @Test
    public void testGetAttributedStringAndBoundingBox() {
        Style.createStyles();
        TextItem item = new TextItem(1, "Hello JabberPoint");

        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        try {
            AttributedString attributedString = item.getAttributedString(Style.getStyle(1), 1.0f);
            Rectangle bounds = item.getBoundingBox(g2d, null, 1.0f, Style.getStyle(1));

            assertNotNull(attributedString);
            assertNotNull(bounds);
            assertTrue(bounds.height > 0);
        } finally {
            g2d.dispose();
        }
    }

    @Test
    public void testDrawWithEmptyTextDoesNothing() {
        Style.createStyles();
        TextItem item = new TextItem(1, "");

        BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        try {
            item.draw(0, 0, 1.0f, g2d, Style.getStyle(1), null);
        } finally {
            g2d.dispose();
        }

        assertTrue(item.toString().contains("TextItem[1,"));
    }
}
