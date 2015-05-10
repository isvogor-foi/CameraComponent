package hr.foi.thor.tiwo.vision.providedinterface;

import java.awt.image.BufferedImage;

public interface CameraInterface {
	public BufferedImage getSingleImage();
	public BufferedImage getSingleDepthImage();
	public void setup(int depthFormat, int videoFormat);
}
