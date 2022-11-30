# 博客园文章导出到jelly工具

- 优化了这篇文章里的代码 https://www.cnblogs.com/greyzeng/p/14949545.html
- 使用方法：

1. 在 https://i.cnblogs.com/posts 点击右侧的【博客备份】，导出xml(工作日晚上和周末可以导出)，或者直接点击 https://i1.cnblogs.com/BlogBackup.aspx 链接
2. 修改源码里的输入文件，使用上文导出的xml作为输入，修改这一行: https://github.com/lmmsoft/cnblogs_to_jelly/blob/480624fded69e681c4997225a212a6ecd55701dd/src/main/java/git/snippets/App.java#L21
3. 运行代码，在输出目录里找到导出的【日期-标题.html】文件列表

- 代码已整理，大家凑合用吧
- 导出效果可参考: https://github.com/lmmsoft/cnblogs_to_jelly/blob/master/2011-01-22-%E6%88%91%E6%9D%A5%E5%88%B0%E5%8D%9A%E5%AE%A2%E5%9B%AD%E4%BA%86%EF%BC%81.html
