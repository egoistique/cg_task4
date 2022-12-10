package task4;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class RLE {
    private BufferedImage startImage, afterCompImage;
    private byte[] compressed = new byte[0];

    public RLE(String path) throws IOException {
        startImage = ImageIO.read(new File(path));
    }

    public void rleCompressing(BufferedImage image) throws IOException {
        startImage = image;
        Raster raster = startImage.getRaster();
        DataBufferByte bufferByte = (DataBufferByte) raster.getDataBuffer();
        byte[] sourceData = bufferByte.getData();

        compressed = compression(sourceData);
        byte[] dBytes = decompression(compressed);

        afterCompImage = new BufferedImage(startImage.getWidth(), startImage.getHeight(), startImage.getType());
        DataBufferByte dBufferByte = new DataBufferByte(dBytes, dBytes.length);
        Raster dRaster = Raster.createRaster(raster.getSampleModel(), dBufferByte, new Point());
        afterCompImage.setData(dRaster);

        File outputfile = new File("decomped.jpg");
        ImageIO.write(afterCompImage, "jpg", outputfile);
    }

    public byte[] decompression(byte[] bytes) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        for (int i = 0; i < bytes.length; i++) {
            byte count = (byte) (bytes[i] & 0b0011_1111);

            byte value = bytes[++i];
            for (int j = 0; j < count; j++) {
                byteStream.write(value);
            }
        }

        return byteStream.toByteArray();
    }

    public byte[] compression(byte[] bytes) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte count = 1;
        byte prev = bytes[1];

        for (int i = 1; i < bytes.length; i++) {
            byte curr = bytes[i];
            if (curr == prev && count < 63) {
                count++;
            } else {
                count |= 0b1100_0000;
                byteStream.write(count);
                byteStream.write(prev);

                count = 1;
            }
            prev = curr;
        }
        count |= 0b1100_0000;
        byteStream.write(count);
        byteStream.write(prev);
        return byteStream.toByteArray();
    }

    public BufferedImage getAfterCompImage() {
        return afterCompImage;
    }

//    Определение: Набор идущих подряд точек изображения одного цвета называется серией.Длина этого набора точек называется длиной серии.
//    В таблице, приведенной ниже, заданы два вида кодов:
//    Коды завершения серий — заданы с 0 до 63 с шагом 1.
}