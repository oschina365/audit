package com.kz.audit.controller;

import java.io.*;
import java.util.LinkedList;

public class AlipayAppCodeUpload {
    private java.lang.Process p;
    private InputStream is;
    private OutputStream os;
    private BufferedWriter bw;
    private BufferedReader br;
    private ProcessBuilder pb;
    private InputStream stdErr;

    public AlipayAppCodeUpload() {
    }

    /**
     * 获取Process的输入，输出流
     *
     * @param cmd
     */
    public void setCmd(String cmd) {
        try {
            p = Runtime.getRuntime().exec(cmd);
            os = p.getOutputStream();
            is = p.getInputStream();
            stdErr = p.getErrorStream();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * 向Process输出命令
     *
     * @param cmd
     */
    public void writeCmd(String cmd) {
        try {
            bw = new BufferedWriter(new OutputStreamWriter(os, getDefaultEncoding()));
            bw.write(cmd);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读出Process执行的结果
     *
     * @return
     */
    public String readCmd() {
        StringBuffer sb = new StringBuffer();
        try {
            br = new BufferedReader(new InputStreamReader(is, getDefaultEncoding()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String buffer = null;
        try {
            while ((buffer = br.readLine()) != null) {
                sb.append(buffer + "\n");
            }
            System.out.println(p.waitFor());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getDefaultEncoding() {
        if (getOS().trim().toLowerCase().startsWith("win")) {
            return "GBK";
        } else {
            return "UTF-8";
        }
    }

    public static String getOS() {
        String os = System.getProperty("os.name");
        System.out.println(os);
        return os;
    }

    public LinkedList<String> doCmd(LinkedList<String> lists) {
        LinkedList<String> list = new LinkedList<String>();
        for (String s : lists) {
            writeCmd(s);
            list.add(readCmd());
        }
        return list;
    }

    /**
     * 修改文件内容
     *
     * @param tenantCode
     * @return
     */
    private static boolean modifyFileContent1(String tenantCode) {

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile("D:\\tools\\bat\\alipay_upload.bat", "rw");
            String line = null;
            //记住上一次的偏移量
            long lastPoint = 0;
            while ((line = raf.readLine()) != null) {
                final long ponit = raf.getFilePointer();
                if (line.contains("miniuper")) {
                    String str = "miniuper upload -v 1.0.3";
                    raf.seek(lastPoint);
                    raf.writeBytes(str);
                }
                lastPoint = ponit;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 修改文件内容
     *
     * @param originStr
     * @param replaceStr
     * @return
     */
    private static boolean modifyFileContent(String filePath, String originStr, String replaceStr) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile("D:\\tools\\bat\\alipay_upload.bat", "rw");
            String line = null;
            //记住上一次的偏移量
            long lastPoint = 0;
            while ((line = raf.readLine()) != null) {
                final long ponit = raf.getFilePointer();
                if (line.contains(originStr)) {
                    raf.seek(lastPoint);
                    raf.writeBytes(replaceStr);
                }
                lastPoint = ponit;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}