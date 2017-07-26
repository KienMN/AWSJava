import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import javax.swing.*;

import java.awt.*;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;


/**
 * Created by gtiwari on 1/3/2017.
 */

public class Test implements Runnable {
    final int INTERVAL = 00;///you may use interval
    CanvasFrame canvas = new CanvasFrame("Web Cam");

    public Test() {
        Graphics2D graph = canvas.createGraphics();
//        graph.setStroke(new BasicStroke(5));
//        graph.drawRect(0, 0, 100, 100);
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }

    public void run() {

//        FrameGrabber grabber = new VideoInputFrameGrabber(0); // 1 for next camera

        try {
            FrameGrabber grabber = FrameGrabber.createDefault(0);
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            IplImage img;
            int i = 0;
            grabber.start();
            while (true) {
                Frame frame = grabber.grab();

                img = converter.convert(frame);

                //the grabbed frame will be flipped, re-flip to make it right
//                cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise

                //save
//                cvSaveImage((i++) + "-aa.jpg", img);

//                canvas.showImage(converter.convert(img));
                canvas.showImage(frame);

                Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Test gs = new Test();
        Thread th = new Thread(gs);
        th.start();

    }
}