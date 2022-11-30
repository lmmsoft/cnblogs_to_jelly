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
import java.util.*;

/**
 * 博客园xml转md文档
 */
public class App {

    static final String input_file_path = "CNBlogs_BlogBackup_131_201101_202211.xml";
    static final String output_folder_path = ".";// 指定你要导出的目录

    public static void main(String[] args) throws DocumentException, IOException, ParseException {
        SAXReader reader = new SAXReader();

        URL url = App.class.getClassLoader().getResource(input_file_path);

        Document document = reader.read(url);
        Element root = document.getRootElement();

        saveOldMarkdown(root);
    }

    private static void saveOldMarkdown(Element root) throws IOException {
        Element channel = null;
        for (Iterator<Element> it = root.elementIterator(); it.hasNext(); ) {
            channel = it.next();
        }
        for (Iterator<Element> it = channel.elementIterator("item"); it.hasNext(); ) {
            Element item = it.next();

            String title = item.element("title").getTextTrim();
            String link = item.element("link").getTextTrim();
            String pubDate = item.element("pubDate").getTextTrim();

            String meta = "---"
                    + "\n" + "title: '" + title + "'"
                    + "\n" + "date: " + getDatetimeString(pubDate)
                    + "\nlayout: post"
                    + "\npublished: true"
                    + "\ncomments: true"
                    + "\nmoveForm: cnblogs"
                    + "\nguid: " + link
                    + "\n---"
                    + "\n\n";
            String description = item.element("description").getText();

            saveOuputFile(
                    getFileNameString(pubDate) + getFileName(title),
                    meta + description
            );
        }
    }

    // 替换windows中非法文件名
    public static String getFileName(String invalidTitle) {
        return invalidTitle.replaceAll("[/\\\\:*?<>|]", "") + ".html";//cnblogs实际转出的是html的格式，如果是md, 可以改成.md
    }

    public static void saveOuputFile(String filename, String content) throws IOException {
        File file = new File(output_folder_path, filename);
//        file.deleteOnExit();// 删除存在的文件
        file.createNewFile();
        FileUtil.appendString(content, file, "UTF-8");
        System.out.println("已保存:" + file.getAbsolutePath());
    }

    static String getDatetimeString(String stringDate) {
        return DateUtil.format(new Date(stringDate), "yyyy-MM-dd HH:mm:ss");
    }

    static String getFileNameString(String stringDate) {
        return DateUtil.format(new Date(stringDate), "yyyy-MM-dd-");
    }
}
