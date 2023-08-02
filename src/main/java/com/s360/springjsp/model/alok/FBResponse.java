package com.s360.springjsp.model.alok;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class FBResponse {
    public String referenceId;
    public boolean success;
    public Data data;
}

@Getter
@Setter
@ToString
class Data{
    public ArrayList<Post> posts;
    public Pagination pagination;
}

@Getter
@Setter
@ToString
class Pagination{
    public int offset;
    public int limit;
    public int total;
    public boolean hasNext;
}

@Getter
@Setter
@ToString
class Post{
    public String postId;
    public String createdOnGMT;
    public String imageUrl;
    public String brandLogo;
    public String message;
    public String channelName;
    public int reactionsCount;
    public int likesCount;
    public int commentsCount;
    public int sharesCount;
    public int engagement;
    public int s360Engagement;
    public int reach;
    public int impression;
    public String postType;
    public String videoUrl;
    public String permalink;
    public String title;
    public String description;
    public long channelId;
    public boolean admin;
    public String userName;
    public int post_clicks_by_type_linkclicks;
    public int post_clicks_by_type_otherclicks;
    public int post_clicks_by_type_photoview;
    public int post_impressions_lifetime;
    public int post_impressions_unique_lifetime;
    public int post_impressions_paid_lifetime;
    public int post_impressions_paid_unique_lifetime;
    public int post_impressions_organic_lifetime;
    public int post_impressions_organic_unique_lifetime;
    public int post_video_views_lifetime;
    public int post_video_views_organic_lifetime;
    public int post_video_views_paid_lifetime;
    public int post_negative_feedback_lifetime;
    public int post_engaged_users_lifetime;
    public int post_reactions_like_total_lifetime;
    public int post_reactions_love_total;
    public int post_reactions_wow_total;
    public int post_reactions_haha_total;
    public int post_reactions_sorry_total;
    public int post_reactions_anger_total;
    public int post_reactions_by_type_total_lifetime;
    public boolean paidPost;
    public boolean isAdsPost;
    public String docKey;
}