package com.skysoft.framework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.skysoft.util.FileUtils;
import com.skysoft.util.GerneralUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.TruncatedChunkException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * HttpClient4������
 *
 */
@SuppressWarnings({"deprecation", "unused"})
public class HttpClientUtils {
    /**
     * ���ӳ����������
     */
    private static final int MAX_TOTAL_CONNECTION = 500000;
    /**
     * ���ӳ�ÿ��·��������Ӳ�����
     */
    private static final int MAX_PRE_ROUTE = 5000;
    /**
     * ���ӳ�HTTP����80�˿�������Ӳ�����
     */
    private static final int MAX_HTTP_ROUTE = 2000;
    /**
     * �û����������
     */
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0";
    /**
     * Ĭ���������UTF-8
     */
    private static final String HTTP_CONTENT_CHARSET = "UTF-8";
    /**
     * Socket��ȡ���س�ʱʱ��
     */
    private static final int SO_TIMEOUT = 3000000;
    /**
     * ���ӳ�ʱʱ��
     */
    public static final int CONNECT_TIMEOUT = 120000;
    /**
     * ��ȡ���ӵ����ȴ�ʱ��
     */
    public static final int WAITING_TIMEOUT = 120000;
    /**
     * Socket����ע����
     */
    private static SchemeRegistry schemeRegistry;
    /**
     * ���ӳع�����
     */
    private static PoolingClientConnectionManager connectionManager;

    /**HTTP�������*/
    //private static HttpParams httpParams;

    static {
        //setHttpParams();
        createConnectionManager();
    }

    /**
     * �쳣�Զ��ָ�����, ʹ��HttpRequestRetryHandler�ӿ�ʵ��������쳣�ָ�
     */
    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        // �Զ���Ļָ�����
        public boolean retryRequest(IOException exception, int executionCount,
                                    HttpContext context) {
            // ���ûָ����ԣ��ڷ����쳣ʱ���Զ�����5��
            if (executionCount >= 5) {
                // ������Ӵ������������ֵ��ֹͣ����
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                // �������������ʧ������
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                //SSL�����쳣 ʱ������
                return false;
            }
            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
            if (!idempotent) {
                // ����������ͬ������
                return true;
            }
            return false;
        }
    };

    /**
     * ��д��֤������ȡ�����ssl
     */
    private static TrustManager truseAllManager = new X509TrustManager() {
        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    /**
     * ֧�ַ���https����վ
     *
     * @param httpclient
     */
    private static void enableSSL(DefaultHttpClient httpclient) {
        //����SSL
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{truseAllManager}, null);
            SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme https = new Scheme("https", sf, 443);
            httpclient.getConnectionManager().getSchemeRegistry().register(https);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����HTTP�������Ӳ���
     */
    private static void setHttpParams(HttpParams httpParams, String charset) {
        if (null == httpParams) {
            return;
        }
        // �������ӳ�ʱʱ��
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
        // ���ö�ȡ��ʱʱ��
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        // ģ������������һЩ����������ֻ������������ʵ�����
        httpParams.setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
        httpParams.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
        httpParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset == null ? HTTP_CONTENT_CHARSET : charset);
        //��ֹ�Զ��ض���
        httpParams.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
        // �����Cookie������
        httpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        // �������������
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTION);
        // ���û�ȡ���ӵ����ȴ�ʱ��
        ConnManagerParams.setTimeout(httpParams, WAITING_TIMEOUT);
        // ����ÿ��·�����������
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(MAX_PRE_ROUTE);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
    }

    /**
     * ����HTTP�������Ӳ���
     */
    private static void setHttpParams(HttpParams httpParams) {
        setHttpParams(httpParams, HTTP_CONTENT_CHARSET);
    }

    /**
     * Socket����ע��
     *
     * @return
     */
    private static SchemeRegistry getSchemeRegistry() {
        if (null == schemeRegistry) {
            schemeRegistry = new SchemeRegistry();
        }
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        return schemeRegistry;
    }

    /**
     * �������ӳ�
     *
     * @param schemeRegistry
     * @return
     */
    private static PoolingClientConnectionManager createConnectionManager() {
        if (null == connectionManager) {
            connectionManager = new PoolingClientConnectionManager(getSchemeRegistry());
        }
        connectionManager.setDefaultMaxPerRoute(MAX_PRE_ROUTE);
        HttpHost localhost = new HttpHost("locahost", 80);
        connectionManager.setMaxPerRoute(new HttpRoute(localhost), MAX_HTTP_ROUTE);
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTION);
        return connectionManager;
    }

    /**
     * ����HttpClientʵ��
     *
     * @param charset
     * @return
     */
    private static DefaultHttpClient getDefaultHttpClient(String charset) {
        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager);
        HttpParams httpParams = httpClient.getParams();
        setHttpParams(httpParams, charset);
        //�������Բ���
        httpClient.setHttpRequestRetryHandler(requestRetryHandler);
        return httpClient;
    }

    /**
     * ����HttpClientʵ��
     *
     * @param charset
     * @return
     */
    private static DefaultHttpClient getDefaultHttpClient() {
        return getDefaultHttpClient(HTTP_CONTENT_CHARSET);
    }

    /**
     * ����get����(����1)
     *
     * @param url ����URL
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result get(String url) throws ClientProtocolException, IOException {
        return get(url, null, null, null, 0);
    }

    /**
     * ����get����(����2)
     *
     * @param url    ����URL
     * @param params �������
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result get(String url, Map<String, String> params) throws ClientProtocolException, IOException {
        return get(url, null, params, null, 0);
    }

    /**
     * ����get����(����3)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result get(String url, Map<String, String> headers, Map<String, String> params) throws ClientProtocolException, IOException {
        return get(url, headers, params, null, 0);
    }

    /**
     * ����get����
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @param host    ��������IP
     * @param port    ����˿ں�
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result get(String url, Map<String, String> headers, Map<String, String> params, String host, int port) throws ClientProtocolException, IOException {
        DefaultHttpClient httpClient = getDefaultHttpClient();
        url = url + (null == params ? "" : assemblyParameter(params));
        HttpGet httpGet = new HttpGet(url);
        //�ж��Ƿ���https����
        if (url.startsWith("https")) {
            enableSSL(httpClient);
        }
        //��������ͷ��Ϣ
        if (null != headers) {
            httpGet.setHeaders(assemblyHeader(headers));
        }
        //�����������
        if (null != host && host.equals("")) {
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(host, port));
        }
        HttpResponse response = httpClient.execute(httpGet);
        BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());

        Result result = new Result();
        //���÷��ص�cookie
        result.setCookie(assemblyCookie(httpClient.getCookieStore().getCookies()));
        //������Ӧ״̬��
        result.setStatusCode(response.getStatusLine().getStatusCode());
        //������Ӧͷ��Ϣ
        result.setHeaders(response.getAllHeaders());
        //������Ӧ��
        result.setHttpEntity(entity);
        //�Ͽ�����
        httpGet.abort();
        httpGet.releaseConnection();
        return result;
    }

    /**
     * ����post����(����1)
     *
     * @param url ����URL
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url) throws ClientProtocolException, IOException {
        return post(url, null, null, null, 0);
    }

    /**
     * ����post����(����2)
     *
     * @param url    ����URL
     * @param params �������
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url, Map<String, String> params) throws ClientProtocolException, IOException {
        return post(url, null, params, null, 0);
    }

    /**
     * ����post����(����3)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url, Map<String, String> headers, Map<String, String> params) throws ClientProtocolException, IOException {
        return post(url, headers, params, null, 0);
    }

    /**
     * ����post����(����4)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @param host    ��������IP
     * @param port    ����˿ں�
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url, Map<String, String> headers, Map<String, String> params, String host, int port) throws ClientProtocolException, IOException {
        return post(url, headers, params, host, port, HTTP_CONTENT_CHARSET);
    }

    /**
     * ����post����
     *
     * @param url      ����URL
     * @param headers  ����ͷ��Ϣ
     * @param params   �������
     * @param host     ��������IP
     * @param port     ����˿ں�
     * @param encoding �������
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static Result post(String url, Map<String, String> headers, Map<String, String> params, String host, int port, String encoding) throws ClientProtocolException, IOException {
        DefaultHttpClient httpClient = getDefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        //�ж��Ƿ���https����
        if (url.startsWith("https")) {
            enableSSL(httpClient);
        }
        //��������ͷ��Ϣ
        if (null != headers) {
            httpPost.setHeaders(assemblyHeader(headers));
        }
        //�����������
        if (null != host && host.equals("")) {
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(host, port));
        }
        //�����������
        if (GerneralUtils.isNotEmptyMap(params)) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (String temp : params.keySet()) {
                list.add(new BasicNameValuePair(temp, params.get(temp)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(list, encoding));
        }
        HttpResponse response = httpClient.execute(httpPost);
        BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());

        Result result = new Result();
        //���÷��ص�cookie
        result.setCookie(assemblyCookie(httpClient.getCookieStore().getCookies()));
        //������Ӧ״̬��
        result.setStatusCode(response.getStatusLine().getStatusCode());
        //������Ӧͷ��Ϣ
        result.setHeaders(response.getAllHeaders());
        //������Ӧ��
        result.setHttpEntity(entity);
        httpPost.abort();
        httpPost.releaseConnection();
        return result;
    }

    /**
     * ����POST����֧���ļ������ϴ�
     *
     * @param requestParams ���������װ��
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static Result postWithFileUpload(RequestParamsBeanWrapper requestParams) throws ClientProtocolException, IOException {
        DefaultHttpClient httpClient = getDefaultHttpClient();
        //������ͨ���������
        MultipartEntity reqEntity = new MultipartEntity();
        if (GerneralUtils.isNotEmptyMap(requestParams.getParams())) {
            for (String key : requestParams.getParams().keySet()) {
                reqEntity.addPart(key, new StringBody(requestParams.getParams().get(key), "text/plain", Charset.forName(requestParams.getEncoding())));
            }
        }
        //�����ļ������
        if (GerneralUtils.isNotEmptyCollection(requestParams.getFileParams())) {
            for (String file : requestParams.getFileParams()) {
                reqEntity.addPart(requestParams.getFileFormName(), new FileBody(new File(file)));
            }
        }
        HttpPost httpPost = new HttpPost(requestParams.getUrl());
        //�ж��Ƿ���https����
        if (requestParams.getUrl().startsWith("https")) {
            enableSSL(httpClient);
        }
        //��������ͷ��Ϣ
        if (null != requestParams.getHeaders()) {
            httpPost.setHeaders(assemblyHeader(requestParams.getHeaders()));
        }
        //�����������
        if (null != requestParams.getHost() && requestParams.getHost().equals("")) {
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(requestParams.getHost(), requestParams.getPort()));
        }
        httpPost.setEntity(reqEntity);
        HttpResponse response = httpClient.execute(httpPost);
        BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());

        Result result = new Result();
        //���÷��ص�cookie
        result.setCookie(assemblyCookie(httpClient.getCookieStore().getCookies()));
        //������Ӧ״̬��
        result.setStatusCode(response.getStatusLine().getStatusCode());
        //������Ӧͷ��Ϣ
        result.setHeaders(response.getAllHeaders());
        //������Ӧ��
        result.setHttpEntity(entity);
        httpPost.abort();
        httpPost.releaseConnection();
        return result;
    }

    /**
     * ��װ����ͷ��Ϣ
     *
     * @param headers
     * @return
     */
    public static Header[] assemblyHeader(Map<String, String> headers) {
        Header[] allHeader = new BasicHeader[headers.size()];
        int i = 0;
        for (String str : headers.keySet()) {
            allHeader[i] = new BasicHeader(str, headers.get(str));
            i++;
        }
        return allHeader;
    }

    /**
     * ��װCookie
     *
     * @param cookies
     * @return
     */
    public static String assemblyCookie(List<Cookie> cookies) {
        StringBuffer buffer = new StringBuffer();
        for (Cookie cookie : cookies) {
            buffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * ��װ�������
     *
     * @param parameters
     * @return
     */
    public static String assemblyParameter(Map<String, String> parameters) {
        String para = "?";
        for (String str : parameters.keySet()) {
            String val = parameters.get(str);
            if (GerneralUtils.containsChinese(val)) {
                val = URLEncoder.encode(val);
            }
            para += str + "=" + val + "&";
        }
        return para.substring(0, para.length() - 1);
    }

    /**
     * ��ȡHTML����(Ĭ��get��ʽ)
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTML(String url) throws ClientProtocolException, IOException {
        return getHTML(url, null, true);
    }

    /**
     * ��ȡHTML����(Ĭ��get��ʽ)
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTML(String url, Map<String, String> headers) throws ClientProtocolException, IOException {
        return getHTML(url, headers, true);
    }

    /**
     * ��ȡHTML����(Ĭ��get��ʽ)
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTML(String url, Map<String, String> headers, String host, int port, boolean isGet) throws ClientProtocolException, IOException {
        Result result = null;
        if (isGet) {
            result = get(url, headers, null, host, port);
        } else {
            result = post(url, headers, null, host, port);
        }
        StringEntityHandler entityHandler = new StringEntityHandler();
        return entityHandler.handleEntity(result.getHttpEntity());
    }

    /**
     * ��ȡHTML����(Ĭ��get��ʽ)
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTML(String url, Map<String, String> headers, String host, int port) throws ClientProtocolException, IOException {
        return getHTML(url, headers, host, port, true);
    }

    /**
     * ��ȡHTML����(Ĭ��get��ʽ)
     *
     * @param url  �����ַ
     * @param host �������������IP
     * @param port �˿ں�
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTML(String url, String host, int port, boolean isGet) throws ClientProtocolException, IOException {
        Result result = null;
        if (isGet) {
            result = get(url, null, null, host, port);
        } else {
            result = post(url, null, null, host, port);
        }
        StringEntityHandler entityHandler = new StringEntityHandler();
        return entityHandler.handleEntity(result.getHttpEntity());
    }

    /**
     * ��ȡHTML����(Ĭ��get��ʽ)
     *
     * @param url  �����ַ
     * @param host �������������IP
     * @param port �˿ں�
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTML(String url, String host, int port) throws ClientProtocolException, IOException {
        return getHTML(url, host, port, true);
    }


    /**
     * ��ȡHTML����
     *
     * @param url
     * @param isGet get/post
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTML(String url, Map<String, String> headers, boolean isGet) throws ClientProtocolException, IOException {
        Result result = null;
        if (isGet) {
            result = get(url, headers, null);
        } else {
            result = post(url, headers, null);
        }
        StringEntityHandler entityHandler = new StringEntityHandler();
        return entityHandler.handleEntity(result.getHttpEntity());
    }

    /**
     * ��ȡHTML����
     *
     * @param url
     * @param isGet get/post
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTML(String url, Map<String, String> headers, Map<String, String> params, boolean isGet) throws ClientProtocolException, IOException {
        Result result = null;
        if (isGet) {
            result = get(url, headers, params);
        } else {
            result = post(url, headers, params);
        }
        StringEntityHandler entityHandler = new StringEntityHandler();
        return entityHandler.handleEntity(result.getHttpEntity());
    }

    /**
     * ��ȡHTML����
     *
     * @param url
     * @param isGet get/post
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTML(String url, Map<String, String> headers, Map<String, String> params) throws ClientProtocolException, IOException {
        return getHTML(url, headers, params, true);
    }

    /**
     * ��ȡHTML����(����Զ���������)
     *
     * @param url
     * @param headers
     * @param isGet
     * @param retryCount
     * @return
     */
    public static String getHTMLWithRetry(String url, Map<String, String> headers, boolean isGet, int retryCount) {
        boolean flag = true;
        String html = "";
        int count = 0;
        while (flag) {
            try {
                //�����������������ֹͣ
                if (count > retryCount) {
                    break;
                }
                html = getHTML(url, headers, isGet);
            } catch (ClientProtocolException e) {
                count++;
                continue;
            } catch (TruncatedChunkException e) {
                count++;
                continue;
            } catch (IOException e) {
                count++;
                continue;
            }

            if (GerneralUtils.isNotEmptyString(html)) {
                if (!html.startsWith("<html><body><h1>Request Time-out</h1>")) {
                    flag = false;
                    count = 0;
                } else {
                    count++;
                    continue;
                }
                FileUtils.writeFile(url + "\n", "C:/debug-sina.txt", true);
            }
        }
        return html;
    }

    /**
     * ��ȡHTML����(����Զ���������)
     *
     * @param url
     * @param headers
     * @param isGet
     * @param retryCount
     * @return
     */
    public static String getHTMLWithRetry(String url, Map<String, String> headers, int retryCount) {
        return getHTMLWithRetry(url, headers, true, retryCount);
    }

    /**
     * ��ȡHTML����(����Զ���������)
     * ��ָ�����Դ�����Ĭ���Զ�����15��
     *
     * @param url
     * @param headers
     * @param isGet
     * @param retryCount
     * @return
     */
    public static String getHTMLWithRetry(String url, Map<String, String> headers) {
        return getHTMLWithRetry(url, headers, true, 15);
    }

    /**
     * ��ȡHTML����(����Զ���������)
     *
     * @param url
     * @param headers
     * @param isGet
     * @param retryCount
     * @return
     */
    public static String getHTMLWithRetry(String url, int retryCount) {
        return getHTMLWithRetry(url, null, true, retryCount);
    }

    /**
     * ��ȡHTML����(����Զ���������)
     * ��ָ�����Դ�����Ĭ���Զ�����15��
     *
     * @param url
     * @param headers
     * @param isGet
     * @param retryCount
     * @return
     */
    public static String getHTMLWithRetry(String url) {
        return getHTMLWithRetry(url, null, true, 15);
    }

    /**
     * ͨ��Selenium���ȡHTMLҳ������
     * (ע�⣺�����е���driver�����close�����ͷ���Դ)
     * (��Ҫ������ҳ�����ݲ�����Ajax�������ص������Ψһȱ�ݾ�����Ӧ�ٶȺ���)
     *
     * @param driver
     * @param url              ��������
     * @param enableJavaScript �Ƿ�����JavaScript���Ƿ�֧��Ajax
     * @return
     */
    public static String getHTMLBySelenium(WebDriver driver, String url, boolean enableJavaScript) {
        driver.get(url);
        String html = driver.getPageSource();
        return html;
    }

    /**
     * ͨ��Selenium���ȡHTMLҳ������(����)
     * (ע�⣺�����е���driver�����close�����ͷ���Դ)
     * (��Ҫ������ҳ�����ݲ�����Ajax�������ص������Ψһȱ�ݾ�����Ӧ�ٶȺ���)
     *
     * @param driver
     * @param url              ��������
     * @param enableJavaScript �Ƿ�����JavaScript���Ƿ�֧��Ajax
     * @return
     */
    public static String getHTMLBySelenium(WebDriver driver, String url) {
        return getHTMLBySelenium(driver, url, false);
    }

    /**
     * ͨ��Selenium���ȡHTMLҳ������
     * (��Ҫ������ҳ�����ݲ�����Ajax�������ص������Ψһȱ�ݾ�����Ӧ�ٶȺ���)
     *
     * @param url              ��������
     * @param enableJavaScript �Ƿ�����JavaScript���Ƿ�֧��Ajax
     * @return
     */
    public static String getHTMLBySelenium(String url, boolean enableJavaScript) {
        WebDriver driver = new HtmlUnitDriver(enableJavaScript);
        //WebDriver driver = new FirefoxDriver();
        //��ʼ����
        driver.get(url);
        String html = driver.getPageSource();
        //�ͷ���Դ
        driver.quit();
        //driver.close();
        return html;
    }

    /**
     * ͨ��Selenium���ȡHTMLҳ������(����)
     * (��Ҫ������ҳ�����ݲ�����Ajax�������ص������Ψһȱ�ݾ�����Ӧ�ٶȺ���)
     * (Ĭ�Ͻ���JavaScript����ִ��ҳ���ϵ����Javascript)
     *
     * @param url ��������
     * @return
     */
    public static String getHTMLBySelenium(String url) {
        return getHTMLBySelenium(url, false);
    }

    /**
     * ͨ��Selenium��������ȡHTMLҳ������
     *
     * @param urls             HTMLҳ�����Ӽ���
     * @param enableJavaScript �Ƿ�����JavaScript
     * @return
     */
    public static List<String> getHTMLBySelenium(Collection<String> urls, boolean enableJavaScript) {
        if (GerneralUtils.isEmptyCollection(urls)) {
            return null;
        }
        WebDriver driver = new HtmlUnitDriver(enableJavaScript);
        List<String> pageList = new ArrayList<String>();
        for (String url : urls) {
            driver.get(url);
            String html = driver.getPageSource();
            pageList.add(html);
        }
        driver.quit();
        return pageList;
    }

    /**
     * ͨ��Selenium��������ȡHTMLҳ������(����)
     *
     * @param urls HTMLҳ�����Ӽ���
     * @return
     */
    public static List<String> getHTMLBySelenium(Collection<String> urls) {
        return getHTMLBySelenium(urls, false);
    }

    /**
     * ��ȡ�ֽ�����
     * (������GET��ʽ���������δָ��,�����а��������ƴ����URL����,�磺http://www.xxxx.com?id=xx&page=xx)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @param isGet   GET/POST
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static byte[] getByteArray(String url, Map<String, String> headers, Map<String, String> params, boolean isGet) throws ClientProtocolException, IOException {
        Result result = null;
        if (isGet) {
            result = get(url, headers, params);
        } else {
            result = post(url, headers, params);
        }
        ByteArrayEntityHandler entityHandler = new ByteArrayEntityHandler();
        return entityHandler.handleEntity(result.getHttpEntity());
    }

    /**
     * ��ȡ�ֽ�����
     * (Ĭ�ϲ���GET��ʽ����HTTP����)
     * (������GET��ʽ���������δָ��,�����а��������ƴ����URL����,�磺http://www.xxxx.com?id=xx&page=xx)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static byte[] getByteArray(String url, Map<String, String> headers, Map<String, String> params) throws ClientProtocolException, IOException {
        return getByteArray(url, headers, params, true);
    }

    /**
     * ��ȡ�ֽ�����
     * (Ĭ�ϲ���GET��ʽ����HTTP����)
     * (������GET��ʽ���������δָ��,�����а��������ƴ����URL����,�磺http://www.xxxx.com?id=xx&page=xx)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static byte[] getByteArray(String url, Map<String, String> headers) throws ClientProtocolException, IOException {
        return getByteArray(url, headers, null, true);
    }

    /**
     * ��ȡ�ֽ�����
     * (Ĭ�ϲ���GET��ʽ����HTTP����)
     * (������GET��ʽ���������δָ��,�����а��������ƴ����URL����,�磺http://www.xxxx.com?id=xx&page=xx)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static byte[] getByteArray(String url) throws ClientProtocolException, IOException {
        return getByteArray(url, null, null, true);
    }

    /**
     * ��ȡ������
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @param isGet   GET/POST
     * @param isRange �Ƿ����ϵ�ģʽ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static InputStream getInputStream(String url, Map<String, String> headers, Map<String, String> params, boolean isGet, boolean isRange) throws ClientProtocolException, IOException {
        Result result = null;
        if (isGet) {
            result = get(url, headers, params);
        } else {
            result = post(url, headers, params);
        }
        InputStreamEntityHandler entityHandler = new InputStreamEntityHandler(isRange, result.getStatusCode());
        return entityHandler.handleEntity(result.getHttpEntity());
    }

    /**
     * ��ȡ������
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @param isGet   GET/POST
     * @param isRange �Ƿ����ϵ�ģʽ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static InputStream getInputStream(String url, Map<String, String> headers, Map<String, String> params, boolean isRange) throws ClientProtocolException, IOException {
        return getInputStream(url, headers, params, true, isRange);
    }

    /**
     * ��ȡ������
     * (Ĭ��GET��ʽ��Ĭ�϶ϵ�ģʽ�ر�)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param params  �������
     * @param isGet   GET/POST
     * @param isRange �Ƿ����ϵ�ģʽ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static InputStream getInputStream(String url, Map<String, String> headers, Map<String, String> params) throws ClientProtocolException, IOException {
        return getInputStream(url, headers, params, true, false);
    }

    /**
     * ��ȡ������
     * (Ĭ��GET��ʽ��Ĭ�϶ϵ�ģʽ�ر�)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param isGet   GET/POST
     * @param isRange �Ƿ����ϵ�ģʽ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static InputStream getInputStream(String url, Map<String, String> headers) throws ClientProtocolException, IOException {
        return getInputStream(url, headers, null, true, false);
    }

    /**
     * ��ȡ������
     * (Ĭ��GET��ʽ)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param isGet   GET/POST
     * @param isRange �Ƿ����ϵ�ģʽ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static InputStream getInputStream(String url, Map<String, String> headers, boolean isRange) throws ClientProtocolException, IOException {
        return getInputStream(url, headers, null, true, isRange);
    }

    /**
     * ��ȡ������
     * (Ĭ��GET��ʽ��Ĭ�϶ϵ�ģʽ�ر�)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param isGet   GET/POST
     * @param isRange �Ƿ����ϵ�ģʽ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static InputStream getInputStream(String url) throws ClientProtocolException, IOException {
        return getInputStream(url, null, null, true, false);
    }

    /**
     * ��ȡ������
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param isGet   GET/POST
     * @param isRange �Ƿ����ϵ�ģʽ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static InputStream getInputStream(String url, boolean isGet, boolean isRange) throws ClientProtocolException, IOException {
        return getInputStream(url, null, null, isGet, isRange);
    }

    /**
     * ��ȡ������
     * (Ĭ��GET��ʽ)
     *
     * @param url     ����URL
     * @param headers ����ͷ��Ϣ
     * @param isGet   GET/POST
     * @param isRange �Ƿ����ϵ�ģʽ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static InputStream getInputStream(String url, boolean isRange) throws ClientProtocolException, IOException {
        return getInputStream(url, null, null, true, isRange);
    }

    /**
     * �����ļ�(���߳����أ������ڼ�M��С�ļ�)
     *
     * @param url      �������ļ�����
     * @param headers  ����ͷ��Ϣ
     * @param savePath ����·��(��ָ����Ĭ������ΪC:/temp/DOWNLOAD)
     * @param fileName �����������ļ�������ļ�����(��ָ����Ĭ������ΪNew-File.temp)
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static File downloadFile(String url, Map<String, String> headers, String savePath, String fileName) throws ClientProtocolException, IOException {
//		Result result = get(url, headers, null);
//		FileEntityHandler entityHandler = new FileEntityHandler(savePath, fileName);
//		return entityHandler.handleEntity(result.getHttpEntity());
        return null;
    }

    /**
     * �����ļ�(���߳����أ������ڼ�M��С�ļ�)
     * (���û�δ�Զ��������ļ����ƣ���Ĭ������ΪNew-File.temp)
     *
     * @param url      �������ļ�����
     * @param headers  ����ͷ��Ϣ
     * @param savePath ����·��(��ָ����Ĭ������ΪC:/temp/DOWNLOAD)
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static File downloadFile(String url, Map<String, String> headers, String savePath) throws ClientProtocolException, IOException {
        return downloadFile(url, headers, savePath, null);
    }

    /**
     * �����ļ�(���߳����أ������ڼ�M��С�ļ�)
     * (���û�δ�Զ��������ļ����ƣ���Ĭ������ΪNew-File.temp)
     * (���û�δ�Զ������ر���Ŀ¼����Ĭ������ΪC:/temp/DOWNLOAD)
     *
     * @param url     �������ļ�����
     * @param headers ����ͷ��Ϣ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static File downloadFile(String url, Map<String, String> headers) throws ClientProtocolException, IOException {
        return downloadFile(url, headers, null, null);
    }

    /**
     * �����ļ�(���߳����أ������ڼ�M��С�ļ�)
     * (���û�δ�Զ��������ļ����ƣ���Ĭ������ΪNew-File.temp)
     * (���û�δ�Զ������ر���Ŀ¼����Ĭ������ΪC:/temp/DOWNLOAD)
     *
     * @param url     �������ļ�����
     * @param headers ����ͷ��Ϣ
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static File downloadFile(String url) throws ClientProtocolException, IOException {
        return downloadFile(url, null, null, null);
    }

    /**
     * ��ȡ��Ӧ��
     *
     * @param url
     * @param headers
     * @return
     */
    public static Object[] getResponse(String url, Map<String, String> headers) {
        DefaultHttpClient httpClient = getDefaultHttpClient();
        HttpHead httpHead = new HttpHead(url);
        //��������ͷ��Ϣ
        if (GerneralUtils.isNotEmptyMap(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                httpHead.addHeader(key, val);
            }
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpHead);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //��ȡHTTP��Ӧ״̬��
        int statusCode = response.getStatusLine().getStatusCode();
        Object[] result = null;
        if (!(statusCode >= 200 && statusCode <= 399)) {
            result = new Object[]{statusCode, null};
        } else {
            Header[] headerArray = response.getAllHeaders();
            result = new Object[]{statusCode, headerArray};
        }
        httpHead.abort();
        httpHead.releaseConnection();
        return result;
    }

    /**
     * ��ȡ��Ӧ��
     *
     * @param url
     * @param headers
     * @return
     */
    public static Object[] getResponse(String url) {
        return getResponse(url, null);
    }

    /**
     * ��ȡContent-Disposition��Ӧͷ
     *
     * @param url
     * @return
     */
    public static String getContentDisposition(String url) {
        DefaultHttpClient httpClient = getDefaultHttpClient();
        HttpHead httpHead = new HttpHead(url);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpHead);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String contentDisposition = null;
        if (response.getStatusLine().getStatusCode() == 200) {
            Header[] headers = response.getHeaders("Content-Disposition");
            if (headers.length > 0) {
                contentDisposition = headers[0].getValue();
            }
        }
        httpHead.abort();
        httpHead.releaseConnection();
        return contentDisposition;
    }

    /**
     * ��ȡ��Ӧͷ��Ϣ����Content-Disposition��Content-Length
     *
     * @param url ����URL
     * @return
     * @headers ��������ͷ��Ϣ
     */
    public static Header[] getResponseHeaders(String url, Map<String, String> headers) {
        DefaultHttpClient httpClient = getDefaultHttpClient();
        HttpHead httpHead = new HttpHead(url);
        //��������ͷ��Ϣ
        if (GerneralUtils.isNotEmptyMap(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                httpHead.addHeader(key, val);
            }
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpHead);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //��ȡHTTP��Ӧ״̬��
        int statusCode = response.getStatusLine().getStatusCode();
        if (!(statusCode >= 200 && statusCode <= 399)) {
            return null;
        }
        Header[] headerArray = response.getAllHeaders();
        httpHead.abort();
        httpHead.releaseConnection();
        return headerArray;
    }

    /**
     * ��ȡ��Ӧͷ��Ϣ����Content-Disposition��Content-Length
     *
     * @param url ����URL
     * @return
     */
    public static Header[] getResponseHeaders(String url) {
        return getResponseHeaders(url, null);
    }

    /**
     * ��ȡ��Ӧ״̬��
     *
     * @param url ����URL
     * @return
     * @headers ��������ͷ��Ϣ
     */
    public static int getStatusCode(String url, Map<String, String> headers) {
        DefaultHttpClient httpClient = getDefaultHttpClient();
        HttpHead httpHead = new HttpHead(url);
        //��������ͷ��Ϣ
        if (GerneralUtils.isNotEmptyMap(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                httpHead.addHeader(key, val);
            }
        }
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpHead);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //��ȡHTTP��Ӧ״̬��
        int statusCode = response.getStatusLine().getStatusCode();
        httpHead.abort();
        httpHead.releaseConnection();
        return statusCode;
    }

    /**
     * ��ȡ��Ӧ״̬��
     *
     * @param url ����URL
     * @return
     */
    public static int getStatusCode(String url) {
        return getStatusCode(url, null);
    }

    /**
     * @param @param  url
     * @param @return
     * @return String
     * @throws
     * @Title: getLocation
     * @Description: ��ȡ�ض���������URL
     */
    public static String getLocation(String url) {
        Header[] headers = getResponseHeaders(url);
        return HttpClientUtils.getHeaderByKey(headers, "Location");
    }

    /**
     * ����key��ȡָ��������/��Ӧͷ��Ϣ
     *
     * @param headers
     * @param key
     * @return
     */
    public static String getHeaderByKey(Header[] headers, String key) {
        if (GerneralUtils.isEmptyArray(headers) || GerneralUtils.isEmptyString(key)) {
            return null;
        }
        for (Header header : headers) {
            if (header.getName().equals(key)) {
                return header.getValue();
            }
        }
        return "";
    }

    /**
     * ����key��ȡ����/��Ӧͷ��Ϣ
     *
     * @param key
     * @return
     */
    public static String getHeaderByKey(Map<String, String> headerMap, String key) {
        if (headerMap == null || headerMap.isEmpty()) {
            return null;
        }
        Iterator<Map.Entry<String, String>> iter = headerMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            String ky = entry.getKey();
            if (ky.equals(key)) {
                return entry.getValue().toString();
            }
        }
        return "";
    }
} 