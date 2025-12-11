package com.aiu.trips.dto;

/**
 * FeedbackDTO for transferring feedback data
 */
public class FeedbackDTO {
    private Long feedbackId;
    private Long studentId;
    private Long activityId;
    private Integer rating;
    private String comment;

    public FeedbackDTO() {}

    // Getters and Setters
    public Long getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Long feedbackId) { this.feedbackId = feedbackId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
