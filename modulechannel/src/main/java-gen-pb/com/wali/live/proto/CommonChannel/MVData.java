// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: CommonChannel.proto
package com.wali.live.proto.CommonChannel;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class MVData extends Message<MVData, MVData.Builder> {
  public static final ProtoAdapter<MVData> ADAPTER = new ProtoAdapter_MVData();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_FEED_ID = "";

  public static final String DEFAULT_COVER_URL = "";

  public static final Integer DEFAULT_WIDTH = 0;

  public static final Integer DEFAULT_HEIGHT = 0;

  public static final String DEFAULT_TITLE = "";

  public static final Long DEFAULT_DURATION = 0L;

  public static final Integer DEFAULT_PLAY_COUNT = 0;

  /**
   * mv对应的feedId
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String feed_id;

  /**
   * 视频封面
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String cover_url;

  /**
   * 图片宽度
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer width;

  /**
   * 图片高度
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer height;

  /**
   * 标题
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String title;

  /**
   * 播放时长
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long duration;

  /**
   * 播放次数
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer play_count;

  /**
   * 用户资料[同模板协议UserInfo]
   */
  @WireField(
      tag = 8,
      adapter = "com.wali.live.proto.CommonChannel.UserInfo#ADAPTER"
  )
  public final UserInfo user;

  public MVData(String feed_id, String cover_url, Integer width, Integer height, String title,
      Long duration, Integer play_count, UserInfo user) {
    this(feed_id, cover_url, width, height, title, duration, play_count, user, ByteString.EMPTY);
  }

  public MVData(String feed_id, String cover_url, Integer width, Integer height, String title,
      Long duration, Integer play_count, UserInfo user, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.feed_id = feed_id;
    this.cover_url = cover_url;
    this.width = width;
    this.height = height;
    this.title = title;
    this.duration = duration;
    this.play_count = play_count;
    this.user = user;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.feed_id = feed_id;
    builder.cover_url = cover_url;
    builder.width = width;
    builder.height = height;
    builder.title = title;
    builder.duration = duration;
    builder.play_count = play_count;
    builder.user = user;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MVData)) return false;
    MVData o = (MVData) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(feed_id, o.feed_id)
        && Internal.equals(cover_url, o.cover_url)
        && Internal.equals(width, o.width)
        && Internal.equals(height, o.height)
        && Internal.equals(title, o.title)
        && Internal.equals(duration, o.duration)
        && Internal.equals(play_count, o.play_count)
        && Internal.equals(user, o.user);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (feed_id != null ? feed_id.hashCode() : 0);
      result = result * 37 + (cover_url != null ? cover_url.hashCode() : 0);
      result = result * 37 + (width != null ? width.hashCode() : 0);
      result = result * 37 + (height != null ? height.hashCode() : 0);
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (duration != null ? duration.hashCode() : 0);
      result = result * 37 + (play_count != null ? play_count.hashCode() : 0);
      result = result * 37 + (user != null ? user.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (feed_id != null) builder.append(", feed_id=").append(feed_id);
    if (cover_url != null) builder.append(", cover_url=").append(cover_url);
    if (width != null) builder.append(", width=").append(width);
    if (height != null) builder.append(", height=").append(height);
    if (title != null) builder.append(", title=").append(title);
    if (duration != null) builder.append(", duration=").append(duration);
    if (play_count != null) builder.append(", play_count=").append(play_count);
    if (user != null) builder.append(", user=").append(user);
    return builder.replace(0, 2, "MVData{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MVData.ADAPTER.encode(this);
  }

  public static final MVData parseFrom(byte[] data) throws IOException {
    MVData c = null;
       c = MVData.ADAPTER.decode(data);
    return c;
  }

  /**
   * mv对应的feedId
   */
  public String getFeedId() {
    if(feed_id==null){
        return DEFAULT_FEED_ID;
    }
    return feed_id;
  }

  /**
   * 视频封面
   */
  public String getCoverUrl() {
    if(cover_url==null){
        return DEFAULT_COVER_URL;
    }
    return cover_url;
  }

  /**
   * 图片宽度
   */
  public Integer getWidth() {
    if(width==null){
        return DEFAULT_WIDTH;
    }
    return width;
  }

  /**
   * 图片高度
   */
  public Integer getHeight() {
    if(height==null){
        return DEFAULT_HEIGHT;
    }
    return height;
  }

  /**
   * 标题
   */
  public String getTitle() {
    if(title==null){
        return DEFAULT_TITLE;
    }
    return title;
  }

  /**
   * 播放时长
   */
  public Long getDuration() {
    if(duration==null){
        return DEFAULT_DURATION;
    }
    return duration;
  }

  /**
   * 播放次数
   */
  public Integer getPlayCount() {
    if(play_count==null){
        return DEFAULT_PLAY_COUNT;
    }
    return play_count;
  }

  /**
   * 用户资料[同模板协议UserInfo]
   */
  public UserInfo getUser() {
    if(user==null){
        return new UserInfo.Builder().build();
    }
    return user;
  }

  /**
   * mv对应的feedId
   */
  public boolean hasFeedId() {
    return feed_id!=null;
  }

  /**
   * 视频封面
   */
  public boolean hasCoverUrl() {
    return cover_url!=null;
  }

  /**
   * 图片宽度
   */
  public boolean hasWidth() {
    return width!=null;
  }

  /**
   * 图片高度
   */
  public boolean hasHeight() {
    return height!=null;
  }

  /**
   * 标题
   */
  public boolean hasTitle() {
    return title!=null;
  }

  /**
   * 播放时长
   */
  public boolean hasDuration() {
    return duration!=null;
  }

  /**
   * 播放次数
   */
  public boolean hasPlayCount() {
    return play_count!=null;
  }

  /**
   * 用户资料[同模板协议UserInfo]
   */
  public boolean hasUser() {
    return user!=null;
  }

  public static final class Builder extends Message.Builder<MVData, Builder> {
    public String feed_id;

    public String cover_url;

    public Integer width;

    public Integer height;

    public String title;

    public Long duration;

    public Integer play_count;

    public UserInfo user;

    public Builder() {
    }

    /**
     * mv对应的feedId
     */
    public Builder setFeedId(String feed_id) {
      this.feed_id = feed_id;
      return this;
    }

    /**
     * 视频封面
     */
    public Builder setCoverUrl(String cover_url) {
      this.cover_url = cover_url;
      return this;
    }

    /**
     * 图片宽度
     */
    public Builder setWidth(Integer width) {
      this.width = width;
      return this;
    }

    /**
     * 图片高度
     */
    public Builder setHeight(Integer height) {
      this.height = height;
      return this;
    }

    /**
     * 标题
     */
    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    /**
     * 播放时长
     */
    public Builder setDuration(Long duration) {
      this.duration = duration;
      return this;
    }

    /**
     * 播放次数
     */
    public Builder setPlayCount(Integer play_count) {
      this.play_count = play_count;
      return this;
    }

    /**
     * 用户资料[同模板协议UserInfo]
     */
    public Builder setUser(UserInfo user) {
      this.user = user;
      return this;
    }

    @Override
    public MVData build() {
      return new MVData(feed_id, cover_url, width, height, title, duration, play_count, user, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MVData extends ProtoAdapter<MVData> {
    public ProtoAdapter_MVData() {
      super(FieldEncoding.LENGTH_DELIMITED, MVData.class);
    }

    @Override
    public int encodedSize(MVData value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.feed_id)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.cover_url)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.width)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.height)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.title)
          + ProtoAdapter.UINT64.encodedSizeWithTag(6, value.duration)
          + ProtoAdapter.UINT32.encodedSizeWithTag(7, value.play_count)
          + UserInfo.ADAPTER.encodedSizeWithTag(8, value.user)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MVData value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.feed_id);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.cover_url);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.width);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.height);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.title);
      ProtoAdapter.UINT64.encodeWithTag(writer, 6, value.duration);
      ProtoAdapter.UINT32.encodeWithTag(writer, 7, value.play_count);
      UserInfo.ADAPTER.encodeWithTag(writer, 8, value.user);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MVData decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setFeedId(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setCoverUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setWidth(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setHeight(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setTitle(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.setDuration(ProtoAdapter.UINT64.decode(reader)); break;
          case 7: builder.setPlayCount(ProtoAdapter.UINT32.decode(reader)); break;
          case 8: builder.setUser(UserInfo.ADAPTER.decode(reader)); break;
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
    public MVData redact(MVData value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = UserInfo.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
