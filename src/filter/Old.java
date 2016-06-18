package filter;

import java.awt.image.BufferedImage;

public class Old implements Filter {
    public BufferedImage filter(BufferedImage image) {
        image = ImageTools.applyEnhancedColors(image, 20, 5);
        return ImageTools.applySepia(image);
    }
}