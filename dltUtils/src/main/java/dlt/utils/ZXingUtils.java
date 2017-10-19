package dlt.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by denglt on 16/9/30.
 */
public class ZXingUtils {
    private ZXingUtils() {
    }

    private static final String CHARSET = "utf-8";

    /**
     * 编码为字节
     * @param content
     * @param format
     * @param width
     * @param height
     * @param formatName
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public static byte[] encode(String content,
                                BarcodeFormat format,
                                int width,
                                int height,
                                String formatName) throws IOException,WriterException{
        BufferedImage bufferedImage = encode(content,format,width,height);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,formatName,byteArrayOutputStream);
        return  byteArrayOutputStream.toByteArray();
    }

    /**
     * 编码为内存图片
     * @param content
     * @param format
     * @param width
     * @param height
     * @return
     * @throws WriterException
     */
    public static BufferedImage encode(String content,
                                       BarcodeFormat format,
                                       int width,
                                       int height) throws WriterException {
        BitMatrix bitMatrix = _encode(content, format, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }


    /**
     * 编码直接保存为图片
     * @param content
     * @param format
     * @param width
     * @param height
     * @param filePath
     * @throws IOException
     * @throws WriterException
     */
    public static void encode(String content,
                              BarcodeFormat format,
                              int width,
                              int height,
                              Path filePath) throws IOException, WriterException {
        BitMatrix bitMatrix = _encode(content, format, width, height);
        String filename = filePath.getFileName().toString();
        String[] splits = filename.split("\\.");
        String ext = splits.length == 1 ? "png" : splits[splits.length - 1];
        MatrixToImageWriter.writeToPath(bitMatrix, ext, filePath);
    }

    /**
     * 解码图片文件
     * @param input
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    public static Result decode(Path input) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(input.toFile());
        return _decode(bufferedImage);
    }

    /**
     * 解码流
     * @param input
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    public static Result decode(InputStream input) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(input);
        return _decode(bufferedImage);
    }

    /**
     * 解码字节数组
     * @param input
     * @return
     * @throws IOException
     * @throws NotFoundException
     */
    public static Result decode(byte[] input) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(input));
        return _decode(bufferedImage);
    }

    private static BitMatrix _encode(String content,
                                     BarcodeFormat format,
                                     int width,
                                     int height) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, format, width, height, hints);
        return bitMatrix;
    }

    private static Result _decode(BufferedImage bufferedImage) throws NotFoundException {
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(luminanceSource);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        MultiFormatReader reader = new MultiFormatReader();
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        Result result = reader.decode(binaryBitmap, hints);
        return result;
    }
}
