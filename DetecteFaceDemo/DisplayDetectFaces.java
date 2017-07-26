import com.amazonaws.services.rekognition.model.FaceDetail;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by kienmaingoc on 6/26/17.
 */
public class DisplayDetectFaces {
    DisplayDetectFaces(String accessKeyID, String secretAccessKey, String region, String bucketName, String key) {
        //get image
        BufferedImage image = GetFile.getFile(accessKeyID, secretAccessKey, region, bucketName, key);
        //get detail of detected faces
        List<FaceDetail> facesDetail = DetectFaces.detectFaces(accessKeyID, secretAccessKey, region, bucketName, key);

        //size of image
        int width = image.getWidth();
        int height = image.getHeight();

        Graphics2D graph = image.createGraphics();

        //draw rectangles containing faces
        for (FaceDetail faceDetail : facesDetail) {
            int x = (int) (faceDetail.getBoundingBox().getLeft() * width);
            int y = (int) (faceDetail.getBoundingBox().getTop() * height);
            int w = (int) (faceDetail.getBoundingBox().getWidth() * width);
            int h = (int) (faceDetail.getBoundingBox().getHeight() * height);
            graph.drawRect(x, y, w, h);
        }

        ImageIcon icon = new ImageIcon(image);

        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(image.getWidth(),image.getHeight());
        frame.setLocationRelativeTo(null);

        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
