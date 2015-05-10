package hr.foi.thor.tiwo.vision;

import java.awt.image.BufferedImage;

import org.inria.helper.VideoHandler;

public class CameraVideoThread implements Runnable{
	private int width;
	private int height;
	private VideoHandler videoHandler;
	
	private volatile BufferedImage image;
	private volatile boolean running = false;
	public boolean useDelay = false;
	
	public int[] frame;
	
	public void initialize(int width, int height, final VideoHandler videoHandler){
		this.width = width;
		this.height = height;
		
		this.videoHandler = videoHandler;
		
		frame = new int[width * height];
	}
	
	
	@Override
	public void run() {
		running = true;
		
		while(running){
			// get frame
			videoHandler.getFrame(frame);
			
			// create image
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			image.setRGB(0, 0, width, height, frame, 0, width);
			
			if(useDelay){
				try{
					Thread.sleep(1000);
				} catch (InterruptedException ex) {}
			}
		}
		
	}
	
	public synchronized void stop(){
		this.running = false;
		System.out.println("Kinect thread stopped (running = " + running + ")");
	}
	
	public synchronized BufferedImage getImage(){
		return image;
	}
}
