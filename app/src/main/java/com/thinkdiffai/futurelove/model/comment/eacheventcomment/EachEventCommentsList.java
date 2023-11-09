package com.thinkdiffai.futurelove.model.comment.eacheventcomment;

import com.google.gson.annotations.SerializedName;
import com.thinkdiffai.futurelove.model.comment.CommentPage;

import java.util.List;

public class EachEventCommentsList {
    @SerializedName("comment")
    private List<CommentPage> eachEventCommentList;

    public List<CommentPage> getEachEventCommentList() {
        return eachEventCommentList;
    }

    public void setEachEventCommentList(List<CommentPage> eachEventCommentList) {
        this.eachEventCommentList = eachEventCommentList;
    }
}
