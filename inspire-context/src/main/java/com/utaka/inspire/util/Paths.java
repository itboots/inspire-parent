/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * @author XINEN
 */
public abstract class Paths {

    public static final class PathHelper {
        private final String path;

        private PathHelper(String path) {
            this.path = path;
        }

        /**
         * 使用当前路径直接创建新文件
         */
        public File newFile() {
            return new File(this.path);

        }

        /**
         * 使用指定的后缀和当前路径直接创建新文件
         */
        public File newFile(String extend) {
            return new File(this.path + extend);

        }

        /**
         * 列出当前路径下的所有文件
         *
         * @param filter 指定过滤文件的对象
         */
        public File[] listFiles(FileFilter filter) {
            List<File> files = listFiles(this.newFile(), filter);
            return files.toArray(new File[files.size()]);

        }

        /**
         * 列出当前路径下的所有文件
         *
         * @param filter 指定过滤文件的对象
         */
        public List<File> list(FileFilter filter) {
            return listFiles(this.newFile(), filter);
        }

        /**
         * 根据当前路径创建目录
         *
         * @return
         */
        public File mkdirs() {
            File dir = this.newFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        }

        @Override
        public String toString() {
            return this.path;
        }

        private static List<File> listFiles(File file, FileFilter filter) {
            if (file.isFile() && filter != null && filter.accept(file)) {
                return Lists.newArrayList(file);

            }

            List<File> result = Lists.newArrayList();

            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isDirectory()) {
                    result.addAll(listFiles(f, filter));

                } else {
                    if (filter != null && filter.accept(f)) {
                        result.add(f);
                    }
                }
            }

            return result;

        }
    }

    /**
     * 连接路径,构造 {@link PathHelper} 对象
     * <pre>
     * /foo/ + bar          -->   /foo/bar
     * /foo + bar           -->   /foo/bar
     * /foo + /bar          -->   /foo/bar
     * c:/foo + C:/bar        -->   C:/bar
     * c:/foo + C:bar         -->   C:bar (*)
     * /foo/a/ + ../bar     -->   foo/bar
     * /foo/ + ../../bar    -->   null
     * /foo/ + /bar         -->   /bar
     * /foo/.. + /bar       -->   /bar
     * /foo + bar/c.txt     -->   /foo/bar/c.txt
     * </pre>
     */
    public static PathHelper concat(String... paths) {
        File file = new File(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            file = new File(file, paths[i]);
        }

        return new PathHelper(file.getPath());
    }


    /**
     * 构造 {@link PathHelper} 对象
     */
    public static PathHelper with(String path) {
        return new PathHelper(path);
    }
}
