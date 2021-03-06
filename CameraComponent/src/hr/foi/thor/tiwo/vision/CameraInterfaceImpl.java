package hr.foi.thor.tiwo.vision;

import hr.foi.thor.tiwo.vision.providedinterface.CameraInterface;

import java.awt.image.BufferedImage;

import org.inria.helper.DepthHandlerImpl;
import org.inria.helper.KinectManager;
import org.inria.helper.VideoHandlerImpl;
import org.openkinect.freenect.DepthFormat;
import org.openkinect.freenect.VideoFormat;

public class CameraInterfaceImpl implements CameraInterface {

	private KinectManager manager;
	private DepthHandlerImpl depthHandler;
	private VideoHandlerImpl videoHandler;
	
	private CameraVideoThread cvt;
	private Thread videoThread;
	
	private CameraDepthThread cdt;
	private Thread depthThread;
	
	private VideoFormat mVideoFormat = VideoFormat.IR_8BIT;
	private DepthFormat mDepthFormat = DepthFormat.D11BIT;


	public CameraInterfaceImpl() {
		depthHandler = new DepthHandlerImpl(640, 480, mDepthFormat);
		videoHandler = new VideoHandlerImpl(640, 480, mVideoFormat);

		manager = new KinectManager();
		manager.initializeContext();
		manager.initializeDevice(0);
		manager.defineDepth(depthHandler);
		manager.defineVideo(videoHandler);
		
		start();
	}
	
	private void start(){
		// start vision thread
		cvt = new CameraVideoThread();
		videoThread = new Thread(cvt);
		cvt.initialize(640, 480, videoHandler);
		videoThread.start();
		
		// start depth thread
		cdt = new CameraDepthThread();
		depthThread = new Thread(cdt);
		cdt.initialize(640, 480, depthHandler);
		depthThread.start();
		
	}
	
	public void stop(){
		if(cvt != null){
			cvt.stop();
			videoThread = null;
		}
		if(cdt != null){
			cdt.stop();
			depthThread = null;
		}
	}

	@Override
	public BufferedImage getSingleImage() {		
		return cvt.getImage();
	}

	@Override
	public BufferedImage getSingleDepthImage() {
		return cdt.getImage();
	}

	@Override
	public void setup(int videoFormat, int depthFormat) {
		switch(videoFormat){
			case 1: mVideoFormat = VideoFormat.IR_8BIT; break;
			case 2: mVideoFormat = VideoFormat.RGB; break;
		}
		switch(depthFormat){
		case 1: mDepthFormat = DepthFormat.D11BIT; break;
		case 2: mDepthFormat = DepthFormat.D10BIT; break;
		}
	}

}
