package com.kz.audit.util;


import cn.hutool.http.HttpUtil;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件处理工具类
 *
 * @author jmy
 * @version 1.0.7
 * @date 2020/3/31 0031 14:01
 */
public class FileUtils {

    /**
     * 将图片进行缩放并返回流
     *
     * @param is
     * @param width
     * @param height
     * @return
     */
    public static InputStream resizeImage(InputStream is, Integer width, Integer height) throws IOException {
        BufferedImage image = Thumbnails.of(is).size(width, height).asBufferedImage();
        return bufferedImageToInputStream(image);
    }

    /**
     * 将BufferedImage转换为InputStream
     *
     * @param image
     * @return
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 网络图片转化为二进制数组
     *
     * @param url
     * @return
     */
    public static byte[] getFileStream(String url) {
        try {
            // 得到图片的二进制数据
            byte[] btImg = readInputStream(getFileStreamFromUrl(url));
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片转化
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 获取网络文件流
     *
     * @param url
     * @return
     */
    public static InputStream getFileStreamFromUrl(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String downloadImage(String fileUrl) {
        long l = 0L;
        String path = null;
        if (fileUrl != null) {
            //下载时文件名称
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/"));
            try {
                path = "/download/qiying_coupon" + fileName;
                HttpUtil.downloadFile(fileUrl, path);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
        System.out.println(System.currentTimeMillis() - l);
        return path;
    }
}
