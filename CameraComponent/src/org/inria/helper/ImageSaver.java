package org.inria.helper;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class ImageSaver {
	
    protected int width;
    protected int height;
    protected BufferStrategy bufferStrategy;
    public BufferedImage image;
    
    public ImageSaver(int width, int height) {
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration()
                .createCompatibleImage(width, height);
	}
    
    public void buildFrameImage(int[] frame) {
        image.setRGB(0, 0, width, height, frame, 0, width);
    }

    public void displayFrame() {
        try {
            Graphics2D g = image.createGraphics();
            g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
            g.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
