package com.thinkdiffai.futurelove.model.comment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentList {

    @SerializedName("comment")
    private List<CommentPage> comment;
//    @SerializedName("sophantu")
//    private int sophantu;
//    @SerializedName("sotrang")
//    private double sotrang;


    public List<CommentPage> getComment() {
        return comment;
    }

    public void setComment(List<CommentPage> comment) {
        this.comment = comment;
    }

//    public int getSophantu() {
//        return sophantu;
//    }
//
//    public void setSophantu(int sophantu) {
//        this.sophantu = sophantu;
//    }
//
//    public double getSotrang() {
//        return sotrang;
//    }
//
//    public void setSotrang(double sotrang) {
//        this.sotrang = sotrang;
//    }
}
