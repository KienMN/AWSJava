import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.amazonaws.util.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

/**
 * Created by kienmaingoc on 7/9/17.
 */
public class UI extends JFrame implements ActionListener {
    JFrame jFrame;
    JButton captureButton;
    JButton addNewUserButton;
    JTextField newUserName;

    JLabel statusLabel;

    UI() {
        jFrame = new JFrame("Test");
        jFrame.setSize(300, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new FlowLayout());

        captureButton = new JButton("Capture");
        captureButton.addActionListener(this);

        addNewUserButton = new JButton("Add New");
        addNewUserButton.addActionListener(this);

        newUserName = new JTextField("Usename");
        newUserName.setSize(200, 40);

        statusLabel = new JLabel();

        jFrame.add(captureButton);
//        jFrame.add(addNewUserButton);
        jFrame.add(statusLabel);
//        jFrame.add(newUserName);
        jFrame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == captureButton) {
            long startTime = System.currentTimeMillis();
            cvSaveImage("Test.jpg", Camera.image);
            try {
                InputStream inputStream = new FileInputStream("Test.jpg");
                ByteBuffer byteBuffer = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
                Image image = new Image().withBytes(byteBuffer);
                SearchFacesByImageResult result = RekognitionProcess.searchFaces(image);
                System.out.println(result.toString());
                if (result == null) {
                    statusLabel.setText("Error Argument");
                }
                if (result.getFaceMatches().isEmpty()) {
                    statusLabel.setText("Who is that!");
                } else {
                    List<FaceMatch> faceMatchList = result.getFaceMatches();
                    for (FaceMatch faceMatch: faceMatchList) {
                        statusLabel.setText("Hello " + faceMatch.getFace().getExternalImageId());
                        Date date = new Date();
                        DatabaseProcess.updateDatabase(faceMatch.getFace().getExternalImageId(), date.toString());
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Time recognition: " + (endTime - startTime));
        }

        if (e.getSource() == addNewUserButton) {
            jFrame.add(newUserName);
            JFileChooser fileChooser = new JFileChooser();
            int i = fileChooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
            }
        }
    }
}
