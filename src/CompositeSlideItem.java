import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A composite slide item that can contain child SlideItems.</p>
 * <p>This class implements the Composite part of the Composite design pattern.</p>
 * <p>It can hold both leaf items (TextItem, BitmapItem) and other composite items,
 * allowing for hierarchical structures within slides.</p>
 * @author Design Pattern Implementation
 * @version 2.0 - Composite Pattern
 */
public class CompositeSlideItem extends SlideItem {
	private ArrayList<SlideItem> children;

	/**
	 * Create an empty composite with a given level.
	 * @param level the level of this composite item
	 */
	public CompositeSlideItem(int level) {
		super(level);
		this.children = new ArrayList<>();
	}

	/**
	 * Create an empty composite with default level (0).
	 */
	public CompositeSlideItem() {
		this(0);
	}

	/**
	 * Add a child component to this composite.
	 * @param item the SlideItem to add
	 */
	@Override
	public void addChild(SlideItem item) {
		if (item != null && item != this) {
			children.add(item);
		}
	}

	/**
	 * Remove a child component from this composite.
	 * @param item the SlideItem to remove
	 * @return true if the item was removed, false if it was not found
	 */
	@Override
	public void removeChild(SlideItem item) {
		if (item != null) {
			children.remove(item);
		}
	}

	/**
	 * Get all child components.
	 * @return a list of all child SlideItems
	 */
	@Override
	public List<SlideItem> getChildren() {
		return new ArrayList<>(children);
	}

	@Override
	public SlideItemKind getKind() {
		return SlideItemKind.COMPOSITE;
	}

	/**
	 * Check if this is a composite/container.
	 * @return true since this is a composite
	 */
	@Override
	public boolean isComposite() {
		return true;
	}

	/**
	 * Get the number of children.
	 * @return the count of child items
	 */
	public int getChildCount() {
		return children.size();
	}

	/**
	 * Get a specific child by index.
	 * @param index the index of the child
	 * @return the SlideItem at that index, or null if out of bounds
	 */
	public SlideItem getChild(int index) {
		if (index >= 0 && index < children.size()) {
			return children.get(index);
		}
		return null;
	}

	/**
	 * Calculate the bounding box for this composite by encompassing all children.
	 * @param g the Graphics context
	 * @param observer the image observer
	 * @param scale the scale factor
	 * @param style the style to apply
	 * @return the bounding rectangle encompassing all children
	 */
	@Override
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style style) {
		if (children.isEmpty()) {
			return new Rectangle(0, 0, 0, 0);
		}

		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;

		for (SlideItem child : children) {
			Rectangle childBounds = child.getBoundingBox(g, observer, scale, style);
			if (childBounds != null) {
				minX = Math.min(minX, childBounds.x);
				minY = Math.min(minY, childBounds.y);
				maxX = Math.max(maxX, childBounds.x + childBounds.width);
				maxY = Math.max(maxY, childBounds.y + childBounds.height);
			}
		}

		if (minX == Integer.MAX_VALUE) {
			return new Rectangle(0, 0, 0, 0);
		}

		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * Draw this composite and all its children recursively.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param scale the scale factor
	 * @param g the Graphics context
	 * @param style the style to apply
	 * @param observer the image observer
	 */
	@Override
	public void draw(int x, int y, float scale, Graphics g, Style style, ImageObserver observer) {
		// Draw each child component
		int currentY = y;
		for (SlideItem child : children) {
			if (child != null) {
				Style childStyle = Style.getStyle(child.getLevel());
				child.draw(x, currentY, scale, g, childStyle, observer);
				Rectangle bounds = child.getBoundingBox(g, observer, scale, childStyle);
				if (bounds != null) {
					currentY += bounds.height;
				}
			}
		}
	}

	@Override
	public String toString() {
		return "CompositeSlideItem[level=" + getLevel() + ", children=" + children.size() + "]";
	}
}
