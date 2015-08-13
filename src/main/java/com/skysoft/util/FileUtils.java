package com.skysoft.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.io.IOUtils;

/**
 * �ļ�����������
 */
public class FileUtils {

    /**
     * ɾ������Ŀ¼������������Ŀ¼���ļ�
     *
     * @param path
     */
    public static void deleteDirs(String path) {
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            return;
        }
        File[] files = rootFile.listFiles();
        if (null == files) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                deleteDirs(file.getPath());
            } else {
                file.delete();
            }
        }
        rootFile.delete();
    }

    /**
     * ɾ��ָ���ļ�
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        return file.delete();
    }

    /**
     * �ļ�����
     *
     * @param is    ������
     * @param os    �����
     * @param close д��֮���Ƿ���Ҫ�ر�OutputStream
     * @throws IOException
     */
    public static int copy(InputStream is, OutputStream os, boolean close) {
        try {
            return IOUtils.copy(is, os);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (close) {
                try {
                    is.close();
                    os.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * �ļ�����
     *
     * @param inputName
     * @param outputName
     * @return
     */
    public static boolean copyFile(String inputName, String outputName) {
        InputStream is = null;
        OutputStream os = null;
        int copyed = 0;
        try {
            is = new FileInputStream(inputName);
            os = new FileOutputStream(outputName);
            copyed = copy(is, os, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return copyed > 0;
    }

    /**
     * �ļ��и���
     *
     * @param srcFolder
     * @param destFolder
     * @return
     */
    public static boolean copyDirctory(String srcFolder, String destFolder) {
        File srcFile = new File(srcFolder);
        File destFile = new File(destFolder);
        if (!srcFile.exists() || (srcFile.isDirectory() && destFile.isFile())) {
            return false;
        }
        //�ļ�copy���ļ�
        if (srcFile.isFile() && destFile.isFile()) {
            return copyFile(srcFolder, destFolder);
        }
        //����Ŀ��Ŀ¼
        if (!destFile.exists() && !destFile.isFile()) {
            destFile.mkdir();
        }
        //�ļ�copy��Ŀ¼
        if (srcFile.isFile()) {
            String srcFileName = srcFile.getName();
            String destFilePath = wrapFilePath(getFullFilePath(srcFileName, destFolder));
            return copyFile(wrapFilePath(srcFolder), destFilePath);
        }

        //Ŀ¼copy��Ŀ¼
        File[] allFiles = srcFile.listFiles();
        String srcName = null;
        String desName = null;
        for (File file : allFiles) {
            srcName = file.getName();
            if (file.isFile()) {
                desName = wrapFilePath(getFullFilePath(srcName, destFolder));
                copyFile(wrapFilePath(file.getAbsolutePath()), desName);
            } else {
                copyDirctory(wrapFilePath(file.getAbsolutePath()), getFullFilePath(srcName, destFolder));
            }
        }
        return true;
    }

    /**
     * �õ�ĳ�ļ����µ������ļ�
     *
     * @param path
     * @return
     */
    public static List<File> getAllFile(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        List<File> fileList = new ArrayList<File>();
        for (File f : files) {
            if (f.isFile()) {
                fileList.add(f);
            }
        }
        return fileList;
    }

    /**
     * ��ȡ�ļ�
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readFile(String filePath) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(new File(filePath)));
        StringBuffer content = new StringBuffer();
        String line = null;
        while ((line = in.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString().replaceAll("\n$", "");
    }

    /**
     * ��ȡ�ļ�
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFile(File file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        StringBuffer content = new StringBuffer();
        String line = null;
        while ((line = in.readLine()) != null) {
            content.append(line).append("\n");
        }
        return content.toString().replaceAll("\n$", "");
    }

    /**
     * ���ַ�����ָ������д���ļ���<br/>����ָ��д�뷽ʽ��׷��/����
     *
     * @param content  д����ַ���
     * @param filePath �ļ�����·��
     * @param charset  д�����
     * @param append   �Ƿ�׷��
     */
    public static void writeFile(String content, String filePath, String charset, boolean append) {
        BufferedWriter writer = null;
        OutputStream os = null;
        OutputStreamWriter osw = null;
        try {
            os = new FileOutputStream(filePath, append);
            osw = new OutputStreamWriter(os, charset);
            writer = new BufferedWriter(osw);
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
                osw.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ���ַ�����ָ������д���ļ���<br/>����ָ��д�뷽ʽ��׷��/����
     * <br/>Ĭ��д�뷽ʽΪ����
     *
     * @param content  д����ַ���
     * @param filePath �ļ�����·��
     * @param charset  д�����
     */
    public static void writeFile(String content, String filePath, String charset) {
        writeFile(content, filePath, charset, false);
    }

    /**
     * ���ַ�����ָ������д���ļ���<br/>����ָ��д�뷽ʽ��׷��/����
     * <br/>Ĭ��д�����ΪUTF-8
     *
     * @param content  д����ַ���
     * @param filePath �ļ�����·��
     * @param append   �Ƿ�׷��
     */
    public static void writeFile(String content, String filePath, boolean append) {
        writeFile(content, filePath, "UTF-8", append);
    }

    /**
     * ���ַ�����ָ������д���ļ���<br/>����ָ��д�뷽ʽ��׷��/����
     * <br/>Ĭ��д�뷽ʽΪ����,Ĭ��д�����ΪUTF-8
     *
     * @param content  д����ַ���
     * @param filePath �ļ�����·��
     */
    public static void writeFile(String content, String filePath) {
        writeFile(content, filePath, "UTF-8", false);
    }

    /**
     * �����ļ�
     *
     * @param link
     * @param filePath
     * @throws IOException
     */
    public static void download(String link, String filePath)
            throws IOException {
        URL url = new URL(link);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                url.openStream()));
        String inputLine = null;
        FileOutputStream fos = new FileOutputStream(filePath);
        while ((inputLine = in.readLine()) != null) {
            fos.write(inputLine.getBytes());
        }
        in.close();
        fos.close();
    }

    /**
     * ��ȡԶ���ļ���������
     *
     * @param url
     * @return
     */
    public static byte[] getBinaryDataFromURL(String link) throws IOException {
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        try {
            url = new URL(link);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            return inputStream2ByteArray(bis);
        } catch (IOException e) {
            return null;
        } finally {
            httpUrl.disconnect();
        }
    }

    /**
     * �ַ���ת����Clob
     *
     * @param string
     * @return
     */
    public static Clob string2Clob(String string) {
        if (GerneralUtils.isEmptyString(string)) {
            return null;
        }
        try {
            return new SerialClob(string.toCharArray());
        } catch (SerialException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Clobת�����ַ���
     *
     * @param clob
     * @return
     */
    public static String clob2String(Clob clob) {
        if (null == clob) {
            return null;
        }
        try {
            return clob.getSubString(1L, (int) clob.length());
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * �ֽ�����ת����Clob
     *
     * @param byteArray
     * @param charsetName
     * @return
     */
    public static Clob byteArray2Clob(byte[] byteArray, String charsetName) {
        if (null == byteArray) {
            return null;
        }
        try {
            String string = new String(byteArray,
                    charsetName == null ? Constant.DEFAULT_CHARSET
                            : charsetName);
            return string2Clob(string);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * �ֽ�����ת����Clob(����) ������ʽָ�����룬Ĭ�ϱ���ΪUTF-8
     *
     * @param byteArray
     * @param charsetName
     * @return
     */
    public static Clob byteArray2Clob(byte[] byteArray) {
        return byteArray2Clob(byteArray, null);
    }

    /**
     * Clobת�����ֽ�����
     *
     * @param clob
     * @return
     */
    public static byte[] clob2ByteArray(Clob clob) {
        if (null == clob) {
            return null;
        }
        InputStream in = null;
        byte[] byteArray = null;
        int length = 0;
        try {
            length = (int) clob.length();
            byteArray = new byte[length];
            in = clob.getAsciiStream();
        } catch (SQLException e) {
            return null;
        }
        int offset = 0;
        int n = 0;
        try {
            do {
                n = in.read(byteArray, offset, length - offset);
            } while (n != -1);
        } catch (IOException e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArray;
    }

    /**
     * �ֽ�����ת����Blob
     *
     * @param byteArray
     * @return
     * @throws SQLException
     * @throws SerialException
     */
    public static Blob byteArray2Blob(byte[] byteArray) {
        if (null == byteArray) {
            return null;
        }
        try {
            return new SerialBlob(byteArray);
        } catch (SerialException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Blobת�����ֽ�����
     *
     * @param blob
     * @return
     */
    public static byte[] blob2ByteArray(Blob blob) {
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            byte[] bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;
            while (offset < len
                    && (read = is.read(bytes, offset, len - offset)) != -1) {
                offset += read;
            }
            return bytes;
        } catch (SQLException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * �ֽ�����ת����������
     *
     * @param byteArray �ֽ�����
     * @return
     */
    public static InputStream byteArray2InputStream(byte[] byteArray) {
        return new ByteArrayInputStream(byteArray);
    }

    /**
     * ������ת�����ֽ�����
     *
     * @param is ����������
     * @return
     * @throws IOException
     */
    public static byte[] inputStream2ByteArray(InputStream is)
            throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch = 0;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        is.close();
        return imgdata;
    }

    /**
     * Stringת����������
     *
     * @param text
     * @param charset
     * @return
     */
    public static InputStream string2InputStream(String text, String charset) {
        try {
            return new ByteArrayInputStream(text.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ������ת����String(�ٶȿ쵫���ڴ�)
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line).append("\n");
        }
        return buffer.toString();
    }

    /**
     * ������ת����String(������Դ�ٵ��ٶ���)
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bt = new byte[4096];
        int i = -1;
        while ((i = is.read(bt)) > 0) {
            bos.write(bt, 0, i);
        }
        return bos.toString();
    }

    /**
     * �ļ�ת����������
     *
     * @param file
     * @return
     */
    public static InputStream file2InputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ������д���ļ�
     *
     * @param is       ������
     * @param filePath �ļ�����Ŀ¼·��
     * @throws IOException
     */
    public static void write2File(InputStream is, String filePath)
            throws IOException {
        OutputStream os = new FileOutputStream(filePath);
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((len = is.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        is.close();
    }

    /**
     * ������д���ļ�
     *
     * @param is       ������
     * @param filePath �ļ�����Ŀ¼·��
     * @param append   �Ƿ�׷��
     * @throws IOException
     */
    public static void write2File(InputStream is, String filePath, boolean append)
            throws IOException {
        OutputStream os = new FileOutputStream(filePath, append);
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((len = is.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        is.close();
    }

    /**
     * ��ȡ�ļ�������·��
     *
     * @param fileName �ļ�����
     * @param filePath �ļ�����·��
     * @return
     */
    public static String getFullFilePath(String fileName, String filePath) {
        if (GerneralUtils.isEmptyString(filePath)
                || GerneralUtils.isEmptyString(fileName)) {
            return null;
        }
        filePath = wrapFilePath(filePath);
        return filePath + fileName;
    }

    /**
     * ת���ļ�·���е�\\Ϊ/
     *
     * @param filePath
     * @return
     */
    public static String wrapFilePath(String filePath) {
        if (filePath.split("\\\\").length > 1) {
            filePath = filePath.replace("\\", "/");
        }
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        return filePath;
    }

    /**
     * ���ļ�·���л�ȡ�ļ�����·��
     *
     * @param fullPath �ļ�ȫ·��
     * @return �ļ�����·��
     */
    public static String getFileDir(String fullPath) {
        int iPos1 = fullPath.lastIndexOf("/");
        int iPos2 = fullPath.lastIndexOf("\\");
        if (-1 == iPos1 && -1 == iPos2) {
            return fullPath;
        }
        iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
        return fullPath.substring(0, iPos1 + 1);
    }

    /**
     * ���ļ�·���л�ȡ�ļ�����(������׺��)
     *
     * @param fullPath
     * @return
     */
    public static String getFileName(String fullPath) {
        if (GerneralUtils.isEmptyString(fullPath)) {
            return "";
        }
        int iPos1 = fullPath.lastIndexOf("/");
        int iPos2 = fullPath.lastIndexOf("\\");
        if (-1 == iPos1 && -1 == iPos2) {
            return fullPath;
        }
        iPos1 = (iPos1 > iPos2 ? iPos1 : iPos2);
        return fullPath.substring(iPos1 + 1);
    }

    /**
     * ��URL��������ȡ�ļ�����
     *
     * @param url
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        if (GerneralUtils.isEmptyString(url)) {
            return "";
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        url = url.replaceAll("(?:http|https)://www\\.([\\s\\S]*)", "$1");
        return getFileName(url);
    }

    /**
     * ���ļ�·���л�ȡ�ļ�����(ȥ����׺��)
     *
     * @param fullPath
     * @return
     */
    public static String getPureFileName(String fullPath) {
        String fileFullName = getFileName(fullPath);
        int index = fileFullName.lastIndexOf(".");
        if (index != -1) {
            return fileFullName.substring(0, index);
        }
        return fileFullName;
    }

    /**
     * ����ļ����еĺ�׺��
     *
     * @param fileName Դ�ļ���
     * @return String ��׺��
     */
    public static String getFileSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1, fileName.length());
        }
        return fileName;
    }

    /**
     * ��CLASSPATH·���¼���ָ���ļ�
     *
     * @param fileName �ļ�����
     * @return
     */
    public static String getFileFromClassPath(String fileName) {
        URL url = FileUtils.class.getClassLoader().getResource(fileName);
        String filepath = url.getFile();
        File file = new File(filepath);
        byte[] retBuffer = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(filepath);
            fis.read(retBuffer);
            fis.close();
            return new String(retBuffer, "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * ��URL�����в²��ļ�����
     *
     * @param url
     * @return
     */
    public static String guessFileNameFromUrl(String url) {
        String reg = "(/|=)([^/&?]+\\.[a-zA-Z]+)";
        if (GerneralUtils.isEmptyString(url) || Pattern.compile(reg).matcher(url).find()) {
            return "UnknowName.temp";
        }
        Matcher matcher = Pattern.compile(reg).matcher(url);
        String s = "";
        while (matcher.find()) {
            s = matcher.group(2);
        }
        return s;
    }

    /**
     * ��Content-Disposition����ȡ�ļ���
     *
     * @param contentDisposition
     * @return
     */
    public static String getFileNameFromContentDisposition(String contentDisposition) {
        if (GerneralUtils.isEmptyString(contentDisposition)) {
            return "UnknowName.temp";
        }
        if (!contentDisposition.startsWith("attachment")) {
            return null;
        }
        return contentDisposition.substring(contentDisposition.indexOf("=") + 1);
    }
}
