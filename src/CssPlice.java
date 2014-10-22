import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CssPlice {
    static class Sprite {
        int width, height;
        String name;
        int x, y;

        public Sprite(int w, int h) {
            width = w;
            height = h;
        }

        @Override
        public String toString() {
            return "Sprite{" +
                    "width=" + width +
                    ", height=" + height +
                    ", name='" + name + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public static void main(String[] args) {
        int maxX = 0;
        int maxY = 0;
        List<Sprite> sprites = new ArrayList<Sprite>();
        sprites.add(new Sprite(4, 4));
        sprites.add(new Sprite(2, 2));
        sprites.add(new Sprite(1, 1));


        Collections.sort(sprites, new Comparator<Sprite>() {
            public int compare(Sprite a, Sprite b) {
                return -Integer.valueOf(a.height).compareTo(b.height);
            }
        });


        maxY = sprites.iterator().next().height;

        for (int i = 0; i < sprites.size(); i++) {
            Sprite s = sprites.get(i);
//           findBiggestSpriteThatFitsBelowMe
            s.x = maxX;
            maxX += s.width;
        }


        System.out.println(sprites);
    }

}

