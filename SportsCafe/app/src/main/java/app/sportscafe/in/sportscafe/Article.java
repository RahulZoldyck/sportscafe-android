package app.sportscafe.in.sportscafe;

/**
 * Created by rb on 1/12/15.
 */
public class Article
{
    private String title;
    private String summary;
    private String image_URL;
    private String author;
    private String sport;
    private String date;
    private String articleType;

    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setSummary(String summary)
    {
        this.summary = summary;
    }
    public void setImage_URL(String image_url)
    {
        this.image_URL = image_url;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSummary()
    {
        return summary;
    }

    public String getImage_URL()
    {
        return image_URL;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getSport()
    {
        return sport;
    }

    public void setSport(String sport)
    {
        this.sport = sport;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getArticleType()
    {
        return articleType;
    }

    public void setArticleType(String articleType)
    {
        this.articleType = articleType;
    }
}
