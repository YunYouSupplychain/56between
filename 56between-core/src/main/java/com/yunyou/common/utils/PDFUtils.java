package com.yunyou.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PDFUtils {

    public static List<String> icePdfToImage(byte[] pdf) {
        List<String> imageBase64List = Lists.newArrayList();
        Document document = new Document();
        try {
            document.setByteArray(pdf, 0, pdf.length, null);
            // 缩放比例
            float scale = 1.35f;
            // 旋转角度
            float rotation = 0f;
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, rotation, scale);
                imageBase64List.add(getImageBase64(image));
                image.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.dispose();
        return imageBase64List;
    }

    /**
     * 将PDF按页数每页转换成一个jpg图片
     */
    public static OutputStream pdfToImagePath(byte[] pdf, OutputStream os) {
        try {
            PDDocument doc = PDDocument.load(pdf);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                // 方式1,第二个参数是设置缩放比(即像素)
                // BufferedImage image = renderer.renderImageWithDPI(i, 296);
                // 方式2,第二个参数是设置缩放比(即像素)，第二个参数越大生成图片分辨率越高，转换时间也就越长
                BufferedImage image = renderer.renderImage(i, 1.25f);
                ImageIO.write(image, "PNG", os);
            }
            // 关闭文件,不然该pdf文件会一直被占用。
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os;
    }

    /**
     * 将PDF按页数每页转换成一个jpg图片
     */
    public static List<String> pdfToImagePath(byte[] pdf) {
        List<String> imageBase64List = Lists.newArrayList();
        try {
            PDDocument doc = PDDocument.load(pdf);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                // 方式1,第二个参数是设置缩放比(即像素)
                // BufferedImage image = renderer.renderImageWithDPI(i, 296);
                // 方式2,第二个参数是设置缩放比(即像素)，第二个参数越大生成图片分辨率越高，转换时间也就越长
                String imageBase64 = getImageBase64(renderer.renderImage(i, 1.35f));
                imageBase64List.add(imageBase64);
            }
            //关闭文件,不然该pdf文件会一直被占用。
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBase64List;
    }

    private static String getImageBase64(RenderedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        //转换成字节
        byte[] bytes = baos.toByteArray();
        //转换成base64串 删除 \r\n
        String base64 = Base64.encodeBase64String(bytes)
                .trim().replaceAll("\n", "").replaceAll("\r", "");
        baos.flush();
        baos.close();
        return base64;
    }

    /**
     * 将PDF按页数每页转换成一个jpg图片
     *
     * @param filePath
     */
    public static List<String> pdfToImagePath(String filePath) {
        List<String> list = new ArrayList<>();
        // 获取去除后缀的文件路径
        String fileDirectory = filePath.substring(0, filePath.lastIndexOf("."));

        String imagePath;
        File file = new File(filePath);
        try {
            File f = new File(fileDirectory);
            if (!f.exists()) {
                f.mkdir();
            }
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                // 方式1,第二个参数是设置缩放比(即像素)
                // BufferedImage image = renderer.renderImageWithDPI(i, 296);
                // 方式2,第二个参数是设置缩放比(即像素) 第二个参数越大生成图片分辨率越高，转换时间也就越长
                BufferedImage image = renderer.renderImage(i, 1.25f);
                imagePath = fileDirectory + "/" + i + ".jpg";
                ImageIO.write(image, "PNG", new File(imagePath));
                list.add(imagePath);
            }
            // 关闭文件,不然该pdf文件会一直被占用。
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * pdf转成一张图片
     */
    private static void pdf2multiImage(String pdfFile, String outPath) {
        try {
            InputStream is = new FileInputStream(pdfFile);
            PDDocument pdf = PDDocument.load(is);
            int actSize = pdf.getNumberOfPages();
            List<BufferedImage> picList = new ArrayList<>();
            for (int i = 0; i < actSize; i++) {
                BufferedImage image = new PDFRenderer(pdf).renderImageWithDPI(i, 130, ImageType.RGB);
                picList.add(image);
            }
            yPic(picList, outPath);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将宽度相同的图片，竖向追加在一起 ##注意：宽度必须相同
     *
     * @param picList 文件流数组
     * @param outPath 输出路径
     */
    public static void yPic(List<BufferedImage> picList, String outPath) {
        if (picList == null || picList.size() <= 0) {
            System.out.println("图片数组为空!");
            return;
        }
        try {
            // 总高度、总宽度、临时的高度，或保存偏移高度、临时的高度，主要保存每个高度、图片的数量
            int height = 0, width = 0, _height = 0, __height = 0, picNum = picList.size();
            // 保存每个文件的高度
            int[] heightArray = new int[picNum];
            // 保存图片流
            BufferedImage buffer;
            // 保存所有的图片的RGB
            List<int[]> imgRGB = new ArrayList<>();
            // 保存一张图片中的RGB数据
            int[] _imgRGB;
            for (int i = 0; i < picNum; i++) {
                buffer = picList.get(i);
                // 图片高度
                heightArray[i] = _height = buffer.getHeight();
                if (i == 0) {
                    // 图片宽度
                    width = buffer.getWidth();
                }
                // 获取总高度
                height += _height;
                // 从图片中读取RGB
                _imgRGB = new int[width * _height];
                _imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
                imgRGB.add(_imgRGB);
            }
            // 设置偏移高度为0
            _height = 0;
            // 生成新图片
            BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < picNum; i++) {
                __height = heightArray[i];
                // 计算偏移高度
                if (i != 0) {
                    _height += __height;
                }
                imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width);
            }
            File outFile = new File(outPath);
            // 写图片
            ImageIO.write(imageResult, "jpg", outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}