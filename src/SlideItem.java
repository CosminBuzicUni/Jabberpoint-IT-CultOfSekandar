import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** <p>The abstract class for an item on a slide<p>
 * <p>All SlideItems have drawingfunctionality.</p>
 * <p>Implements the Component part of the Composite design pattern.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 * @version 2.0 - Composite Pattern Implementation
*/

public abstract class SlideItem {
	private static final Map<SlideItemKind, SlideItemBuilder> ITEM_BUILDERS = new HashMap<>();

	static {
		for (SlideItemKind kind : SlideItemKind.values()) {
			if (kind == SlideItemKind.UNKNOWN) continue;
			registerSlideItemType(kind, new SlideItemBuilder() {
				public SlideItem create(int level, String text) {
					return new TextItem(level, text);
				}
			});
		}
	}

	public interface SlideItemBuilder {
		SlideItem create(int level, String text);
	}

	public static void registerSlideItemType(SlideItemKind kind, SlideItemBuilder builder) {
		if (kind != null && builder != null) {
			ITEM_BUILDERS.put(kind, builder);
		}
	}

	public static SlideItem createSlideItem(SlideItemKind kind, int level, String text) {
		SlideItemBuilder builder = ITEM_BUILDERS.get(kind);
		return builder == null ? null : builder.create(level, text);
	}

	public SlideItemKind getKind() {
		return SlideItemKind.UNKNOWN;
	}

	public String getSerializationText() {
		return "";
	}

	private int level = 0; // level of the slideitem

	public SlideItem(int lev) {
		level = lev;
	}

	public SlideItem() {
		this(0);
	}

// Give the level
	public int getLevel() {
		return level;
	}

// Give the bounding box
	public abstract Rectangle getBoundingBox(Graphics g, 
			ImageObserver observer, float scale, Style style);

// Draw the item
	public abstract void draw(int x, int y, float scale, 
			Graphics g, Style style, ImageObserver observer);

	/**
	 * Add a child component to this item.
	 * Default implementation for leaf nodes - does nothing.
	 * Override in composite classes.
	 * @param item the SlideItem to add as a child
	 */
	public void addChild(SlideItem item) {
		// Default implementation for leaf nodes - no children
	}

	/**
	 * Remove a child component from this item.
	 * Default implementation for leaf nodes - does nothing.
	 * Override in composite classes.
	 * @param item the SlideItem to remove from children
	 */
	public void removeChild(SlideItem item) {
		// Default implementation for leaf nodes - no children
	}

	/**
	 * Get all child components of this item.
	 * Default implementation for leaf nodes - returns empty list.
	 * Override in composite classes.
	 * @return list of child SlideItems, or empty list for leaf nodes
	 */
	public List<SlideItem> getChildren() {
		// Default implementation for leaf nodes - no children
		return new java.util.ArrayList<>();
	}

	/**
	 * Check if this item is a composite (container) or a leaf.
	 * @return true if this is a composite/container, false if it's a leaf
	 */
	public boolean isComposite() {
		return false;
	}
}
