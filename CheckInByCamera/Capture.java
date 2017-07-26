import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import com.amazonaws.services.rekognition.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

/**
 * Created by kienmaingoc on 7/9/17.
 */
public class Capture extends JFrame implements ActionListener {
    JFrame jFrame;
    JButton captureButton;
    JLabel checkinStatus;

    Capture() {
        jFrame = new JFrame("Test");
        jFrame.setLayout(new FlowLayout());

        captureButton = new JButton("Capture");
        captureButton.addActionListener(this);

        checkinStatus = new JLabel("Hello");

        jFrame.add(captureButton);
        jFrame.add(checkinStatus);

        jFrame.setSize(400, 500);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setFocusable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == captureButton) {
            cvSaveImage("Test.jpg", Camera.image);
            try {
                InputStream inputStream = new FileInputStream("Test.jpg");
                ByteBuffer byteBuffer = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
                Image image = new Image().withBytes(byteBuffer);
                SearchFacesByImageResult result = RekognitionProcess.searchFaces(image);
                System.out.println(result.toString());
                if (result.getFaceMatches().isEmpty()) {
                    System.out.println("No face found!");
                } else {
                    List<FaceMatch> faceMatchList = result.getFaceMatches();
                    for (FaceMatch faceMatch: faceMatchList) {
                        checkinStatus.setText("Hello " + faceMatch.getFace().getExternalImageId());
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
