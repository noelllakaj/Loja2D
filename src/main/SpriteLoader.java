package main;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteLoader {

    private static final String[] DIRECTIONS = {
        "back", "left", "front", "right"
    };

    /**
     * @param basePath   e.g. "/Player/Idle"
     * @param animation  "Idle", "Walking", "Death"
     * @param frames     number of frames per direction
     */
    public static BufferedImage[][] load(
            String basePath,
            String animation,
            int frames) {

        BufferedImage[][] sprites =
                new BufferedImage[4][frames];

        for (int dir = 0; dir < 4; dir++) {
            for (int frame = 0; frame < frames; frame++) {

                String path = basePath + "/" +
                        DIRECTIONS[dir] +
                        animation + " ("+
                        (frame + 1)+")" + ".png";

                try {
                	//System.out.println(path);
                    sprites[dir][frame] =
                            ImageIO.read(
                                    SpriteLoader.class
                                            .getResourceAsStream(path)
                            );
                } catch (IOException | NullPointerException e) {
                    throw new RuntimeException(
                            "Sprite not found: " + path, e
                    );
                }
            }
        }
        return sprites;
    }
}
