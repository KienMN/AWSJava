import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;

/**
 * Created by kienmaingoc on 7/9/17.
 */
public class Camera implements Runnable {
    CanvasFrame frame;
    FrameGrabber grabber;
    Frame grabberFrame;
    public static IplImage image;

    @Override
    public void run() {

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        try {
            grabber = FrameGrabber.createDefault(0);
            grabber.start();
            frame = new CanvasFrame("Camera", CanvasFrame.getDefaultGamma()/grabber.getGamma());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            while (true) {
                grabberFrame = grabber.grab();
                image = converter.convertToIplImage(grabberFrame);
                frame.showImage(grabberFrame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
