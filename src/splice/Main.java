package splice;

import java.util.List;

public class Main {

    public static String generateCss(List<Sprite> sprites) {
        String text = "";
        for (Sprite sprite : sprites) {
            text += generateCss(sprite);
            text += "\n";
        }
        return text;
    }

    public static String generateCss(Sprite sprite) {
        return String.format(
                ".%s { height: %spx; width: %spx; background: url( 'our.png' ) -%spx -%spx; }",
                sprite.name,
                sprite.height,
                sprite.width,
                sprite.x, sprite.y
        );
    }
}