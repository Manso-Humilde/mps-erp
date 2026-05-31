/// LogoService.java
package com.mps.erp.service;

import com.lowagie.text.Image;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

@Service
public class LogoService {

    private byte[] cachedLogo;

    public Image getLogoImage() {
        try {
            if (cachedLogo == null) {
                cachedLogo = generateLogoPNG();
            }
            return Image.getInstance(cachedLogo);
        } catch (Exception e) {
            throw new RuntimeException("Error generando logo", e);
        }
    }

    private byte[] generateLogoPNG() throws Exception {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Círculo marrón
        g.setColor(new Color(139, 69, 19));
        g.fillOval(2, 2, 60, 60);

        // Texto MPS blanco
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();
        String text = "MPS";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g.drawString(text, (64 - textWidth) / 2, (64 + textHeight) / 2 - 4);

        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }
}
