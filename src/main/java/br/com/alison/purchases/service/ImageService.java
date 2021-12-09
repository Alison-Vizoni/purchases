package br.com.alison.purchases.service;

import br.com.alison.purchases.service.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile){
        String extension = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
        if(!"png".equals(extension) && !"jpg".equals(extension)){
            throw new FileException("Png or jpg images only");
        }

        try {
            BufferedImage image = ImageIO.read(uploadedFile.getInputStream());
            if("png".equals(extension)){
                image = pnjToJpg(image);
            }
            return image;
        } catch (IOException e) {
            throw new FileException("Unable to read the file");
        }
    }

    private BufferedImage pnjToJpg(BufferedImage image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return bufferedImage;
    }

    public InputStream getInputStream(BufferedImage image, String extension){
        try {
            ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
            ImageIO.write(image, extension, inputStream);
            return new ByteArrayInputStream(inputStream.toByteArray());
        } catch(IOException e) {
            throw new FileException("Unable to read the file");
        }
    }

    public BufferedImage cropSquare(BufferedImage sourceImg) {
        int min = Math.min(sourceImg.getHeight(), sourceImg.getWidth());
        return Scalr.crop(
                sourceImg,
                (sourceImg.getWidth()/2) - (min/2),
                (sourceImg.getHeight()/2) - (min/2),
                min,
                min);
    }

    public BufferedImage resize(BufferedImage sourceImg, int size) {
        return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
    }
}
