import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedSourceImageFace;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Created by kienmaingoc on 6/27/17.
 */
public class FileChooserUI extends JFrame implements ActionListener {
    JFrame frame;
    JButton sourceImageChooseButton;
    JButton targetImageChooseButton;
    JButton compareButton;
    JLabel sourceImageLabel;
    JLabel targetImageLabel;
    JLabel percentagesResultLabel;
    JLabel percentages;
    Image sourceImage = null;
    Image targetImage = null;

    final int FRAME_WIDTH = 1200;
    final int FRAME_HEIGHT = 700;
    final int BUTTON_WIDTH = 250;
    final int BUTTON_HEIGHT = 40;
    final int LABEL_WIDTH = 500;
    final int LABEL_HEIGHT = 400;

    String sourcePath = "";
    String targetPath = "";

    FileChooserUI() {
        frame = new JFrame("Compare Faces Demo");
//        frame.setLayout(new FlowLayout());
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);

        sourceImageChooseButton = new JButton("Source Image");
        sourceImageChooseButton.setBounds(300, 20, BUTTON_WIDTH, BUTTON_HEIGHT);
        sourceImageChooseButton.addActionListener(this);
        targetImageChooseButton = new JButton("Target Image");
        targetImageChooseButton.setBounds(650, 20, BUTTON_WIDTH, BUTTON_HEIGHT);
        targetImageChooseButton.addActionListener(this);
        compareButton = new JButton("Compare");
        compareButton.setBounds(450, 520, BUTTON_WIDTH + 50, BUTTON_HEIGHT);
        compareButton.addActionListener(this);

        sourceImageLabel = new JLabel("Open Source Image");
        sourceImageLabel.setBounds(100, 100, LABEL_WIDTH, LABEL_HEIGHT);
        sourceImageLabel.setVerticalAlignment(JLabel.CENTER);
        sourceImageLabel.setHorizontalAlignment(JLabel.CENTER);

        targetImageLabel = new JLabel("Open Target Image");
        targetImageLabel.setVerticalAlignment(JLabel.CENTER);
        targetImageLabel.setHorizontalAlignment(JLabel.CENTER);
//        targetImageLabel.setBorder();
        targetImageLabel.setBounds(600, 100, LABEL_WIDTH, LABEL_HEIGHT);

        percentagesResultLabel = new JLabel("");
        percentages = new JLabel("Result: 0%");
        percentages.setBounds(450, 600, BUTTON_WIDTH + 50, BUTTON_HEIGHT);
//        percentagesResultLabel.setBounds(450,600,BUTTON_WIDTH + 50, BUTTON_HEIGHT);

        frame.add(sourceImageChooseButton);
        frame.add(targetImageChooseButton);
        frame.add(sourceImageLabel);
        frame.add(targetImageLabel);
        frame.add(compareButton);
        frame.add(percentages);
        frame.add(percentagesResultLabel);      //BUG ??
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sourceImageChooseButton) {
            JFileChooser fileChooser = new JFileChooser();
            int i = fileChooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                sourcePath = file.getPath();
                try {
                    InputStream inputStream = new FileInputStream(file);
                    sourceImage = ImageIO.read(inputStream);
                    ImageIcon icon = new ImageIcon(sourceImage.getScaledInstance(500,400,Image.SCALE_SMOOTH));
                    sourceImageLabel.setIcon(icon);
                    sourceImageLabel.setText("");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } else if (e.getSource() == targetImageChooseButton) {
            JFileChooser fileChooser = new JFileChooser();
            int i = fileChooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                targetPath = file.getPath();
                try {
                    InputStream inputStream = new FileInputStream(file);
                    targetImage = ImageIO.read(inputStream);
                    ImageIcon icon = new ImageIcon(targetImage.getScaledInstance(500,400, Image.SCALE_SMOOTH));
                    targetImageLabel.setIcon(icon);
                    targetImageLabel.setText("");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } else if (e.getSource() == compareButton) {
            CompareFacesResult compareFacesResult = CompareFacesDemo.compareFaces(sourcePath, targetPath);
            if (compareFacesResult != null) {
                ComparedSourceImageFace sourceFace = compareFacesResult.getSourceImageFace();
                int x = (int) (sourceFace.getBoundingBox().getLeft() * sourceImage.getWidth(this));
                int y = (int) (sourceFace.getBoundingBox().getTop() * sourceImage.getHeight(this));
                int w = (int) (sourceFace.getBoundingBox().getWidth() * sourceImage.getWidth(this));
                int h = (int) (sourceFace.getBoundingBox().getHeight() * sourceImage.getHeight(this));

                Graphics2D graph = (Graphics2D) sourceImage.getGraphics();
                graph.setStroke(new BasicStroke(5));

                graph.drawRect(x, y, w, h);
                ImageIcon icon = new ImageIcon(sourceImage.getScaledInstance(500,400,Image.SCALE_SMOOTH));
                sourceImageLabel.setIcon(icon);


                System.out.println(x + " " + y + " " + w + " " + h + " " + sourceImage.getWidth(this) + " " +sourceImage.getHeight(this));

                String result = "Result: ";
                List<CompareFacesMatch> matchFaces = compareFacesResult.getFaceMatches();
                for (CompareFacesMatch match: matchFaces) {

                    int matchX = (int) (match.getFace().getBoundingBox().getLeft() * targetImage.getWidth(this));
                    int matchY = (int) (match.getFace().getBoundingBox().getTop() * targetImage.getHeight(this));
                    int matchW = (int) (match.getFace().getBoundingBox().getWidth() * targetImage.getWidth(this));
                    int matchH = (int) (match.getFace().getBoundingBox().getHeight() * targetImage.getHeight(this));
                    graph = (Graphics2D) targetImage.getGraphics();
                    graph.setStroke(new BasicStroke(5));
                    graph.drawRect(matchX, matchY, matchW, matchH);
                    result += (match.getFace().getConfidence() + " %" + "\n");

                }

                icon = new ImageIcon(targetImage.getScaledInstance(500,400, Image.SCALE_SMOOTH));
                targetImageLabel.setIcon(icon);
                percentages.setText(result);

            }
        }

    }

    public static void main(String[] args) {
        new FileChooserUI();
    }
}
