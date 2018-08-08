/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.io.Files;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;

/**
 * @author XINEN
 */
public final class Zips {
    private static final int BUFFER = 2048;

    /**
     * 压缩文件或者文件夹 压缩。
     *
     * @param source 要压缩的文件或者文件夹 建议使用"c:/abc"或者"c:/abc/aaa.txt"这种形式来给定压缩路径 使用"c:\\abc"
     *               或者"c:\\abc\\aaa.txt"这种形式来给定路径的话，可能导致出现压缩和解压缩路径意外故障。。。
     * @param target 压缩后的zip文件名称 压缩后的目录组织与windows的zip压缩的目录组织相同。 会根据压缩的目录的名称，在压缩文件夹中创建一个改名的根目录，
     *               其它压缩的文件和文件夹都在该目录下依照原来的文件目录组织形式
     * @throws IOException 压缩文件的过程中可能会抛出IO异常，请自行处理该异常。
     */
    public static File zip(File source, File target)
            throws IOException {
        ZipOutputStream zos = new ZipOutputStream(
                Files.asByteSink(target).openStream());

        if (source.isDirectory()) {
            // 如果直接压缩文件夹
            zipDir(source.getAbsolutePath(), zos, source.getName() + File.separatorChar);// 此处使用/来表示目录，如果使用\\来表示目录的话，会导致压缩后的文件目录组织形式在解压缩的时候不能正确识别。

        } else {
            // 如果直接压缩文件
            zipDir(source.getPath(), zos, new File(source.getParent()).getName()
                    + File.separatorChar);
            zipFile(source.getPath(), zos, new File(source.getParent()).getName()
                    + File.separatorChar
                    + source.getName());

        }

        zos.closeEntry();
        zos.close();

        return target;
    }

    /**
     * zip 压缩单个文件。 除非有特殊需要，否则请调用ZIP方法来压缩文件！
     *
     * @param source 要压缩的原文件
     * @param zos    压缩后的文件名
     * @param target 压缩后的文件名
     * @throws IOException 抛出文件异常
     */
    public static void zipFile(String source, ZipOutputStream zos, String target)
            throws IOException {

        ZipEntry ze = new ZipEntry(target);
        FileInputStream fis = null;
        try {
            zos.putNextEntry(ze);

            // 读取要压缩文件并将其添加到压缩文件中
            fis = new FileInputStream(new File(source));
            byte[] bf = new byte[BUFFER];
            int location = 0;
            while ((location = fis.read(bf)) != -1) {
                zos.write(bf, 0, location);
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 压缩目录。 除非有特殊需要，否则请调用ZIP方法来压缩文件！
     *
     * @param source 需要压缩的目录位置
     * @param zos    压缩到的zip文件
     * @param target 压缩到的目标位置
     * @throws IOException 压缩文件的过程中可能会抛出IO异常，请自行处理该异常。
     */
    public static void zipDir(String source, ZipOutputStream zos,
                              String target) throws IOException {

        ZipEntry ze = new ZipEntry(target);
        zos.putNextEntry(ze);
        // 提取要压缩的文件夹中的所有文件
        File f = new File(source);
        File[] flist = f.listFiles();
        if (flist != null) {
            // 如果该文件夹下有文件则提取所有的文件进行压缩
            for (File fsub : flist) {
                if (fsub.isDirectory()) {
                    // 如果是目录则进行目录压缩
                    zipDir(fsub.getPath(), zos, target + fsub.getName() + File.separatorChar);
                } else {
                    // 如果是文件，则进行文件压缩
                    zipFile(fsub.getPath(), zos, target + fsub.getName());
                }
            }
        }
    }

    /**
     * 解压缩zip文件
     *
     * @param source 要解压缩的zip文件
     * @param target 解压缩到的目录
     * @throws IOException 压缩文件的过程中可能会抛出IO异常，请自行处理该异常。
     */
    public static File unzip(File source, File target)
            throws IOException {
        // 创建压缩文件对象
        ZipFile zf = new ZipFile(source);

        // 获取压缩文件中的文件枚举
        Enumeration<? extends ZipEntry> en = zf.getEntries();
        int length = 0;
        byte[] b = new byte[BUFFER];

        // 提取压缩文件夹中的所有压缩实例对象
        while (en.hasMoreElements()) {
            ZipEntry ze = en.nextElement();

            // 创建解压缩后的文件实例对象
            File f = Paths.concat(target.getAbsolutePath(), ze.getName()).newFile();

            // 如果当前压缩文件中的实例对象是文件夹就在解压缩后的文件夹中创建该文件夹
            if (ze.isDirectory()) {
                f.mkdirs();
            } else {
                // 如果当前解压缩文件的父级文件夹没有创建的话，则创建好父级文件夹
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }

                // 将当前文件的内容写入解压后的文件夹中。
                OutputStream outputStream = new FileOutputStream(f);
                InputStream inputStream = zf.getInputStream(ze);
                while (inputStream != null && (length = inputStream.read(b)) > 0) {
                    outputStream.write(b, 0, length);
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                outputStream.close();
            }
        }
        zf.close();

        return target;
    }
}
