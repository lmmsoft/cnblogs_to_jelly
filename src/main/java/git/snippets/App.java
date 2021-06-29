package git.snippets;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

/**
 * 博客园xml转md文档
 */
public class App {
    // 指定你要导出的目录
    static final String path = "C:\\Users\\zhuiz\\OneDrive\\blogs2";

    public static void main(String[] args) throws DocumentException, IOException, ParseException {
        SAXReader reader = new SAXReader();

        URL url = App.class.getClassLoader().getResource("sample.xml");

        Document document = reader.read(url);
        Element root = document.getRootElement();
        Element channel = null;
        for (Iterator<Element> it = root.elementIterator(); it.hasNext(); ) {
            channel = it.next();
        }
        for (Iterator<Element> it = channel.elementIterator("item"); it.hasNext(); ) {
            Element item = it.next();
            String title = item.element("title").getTextTrim();
            String link = item.element("link").getTextTrim();
            String prefix = "---\n" +
                    "title: '" + title + "'\n" +
                    "date: " + format(item.element("pubDate").getTextTrim()) + "\n\n\n\n" +
                    "---\n\n\n\n" +
                    "<meta name = \"referrer\" content = \"no-referrer\" />\n\n\n\n";
            inputFile(title(title), link + "\n\n\n\n\n" + prefix + item.element("description").getText());
        }
    }

    // 替换windows中非法文件名
    public static String title(String invalidTitle) {
        return invalidTitle.replaceAll("[/\\\\:*?<>|]", "") + ".md";
    }

    public static void inputFile(String title, String content) throws IOException {
        File file = new File(path, title);
        file.createNewFile();
        FileUtil.appendString(content, file, "UTF-8");

    }

    static String format(String stringDate) {
        return DateUtil.format(new Date(stringDate), "yyyy-MM-dd HH:mm:ss");
    }
}
