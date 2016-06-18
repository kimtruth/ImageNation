import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyFunction {

    public static BufferedImage insert_text_to_image(BufferedImage image, String text){
        String bin_text = string_to_bin(text);

        if (bin_text.length() % 3 != 0) {
            for (int i = 0; i < bin_text.length()%3; i++) {
                bin_text += "1";
            }
        }
        System.out.println(bin_text);

        int offset = 0;
        for(int i = 0 ; i < image.getHeight(); i++){
            for (int j = 0; j < image.getWidth(); j++){
                Color c = new Color(image.getRGB(j, i));
                int r, g, b;

                if (offset+2 < bin_text.length()) {
                    if (bin_text.charAt(offset) == '0')
                        r = c.getRed() & 254;
                    else
                        r = c.getRed() | 1;

                    if (bin_text.charAt(offset + 1) == '0')
                        g = c.getGreen() & 254;
                    else
                        g = c.getGreen() | 1;

                    if (bin_text.charAt(offset + 2) == '0')
                        b = c.getBlue() & 254;
                    else
                        b = c.getBlue() | 1;
                } else {
                    r = c.getRed() | 1;
                    g = c.getGreen() | 1;
                    b = c.getBlue() | 1;
                }
                Color rgb = new Color(r, g, b, c.getAlpha());

                //System.out.println(r + " " + g + " " + b);
                image.setRGB(j, i, rgb.getRGB());
                offset += 3;
            }
        }

        return image;
    }

    public static String string_to_bin(String text){
        StringBuffer s = new StringBuffer("");

        char [] ch = text.toCharArray();

        for (char aCh : ch) {
            s.append(String.format("%8s", Integer.toBinaryString(aCh)).replace(' ', '0'));
        }

        return s.toString();
    }

//    public static void main(String [] args){
//
//        File file = new File("5wBAG.jpg");
//        BufferedImage image = null;
//        try {
//            image = ImageIO.read(file);
//        } catch (IOException e) {
//            System.out.println("파일이 올바르지 않거나 존재하지 않습니다.");
//            System.exit(-1);
//        }
//        image = insert_text_to_image(image, "Hello World2");
//
//        try {
//            ImageIO.write(image, "png", new File("test.png"));
//        } catch (IOException e) {
//            System.out.println("파일 저장에 실패하였습니다.");
//        }
//
//    }


}
