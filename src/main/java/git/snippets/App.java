package git.snippets;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.BackedList;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

/**
 * 博客园xml转md文档
 */
public class App {
    // 指定你要导出的目录
    static final String output_folder_path = ".";
    //    static final String input_file_path = "sample.xml";CNBlogs_BlogBackup_131_201101_202211.xml
    static final String input_file_path = "CNBlogs_BlogBackup_131_201101_202211.xml";

//    static final String input_file_path = "un_estate.xml";

    public static void main(String[] args) throws DocumentException, IOException, ParseException {
        SAXReader reader = new SAXReader();

        URL url = App.class.getClassLoader().getResource(input_file_path);

        Document document = reader.read(url);
        Element root = document.getRootElement();

//        Blog blog = getPostList(root);

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
                    + "\n" + "date: " + formatDatetime(pubDate)
                    + "\nlayout: post"
                    + "\npublished: true"
                    + "\ncomments: true"
                    + "\nmoveForm: cnblogs"
                    + "\nguid: " + link
                    + "\n---"
                    + "\n\n";
            String description = item.element("description").getText();

            saveOuputFile(
                    getFileNameString(pubDate)+getFileName(title),
                    meta + description
            );
        }
    }

    public static Blog getPostList(Element root) {
        Blog blog = new Blog();
        blog.postList = new ArrayList<Post>();

        String rootName = root.getName();
        BackedList postList;
        switch (rootName) {
            case "rss":
                System.out.println("rss");
                Element channel = root.element("channel");

                blog.title = channel.elementText("title");
                blog.url = channel.elementText("link");

                for (Iterator<Element> it = channel.elementIterator("item"); it.hasNext(); ) {
                    Element item = it.next();

                    Post post = new Post();
                    post.title = item.element("title").getTextTrim();
                    post.link = item.element("link").getTextTrim();
                    post.content = item.element("description").getText();

                    blog.postList.add(post);
                }
                break;
            case "feed":
                System.out.println("feed");

                blog.title = root.elementText("title");
                blog.url = root.element("author").elementText("uri");

                for (Iterator<Element> it = root.elementIterator("entry"); it.hasNext(); ) {
                    Element e = it.next();

                    Post post = new Post();
                    post.title = e.element("title").getTextTrim();
                    post.link = e.element("link").getTextTrim();
                    post.content = e.element("content").getText();

                    String published = e.elementText("published");
                    String dt2 = formatDatetime(published);

                    blog.postList.add(post);
                }
                break;
            default:
                break;
        }
        return blog;
    }

    // 替换windows中非法文件名
    public static String getFileName(String invalidTitle) {
        return invalidTitle.replaceAll("[/\\\\:*?<>|]", "") + ".html";
    }

    public static void saveOuputFile(String filename, String content) throws IOException {
        File file = new File(output_folder_path, filename);
//        file.deleteOnExit();
        file.createNewFile();
        FileUtil.appendString(content, file, "UTF-8");
        System.out.println("已保存:" + file.getAbsolutePath());
    }

    static String formatDatetime(String stringDate) {
        return DateUtil.format(new Date(stringDate), "yyyy-MM-dd HH:mm:ss");
    }

    static String getFileNameString(String stringDate) {
        return DateUtil.format(new Date(stringDate), "yyyy-MM-dd-");
    }
}

class Blog {
    public String title;
    public String url;
    public List<Post> postList;
}

class Post {
    public String title;
    public String link;
    public String published;
    public String content;
}