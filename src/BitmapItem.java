import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;

import java.io.IOException;


/**
 * <p>A bitmap/image item on a slide.</p>
 * <p>Bitmap items have the responsibility to draw themselves. This class is a Leaf node in the Composite pattern.</p>
 * <p>This class is a Leaf component - it cannot contain child items.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 * @version 2.0 - Composite Pattern (Leaf Node)
*/

public class BitmapItem extends SlideItem {
  public static final SlideItemKind KIND = SlideItemKind.IMAGE;
  private BufferedImage bufferedImage;
  private String imageName;
  
  protected static final String FILE = "File ";
  protected static final String NOTFOUND = " not found";

  static {
    registerSlideItemType(KIND, new SlideItemBuilder() {
      public SlideItem create(int level, String text) {
        return new BitmapItem(level, text);
      }
    });
  }

// level is equal to item-level; name is the name of the file with the Image
	public BitmapItem(int level, String name) {
		super(level);
		imageName = name;
		try {
			bufferedImage = ImageIO.read(new File(imageName));
			if (bufferedImage == null) {
				throw new IOException("Cannot read image file");
			}
		}
		catch (IOException e) {
			System.err.println(FILE + imageName + NOTFOUND);
			bufferedImage = null;
		}
	}

// An empty bitmap-item
	public BitmapItem() {
		this(0, null);
	}

// give the filename of the image
	public String getName() {
		return imageName;
	}

	@Override
	public SlideItemKind getKind() {
		return KIND;
	}

	@Override
	public String getSerializationText() {
		return getName();
	}

// give the  bounding box of the image
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style myStyle) {
		if (bufferedImage == null) {
			int w = (int) (myStyle.indent * scale);
			int h = (int) (myStyle.leading * scale);
			return new Rectangle(w, 0, 0, h);
		}
		return new Rectangle((int) (myStyle.indent * scale), 0,
				(int) (bufferedImage.getWidth(observer) * scale),
				((int) (myStyle.leading * scale)) + 
				(int) (bufferedImage.getHeight(observer) * scale));
	}

// draw the image
	public void draw(int x, int y, float scale, Graphics g, Style myStyle, ImageObserver observer) {
		int width = x + (int) (myStyle.indent * scale);
		int height = y + (int) (myStyle.leading * scale);
		if (bufferedImage == null) {
			String fallback = "Image not found: " + imageName;
			g.drawString(fallback, width, height + g.getFontMetrics().getAscent());
			return;
		}
		g.drawImage(bufferedImage, width, height,(int) (bufferedImage.getWidth(observer)*scale),
                (int) (bufferedImage.getHeight(observer)*scale), observer);
	}

	public String toString() {
		return "BitmapItem[" + getLevel() + "," + imageName + "]";
	}
}
