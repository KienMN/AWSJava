import com.amazonaws.services.rekognition.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Created by kienmaingoc on 6/28/17.
 */
public class FileChooserUI extends JFrame implements ActionListener {

    JFrame frame;
    JButton chooseSourceImageButton;
    JButton chooseTargetImagesButton;
    JButton compareButton;
    JButton nextImageButton;
    JButton prevImageButton;
    JLabel resultLabel;
    JLabel targetImageLabel;
    JLabel sourceImageLabel;
    JProgressBar progressBar;

    String sourceImagePath = "";
    Map<Integer, String> fileNames = new HashMap<Integer, String>();    //file index - file name
    Map<String, String> filePaths = new HashMap<String, String>();      //file name - file path
    Map<String, Float> result = new HashMap<String, Float>();           //file name - confidence of result
    ArrayList<ImageIcon> targetImageIconResult = new ArrayList<ImageIcon>();
    int targetIndex = 0;

    final int FRAME_HEIGHT = 600;
    final int FRAME_WIDTH = 1200;
    final int STANDARD_BUTTON_HEIGHT = 20;
    final int STANDARD_BUTTON_WIDTH = 200;
    final int IMAGE_ICON_WIDTH = 600;
    final int IMAGE_ICON_HEIGHT = 400;


    FileChooserUI() {
        frame = new JFrame("Test");

        chooseTargetImagesButton = new JButton("Open Target Photos");
        chooseTargetImagesButton.addActionListener(this);
        chooseTargetImagesButton.setBounds(610, 20, STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT);

        chooseSourceImageButton = new JButton("Open Source Photo");
        chooseSourceImageButton.addActionListener(this);
        chooseSourceImageButton.setBounds(390, 20, STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT);

        compareButton = new JButton("Compare");
        compareButton.addActionListener(this);
        compareButton.setBounds(500, 60, STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT);

        nextImageButton = new JButton("Next");
        nextImageButton.addActionListener(this);
        nextImageButton.setBounds(710, 520, 40, STANDARD_BUTTON_HEIGHT);

        prevImageButton = new JButton("Prev");
        prevImageButton.addActionListener(this);
        prevImageButton.setBounds(450, 520, 40, STANDARD_BUTTON_HEIGHT);

        progressBar = new JProgressBar(SwingConstants.HORIZONTAL,0, 100);

        sourceImageLabel = new JLabel("Select source image");
        sourceImageLabel.setHorizontalAlignment(JLabel.CENTER);
        sourceImageLabel.setBounds(0, 100, IMAGE_ICON_WIDTH, IMAGE_ICON_HEIGHT);

        targetImageLabel = new JLabel("Select target images");
        targetImageLabel.setHorizontalAlignment(JLabel.CENTER);
        targetImageLabel.setBounds(600, 100, IMAGE_ICON_WIDTH, IMAGE_ICON_HEIGHT);

        resultLabel = new JLabel("Result: 0%");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setBounds(500, 520, STANDARD_BUTTON_WIDTH, STANDARD_BUTTON_HEIGHT);

        frame.setLayout(null);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.add(chooseSourceImageButton);
        frame.add(chooseTargetImagesButton);
        frame.add(compareButton);
        frame.add(nextImageButton);
        frame.add(prevImageButton);
        frame.add(resultLabel);
        frame.add(progressBar);
        frame.add(sourceImageLabel);
        frame.add(targetImageLabel);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Choose a source image containing at least a face
        if (e.getSource() == chooseSourceImageButton) {
            JFileChooser fileChooser = new JFileChooser();
            int i = fileChooser.showOpenDialog(this);
            if (i ==  JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                sourceImageLabel.setText("");
                sourceImagePath = file.getPath();
                System.out.println(sourceImagePath);
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(sourceImagePath);
                    Image sourceImage = ImageIO.read(inputStream);
                    ImageIcon icon = new ImageIcon(sourceImage.getScaledInstance(IMAGE_ICON_WIDTH, IMAGE_ICON_HEIGHT, Image.SCALE_SMOOTH));
                    sourceImageLabel.setIcon(icon);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        //Choose one or more target images
        if (e.getSource() == chooseTargetImagesButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            int i = fileChooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                fileNames.clear();
                filePaths.clear();
                String choosenFiles = "";
                File[] files = fileChooser.getSelectedFiles();

                for (int j = 0; j < files.length; j++) {
                    fileNames.put(j, files[j].getName());
                    filePaths.put(files[j].getName(), files[j].getPath());
                    choosenFiles += (files[j].getName() + "; ");
                }
//                targetImageLabel.setHorizontalAlignment(JLabel.LEFT);
                targetImageLabel.setIcon(null);
                targetImageLabel.setText(choosenFiles);
            }
            chooseTargetImagesButton.setText("Change Target Images");
            System.out.println(fileNames.toString());
            System.out.println(filePaths.toString());
        }

        //managing comparing task
        if (e.getSource() == compareButton) {
            targetIndex = 0;
            CompareFacesResult compareFacesResult = null;

            result.clear();
            targetImageIconResult.clear();

            //compare faces for each images and get data
            for (int i = 0; i < fileNames.size(); i++) {
                int val = i;
//                progressBar.setValue((val + 1) * 100 / fileNames.size());
                String fileName = fileNames.get(i);
                String filePath = filePaths.get(fileName);

                //upload an image to aws s3
                S3Process.uploadFile(Main.BUCKET_NAME, fileName, filePath);

                //compare face using aws rekognition
                compareFacesResult = RekognitionProcess.compareFace(sourceImagePath, Main.BUCKET_NAME, fileName);
                System.out.println(compareFacesResult.toString());

                // face matched will be store in a list
                List<CompareFacesMatch> faceMatches  = compareFacesResult.getFaceMatches();

                // add analised target image
                try {
//                    ByteBuffer byteBuffer = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
                    InputStream inputStream = new FileInputStream(filePath);
                    Image image = ImageIO.read(inputStream);
                    Graphics2D graph = (Graphics2D) image.getGraphics();

                    if (faceMatches.isEmpty()) {
                        result.put(fileName, (float) 0);
                    } else {
                        for (CompareFacesMatch match : faceMatches) {
                            //draw bound of face
                            int matchX = (int) (match.getFace().getBoundingBox().getLeft() * image.getWidth(this));
                            int matchY = (int) (match.getFace().getBoundingBox().getTop() * image.getHeight(this));
                            int matchW = (int) (match.getFace().getBoundingBox().getWidth() * image.getWidth(this));
                            int matchH = (int) (match.getFace().getBoundingBox().getHeight() * image.getHeight(this));
                            graph.setStroke(new BasicStroke(5));
                            graph.drawRect(matchX, matchY, matchW, matchH);

                            //get confidence of result
                            result.put(fileName, match.getFace().getConfidence());
                        }
                    }

                    ImageIcon icon = new ImageIcon(image.getScaledInstance(IMAGE_ICON_WIDTH, IMAGE_ICON_HEIGHT, Image.SCALE_SMOOTH));
                    targetImageIconResult.add(icon);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
            progressBar.setValue(100);
            targetImageLabel.setIcon(targetImageIconResult.get(targetIndex));
            resultLabel.setText("Result: " + result.get(fileNames.get(targetIndex)) + "%");

            //display source image with bound of face
            try {
                InputStream inputStream = new FileInputStream(sourceImagePath);
                Image sourceImage = ImageIO.read(inputStream);
                if (compareFacesResult != null) {
                    //draw bound of source face
                    ComparedSourceImageFace sourceFace = compareFacesResult.getSourceImageFace();
                    int x = (int) (sourceFace.getBoundingBox().getLeft() * sourceImage.getWidth(this));
                    int y = (int) (sourceFace.getBoundingBox().getTop() * sourceImage.getHeight(this));
                    int w = (int) (sourceFace.getBoundingBox().getWidth() * sourceImage.getWidth(this));
                    int h = (int) (sourceFace.getBoundingBox().getHeight() * sourceImage.getHeight(this));
                    Graphics2D graph = (Graphics2D) sourceImage.getGraphics();
                    graph.setStroke(new BasicStroke(5));
                    graph.drawRect(x, y, w, h);
                }
                ImageIcon icon = new ImageIcon(sourceImage.getScaledInstance(IMAGE_ICON_WIDTH, IMAGE_ICON_HEIGHT, Image.SCALE_SMOOTH));
                sourceImageLabel.setIcon(icon);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        if (e.getSource() == nextImageButton) {
            targetIndex++;
            if (targetIndex == targetImageIconResult.size()) {
                targetIndex = 0;
            }
            targetImageLabel.setIcon(targetImageIconResult.get(targetIndex));
            resultLabel.setText("Result: " + result.get(fileNames.get(targetIndex)) + "%");
        }

        if (e.getSource() == prevImageButton) {
            targetIndex--;
            if (targetIndex == -1) {
                targetIndex = targetImageIconResult.size() - 1;
            }
            targetImageLabel.setIcon(targetImageIconResult.get(targetIndex));
            resultLabel.setText("Result: " + result.get(fileNames.get(targetIndex)) + "%");
        }
    }
}
