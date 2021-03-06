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
import com.zq.live.proto.Common.EMsgRoomMediaType;
import com.zq.live.proto.Common.UserInfo;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class InviteStandMsg extends Message<InviteStandMsg, InviteStandMsg.Builder> {
  public static final ProtoAdapter<InviteStandMsg> ADAPTER = new ProtoAdapter_InviteStandMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_ROOMID = 0;

  public static final EMsgRoomMediaType DEFAULT_MEDIATYPE = EMsgRoomMediaType.EMR_UNKNOWN;

  public static final Integer DEFAULT_TAGID = 0;

  /**
   * 发起邀请的用户详情
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Common.UserInfo#ADAPTER"
  )
  private final UserInfo user;

  /**
   * 房间id
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roomID;

  /**
   * 房间类型(1:音频房 2:视频房)
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.Common.EMsgRoomMediaType#ADAPTER"
  )
  private final EMsgRoomMediaType mediaType;

  /**
   * 房间专场id
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer tagID;

  public InviteStandMsg(UserInfo user, Integer roomID, EMsgRoomMediaType mediaType, Integer tagID) {
    this(user, roomID, mediaType, tagID, ByteString.EMPTY);
  }

  public InviteStandMsg(UserInfo user, Integer roomID, EMsgRoomMediaType mediaType, Integer tagID,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.user = user;
    this.roomID = roomID;
    this.mediaType = mediaType;
    this.tagID = tagID;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.user = user;
    builder.roomID = roomID;
    builder.mediaType = mediaType;
    builder.tagID = tagID;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof InviteStandMsg)) return false;
    InviteStandMsg o = (InviteStandMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(user, o.user)
        && Internal.equals(roomID, o.roomID)
        && Internal.equals(mediaType, o.mediaType)
        && Internal.equals(tagID, o.tagID);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (user != null ? user.hashCode() : 0);
      result = result * 37 + (roomID != null ? roomID.hashCode() : 0);
      result = result * 37 + (mediaType != null ? mediaType.hashCode() : 0);
      result = result * 37 + (tagID != null ? tagID.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (user != null) builder.append(", user=").append(user);
    if (roomID != null) builder.append(", roomID=").append(roomID);
    if (mediaType != null) builder.append(", mediaType=").append(mediaType);
    if (tagID != null) builder.append(", tagID=").append(tagID);
    return builder.replace(0, 2, "InviteStandMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return InviteStandMsg.ADAPTER.encode(this);
  }

  public static final InviteStandMsg parseFrom(byte[] data) throws IOException {
    InviteStandMsg c = null;
       c = InviteStandMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 发起邀请的用户详情
   */
  public UserInfo getUser() {
    if(user==null){
        return new UserInfo.Builder().build();
    }
    return user;
  }

  /**
   * 房间id
   */
  public Integer getRoomID() {
    if(roomID==null){
        return DEFAULT_ROOMID;
    }
    return roomID;
  }

  /**
   * 房间类型(1:音频房 2:视频房)
   */
  public EMsgRoomMediaType getMediaType() {
    if(mediaType==null){
        return new EMsgRoomMediaType.Builder().build();
    }
    return mediaType;
  }

  /**
   * 房间专场id
   */
  public Integer getTagID() {
    if(tagID==null){
        return DEFAULT_TAGID;
    }
    return tagID;
  }

  /**
   * 发起邀请的用户详情
   */
  public boolean hasUser() {
    return user!=null;
  }

  /**
   * 房间id
   */
  public boolean hasRoomID() {
    return roomID!=null;
  }

  /**
   * 房间类型(1:音频房 2:视频房)
   */
  public boolean hasMediaType() {
    return mediaType!=null;
  }

  /**
   * 房间专场id
   */
  public boolean hasTagID() {
    return tagID!=null;
  }

  public static final class Builder extends Message.Builder<InviteStandMsg, Builder> {
    private UserInfo user;

    private Integer roomID;

    private EMsgRoomMediaType mediaType;

    private Integer tagID;

    public Builder() {
    }

    /**
     * 发起邀请的用户详情
     */
    public Builder setUser(UserInfo user) {
      this.user = user;
      return this;
    }

    /**
     * 房间id
     */
    public Builder setRoomID(Integer roomID) {
      this.roomID = roomID;
      return this;
    }

    /**
     * 房间类型(1:音频房 2:视频房)
     */
    public Builder setMediaType(EMsgRoomMediaType mediaType) {
      this.mediaType = mediaType;
      return this;
    }

    /**
     * 房间专场id
     */
    public Builder setTagID(Integer tagID) {
      this.tagID = tagID;
      return this;
    }

    @Override
    public InviteStandMsg build() {
      return new InviteStandMsg(user, roomID, mediaType, tagID, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_InviteStandMsg extends ProtoAdapter<InviteStandMsg> {
    public ProtoAdapter_InviteStandMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, InviteStandMsg.class);
    }

    @Override
    public int encodedSize(InviteStandMsg value) {
      return UserInfo.ADAPTER.encodedSizeWithTag(1, value.user)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.roomID)
          + EMsgRoomMediaType.ADAPTER.encodedSizeWithTag(3, value.mediaType)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.tagID)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, InviteStandMsg value) throws IOException {
      UserInfo.ADAPTER.encodeWithTag(writer, 1, value.user);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.roomID);
      EMsgRoomMediaType.ADAPTER.encodeWithTag(writer, 3, value.mediaType);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.tagID);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public InviteStandMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUser(UserInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: {
            try {
              builder.setMediaType(EMsgRoomMediaType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 4: builder.setTagID(ProtoAdapter.UINT32.decode(reader)); break;
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
    public InviteStandMsg redact(InviteStandMsg value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = UserInfo.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
