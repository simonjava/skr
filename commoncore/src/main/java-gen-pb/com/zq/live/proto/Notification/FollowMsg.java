// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: notification.proto
package com.zq.live.proto.Notification;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.zq.live.proto.Common.UserInfo;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class FollowMsg extends Message<FollowMsg, FollowMsg.Builder> {
  public static final ProtoAdapter<FollowMsg> ADAPTER = new ProtoAdapter_FollowMsg();

  private static final long serialVersionUID = 0L;

  public static final Boolean DEFAULT_ISFRIEND = false;

  public static final Boolean DEFAULT_ISFOLLOW = false;

  /**
   * 发起关注的用户详情
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Common.UserInfo#ADAPTER"
  )
  private final UserInfo user;

  /**
   * 是否好友
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  private final Boolean isFriend;

  /**
   * 是否关注
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  private final Boolean isFollow;

  public FollowMsg(UserInfo user, Boolean isFriend, Boolean isFollow) {
    this(user, isFriend, isFollow, ByteString.EMPTY);
  }

  public FollowMsg(UserInfo user, Boolean isFriend, Boolean isFollow, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.user = user;
    this.isFriend = isFriend;
    this.isFollow = isFollow;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.user = user;
    builder.isFriend = isFriend;
    builder.isFollow = isFollow;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof FollowMsg)) return false;
    FollowMsg o = (FollowMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(user, o.user)
        && Internal.equals(isFriend, o.isFriend)
        && Internal.equals(isFollow, o.isFollow);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (isFriend != null ? isFriend.hashCode() : 0);
      result = result * 37 + (isFollow != null ? isFollow.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (user != null) builder.append(", user=").append(user);
    if (isFriend != null) builder.append(", isFriend=").append(isFriend);
    if (isFollow != null) builder.append(", isFollow=").append(isFollow);
    return builder.replace(0, 2, "FollowMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return FollowMsg.ADAPTER.encode(this);
  }

  public static final FollowMsg parseFrom(byte[] data) throws IOException {
    FollowMsg c = null;
       c = FollowMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 发起关注的用户详情
   */
  public UserInfo getUser() {
    if(user==null){
        return new UserInfo.Builder().build();
    }
    return user;
  }

  /**
   * 是否好友
   */
  public Boolean getIsFriend() {
    if(isFriend==null){
        return DEFAULT_ISFRIEND;
    }
    return isFriend;
  }

  /**
   * 是否关注
   */
  public Boolean getIsFollow() {
    if(isFollow==null){
        return DEFAULT_ISFOLLOW;
    }
    return isFollow;
  }

  /**
   * 发起关注的用户详情
   */
  public boolean hasUser() {
    return user!=null;
  }

  /**
   * 是否好友
   */
  public boolean hasIsFriend() {
    return isFriend!=null;
  }

  /**
   * 是否关注
   */
  public boolean hasIsFollow() {
    return isFollow!=null;
  }

  public static final class Builder extends Message.Builder<FollowMsg, Builder> {
    private UserInfo user;

    private Boolean isFriend;

    private Boolean isFollow;

    public Builder() {
    }

    /**
     * 发起关注的用户详情
     */
    public Builder setUser(UserInfo user) {
      this.user = user;
      return this;
    }

    /**
     * 是否好友
     */
    public Builder setIsFriend(Boolean isFriend) {
      this.isFriend = isFriend;
      return this;
    }

    /**
     * 是否关注
     */
    public Builder setIsFollow(Boolean isFollow) {
      this.isFollow = isFollow;
      return this;
    }

    @Override
    public FollowMsg build() {
      return new FollowMsg(user, isFriend, isFollow, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_FollowMsg extends ProtoAdapter<FollowMsg> {
    public ProtoAdapter_FollowMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, FollowMsg.class);
    }

    @Override
    public int encodedSize(FollowMsg value) {
      return UserInfo.ADAPTER.encodedSizeWithTag(1, value.user)
          + ProtoAdapter.BOOL.encodedSizeWithTag(2, value.isFriend)
          + ProtoAdapter.BOOL.encodedSizeWithTag(3, value.isFollow)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, FollowMsg value) throws IOException {
      UserInfo.ADAPTER.encodeWithTag(writer, 1, value.user);
      ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.isFriend);
      ProtoAdapter.BOOL.encodeWithTag(writer, 3, value.isFollow);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public FollowMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUser(UserInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setIsFriend(ProtoAdapter.BOOL.decode(reader)); break;
          case 3: builder.setIsFollow(ProtoAdapter.BOOL.decode(reader)); break;
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
    public FollowMsg redact(FollowMsg value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = UserInfo.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
