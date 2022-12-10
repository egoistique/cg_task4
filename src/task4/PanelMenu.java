package task4;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PanelMenu extends JPanel {

    private final JButton buttonChoose = new JButton("Выбрать");
    private final JButton buttonCompress = new JButton("Сжать");
    private final JFileChooser fileChooser = new JFileChooser();

    private File selectedFile = null;

    public PanelMenu(PanelImage panelImage, PanelImage panelImageCompressed) {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(100, 100));
        setMaximumSize(new Dimension(100, 100));

        add(buttonChoose);
        add(buttonCompress);
        fileChooser.setSize(new Dimension(300, 300));
        fileChooser.setCurrentDirectory(new File((GraphicApp.class.getResource("images")).getPath()));
        fileChooser.setLocation(-500, -100);

        buttonChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(getThis());
            }
        });
        buttonCompress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RLE rle = new RLE(selectedFile.getPath());
                    BufferedImage image = ImageIO.read(selectedFile);
                    rle.rleCompressing(image);
                    panelImageCompressed.setImage(rle.getAfterCompImage());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFile = fileChooser.getSelectedFile();
                try {
                    panelImageCompressed.deleteImage();
                    panelImage.setImage(selectedFile.getPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    private JPanel getThis() {
        return this;
    }
}
