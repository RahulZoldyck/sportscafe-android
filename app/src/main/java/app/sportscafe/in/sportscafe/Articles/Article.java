package app.sportscafe.in.sportscafe.Articles;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rb on 1/12/15.
 */
public class Article implements Parcelable
{
    private String id;
    private String title;
    private String summary;
    private String content;
    private String image_URL;
    private String author;
    private String sport;
    private String date;
    private String articleType;
    private String credits;

    public Article()
    {

    }
    public Article(Parcel parcel)
    {
        id= parcel.readString();
        title = parcel.readString();
        summary = parcel.readString();
        content = parcel.readString();
        image_URL = parcel.readString();
        author = parcel.readString();
        sport = parcel.readString();
        date = parcel.readString();
        articleType = parcel.readString();
        credits = parcel.readString();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

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

    public String getCredits()
    {
        return credits;
    }

    public void setCredits(String credits)
    {
        this.credits = credits;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeString(content);
        dest.writeString(image_URL);
        dest.writeString(author);
        dest.writeString(sport);
        dest.writeString(date);
        dest.writeString(articleType);
        dest.writeString(credits);
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>()
    {
        public Article createFromParcel (Parcel in)
        {
            return new Article(in);
        }
        public Article[] newArray (int size)
        {
            return new Article[size];
        }
    };
}
