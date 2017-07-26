import com.amazonaws.services.rekognition.model.CompareFacesResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by kienmaingoc on 6/29/17.
 */
public class MultiThreadMain extends JFrame implements ActionListener{

    final static String PHOTO_NAME = "Test.jpg";
    static boolean AUTHENTICATION = false;
    static Camera camera;

    public static void main(String[] args) {
        Identification identification = new Identification();
        camera = new Camera();
        Thread thread = new Thread(camera);
        CaptureAndCompare captureAndCompare = new CaptureAndCompare(identification);
        Thread thread1 = new Thread(captureAndCompare);
        thread.start();
        thread1.start();

        identification.waitStatusChange();

        camera.stop();
        String notification = "";
        CompareFacesResult result = captureAndCompare.getCompareFaceResult();
        if (result == null) {
            notification = "Failed";
        } else {
            if (result.getFaceMatches().isEmpty()) {
                notification = "You are not Kien";
            } else {
                notification = "Hello Kien";
            }
        }

        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(notification));
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());
        frame.setSize(400,500);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
