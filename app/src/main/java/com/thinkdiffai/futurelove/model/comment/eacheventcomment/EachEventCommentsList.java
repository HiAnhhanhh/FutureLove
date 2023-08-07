package com.thinkdiffai.futurelove.model.comment.eacheventcomment;

import com.google.gson.annotations.SerializedName;
import com.thinkdiffai.futurelove.model.comment.Comment;

import java.util.List;

public class EachEventCommentsList {
    @SerializedName("comment")
    private List<Comment> eachEventCommentList;


    public List<Comment> getEachEventCommentList() {
        return eachEventCommentList;
    }

    public void setEachEventCommentList(List<Comment> eachEventCommentList) {
        this.eachEventCommentList = eachEventCommentList;
    }
}
