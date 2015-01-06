package com.skysoft.util;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangzy on 2015/1/6.
 */
public class HtmlParser
{

        public static List<String> ExtractTableText(String content, String contexturl, String characterset) {
            List listText = null;
            Parser myParser = null;
            NodeList nodeList = null;
            try {
                myParser = new Parser(content);
                // 设置页面编码
                myParser.setEncoding(characterset);
                NodeFilter filter = new NodeClassFilter(TableTag.class);
                nodeList = myParser.extractAllNodesThatMatch(filter);
            } catch (ParserException e) {
                e.printStackTrace();
            }
            listText = new ArrayList();
            TableTag table = (TableTag) nodeList.elementAt(0);
            for (int j = 1; j < table.getRowCount() - 1; j++) {
                TableRow tRow = table.getRow(j);
                TableColumn[] columns = tRow.getColumns();
                listText.add(j-1, columns[1].toPlainTextString());
                System.out.println(columns[1].toPlainTextString());
            }
            listText.add(contexturl);

            return listText;
        }
}
