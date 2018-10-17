// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveShow.proto
package com.wali.live.proto.LiveShow;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * pk信息
 */
public final class PKLive extends Message<PKLive, PKLive.Builder> {
  public static final ProtoAdapter<PKLive> ADAPTER = new ProtoAdapter_PKLive();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_LIVEID = "";

  /**
   * pk的直播房间id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String liveId;

  /**
   * pk的主播信息
   */
  @WireField(
      tag = 2,
      adapter = "com.wali.live.proto.LiveShow.UserShow#ADAPTER"
  )
  public final UserShow user;

  public PKLive(String liveId, UserShow user) {
    this(liveId, user, ByteString.EMPTY);
  }

  public PKLive(String liveId, UserShow user, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.liveId = liveId;
    this.user = user;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.liveId = liveId;
    builder.user = user;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PKLive)) return false;
    PKLive o = (PKLive) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(liveId, o.liveId)
        && Internal.equals(user, o.user);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (liveId != null ? liveId.hashCode() : 0);
      result = result * 37 + (user != null ? user.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (liveId != null) builder.append(", liveId=").append(liveId);
    if (user != null) builder.append(", user=").append(user);
    return builder.replace(0, 2, "PKLive{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PKLive.ADAPTER.encode(this);
  }

  public static final PKLive parseFrom(byte[] data) throws IOException {
    PKLive c = null;
       c = PKLive.ADAPTER.decode(data);
    return c;
  }

  /**
   * pk的直播房间id
   */
  public String getLiveId() {
    if(liveId==null){
        return DEFAULT_LIVEID;
    }
    return liveId;
  }

  /**
   * pk的主播信息
   */
  public UserShow getUser() {
    if(user==null){
        return new UserShow.Builder().build();
    }
    return user;
  }

  /**
   * pk的直播房间id
   */
  public boolean hasLiveId() {
    return liveId!=null;
  }

  /**
   * pk的主播信息
   */
  public boolean hasUser() {
    return user!=null;
  }

  public static final class Builder extends Message.Builder<PKLive, Builder> {
    public String liveId;

    public UserShow user;

    public Builder() {
    }

    /**
     * pk的直播房间id
     */
    public Builder setLiveId(String liveId) {
      this.liveId = liveId;
      return this;
    }

    /**
     * pk的主播信息
     */
    public Builder setUser(UserShow user) {
      this.user = user;
      return this;
    }

    @Override
    public PKLive build() {
      return new PKLive(liveId, user, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PKLive extends ProtoAdapter<PKLive> {
    public ProtoAdapter_PKLive() {
      super(FieldEncoding.LENGTH_DELIMITED, PKLive.class);
    }

    @Override
    public int encodedSize(PKLive value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.liveId)
          + UserShow.ADAPTER.encodedSizeWithTag(2, value.user)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PKLive value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.liveId);
      UserShow.ADAPTER.encodeWithTag(writer, 2, value.user);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PKLive decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setLiveId(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setUser(UserShow.ADAPTER.decode(reader)); break;
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
    public PKLive redact(PKLive value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = UserShow.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
