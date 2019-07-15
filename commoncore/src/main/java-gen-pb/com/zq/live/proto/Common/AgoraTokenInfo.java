// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Common.proto
package com.zq.live.proto.Common;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;

import java.io.IOException;

import okio.ByteString;

public final class AgoraTokenInfo extends Message<AgoraTokenInfo, AgoraTokenInfo.Builder> {
  public static final ProtoAdapter<AgoraTokenInfo> ADAPTER = new ProtoAdapter_AgoraTokenInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final String DEFAULT_TOKEN = "";

  /**
   * 用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * 声网token
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String token;

  public AgoraTokenInfo(Integer userID, String token) {
    this(userID, token, ByteString.EMPTY);
  }

  public AgoraTokenInfo(Integer userID, String token, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.token = token;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.token = token;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof AgoraTokenInfo)) return false;
    AgoraTokenInfo o = (AgoraTokenInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(token, o.token);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (token != null ? token.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (token != null) builder.append(", token=").append(token);
    return builder.replace(0, 2, "AgoraTokenInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return AgoraTokenInfo.ADAPTER.encode(this);
  }

  public static final AgoraTokenInfo parseFrom(byte[] data) throws IOException {
    AgoraTokenInfo c = null;
       c = AgoraTokenInfo.ADAPTER.decode(data);
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
   * 声网token
   */
  public String getToken() {
    if(token==null){
        return DEFAULT_TOKEN;
    }
    return token;
  }

  /**
   * 用户id
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 声网token
   */
  public boolean hasToken() {
    return token!=null;
  }

  public static final class Builder extends Message.Builder<AgoraTokenInfo, Builder> {
    private Integer userID;

    private String token;

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
     * 声网token
     */
    public Builder setToken(String token) {
      this.token = token;
      return this;
    }

    @Override
    public AgoraTokenInfo build() {
      return new AgoraTokenInfo(userID, token, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_AgoraTokenInfo extends ProtoAdapter<AgoraTokenInfo> {
    public ProtoAdapter_AgoraTokenInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, AgoraTokenInfo.class);
    }

    @Override
    public int encodedSize(AgoraTokenInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.token)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, AgoraTokenInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.token);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public AgoraTokenInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setToken(ProtoAdapter.STRING.decode(reader)); break;
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
    public AgoraTokenInfo redact(AgoraTokenInfo value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
