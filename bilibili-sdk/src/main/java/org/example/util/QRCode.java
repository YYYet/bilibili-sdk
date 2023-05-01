package org.example.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCode {

    int [][]arr={};

    public QRCode(){
        String imagPath="C:\\Desktop\\picture\\test.png";    //存放图片路径
        arr=getImagePixArray(imagPath);
    }

        public int[][] getImagePixArray(String path) {
        File file=new File(path);

        //缓冲图片对象
        BufferedImage buffimg=null;

        try {//读取文件给缓冲图
            buffimg = ImageIO.read(file);
        }catch(IOException e){
            e.printStackTrace();
        }

        // 获取图片尺寸
        int w=buffimg.getWidth();
        int h=buffimg.getHeight();
        int [][] imgarr=new int[w][h];
            int width=w/20;

        for(int i=width/2;i<w;i+=width) {
            String str="";

            for(int j=width/2;j<8*width;j+=width) {

                int rgb=buffimg.getRGB(i,j);
                Color color = new Color (rgb);
                int gray = ((color.getRed()+color.getGreen()+color.getGreen())/3);

                if (gray>125){
                    str+="0";
                }else {
                    str+="1";
                }
            }
            int i1 = Integer.parseInt(str, 2);
            System.out.println(str+ " --- "+(char)+i1);
        }
        return imgarr;
    }

    public static void main(String[] args) {
        new QRCode();    //创建一个对象即会调用构造函数，运行代码

    }


}