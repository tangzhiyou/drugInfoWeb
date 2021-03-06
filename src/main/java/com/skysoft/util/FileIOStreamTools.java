package com.skysoft.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

public class FileIOStreamTools {
    private static final int BUFFER_SIZE = 1024 * 4;

    public static void writeDtatFile(String url, String fileName) {
        File file = null;
        FileWriter fwriter = null;
        BufferedWriter bfwriter = null;
        try {
            file = new File(fileName);
            fwriter = new FileWriter(fileName, true);
            bfwriter = new BufferedWriter(fwriter);
            bfwriter.newLine();
            bfwriter.write(url, 0, url.length());
            bfwriter.flush();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bfwriter != null) {
                try {
                    bfwriter.close();
                } catch (IOException e) {

                }
            }
        }
    }

    public static String readerFileContent(String filePath, String host) {
        StringBuffer sbContent = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(new FileInputStream(
                    filePath), "UTF-8");
            br = new BufferedReader(isr);
            String urlText;
            sbContent = new StringBuffer();
            while ((urlText = br.readLine()) != null) {
                sbContent.append(urlText);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return sbContent.toString();
    }


    // 从文件里面读取要抓取的内容
    public static Set<String> readerDataSet(String importFile, String host) {

        Set<String> links = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            links = new HashSet<String>();
            isr = new InputStreamReader(new FileInputStream(
                    importFile), "UTF-8");
            br = new BufferedReader(isr);
            String urlText;
            while ((urlText = br.readLine()) != null) {
                links.add(urlText);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return links;
    }

    public static List<String> readerDataList(String importFile, String host) {

        List<String> links = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            links = new ArrayList<String>();
            isr = new InputStreamReader(new FileInputStream(
                    importFile), "UTF-8");
            br = new BufferedReader(isr);
            String urlText;
            while ((urlText = br.readLine()) != null) {
                links.add(urlText);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return links;
    }

    public static Map<String, String> readerDataHashMap(String filePath) {
        Map<String, String> proper = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            proper = new HashMap<String, String>();
            isr = new InputStreamReader(new FileInputStream(
                    filePath), "UTF-8");
            br = new BufferedReader(isr);
            String urlText;
            while ((urlText = br.readLine()) != null) {
                String[] text = urlText.split("@");
                proper.put(text[0], text[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return proper;
    }

    public static void saveAttachment(byte[] input, String fileName) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            IOUtils.write(input, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 读取输入流为byte[]数组
     */
    public static byte[] read(InputStream instream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = instream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        return bos.toByteArray();
    }


    // inputstream转String
    public static String InputStreamToString(InputStream response, String characterset) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = response.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        return new String(outStream.toByteArray(), characterset);
    }


}
