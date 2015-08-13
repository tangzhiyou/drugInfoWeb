package com.skysoft;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.skysoft.framework.HttpClientUtils;
import com.skysoft.util.GerneralUtils;
import org.apache.http.client.ClientProtocolException;


/**
 * @Description: �ٶ�ͼƬ�����߳�
 */
public class BaiduImageDownloadThread extends Thread {
    /**
     * ͼƬ���Ӽ���
     */
    private List<String> imgURLList;
    private Map<String, String> headers;
    private static final String SAVE_PATH = "C:/baidu/images/";

    public List<String> getImgURLList() {
        return imgURLList;
    }

    public void setImgURLList(List<String> imgURLList) {
        this.imgURLList = imgURLList;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public BaiduImageDownloadThread(List<String> imgURLList, Map<String, String> headers) {
        this.imgURLList = imgURLList;
        this.headers = headers;
    }

    @Override
    public void run() {
        try {
            downloadImage(imgURLList);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param @param  subList
     * @param @throws ClientProtocolException
     * @param @throws IOException
     * @return void
     * @throws
     * @Title: downloadImage
     * @Description: ͼƬ����
     */
    public void downloadImage(List<String> subList) throws ClientProtocolException, IOException {
        for (String imgURL : subList) {
            if (null == imgURL) {
                continue;
            }
            //��ǰҳ��
            String host = imgURL.replaceAll("http://(\\w+\\.baidu\\.com).*", "$1");
            headers.put("Host", host);
            String fileName = GerneralUtils.gernerateRandomFilename() + ".jpg";
            HttpClientUtils.downloadFile(imgURL, headers, SAVE_PATH, fileName);
        }
    }
}
