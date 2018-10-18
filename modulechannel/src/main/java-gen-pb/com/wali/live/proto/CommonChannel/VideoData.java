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
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class VideoData extends Message<VideoData, VideoData.Builder> {
  public static final ProtoAdapter<VideoData> ADAPTER = new ProtoAdapter_VideoData();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_FEED_ID = "";

  public static final String DEFAULT_JUMP_URI = "";

  public static final String DEFAULT_COVER_URL = "";

  public static final Integer DEFAULT_WIDTH = 0;

  public static final Integer DEFAULT_HEIGHT = 0;

  public static final String DEFAULT_TITLE = "";

  public static final Long DEFAULT_LIKE_COUNT = 0L;

  public static final Boolean DEFAULT_IS_LIKED = false;

  public static final Integer DEFAULT_DISTANCE = 0;

  public static final String DEFAULT_CITY = "";

  /**
   * 视频对应的feedId
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String feed_id;

  /**
   * 跳转scheme
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String jump_uri;

  /**
   * 视频封面
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String cover_url;

  /**
   * 图片或者小视频宽度
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer width;

  /**
   * 图片或者小视频高度
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer height;

  /**
   * 标题
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String title;

  /**
   * 点赞数
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long like_count;

  /**
   * 用户资料[同模板协议UserInfo]
   */
  @WireField(
      tag = 8,
      adapter = "com.wali.live.proto.CommonChannel.UserInfo#ADAPTER"
  )
  public final UserInfo user;

  /**
   * 封面运营角标[同模板协议RichText]
   */
  @WireField(
      tag = 9,
      adapter = "com.wali.live.proto.CommonChannel.RichText#ADAPTER"
  )
  public final RichText mark;

  /**
   * 用户是否已经赞过
   */
  @WireField(
      tag = 10,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean is_liked;

  /**
   * 用户与小视频发布地点的距离，单位：米
   */
  @WireField(
      tag = 11,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer distance;

  /**
   * 小视频发布的城市[无定位时显示]
   */
  @WireField(
      tag = 12,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String city;

  public VideoData(String feed_id, String jump_uri, String cover_url, Integer width, Integer height,
      String title, Long like_count, UserInfo user, RichText mark, Boolean is_liked,
      Integer distance, String city) {
    this(feed_id, jump_uri, cover_url, width, height, title, like_count, user, mark, is_liked, distance, city, ByteString.EMPTY);
  }

  public VideoData(String feed_id, String jump_uri, String cover_url, Integer width, Integer height,
      String title, Long like_count, UserInfo user, RichText mark, Boolean is_liked,
      Integer distance, String city, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.feed_id = feed_id;
    this.jump_uri = jump_uri;
    this.cover_url = cover_url;
    this.width = width;
    this.height = height;
    this.title = title;
    this.like_count = like_count;
    this.user = user;
    this.mark = mark;
    this.is_liked = is_liked;
    this.distance = distance;
    this.city = city;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.feed_id = feed_id;
    builder.jump_uri = jump_uri;
    builder.cover_url = cover_url;
    builder.width = width;
    builder.height = height;
    builder.title = title;
    builder.like_count = like_count;
    builder.user = user;
    builder.mark = mark;
    builder.is_liked = is_liked;
    builder.distance = distance;
    builder.city = city;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof VideoData)) return false;
    VideoData o = (VideoData) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(feed_id, o.feed_id)
        && Internal.equals(jump_uri, o.jump_uri)
        && Internal.equals(cover_url, o.cover_url)
        && Internal.equals(width, o.width)
        && Internal.equals(height, o.height)
        && Internal.equals(title, o.title)
        && Internal.equals(like_count, o.like_count)
        && Internal.equals(user, o.user)
        && Internal.equals(mark, o.mark)
        && Internal.equals(is_liked, o.is_liked)
        && Internal.equals(distance, o.distance)
        && Internal.equals(city, o.city);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (feed_id != null ? feed_id.hashCode() : 0);
      result = result * 37 + (jump_uri != null ? jump_uri.hashCode() : 0);
      result = result * 37 + (cover_url != null ? cover_url.hashCode() : 0);
      result = result * 37 + (width != null ? width.hashCode() : 0);
      result = result * 37 + (height != null ? height.hashCode() : 0);
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (like_count != null ? like_count.hashCode() : 0);
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (mark != null ? mark.hashCode() : 0);
      result = result * 37 + (is_liked != null ? is_liked.hashCode() : 0);
      result = result * 37 + (distance != null ? distance.hashCode() : 0);
      result = result * 37 + (city != null ? city.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (feed_id != null) builder.append(", feed_id=").append(feed_id);
    if (jump_uri != null) builder.append(", jump_uri=").append(jump_uri);
    if (cover_url != null) builder.append(", cover_url=").append(cover_url);
    if (width != null) builder.append(", width=").append(width);
    if (height != null) builder.append(", height=").append(height);
    if (title != null) builder.append(", title=").append(title);
    if (like_count != null) builder.append(", like_count=").append(like_count);
    if (user != null) builder.append(", user=").append(user);
    if (mark != null) builder.append(", mark=").append(mark);
    if (is_liked != null) builder.append(", is_liked=").append(is_liked);
    if (distance != null) builder.append(", distance=").append(distance);
    if (city != null) builder.append(", city=").append(city);
    return builder.replace(0, 2, "VideoData{").append('}').toString();
  }

  public byte[] toByteArray() {
    return VideoData.ADAPTER.encode(this);
  }

  public static final VideoData parseFrom(byte[] data) throws IOException {
    VideoData c = null;
       c = VideoData.ADAPTER.decode(data);
    return c;
  }

  /**
   * 视频对应的feedId
   */
  public String getFeedId() {
    if(feed_id==null){
        return DEFAULT_FEED_ID;
    }
    return feed_id;
  }

  /**
   * 跳转scheme
   */
  public String getJumpUri() {
    if(jump_uri==null){
        return DEFAULT_JUMP_URI;
    }
    return jump_uri;
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
   * 图片或者小视频宽度
   */
  public Integer getWidth() {
    if(width==null){
        return DEFAULT_WIDTH;
    }
    return width;
  }

  /**
   * 图片或者小视频高度
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
   * 点赞数
   */
  public Long getLikeCount() {
    if(like_count==null){
        return DEFAULT_LIKE_COUNT;
    }
    return like_count;
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
   * 封面运营角标[同模板协议RichText]
   */
  public RichText getMark() {
    if(mark==null){
        return new RichText.Builder().build();
    }
    return mark;
  }

  /**
   * 用户是否已经赞过
   */
  public Boolean getIsLiked() {
    if(is_liked==null){
        return DEFAULT_IS_LIKED;
    }
    return is_liked;
  }

  /**
   * 用户与小视频发布地点的距离，单位：米
   */
  public Integer getDistance() {
    if(distance==null){
        return DEFAULT_DISTANCE;
    }
    return distance;
  }

  /**
   * 小视频发布的城市[无定位时显示]
   */
  public String getCity() {
    if(city==null){
        return DEFAULT_CITY;
    }
    return city;
  }

  /**
   * 视频对应的feedId
   */
  public boolean hasFeedId() {
    return feed_id!=null;
  }

  /**
   * 跳转scheme
   */
  public boolean hasJumpUri() {
    return jump_uri!=null;
  }

  /**
   * 视频封面
   */
  public boolean hasCoverUrl() {
    return cover_url!=null;
  }

  /**
   * 图片或者小视频宽度
   */
  public boolean hasWidth() {
    return width!=null;
  }

  /**
   * 图片或者小视频高度
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
   * 点赞数
   */
  public boolean hasLikeCount() {
    return like_count!=null;
  }

  /**
   * 用户资料[同模板协议UserInfo]
   */
  public boolean hasUser() {
    return user!=null;
  }

  /**
   * 封面运营角标[同模板协议RichText]
   */
  public boolean hasMark() {
    return mark!=null;
  }

  /**
   * 用户是否已经赞过
   */
  public boolean hasIsLiked() {
    return is_liked!=null;
  }

  /**
   * 用户与小视频发布地点的距离，单位：米
   */
  public boolean hasDistance() {
    return distance!=null;
  }

  /**
   * 小视频发布的城市[无定位时显示]
   */
  public boolean hasCity() {
    return city!=null;
  }

  public static final class Builder extends Message.Builder<VideoData, Builder> {
    public String feed_id;

    public String jump_uri;

    public String cover_url;

    public Integer width;

    public Integer height;

    public String title;

    public Long like_count;

    public UserInfo user;

    public RichText mark;

    public Boolean is_liked;

    public Integer distance;

    public String city;

    public Builder() {
    }

    /**
     * 视频对应的feedId
     */
    public Builder setFeedId(String feed_id) {
      this.feed_id = feed_id;
      return this;
    }

    /**
     * 跳转scheme
     */
    public Builder setJumpUri(String jump_uri) {
      this.jump_uri = jump_uri;
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
     * 图片或者小视频宽度
     */
    public Builder setWidth(Integer width) {
      this.width = width;
      return this;
    }

    /**
     * 图片或者小视频高度
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
     * 点赞数
     */
    public Builder setLikeCount(Long like_count) {
      this.like_count = like_count;
      return this;
    }

    /**
     * 用户资料[同模板协议UserInfo]
     */
    public Builder setUser(UserInfo user) {
      this.user = user;
      return this;
    }

    /**
     * 封面运营角标[同模板协议RichText]
     */
    public Builder setMark(RichText mark) {
      this.mark = mark;
      return this;
    }

    /**
     * 用户是否已经赞过
     */
    public Builder setIsLiked(Boolean is_liked) {
      this.is_liked = is_liked;
      return this;
    }

    /**
     * 用户与小视频发布地点的距离，单位：米
     */
    public Builder setDistance(Integer distance) {
      this.distance = distance;
      return this;
    }

    /**
     * 小视频发布的城市[无定位时显示]
     */
    public Builder setCity(String city) {
      this.city = city;
      return this;
    }

    @Override
    public VideoData build() {
      return new VideoData(feed_id, jump_uri, cover_url, width, height, title, like_count, user, mark, is_liked, distance, city, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_VideoData extends ProtoAdapter<VideoData> {
    public ProtoAdapter_VideoData() {
      super(FieldEncoding.LENGTH_DELIMITED, VideoData.class);
    }

    @Override
    public int encodedSize(VideoData value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.feed_id)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.jump_uri)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.cover_url)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.width)
          + ProtoAdapter.UINT32.encodedSizeWithTag(5, value.height)
          + ProtoAdapter.STRING.encodedSizeWithTag(6, value.title)
          + ProtoAdapter.UINT64.encodedSizeWithTag(7, value.like_count)
          + UserInfo.ADAPTER.encodedSizeWithTag(8, value.user)
          + RichText.ADAPTER.encodedSizeWithTag(9, value.mark)
          + ProtoAdapter.BOOL.encodedSizeWithTag(10, value.is_liked)
          + ProtoAdapter.UINT32.encodedSizeWithTag(11, value.distance)
          + ProtoAdapter.STRING.encodedSizeWithTag(12, value.city)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, VideoData value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.feed_id);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.jump_uri);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.cover_url);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.width);
      ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.height);
      ProtoAdapter.STRING.encodeWithTag(writer, 6, value.title);
      ProtoAdapter.UINT64.encodeWithTag(writer, 7, value.like_count);
      UserInfo.ADAPTER.encodeWithTag(writer, 8, value.user);
      RichText.ADAPTER.encodeWithTag(writer, 9, value.mark);
      ProtoAdapter.BOOL.encodeWithTag(writer, 10, value.is_liked);
      ProtoAdapter.UINT32.encodeWithTag(writer, 11, value.distance);
      ProtoAdapter.STRING.encodeWithTag(writer, 12, value.city);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public VideoData decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setFeedId(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setJumpUri(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setCoverUrl(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setWidth(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setHeight(ProtoAdapter.UINT32.decode(reader)); break;
          case 6: builder.setTitle(ProtoAdapter.STRING.decode(reader)); break;
          case 7: builder.setLikeCount(ProtoAdapter.UINT64.decode(reader)); break;
          case 8: builder.setUser(UserInfo.ADAPTER.decode(reader)); break;
          case 9: builder.setMark(RichText.ADAPTER.decode(reader)); break;
          case 10: builder.setIsLiked(ProtoAdapter.BOOL.decode(reader)); break;
          case 11: builder.setDistance(ProtoAdapter.UINT32.decode(reader)); break;
          case 12: builder.setCity(ProtoAdapter.STRING.decode(reader)); break;
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
    public VideoData redact(VideoData value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = UserInfo.ADAPTER.redact(builder.user);
      if (builder.mark != null) builder.mark = RichText.ADAPTER.redact(builder.mark);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
