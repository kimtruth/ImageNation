package filter;

import java.awt.image.BufferedImage;

public class Sepia implements Filter {
    public BufferedImage filter(BufferedImage image) {
        return ImageTools.applySepia(image);
    }
}