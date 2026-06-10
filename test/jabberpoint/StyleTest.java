package jabberpoint;

import jabberpoint.model.Style;

import org.junit.Test;

import java.awt.Font;

import static org.junit.Assert.*;

public class StyleTest {

    @Test
    public void testCreateAndGetStyleWithinBounds() {
        Style.createStyles();

        Style style0 = Style.getStyle(0);
        Style style4 = Style.getStyle(4);

        assertNotNull(style0);
        assertNotNull(style4);
    }

    @Test
    public void testGetStyleClampsOutOfBoundsLevel() {
        Style.createStyles();

        Style max = Style.getStyle(4);
        Style over = Style.getStyle(99);

        assertSame(max, over);
    }

    @Test
    public void testFontScaling() {
        Style style = new Style(10, java.awt.Color.BLACK, 20, 5);

        Font scaled = style.getFont(1.5f);

        assertEquals(30, scaled.getSize());
        assertTrue(style.toString().contains("[10,"));
    }
}
