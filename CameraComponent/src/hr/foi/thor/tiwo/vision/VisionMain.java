package hr.foi.thor.tiwo.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class VisionMain {
	
	public static void main(String[] args) {
		
		CameraInterfaceImpl cam = new CameraInterfaceImpl();
		
		try{
			System.out.println("Start...");
			Thread.sleep(1000);
			
			BufferedImage videoImg = cam.getSingleImage();
			BufferedImage depthImg = cam.getSingleDepthImage();
			
			File outVideoFile = new File("videoImg.png");
			ImageIO.write(videoImg, "png", outVideoFile);
			File outDepthFile = new File("depthImg.png");
			ImageIO.write(depthImg, "png", outDepthFile);
			
			
			System.out.println("Done!");
			cam.stop();
			
		} catch(InterruptedException ex){
			System.out.println("Interrupted - message: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("IO - message: " + ex.getMessage());

		}
		
	}
	
}
