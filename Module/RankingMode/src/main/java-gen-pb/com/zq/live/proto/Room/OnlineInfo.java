// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.zq.live.proto.Room;

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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class OnlineInfo extends Message<OnlineInfo, OnlineInfo.Builder> {
  public static final ProtoAdapter<OnlineInfo> ADAPTER = new ProtoAdapter_OnlineInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Boolean DEFAULT_ISONLINE = false;

  /**
   * 用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer userID;

  /**
   * 是否在线
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean isOnline;

  public OnlineInfo(Integer userID, Boolean isOnline) {
    this(userID, isOnline, ByteString.EMPTY);
  }

  public OnlineInfo(Integer userID, Boolean isOnline, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.isOnline = isOnline;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.isOnline = isOnline;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof OnlineInfo)) return false;
    OnlineInfo o = (OnlineInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(isOnline, o.isOnline);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (isOnline != null ? isOnline.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (isOnline != null) builder.append(", isOnline=").append(isOnline);
    return builder.replace(0, 2, "OnlineInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return OnlineInfo.ADAPTER.encode(this);
  }

  public static final OnlineInfo parseFrom(byte[] data) throws IOException {
    OnlineInfo c = null;
       c = OnlineInfo.ADAPTER.decode(data);
    return c;
  }

  /**
   * 用户id
   */
  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  /**
   * 是否在线
   */
  public Boolean getIsOnline() {
    if(isOnline==null){
        return DEFAULT_ISONLINE;
    }
    return isOnline;
  }

  /**
   * 用户id
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 是否在线
   */
  public boolean hasIsOnline() {
    return isOnline!=null;
  }

  public static final class Builder extends Message.Builder<OnlineInfo, Builder> {
    public Integer userID;

    public Boolean isOnline;

    public Builder() {
    }

    /**
     * 用户id
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    /**
     * 是否在线
     */
    public Builder setIsOnline(Boolean isOnline) {
      this.isOnline = isOnline;
      return this;
    }

    @Override
    public OnlineInfo build() {
      return new OnlineInfo(userID, isOnline, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_OnlineInfo extends ProtoAdapter<OnlineInfo> {
    public ProtoAdapter_OnlineInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, OnlineInfo.class);
    }

    @Override
    public int encodedSize(OnlineInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.BOOL.encodedSizeWithTag(2, value.isOnline)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, OnlineInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.isOnline);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public OnlineInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setIsOnline(ProtoAdapter.BOOL.decode(reader)); break;
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
    public OnlineInfo redact(OnlineInfo value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
