package splice;

import java.awt.image.BufferedImage;

public class Sprite {
    BufferedImage bufferedImage;
    int width, height;
    String name;
    int x, y;

    static int count = 0;

    public Sprite(int w, int h, BufferedImage bi) {
        name = "image" + count++;
        width = w;
        height = h;
        bufferedImage = bi;
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
