package jabberpoint;

import jabberpoint.model.BitmapItem;
import jabberpoint.model.Style;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.*;

public class BitmapItemTest {

    @Test
    public void testBitmapItemNameAndToString() {
        BitmapItem item = new BitmapItem(2, "does-not-exist.png");

        assertEquals(2, item.getLevel());
        assertEquals("does-not-exist.png", item.getName());
        assertTrue(item.toString().contains("BitmapItem[2,does-not-exist.png]"));
    }

    @Test
    public void testBoundingBoxAndDrawWithRealImage() throws Exception {
        Style.createStyles();

        File tempImage = File.createTempFile("jabberpoint-bitmap", ".png");
        tempImage.deleteOnExit();

        BufferedImage source = new BufferedImage(40, 30, BufferedImage.TYPE_INT_ARGB);
        ImageIO.write(source, "png", tempImage);

        BitmapItem item = new BitmapItem(1, tempImage.getAbsolutePath());

        BufferedImage canvas = new BufferedImage(200, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();
        try {
            Rectangle bounds = item.getBoundingBox(g2d, null, 1.0f, Style.getStyle(1));
            item.draw(0, 0, 1.0f, g2d, Style.getStyle(1), null);

            assertTrue(bounds.width > 0);
            assertTrue(bounds.height > 0);
        } finally {
            g2d.dispose();
        }
    }

    @Test
    public void testMissingImageDoesNotCrash() {
        Style.createStyles();

        BitmapItem item = new BitmapItem(1, "nonexistent-image-12345.png");

        BufferedImage canvas = new BufferedImage(200, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();
        try {
            Rectangle bounds = item.getBoundingBox(g2d, null, 1.0f, Style.getStyle(1));
            item.draw(0, 0, 1.0f, g2d, Style.getStyle(1), null);

            assertEquals(0, bounds.width);
            assertTrue(bounds.height > 0);
        } finally {
            g2d.dispose();
        }
    }
}
