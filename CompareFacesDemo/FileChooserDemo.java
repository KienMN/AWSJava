import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by kienmaingoc on 6/27/17.
 */
public class FileChooserDemo extends JFrame implements ActionListener{

    JButton button1;
    JButton button2;
    JButton button3;
    JFrame jFrame;

    FileChooserDemo() {

        jFrame = new JFrame();
        jFrame.setSize(1200,600);
        jFrame.setLayout(new FlowLayout());
        button1 = new JButton("Image 1");
        button1.addActionListener(this);
        button2 = new JButton("Image 2");
        button2.addActionListener(this);
        button3 = new JButton("Compare");

        jFrame.add(button1);
        jFrame.add(button2);
        jFrame.add(button3);

        jFrame.setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            JFileChooser fileChooser = new JFileChooser();
            int i = fileChooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = file.getPath();
                try {
                    BufferedImage image = ImageIO.read(file);
                    ImageIcon icon = new ImageIcon(image.getScaledInstance(500,500,Image.SCALE_SMOOTH));
                    button1.setVisible(false);
                    JLabel label = new JLabel(icon);
                    jFrame.add(label);
                    jFrame.pack();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }

        if (e.getSource() == button2) {
            JFileChooser fileChooser = new JFileChooser();
            int i = fileChooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = file.getPath();
                try {
                    BufferedImage image = ImageIO.read(file);
                    ImageIcon icon = new ImageIcon(image.getScaledInstance(500,500,Image.SCALE_SMOOTH));
                    button2.setVisible(false);
                    JLabel label = new JLabel(icon);
                    jFrame.add(label);
                    jFrame.pack();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        new FileChooserDemo();
    }
}
