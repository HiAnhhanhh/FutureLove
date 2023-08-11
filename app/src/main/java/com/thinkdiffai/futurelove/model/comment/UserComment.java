package com.thinkdiffai.futurelove.model.comment;

import java.util.List;

public class UserComment {
    private List<CommentUser> comment_user;

    public List<CommentUser> getComment_user() {
        return comment_user;
    }

    public void setComment_user(List<CommentUser> comment_user) {
        this.comment_user = comment_user;
    }
}