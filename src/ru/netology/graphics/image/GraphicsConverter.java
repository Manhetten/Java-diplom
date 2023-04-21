package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class GraphicsConverter implements TextGraphicsConverter {

    int width;
    int height;
    double maxRatio;
    TextColorSchema colorSchema = new ColorSchema();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        double ratio = (double) img.getWidth() / img.getHeight();

        if (maxRatio > 0 && ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        double divWidth = width > 0 && img.getWidth() > width ? (double) img.getWidth() / width : 1.0;
        double divHeight = height > 0 && img.getHeight() > height ? (double) img.getHeight() / height : 1.0;
        double divider = Math.max(divWidth, divHeight);
        int newWidth = (int) Math.ceil(img.getWidth() / divider);
        int newHeight = (int) Math.ceil(img.getHeight() / divider);

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        char[][] charArray = new char[bwImg.getHeight()][bwImg.getWidth()];
        for (int w = 0; w < bwImg.getWidth(); w++) {
            for (int h = 0; h < bwImg.getHeight(); h++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = colorSchema.convert(color);
                charArray[h][w] = c;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (char[] chars : charArray) {
            for (char aChar : chars) {
                sb.append(aChar);
                sb.append(aChar);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.width = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.height = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        colorSchema = schema;
    }
}
