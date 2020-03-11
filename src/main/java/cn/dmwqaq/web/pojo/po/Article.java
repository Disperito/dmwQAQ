package cn.dmwqaq.web.pojo.po;

import java.util.Date;

public class Article {

    private Long id;
    private String title;
    private String authorName;
    private Date createDatetime;
    private Date updateDatetime;
    private String content;

    public Article(Long id, String title, String authorName, Date createDatetime, Date updateDatetime,
                   String content) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.createDatetime = createDatetime;
        this.updateDatetime = updateDatetime;
        this.content = content;
    }

    public Article(Long id, String title, String authorName, String content) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.content = content;
    }

    public Article(String title, String authorName, String content) {
        this.title = title;
        this.authorName = authorName;
        this.content = content;
    }

    public Article() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
