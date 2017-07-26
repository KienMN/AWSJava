import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacpp.opencv_core.IplImage;

import javax.swing.*;

import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;

public class Demo {

    public static void main(String[] args) throws Exception {

        Loader.load(opencv_objdetect.class);
        FrameGrabber grabber = FrameGrabber.createDefault(0);

        grabber.start();
        Frame grabbedImage = grabber.grab();
        JButton button = new JButton("Hello");
        opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
        CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma()/grabber.getGamma());
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (frame.isVisible() && (grabbedImage = grabber.grab()) != null) {
            cvClearMemStorage(storage);
            frame.showImage(grabbedImage);

        }

        grabber.stop();
        frame.dispose();
    }
}