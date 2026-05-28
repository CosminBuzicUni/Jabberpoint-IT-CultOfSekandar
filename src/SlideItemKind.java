public enum SlideItemKind
{
    UNKNOWN ("unknown"),
    TEXT ("text"),
    IMAGE ("image");

    private final String name;

    private SlideItemKind(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }
}
