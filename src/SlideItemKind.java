import java.util.function.BiFunction;

enum SlideItemKind {
    UNKNOWN(null, "unknown"),
    TEXT(TextItem::new, "text"),
    IMAGE(BitmapItem::new, "image"),
    COMPOSITE((level, text) -> new CompositeSlideItem(level), "composite");

    private final BiFunction<Integer, String, SlideItem> factory;
    private final String name;

    SlideItemKind(BiFunction<Integer, String, SlideItem> factory, String name) {
        this.factory = factory;
        this.name = name;
    }

    public SlideItem create(int level, String text) {
        return factory.apply(level, text);
    }

    public String toString() {
        return this.name;
    }
}