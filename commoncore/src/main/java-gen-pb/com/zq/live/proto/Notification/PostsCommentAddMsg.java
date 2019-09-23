// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Notification.proto
package com.zq.live.proto.Notification;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * 帖子评论
 */
public final class PostsCommentAddMsg extends Message<PostsCommentAddMsg, PostsCommentAddMsg.Builder> {
  public static final ProtoAdapter<PostsCommentAddMsg> ADAPTER = new ProtoAdapter_PostsCommentAddMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Integer DEFAULT_POSTSID = 0;

  public static final Integer DEFAULT_COMMENTID = 0;

  public static final Integer DEFAULT_FIRSTLEVELCOMMENTID = 0;

  public static final Integer DEFAULT_REPLYEDCOMMENTID = 0;

  /**
   * 评论人userID
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * posts
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer postsID;

  /**
   * 新生成评论ID
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer commentID;

  /**
   * 若为二级评论，则需要传入所属的一级评论id
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer firstLevelCommentID;

  /**
   * 被回复的commentID
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer replyedCommentID;

  public PostsCommentAddMsg(Integer userID, Integer postsID, Integer commentID,
      Integer firstLevelCommentID, Integer replyedCommentID) {
    this(userID, postsID, commentID, firstLevelCommentID, replyedCommentID, ByteString.EMPTY);
  }

  public PostsCommentAddMsg(Integer userID, Integer postsID, Integer commentID,
      Integer firstLevelCommentID, Integer replyedCommentID, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.postsID = postsID;
    this.commentID = commentID;
    this.firstLevelCommentID = firstLevelCommentID;
    this.replyedCommentID = replyedCommentID;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.postsID = postsID;
    builder.commentID = commentID;
    builder.firstLevelCommentID = firstLevelCommentID;
    builder.replyedCommentID = replyedCommentID;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PostsCommentAddMsg)) return false;
    PostsCommentAddMsg o = (PostsCommentAddMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(postsID, o.postsID)
        && Internal.equals(commentID, o.commentID)
        && Internal.equals(firstLevelCommentID, o.firstLevelCommentID)
        && Internal.equals(replyedCommentID, o.replyedCommentID);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (postsID != null ? postsID.hashCode() : 0);
      result = result * 37 + (commentID != null ? commentID.hashCode() : 0);
      result = result * 37 + (firstLevelCommentID != null ? firstLevelCommentID.hashCode() : 0);
      result = result * 37 + (replyedCommentID != null ? replyedCommentID.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (postsID != null) builder.append(", postsID=").append(postsID);
    if (commentID != null) builder.append(", commentID=").append(commentID);
    if (firstLevelCommentID != null) builder.append(", firstLevelCommentID=").append(firstLevelCommentID);
    if (replyedCommentID != null) builder.append(", replyedCommentID=").append(replyedCommentID);
    return builder.replace(0, 2, "PostsCommentAddMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PostsCommentAddMsg.ADAPTER.encode(this);
  }

  public static final PostsCommentAddMsg parseFrom(byte[] data) throws IOException {
    PostsCommentAddMsg c = null;
       c = PostsCommentAddMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 评论人userID
   */
  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  /**
   * posts
   */
  public Integer getPostsID() {
    if(postsID==null){
        return DEFAULT_POSTSID;
    }
    return postsID;
  }

  /**
   * 新生成评论ID
   */
  public Integer getCommentID() {
    if(commentID==null){
        return DEFAULT_COMMENTID;
    }
    return commentID;
  }

  /**
   * 若为二级评论，则需要传入所属的一级评论id
   */
  public Integer getFirstLevelCommentID() {
    if(firstLevelCommentID==null){
        return DEFAULT_FIRSTLEVELCOMMENTID;
    }
    return firstLevelCommentID;
  }

  /**
   * 被回复的commentID
   */
  public Integer getReplyedCommentID() {
    if(replyedCommentID==null){
        return DEFAULT_REPLYEDCOMMENTID;
    }
    return replyedCommentID;
  }

  /**
   * 评论人userID
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * posts
   */
  public boolean hasPostsID() {
    return postsID!=null;
  }

  /**
   * 新生成评论ID
   */
  public boolean hasCommentID() {
    return commentID!=null;
  }

  /**
   * 若为二级评论，则需要传入所属的一级评论id
   */
  public boolean hasFirstLevelCommentID() {
    return firstLevelCommentID!=null;
  }

  /**
   * 被回复的commentID
   */
  public boolean hasReplyedCommentID() {
    return replyedCommentID!=null;
  }

  public static final class Builder extends Message.Builder<PostsCommentAddMsg, Builder> {
    private Integer userID;

    private Integer postsID;

    private Integer commentID;

    private Integer firstLevelCommentID;

    private Integer replyedCommentID;

    public Builder() {
    }

    /**
     * 评论人userID
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    /**
     * posts
     */
    public Builder setPostsID(Integer postsID) {
      this.postsID = postsID;
      return this;
    }

    /**
     * 新生成评论ID
     */
    public Builder setCommentID(Integer commentID) {
      this.commentID = commentID;
      return this;
    }

    /**
     * 若为二级评论，则需要传入所属的一级评论id
     */
    public Builder setFirstLevelCommentID(Integer firstLevelCommentID) {
      this.firstLevelCommentID = firstLevelCommentID;
      return this;
    }

    /**
     * 被回复的commentID
     */
    public Builder setReplyedCommentID(Integer replyedCommentID) {
      this.replyedCommentID = replyedCommentID;
      return this;
    }

    @Override
    public PostsCommentAddMsg build() {
      return new PostsCommentAddMsg(userID, postsID, commentID, firstLevelCommentID, replyedCommentID, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PostsCommentAddMsg extends ProtoAdapter<PostsCommentAddMsg> {
    public ProtoAdapter_PostsCommentAddMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PostsCommentAddMsg.class);
    }

    @Override
    public int encodedSize(PostsCommentAddMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.postsID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.commentID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.firstLevelCommentID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(5, value.replyedCommentID)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PostsCommentAddMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.postsID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.commentID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.firstLevelCommentID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.replyedCommentID);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PostsCommentAddMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setPostsID(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setCommentID(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setFirstLevelCommentID(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setReplyedCommentID(ProtoAdapter.UINT32.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public PostsCommentAddMsg redact(PostsCommentAddMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
