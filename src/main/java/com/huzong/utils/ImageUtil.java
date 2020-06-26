package com.huzong.utils;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageUtil implements IImage{
    @Override
    public int[] getImgWidthHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int[] result = { 0, 0 };
        try {
            // 获得文件输入流
            is = new FileInputStream(file);
            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(is);
            result[0] =src.getWidth(null); // 得到源图片宽
            result[1] =src.getHeight(null);// 得到源图片高
            is.close();  //关闭输入流
        } catch (Exception ef) {
            ef.printStackTrace();
        }

        return result;
    }

    @Override
    public void reduceImg(File file, String dest, Float rate) {
        try {
            File destFile = new File(dest);
            if(!destFile.exists()){
                destFile.createNewFile();
            }
            BufferedImage bufImg = ImageIO.read(file); //读取图片
            // 如果比例不为空则说明是按比例压缩
            if (rate != null && rate > 0) {
                //获得源图片的宽高存入数组中
                int[] results = getImgWidthHeight(file);
                if (results != null && results[0] != 0 && results[1] != 0) {
                    AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(rate, rate), null);
                    BufferedImage Itemp;//设置缩放目标图片模板
                    Itemp = ato.filter(bufImg, null);
                    ImageIO.write(Itemp, "jpg", destFile); //写入缩减后的图片
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
