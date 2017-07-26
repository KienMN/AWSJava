import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

/**
 * Created by kienmaingoc on 6/29/17.
 */
public class Camera implements Runnable {
    static CanvasFrame frame;
    static FrameGrabber grabber;
    Frame grabberFrame;
    static IplImage image;
    boolean running = true;

    @Override
    public void run() {
        if (running) {
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            try {
                grabber = FrameGrabber.createDefault(0);
                grabber.start();
                frame = new CanvasFrame("Test Camera", CanvasFrame.getDefaultGamma() / grabber.getGamma());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                while (running) {

                    grabberFrame = grabber.grab();

                    image = converter.convertToIplImage(grabberFrame);

                    frame.showImage(grabberFrame);

//                    cvSaveImage(MultiThreadMain.PHOTO_NAME, image);
                }

//                grabber.stop();
//                frame.dispose();

            } catch (FrameGrabber.Exception e) {
                System.out.println("Can not connect to the camera");
            }
        }
    }

    public void stop() {
        running = false;
    }

}
