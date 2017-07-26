/**
 * Created by kienmaingoc on 6/26/17.
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;

import javax.imageio.ImageIO;
import javax.swing.*;

public class CompareFacesExample {
    public static void main(String[] args) throws Exception{ Float similarityThreshold = 70F;
        String sourceImage = "/Users/kienmaingoc/Desktop/source.jpg";
        String targetImage = "/Users/kienmaingoc/Desktop/target.jpg";
        ByteBuffer sourceImageBytes = null;
        ByteBuffer targetImageBytes = null;

        BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAITNPIQ3TMODHVGGA", "qYcQwe6SSJmvV+iqnGvJAxt/8eF0rEbxtEDrr5Ni");
//        EndpointConfiguration endpoint=new EndpointConfiguration("endpoint", "eu-west-1");
        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
                .standard()
                .withRegion("eu-west-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        //Load source and target images and create input parameters
        try (InputStream inputStream = new FileInputStream(new File(sourceImage))) {
            sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        }
        catch(Exception e)
        {
            System.out.println("Failed to load source image " + sourceImage);
            System.exit(1);
        }
        try (InputStream inputStream = new FileInputStream(new File(targetImage))) {
            targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        }
        catch(Exception e)
        {
            System.out.println("Failed to load target images: " + targetImage);
            System.exit(1);
        }
        Image source=new Image().withBytes(sourceImageBytes);
        Image target=new Image().withBytes(targetImageBytes);
        CompareFacesRequest request = new CompareFacesRequest()
                .withSourceImage(source)
                .withTargetImage(target)
                .withSimilarityThreshold(similarityThreshold);
        // Call operation
        CompareFacesResult compareFacesResult=rekognitionClient.compareFaces(request);
        // Display results
        List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();

        for (CompareFacesMatch match: faceDetails){
            ComparedFace face = match.getFace();
            BoundingBox position = face.getBoundingBox();
            System.out.println("Face at " + position.getLeft().toString()
                    + " " + position.getTop()
                    + " matches with " + face.getConfidence().toString()
                    + "% confidence.");
            System.out.println(compareFacesResult.getSourceImageFace().toString());
            System.out.println(match.toString());
        }
        List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();
        System.out.println("There were " + uncompared.size()
                + " that did not match");
        System.out.println("Source image rotation: " +
                compareFacesResult.getSourceImageOrientationCorrection());
        System.out.println("Target image rotation: " +
                compareFacesResult.getTargetImageOrientationCorrection());

        BufferedImage srcImage = ImageIO.read(new File(sourceImage));
        BufferedImage tagImage = ImageIO.read(new File(targetImage));

        Graphics2D graph = srcImage.createGraphics();
        graph.drawRect((int) (0.30333334 * srcImage.getWidth()), (int) (0.14691152 * srcImage.getHeight()),
                (int) (0.35666665 * srcImage.getWidth()), (int) (0.53589314 * srcImage.getHeight()));

        JFrame jFrame = new JFrame();

        ImageIcon sourceIcon = new ImageIcon(srcImage.getScaledInstance(500,500, java.awt.Image.SCALE_SMOOTH));
        ImageIcon targetIcon = new ImageIcon(tagImage.getScaledInstance(500,500, java.awt.Image.SCALE_SMOOTH));

        JLabel sourceLabel = new JLabel(sourceIcon);
        sourceLabel.setBounds(0,0,100,100);
        JLabel targetLabel = new JLabel(targetIcon);
        targetLabel.setBounds(0,100,100,100);

        jFrame.setLayout(new FlowLayout());
        jFrame.setSize(1200,500);

        jFrame.add(sourceLabel);
        jFrame.add(targetLabel);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);


    }
}