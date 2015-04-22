package splice;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssBox {

    public static void main(String[] args) throws Exception {

        final JFrame f = new JFrame();
        f.setSize(1800, 800);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        String url = "http://www.huffingtonpost.com/2013/04/26/adorable-fuzzy-dog-somehow-looks-just-like-penis_n_3164500.html";

        List<Sprite> sprites = makeSprites(getImages(url));
        sprites.stream().forEach(i -> {
            f.getContentPane().getGraphics().drawImage(i.bufferedImage, i.x, i.y, null);
        });

        String x = Main.generateCss(sprites);
        System.out.println(x);

        writeHtml(x);

    }

    private static List<Sprite> makeSprites(List<BufferedImage> images) {
        List<Sprite> out = new ArrayList<>();
        for (BufferedImage image : images) {
            if (image != null)
                out.add(new Sprite(image.getWidth(), image.getHeight(), image));
        }

        goFunky(out);

        return out;
    }

    private static void goFunky(List<Sprite> sprites) {
        int maxX = 0;

        Collections.sort(sprites, (a, b) -> -Integer.valueOf(a.height).compareTo(b.height));

        for (int i = 0; i < sprites.size(); i++) {
            Sprite s = sprites.get(i);
            s.x = maxX;
            maxX += s.width;
        }
    }

    private static List<BufferedImage> getImages(String urlStr) throws Exception {

        URL url = new URL(urlStr);

        URLConnection conn = url.openConnection();

        StringBuilder b = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                conn.getInputStream()))) {
            String l = br.readLine();
            while (l != null) {
                l = br.readLine();
                b.append(l);
            }
        }
        System.out.println(b);

        Pattern p = Pattern.compile("<img.*?src=\"([^\"]*)\".*?>",
                Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(b.toString());

        List<BufferedImage> imgs = new ArrayList<>();
        while (m.find()) {
            String imgurl = m.group(1);
            System.out.println(imgurl);
            if (imgurl.startsWith("//")) imgurl = "http:" + imgurl;
            if (imgurl.startsWith("http")) {
                URLConnection c = new URL(imgurl).openConnection();
                imgs.add(ImageIO.read(c.getInputStream()));
            }
        }
        return imgs;
    }

    private static void writeHtml(String x) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("foo.html"));
        fileWriter.write("<html><head><style>\n");
        fileWriter.write(x);
        fileWriter.write("</style></head><body>\n");
        fileWriter.write("</body></html>");
        fileWriter.flush();
        fileWriter.close();
    }

}
