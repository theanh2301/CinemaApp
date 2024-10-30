package com.example.animatesplash.Domains;

import java.io.Serializable;

public class VideoItem implements Serializable {
    private String titleTxt;
    private String timeTxt;
    private String imbd;
    private String videoPic;

    public VideoItem(){}

    public VideoItem(String titleTxt, String timeTxt, String imbd, String videoPic) {
        this.titleTxt = titleTxt;
        this.timeTxt = timeTxt;
        this.imbd = imbd;
        this.videoPic = videoPic;
    }

    public String getTitleTxt() {
        return titleTxt;
    }

    public void setTitleTxt(String titleTxt) {
        this.titleTxt = titleTxt;
    }

    public String getTimeTxt() {
        return timeTxt;
    }

    public void setTimeTxt(String timeTxt) {
        this.timeTxt = timeTxt;
    }

    public String getImbd() {
        return imbd;
    }

    public void setImbd(String imbd) {
        this.imbd = imbd;
    }

    public String getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(String videoPic) {
        this.videoPic = videoPic;
    }
}
