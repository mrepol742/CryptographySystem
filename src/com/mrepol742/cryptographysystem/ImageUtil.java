package com.mrepol742.cryptographysystem;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Base64;
import javax.imageio.ImageIO;

/**
 *
 * @author mrepol742
 */
public class ImageUtil {

    public static String imgToBase64String(final RenderedImage img, final String formatName) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream b64os = Base64.getEncoder().wrap(os);
        ImageIO.write(img, formatName, b64os);
        return os.toString();
    }

    public static BufferedImage base64StringToImg(final String base64String) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64String)));
    }
}
