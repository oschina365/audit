package com.kz.audit.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.kz.audit.dao.alipay.AuditAlipayDAO;
import com.kz.audit.dao.alipay.vo.AlipayAppletConfigTO;
import com.kz.audit.entity.alipay.AuditAlipayEntity;
import com.kz.audit.entity.alipay.MiniUperJson;
import com.kz.audit.entity.fanxing.shop.ShopConfig;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AlipayAppCodeUpload {
    private Process p;
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
     * 获取文件内容
     *
     * @param originStr
     * @param replaceStr
     * @return
     */
    private static boolean catFileContent(String filePath, String originStr, String replaceStr) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(filePath, "rw");
            String line = null;
            //记住上一次的偏移量
            long lastPoint = 0;
            while ((line = raf.readLine()) != null) {
                final long ponit = raf.getFilePointer();
                if (StrUtil.containsAnyIgnoreCase(line, originStr)) {
                    raf.seek(lastPoint);
                    if (StrUtil.containsAnyIgnoreCase(line, replaceStr)) {
                        return true;
                    }
                }
                return false;
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
        return false;
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
            raf = new RandomAccessFile(filePath, "rw");
            String line = null;
            //记住上一次的偏移量
            long lastPoint = 0;
            while ((line = raf.readLine()) != null) {
                final long ponit = raf.getFilePointer();
                if (StrUtil.containsAnyIgnoreCase(line, originStr)) {
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

    /**
     * 记录内容
     *
     * @param content
     */
    public static void writeRecord(String filePath, String content, boolean showLogTime, boolean flushFile) {
        try {
            if (flushFile) {
                flushFile(filePath);
            }
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), getDefaultEncoding()));
            if (showLogTime) {
                writer.write(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS") + ":>>> " + content);
            } else {
                writer.write(content);
            }

            writer.write(System.getProperty("line.separator"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void flushFile(String filePath) throws IOException {
        RandomAccessFile ra = new RandomAccessFile(filePath, "rw");
        ra.setLength(0);//清空文件内容
        ra.seek(0);
        ra.close();
    }

    public static void uploadCode() {
        String fileLogPath = "D:\\zhifubao\\alipay_upload\\logs\\" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".txt";
        AlipayAppCodeUpload cmd = new AlipayAppCodeUpload();
        String appVendorPath = "D:\\zhifubao\\alipay_upload\\mp-alipay\\common\\vendor.js";
        String miniuperJsonPath = "D:\\zhifubao\\alipay_upload\\mp-alipay\\miniuper.json";

        List<AuditAlipayEntity> list = AuditAlipayDAO.ME.list(1, 1, 1000);

        if (CollectionUtil.isEmpty(list)) {
            System.out.println("企颖商城已订购商户为空");
            return;
        }
        String formatTenantSize = String.format("已订购企颖商城的商户数量：%s", list.size());
        writeRecord(fileLogPath, formatTenantSize, true, false);
        System.out.println(formatTenantSize);
        int fail = 0, success = 0;
        for (AuditAlipayEntity entity : list) {
            if (entity == null || StrUtil.isBlank(entity.getAppid()) || StrUtil.isBlank(entity.getTenant_code())) {
                continue;
            }
            //if (!StrUtil.equalsIgnoreCase(entity.getAppid(), "2021003101607965")) {
            //    continue;
            //}

            if (Long.parseLong(entity.getTenant_code()) < (Long.parseLong("10000067"))) {
                continue;
            }
            String maxAppVersion = AuditAlipayDAO.ME.getMaxAppVersion(entity);
            if (StrUtil.isBlank(maxAppVersion)) {
                String error = String.format("商户[%s]授权token不对", entity.getName());
                writeRecord(fileLogPath, error, true, false);
                continue;
            }

            String uploadVersion = AuditAlipayDAO.ME.getUploadVersion(maxAppVersion);

            String name = entity.getName();
            String tenantCode = entity.getTenant_code();
            String appId = entity.getAppid();
            String formatLog1 = String.format("准备开始上传支付宝代码，商户号=%s，商户=%s，商家小程序appId=%s", tenantCode, name, appId);
            String formatLog2 = String.format("商户[%s]当前最大的版本号=%s，本次上传的版本=%s", name, maxAppVersion, uploadVersion);
            System.out.println(formatLog1);
            System.out.println(formatLog2);
            writeRecord(fileLogPath, formatLog1, true, false);
            writeRecord(fileLogPath, formatLog2, true, false);

            //修改小程序appid
            String json = JSONObject.toJSONString(MiniUperJson.build(appId), false);
            writeRecord(miniuperJsonPath, json, false, true);

            //修改商户号
            modifyFileContent(appVendorPath, "  tenantCode: ", "  tenantCode: \"" + tenantCode + "\"");

            String alipayUploadFilePath = "D:\\tools\\bat\\alipay_upload.bat";
            String versionDesc = "提交审核版本，麻烦支付宝抽空审核一下，辛苦了，谢谢";
            String formatUpload = String.format("miniuper upload -v %s ", uploadVersion);

            try {
                flushFile(alipayUploadFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //修改上传代码行
            writeRecord(alipayUploadFilePath, "CD D:\\zhifubao\\alipay_upload\\mp-alipay", false, false);
            writeRecord(alipayUploadFilePath, "dir", false, false);
            writeRecord(alipayUploadFilePath, formatUpload, false, false);

            //上传前判断下，有没有修改成功
            /*boolean containTenantCode = catFileContent(appVendorPath, "  tenantCode: ", tenantCode);
            if (!containTenantCode) {
                String msg = String.format("商户[%s]的商户号修改错误", name);
                System.out.println(msg);
                writeRecord(fileLogPath, msg, true, false);
                continue;
            }*/

            boolean containAppId = catFileContent(miniuperJsonPath, "{", appId);
            if (!containAppId) {
                String msg = String.format("商户[%s]的appId修改错误", name);
                System.out.println(msg);
                writeRecord(fileLogPath, msg, true, false);
                continue;
            }

            //开始上传代码
            cmd.setCmd("cmd");
            cmd.writeCmd("D:\\tools\\bat\\alipay_upload.bat");
            String cmdLog = cmd.readCmd();

            maxAppVersion = AuditAlipayDAO.ME.getMaxAppVersion(entity);
            if (!StrUtil.equalsIgnoreCase(maxAppVersion, "0.0.0") && StrUtil.equalsIgnoreCase(maxAppVersion, uploadVersion)) {
                String error = String.format("商户[%s]上传成功", name);
                System.out.println(error);
                writeRecord(fileLogPath, error, true, false);
                //自动提审
                boolean apply = AuditAlipayDAO.ME.apply(entity, uploadVersion, null);
                if (!apply) {
                    writeRecord(fileLogPath, "提审失败！", true, false);
                } else {
                    String msg = String.format("商户[%s]提审成功", name);
                    System.out.println(msg);
                    writeRecord(fileLogPath, msg, true, false);
                }
                success++;
            } else {
                fail++;
                String error = String.format("商户[%s]上传失败", name);
                System.out.println(error);
                writeRecord(fileLogPath, error, true, false);
            }
            //writeRecord(fileLogPath, cmdLog, true, false);
        }
        String end = String.format("成功上传%s个商户，失败%s个", success, fail);
        writeRecord(fileLogPath, end, true, false);
    }


    public static void main(String[] args) {
        //AuditAlipayDAO.ME.allMemberCreate();
        //uploadCode();
        List<ShopConfig> list = (List<ShopConfig>) ShopConfig.ME.list(false);
        StringBuilder sb = new StringBuilder();
        for (ShopConfig config : list) {
            AlipayAppletConfigTO to = JSONUtil.toBean(config.getConfig(), AlipayAppletConfigTO.class);
            if (to == null || StrUtil.isBlank(to.getAppAuthToken())) {
                continue;
            }
            AuditAlipayEntity alipayEntity = AuditAlipayDAO.ME.getByToken(to.getAppAuthToken());
            if (alipayEntity == null) {
                alipayEntity = new AuditAlipayEntity();
                alipayEntity.setName(to.getAppName());
                alipayEntity.setType(1);
                alipayEntity.setStatus(1);
                alipayEntity.setCreate_time(new Date());
                alipayEntity.setToken(to.getAppAuthToken());
                alipayEntity.setAppid(to.getAppletId());
                alipayEntity.setTenant_code(config.getTenant_code());
                alipayEntity.save(false);
                System.out.println(String.format("商户[%s]-商户号[%s]添加成功", to.getAppName(), config.getTenant_code()));
            } else {
                alipayEntity.setToken(to.getAppAuthToken());
                alipayEntity.setAppid(to.getAppletId());
                alipayEntity.setTenant_code(config.getTenant_code());
                alipayEntity.doUpdate(false);
                System.out.println(String.format("商户[%s]-商户号[%s]更新成功", to.getAppName(), config.getTenant_code()));
            }

            if (StrUtil.isNotBlank(to.getAppletId())) {
                sb.append("alipays://platformapi/startapp?appId=" + to.getAppletId());
                sb.append(" ,  \t\n");
            }
        }

        System.out.println(sb.toString());
    }
}