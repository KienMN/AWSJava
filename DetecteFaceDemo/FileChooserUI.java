import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by kienmaingoc on 6/26/17.
 */
public class FileChooserUI extends JFrame implements ActionListener {
    JButton button;
    JLabel notification;

    FileChooserUI() {
        JFrame jFrame = new JFrame("Detect Faces");
        JLabel jLabel = new JLabel("Choose an image");
        notification = new JLabel();
        button = new JButton("Open");

        jFrame.setSize(400, 400);
        jFrame.setLocationRelativeTo(null);
        jFrame.setLayout(null);

        jLabel.setBounds(100, 100,200, 40);
        notification.setBounds(100, 300, 200, 40);

        button.setBounds(100, 200,200,50);
        button.addActionListener(this);

        jFrame.add(jLabel);
        jFrame.add(notification);
        jFrame.add(button);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            JFileChooser fileChooser = new JFileChooser();
            int i = fileChooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = file.getPath();
                if (UploadFile.uploadFile(Main.AccessKeyID, Main.SecretAccessKey, Main.Region, Main.BucketName, Main.Key, filePath)) {
                    notification.setText("Upload Successfully");
                    new DisplayDetectFaces(Main.AccessKeyID, Main.SecretAccessKey, Main.Region, Main.BucketName, Main.Key);
                }

            }
        }
    }
}
