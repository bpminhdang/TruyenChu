package com.example.truyenchu._class;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

// Lớp Truyen (Truyện)
public class StoryClass implements Serializable
{
    private int id;
    private String name;
    private String time;
    private String updateTime;
    private String author;
    private String status;
    private String description;
    private int numberOfChapter;
    //private Image image;
    private List<ChapterClass> chapters = new ArrayList<>(); // List để lưu danh sách các chương
    private List<String> genres = new ArrayList<>();
    private List<CommentClass> comments = new ArrayList<>();
    private List<String> uuidLikedUsers = new ArrayList<>();
    private int views = 0;
    private int watching = 0;
    private String uri; // Retrive data from Firebase
    private String userUUID;

    private String
            avgRating = "0";

    // Constructor
    public StoryClass(int id, String name, String time, String uploadTime, String author, String status, String description, int numberOfChapter, List<String> genres, int views)
    {
        this.id = id;
        this.name = name;
        this.time = time;
        this.updateTime = uploadTime;
        this.author = author;
        this.status = status;
        this.description = description;
        this.numberOfChapter = numberOfChapter;
        this.genres = genres;
        this.views = views;
    }


    public StoryClass(int id)
    {
        this.id = id;
    }

    public StoryClass()
    {
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getName(int cutOffWhenMoreThan)
    {
        return cutOff(name, cutOffWhenMoreThan);
    }


    public String getAuthor()
    {
        return author;
    }

    public String getStatus()
    {
        return status;
    }

    public int getNumberOfChapter()
    {
        return numberOfChapter;
    }

    public List<String> getGenresList()
    {
        return genres;
    }

    public String getGenres(int cutOffWhenMoreThan)
    {
        String s;
        if (genres.size() > 1)
        {
            s = String.join(", ", genres);
            try
            {
                s = cutOff(s, cutOffWhenMoreThan);
            } catch (Exception e)
            {
                Log.i("Story Error", e.toString());
            }
        }
        else
            s = genres.get(0);
        return s;
    }

    public List<ChapterClass> getChapters()
    {
        return chapters;
    }

    public int getViews()
    {
        return views;
    }

    // Thêm chương vào danh sách của truyện
    public void addChapter(ChapterClass chapterClass)
    {
        chapters.add(chapterClass);
    }


    public String getTime()
    {
        return time;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUri()
    {
        return uri;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setNumberOfChapter(int numberOfChapter)
    {
        this.numberOfChapter = numberOfChapter;
    }

    public void setChapters(List<ChapterClass> chapters)
    {
        this.chapters = chapters;
    }

    public void setGenres(List<String> genres)
    {
        this.genres = genres;
    }

    public void setViews(int views)
    {
        this.views = views;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public List<CommentClass> getComments()
    {
        return comments;
    }

    public void setComments(List<CommentClass> comments)
    {
        this.comments = comments;
    }

    public List<String> getUuidLikedUsers()
    {
        return uuidLikedUsers;
    }

    public void setUuidLikedUsers(List<String> uuidLikedUsers)
    {
        this.uuidLikedUsers = uuidLikedUsers;
    }

    public static String cutOff(String text, int maxLenght)
    {
        if (text.length() > maxLenght)
        {
            int lastIndex = text.lastIndexOf(' ', maxLenght);
            if (lastIndex != -1)
            {
                return text.substring(0, lastIndex) + "...";
            }
        }
        return text;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("ID: ").append(id).append("\n");
        stringBuilder.append("Name: ").append(name).append("\n");
        stringBuilder.append("Time: ").append(time).append("\n");
        stringBuilder.append("Time update: ").append(updateTime).append("\n");
        stringBuilder.append("Author: ").append(author).append("\n");
        stringBuilder.append("Status: ").append(status).append("\n");
        stringBuilder.append("Description: ").append(description).append("\n");
        stringBuilder.append("Number of Chapters: ").append(numberOfChapter).append("\n");
        stringBuilder.append("Genres: ").append(genres).append("\n");
        stringBuilder.append("Views: ").append(views).append("\n");
        stringBuilder.append("URI: ").append(uri).append("\n");

        // Append chapters information
        stringBuilder.append("Chapters: \n");
        for (ChapterClass chapter : chapters) {
            stringBuilder.append("\tChapter ID: ").append(chapter.getChapterId()).append(", Content: ").append(chapter.getContent()).append("\n");
        }

        return stringBuilder.toString();
    }

    public static void SetText(TextView textView, String text)
    {
        textView.setText(text);
    }

    public static void SetImage(Context context, String uri, ImageView imageView)
    {
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }


//    protected StoryClass(Parcel in) {
//        id = in.readInt();
//        name = in.readString();
//        time = in.readString();
//        updateTime = in.readString();
//        author = in.readString();
//        status = in.readString();
//        description = in.readString();
//        numberOfChapter = in.readInt();
//        in.readList(chapters, ChapterClass.class.getClassLoader());
//        genres = in.createStringArrayList();
//        views = in.readInt();
//        uri = in.readString();
//    }
//
//    public static final Parcelable.Creator<StoryClass> CREATOR = new Parcelable.Creator<StoryClass>() {
//        @Override
//        public StoryClass createFromParcel(Parcel in) {
//            return new StoryClass(in);
//        }
//
//        @Override
//        public StoryClass[] newArray(int size) {
//            return new StoryClass[size];
//        }
//    };


    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getContent(int chapterNum)
    {
        return chapters.get(chapterNum).getContent();
    }

    public int getWatching()
    {
        return watching;
    }

    public void setWatching(int watching)
    {
        this.watching = watching;
    }

    public String getUserUUID()
    {
        return userUUID;
    }

    public void setUserUUID(String userUUID)
    {
        this.userUUID = userUUID;
    }

    public void sortChaptersById() {
        Collections.sort(chapters, Comparator.comparingInt(chapter -> extractIndex(chapter.getChapterId())));
    }

    // Phương thức trích xuất số từ chapterId
    private int extractIndex(String chapterId) {
        String[] parts = chapterId.split("_");
        if (parts.length == 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Xử lý trường hợp không chuyển đổi được thành số nguyên
            }
        }
        return -1; // Trả về giá trị mặc định khi không thành công
    }

    public String GetChapterContent(int chapterID)
    {
        return chapters.get(chapterID - 1).getContent();
    }

    public String GetIdString()
    {
        return String.valueOf(id);
    }

    public static StoryClass loadStoryFromFile(Context context ,String storyId)
    {
        StoryClass loadedStory = null;
        String fileName = storyId + ".json"; // Tên file là ID của truyện + ".json"

        // Lấy đường dẫn đến thư mục "stories" trong internal storage
        File directory = new File(context.getFilesDir() + "/stories");
        File file = new File(directory, fileName);

        if (file.exists())
        {
            try (FileInputStream fis = new FileInputStream(file))
            {
                int size = fis.available();
                byte[] buffer = new byte[size];
                fis.read(buffer);
                fis.close();
                String storyJson = new String(buffer);

                // Chuyển đổi chuỗi JSON thành đối tượng StoryClass
                Gson gson = new Gson();
                loadedStory = gson.fromJson(storyJson, StoryClass.class);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return loadedStory;
    }

    public static String NormalizedData(String s)
    {
        if (s == null)
            return null;
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String converted = pattern.matcher(normalized).replaceAll("").toLowerCase();
        converted = converted.replace("đ", "d").replace(" ", "");
        return converted;
    }

    public String GetAvgRating()
    {
        return avgRating;
    }

    public void SetAvgRating(String avgRating)
    {
        this.avgRating = avgRating;
    }
}