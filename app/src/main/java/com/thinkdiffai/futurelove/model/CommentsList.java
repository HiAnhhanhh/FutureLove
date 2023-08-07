package com.thinkdiffai.futurelove.model;

import java.util.List;

public class CommentsList {
    private List<Comment> comment;
    private int sophantu;
    private float sotrang;


    public CommentsList(List<Comment> comment) {
        this.comment = comment;
    }

    public CommentsList() {
    }

    public CommentsList(List<Comment> comment, int sophantu, int sotrang) {
        this.comment = comment;
        this.sophantu = sophantu;
        this.sotrang = sotrang;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public int getSophantu() {
        return sophantu;
    }

    public void setSophantu(int sophantu) {
        this.sophantu = sophantu;
    }

    public float getSotrang() {
        return sotrang;
    }

    public void setSotrang(int sotrang) {
        this.sotrang = sotrang;
    }
}
