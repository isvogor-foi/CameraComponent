package hr.foi.thor.tiwo.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.inria.helper.CloseKeyListener;
import org.inria.helper.DepthHandler;
import org.inria.helper.DepthHandlerImpl;
import org.inria.helper.ImageSaver;
import org.inria.helper.IndependantDisplayFrame;
import org.inria.helper.KinectManager;
import org.inria.helper.VideoHandler;
import org.inria.helper.VideoHandlerImpl;
import org.openkinect.freenect.DepthFormat;
import org.openkinect.freenect.VideoFormat;

/**
 * User: Erwan Daubert - erwan.daubert@gmail.com Date: 02/10/13 Time: 09:02
 *
 * @author Erwan Daubert
 * @version 1.0
 */
public class FreenectRunner {

	public static void main(String[] args) {
		//DepthHandlerImpl depthHandler = new DepthHandlerImpl(640, 480, DepthFormat.D11BIT);
        VideoHandlerImpl videoHandler = new VideoHandlerImpl(640, 480, VideoFormat.IR_8BIT);		
		
		KinectManager manager = new KinectManager();
		manager.initializeContext();
		manager.initializeDevice(0);
		//manager.defineDepth(depthHandler);
        manager.defineVideo(videoHandler);


		// displayDepth(depthHandler, manager);
		// getOneImage(depthHandler, manager);
		//saveImage(depthHandler, manager);
		//saveSingleImage(depthHandler, manager);
		
		// working
		//saveImgThread(depthHandler);
        
        saveImgThread2(videoHandler);
        
	}
	
	private static void saveImgThread2(final VideoHandler videoHandler){
		CameraVideoThread cvt = new CameraVideoThread();
		Thread th = new Thread(cvt);
		cvt.initialize(640, 480, videoHandler);
		th.start();
		
		try{
			System.out.println("countdown...");
			Thread.sleep(2000);
			BufferedImage img = cvt.getImage();
		
			try {
				File outputfile = new File("savedThreadII.png");
				ImageIO.write(img, "png", outputfile);
				System.out.println("Image saved.");
			} catch (IOException e) {
				System.out.println("Exception: " + e.getMessage());
			}
			
			System.out.println("value" + img.toString());
			Thread.sleep(2000);
			System.out.println("Stop");
		} catch(InterruptedException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		cvt.stop();
		
		System.out.println("Done..");
	}
	
	
	private static void saveImgThread(final DepthHandler depthHandler){
		CameraDepthThread ct = new CameraDepthThread();
		Thread th = new Thread(ct);
		ct.initialize(640, 480, depthHandler);
		th.start();
		
		try{
			System.out.println("countdown...");
			Thread.sleep(2000);
			BufferedImage img = ct.getImage();
		
			try {
				File outputfile = new File("savedThreadII.png");
				ImageIO.write(img, "png", outputfile);
				System.out.println("Image saved.");
			} catch (IOException e) {
				System.out.println("Exception: " + e.getMessage());
			}
			
			System.out.println("value" + img.toString());
			Thread.sleep(2000);
			System.out.println("Stop");
		} catch(InterruptedException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		ct.stop();
		
		System.out.println("Done..");
	}
	
	private static void saveSingleImage(final DepthHandler depthHandler, final KinectManager manager){
		new Thread(new Runnable() {
			int[] frame = new int[640 * 480];

			@Override
			public void run() {
				try{ Thread.sleep(50); }
				catch (InterruptedException e) {};
				
				depthHandler.getFrame(frame);

				BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
				image.setRGB(0, 0, 640, 480, frame, 0, 640);

				try {
					File outputfile = new File("savedThread.png");
					ImageIO.write(image, "png", outputfile);
					System.out.println("Image saved.");
				} catch (IOException e) {
					System.out.println("Exception: " + e.getMessage());
				}
			}
		}).start();
	}

	private static void saveImage(final DepthHandler depthHandler, final KinectManager manager) {
		int[] frame = new int[640 * 480];

		//ImageSaver sav = new ImageSaver(640, 480);
		try {
			//wait connection
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		depthHandler.getFrame(frame);

		BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, 640, 480, frame, 0, 640);

		try {
			File outputfile = new File("saved.png");
			ImageIO.write(image, "png", outputfile);
			System.out.println("Image saved.");
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
		}

	}

	private static void displayDepth(final DepthHandler depthHandler, final KinectManager manager) {
		new Thread(new Runnable() {
			private int[] frame = new int[640 * 480];

			@Override
			public void run() {
				IndependantDisplayFrame displayFrame = new IndependantDisplayFrame(
						"Depth Display", 640, 480);
				displayFrame.addKeyListener(CloseKeyListener
						.getInstance(manager));
				while (!CloseKeyListener.getInstance(manager).isShutdown()) {
					depthHandler.getFrame(frame);
					displayFrame.buildFrameImage(frame);
					displayFrame.displayFrame();
					/*
					 * try{ File outputfile = new File("saved.png");
					 * ImageIO.write(displayFrame.image, "png", outputfile);
					 * System.out.println("Image saved."); } catch (IOException
					 * e){ System.out.println("Exception: " + e.getMessage()); }
					 */
				}
				displayFrame.dispose();
			}
		}).start();
	}

}
