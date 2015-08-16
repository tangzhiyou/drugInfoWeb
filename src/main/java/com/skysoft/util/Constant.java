package com.skysoft.util;

/**
 * 常量类
 *
 */
public class Constant {

    /**
     * 默认读写缓冲区大小
     */
    public static final int BUFFER_SIZE = 4096;

    public static final String HTML_DOCTYPY = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0";

    public static final String[] PATTERNS = new String[]{
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm",
            "yyyy/MM/dd HH:mm:ss",
            "M/dd/yyyy",
            "M/d/yyyy",
            "MM/d/yyyy",
            "MM/dd/yyyy",
            "MM/dd/yyyy HH:mm",
            "MM/dd/yyyy HH:mm:ss",
            "HH:mm:ss",
            "HH:mm",
    };
    /**
     * 每页大小
     */
    public static final int PAGE_SIZE = 2000;
    /**
     * 默认编码
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * HTML页面模版
     */
    public static StringBuffer HTML_TEMPLATE = new StringBuffer();

    static {
        HTML_TEMPLATE.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">").append("\n");
        HTML_TEMPLATE.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">").append("\n");
        HTML_TEMPLATE.append("<head>").append("\n");
        HTML_TEMPLATE.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />").append("\n");
        HTML_TEMPLATE.append("<title>").append("%2$s").append("</title>").append("\n");
        HTML_TEMPLATE.append("</head>").append("\n");
        HTML_TEMPLATE.append("<body>").append("\n");
        HTML_TEMPLATE.append("%1$s").append("\n");
        HTML_TEMPLATE.append("</body>").append("\n");
        HTML_TEMPLATE.append("</html>");
    }
}
