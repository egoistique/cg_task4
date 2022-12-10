package task4;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PanelImage extends JPanel {

    public final int WIDTH_OF_IMG = 500;
    public final int HEIGHT_OF_IMG = 500;

    private File uploadedImage;
    private long sizeOfFile;

    public void setImage(String path) throws IOException {
        uploadedImage = new File(path);
        sizeOfFile = uploadedImage.length();
        revalidate();
        repaint();
    }

    public void setImage(BufferedImage image) throws IOException {
        uploadedImage = new File("image.jpg");
        ImageIO.write(image, "jpg", uploadedImage);
        sizeOfFile = uploadedImage.length();
        revalidate();
        repaint();
    }

    public void deleteImage() {
        uploadedImage = null;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (uploadedImage != null) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(uploadedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (image != null) {
                int height = image.getHeight();
                int width = image.getWidth();
                int finalHeight;
                int finalWeight;
                double coeff = (double) width / WIDTH_OF_IMG;
                if (height / coeff > HEIGHT_OF_IMG) {
                    coeff = (double) height / HEIGHT_OF_IMG;
                    finalHeight = HEIGHT_OF_IMG;
                    finalWeight = (int) (width / coeff);
                } else {
                    finalHeight = (int) (height / coeff);
                    finalWeight = WIDTH_OF_IMG;
                }
                Image img = image.getScaledInstance(finalWeight, finalHeight, 16);
                g.drawImage(img, (this.getWidth() - finalWeight) / 2 + 10, (this.getHeight() - finalHeight) / 2 + 10, this);
                g.drawString((String.format("%.3f", (double) sizeOfFile / 1024) + "КБ"), 200, 40);
            }
        } else {
            removeAll();
        }
    }
}
