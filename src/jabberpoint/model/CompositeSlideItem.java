package jabberpoint.model;

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

	public CompositeSlideItem(int level) {
		super(level);
		this.children = new ArrayList<>();
	}

	public CompositeSlideItem() {
		this(0);
	}

	public void addChild(SlideItem item) {
		if (item != null && item != this) {
			children.add(item);
		}
	}

	public void removeChild(SlideItem item) {
		if (item != null) {
			children.remove(item);
		}
	}

	public List<SlideItem> getChildren() {
		return new ArrayList<>(children);
	}

	@Override
	public SlideItemKind getKind() {
		return SlideItemKind.COMPOSITE;
	}

	public boolean isComposite() {
		return true;
	}

	public int getChildCount() {
		return children.size();
	}

	public SlideItem getChild(int index) {
		if (index >= 0 && index < children.size()) {
			return children.get(index);
		}
		return null;
	}

	@Override
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style style) {
		int totalHeight = (int) (style.leading * scale);
		int maxWidth = 0;

		for (SlideItem child : children) {
			if (child != null) {
				Style childStyle = Style.getStyle(child.getLevel());
				Rectangle childBounds = child.getBoundingBox(g, observer, scale, childStyle);
				if (childBounds != null) {
					maxWidth = Math.max(maxWidth, childBounds.x + childBounds.width);
					totalHeight += childBounds.height;
				}
			}
		}

		return new Rectangle((int) (style.indent * scale), 0, maxWidth, totalHeight);
	}

	@Override
	public void draw(int x, int y, float scale, Graphics g, Style style, ImageObserver observer) {
		int currentY = y + (int) (style.leading * scale);
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
