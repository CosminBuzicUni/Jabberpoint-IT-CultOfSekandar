import org.junit.Before;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.Assert.*;

public class CompositeSlideItemTest {

    private CompositeSlideItem composite;
    private CompositeSlideItem child1;
    private CompositeSlideItem child2;
    private TextItem textItem;
    private BitmapItem bitmapItem;
    private Graphics2D graphics;

    @Before
    public void setUp() {
        Style.createStyles();
        composite = new CompositeSlideItem();
        child1 = new CompositeSlideItem(1);
        child2 = new CompositeSlideItem(2);
        textItem = new TextItem(1, "Test Text");
        bitmapItem = new BitmapItem(1, "test.png");
        
        BufferedImage canvas = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);
        graphics = canvas.createGraphics();
    }

    @Test
    public void testConstructorWithLevel() {
        CompositeSlideItem item = new CompositeSlideItem(3);
        assertEquals(3, item.getLevel());
        assertEquals(0, item.getChildCount());
        assertTrue(item.isComposite());
    }

    @Test
    public void testConstructorWithoutLevel() {
        CompositeSlideItem item = new CompositeSlideItem();
        assertEquals(0, item.getLevel());
        assertEquals(0, item.getChildCount());
        assertTrue(item.isComposite());
    }

    @Test
    public void testAddChildValidItem() {
        composite.addChild(textItem);
        assertEquals(1, composite.getChildCount());
        assertEquals(textItem, composite.getChild(0));
    }

    @Test
    public void testAddMultipleChildren() {
        composite.addChild(textItem);
        composite.addChild(child1);
        composite.addChild(child2);
        
        assertEquals(3, composite.getChildCount());
        assertEquals(textItem, composite.getChild(0));
        assertEquals(child1, composite.getChild(1));
        assertEquals(child2, composite.getChild(2));
    }

    @Test
    public void testAddChildNull() {
        composite.addChild(null);
        assertEquals(0, composite.getChildCount());
    }

    @Test
    public void testAddChildSelfReference() {
        composite.addChild(composite);
        assertEquals(0, composite.getChildCount());
    }

    @Test
    public void testRemoveChild() {
        composite.addChild(textItem);
        composite.addChild(child1);
        
        composite.removeChild(textItem);
        assertEquals(1, composite.getChildCount());
        assertEquals(child1, composite.getChild(0));
    }

    @Test
    public void testRemoveChildNotFound() {
        composite.addChild(textItem);
        TextItem other = new TextItem(2, "Other");
        
        composite.removeChild(other);
        assertEquals(1, composite.getChildCount());
        assertEquals(textItem, composite.getChild(0));
    }

    @Test
    public void testRemoveChildNull() {
        composite.addChild(textItem);
        composite.removeChild(null);
        assertEquals(1, composite.getChildCount());
    }

    @Test
    public void testGetChildren() {
        composite.addChild(textItem);
        composite.addChild(child1);
        
        List<SlideItem> children = composite.getChildren();
        assertEquals(2, children.size());
        assertEquals(textItem, children.get(0));
        assertEquals(child1, children.get(1));
    }

    @Test
    public void testGetChildrenReturnsDefensiveCopy() {
        composite.addChild(textItem);
        List<SlideItem> list1 = composite.getChildren();
        List<SlideItem> list2 = composite.getChildren();
        
        assertNotSame(list1, list2);
        assertEquals(list1, list2);
    }

    @Test
    public void testGetChildValidIndex() {
        composite.addChild(textItem);
        composite.addChild(child1);
        
        assertEquals(textItem, composite.getChild(0));
        assertEquals(child1, composite.getChild(1));
    }

    @Test
    public void testGetChildNegativeIndex() {
        composite.addChild(textItem);
        assertNull(composite.getChild(-1));
    }

    @Test
    public void testGetChildOutOfBounds() {
        composite.addChild(textItem);
        assertNull(composite.getChild(1));
        assertNull(composite.getChild(100));
    }

    @Test
    public void testGetKindIsComposite() {
        assertEquals(SlideItemKind.COMPOSITE, composite.getKind());
    }

    @Test
    public void testIsCompositeAlwaysTrue() {
        assertTrue(composite.isComposite());
        composite.addChild(textItem);
        assertTrue(composite.isComposite());
    }

    @Test
    public void testGetChildCountEmpty() {
        assertEquals(0, composite.getChildCount());
    }

    @Test
    public void testGetChildCountWithChildren() {
        composite.addChild(textItem);
        assertEquals(1, composite.getChildCount());
        composite.addChild(child1);
        assertEquals(2, composite.getChildCount());
    }

    @Test
    public void testGetBoundingBoxEmpty() {
        Rectangle bounds = composite.getBoundingBox(graphics, null, 1.0f, Style.getStyle(0));
        assertNotNull(bounds);
        assertEquals(0, bounds.width);
        assertEquals(0, bounds.height);
        assertEquals(0, bounds.x);
        assertEquals(0, bounds.y);
    }

    @Test
    public void testGetBoundingBoxWithSingleChild() {
        composite.addChild(textItem);
        Rectangle bounds = composite.getBoundingBox(graphics, null, 1.0f, Style.getStyle(0));
        
        assertNotNull(bounds);
        assertTrue(bounds.width >= 0);
        assertTrue(bounds.height >= 0);
    }

    @Test
    public void testGetBoundingBoxWithMultipleChildren() {
        composite.addChild(textItem);
        composite.addChild(child1);
        child1.addChild(new TextItem(2, "Nested"));
        
        Rectangle bounds = composite.getBoundingBox(graphics, null, 1.0f, Style.getStyle(0));
        assertNotNull(bounds);
        assertTrue(bounds.width >= 0);
        assertTrue(bounds.height >= 0);
    }

    @Test
    public void testGetBoundingBoxWithScale() {
        composite.addChild(textItem);
        Rectangle bounds1 = composite.getBoundingBox(graphics, null, 1.0f, Style.getStyle(0));
        Rectangle bounds2 = composite.getBoundingBox(graphics, null, 2.0f, Style.getStyle(0));
        
        assertNotNull(bounds1);
        assertNotNull(bounds2);
    }

    @Test
    public void testDrawEmpty() {
        try {
            composite.draw(0, 0, 1.0f, graphics, Style.getStyle(0), null);
        } finally {
            graphics.dispose();
        }
    }

    @Test
    public void testDrawWithChildren() {
        composite.addChild(textItem);
        composite.addChild(child1);
        
        try {
            composite.draw(10, 10, 1.0f, graphics, Style.getStyle(0), null);
        } finally {
            graphics.dispose();
        }
    }

    @Test
    public void testDrawWithNestedComposites() {
        composite.addChild(child1);
        child1.addChild(textItem);
        
        try {
            composite.draw(0, 0, 1.5f, graphics, Style.getStyle(0), null);
        } finally {
            graphics.dispose();
        }
    }

    @Test
    public void testDrawWithDifferentScales() {
        composite.addChild(textItem);
        
        try {
            composite.draw(0, 0, 0.5f, graphics, Style.getStyle(0), null);
            composite.draw(0, 0, 2.0f, graphics, Style.getStyle(0), null);
        } finally {
            graphics.dispose();
        }
    }

    @Test
    public void testToStringEmpty() {
        String str = composite.toString();
        assertNotNull(str);
        assertTrue(str.contains("CompositeSlideItem"));
        assertTrue(str.contains("level=0"));
        assertTrue(str.contains("children=0"));
    }

    @Test
    public void testToStringWithChildren() {
        composite.addChild(textItem);
        composite.addChild(child1);
        
        String str = composite.toString();
        assertNotNull(str);
        assertTrue(str.contains("children=2"));
    }

    @Test
    public void testComplexHierarchy() {
        CompositeSlideItem root = new CompositeSlideItem();
        CompositeSlideItem branch1 = new CompositeSlideItem(1);
        CompositeSlideItem branch2 = new CompositeSlideItem(1);
        
        root.addChild(branch1);
        root.addChild(branch2);
        
        branch1.addChild(new TextItem(2, "Leaf 1"));
        branch1.addChild(new TextItem(2, "Leaf 2"));
        branch2.addChild(new TextItem(2, "Leaf 3"));
        
        assertEquals(2, root.getChildCount());
        assertEquals(2, branch1.getChildCount());
        assertEquals(1, branch2.getChildCount());
        
        List<SlideItem> children = root.getChildren();
        assertEquals(2, children.size());
    }

    @Test
    public void testRemoveFromComplexHierarchy() {
        CompositeSlideItem root = new CompositeSlideItem();
        CompositeSlideItem branch = new CompositeSlideItem(1);
        TextItem leaf = new TextItem(2, "Leaf");
        
        root.addChild(branch);
        branch.addChild(leaf);
        
        branch.removeChild(leaf);
        assertEquals(0, branch.getChildCount());
        assertEquals(1, root.getChildCount());
    }
}
