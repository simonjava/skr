// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: party_room.proto
package com.zq.live.proto.PartyRoom;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.zq.live.proto.Common.POnlineInfo;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PKTVStopMsg extends Message<PKTVStopMsg, PKTVStopMsg.Builder> {
  public static final ProtoAdapter<PKTVStopMsg> ADAPTER = new ProtoAdapter_PKTVStopMsg();

  private static final long serialVersionUID = 0L;

  public static final EOpKTvMusicType DEFAULT_OPTYPE = EOpKTvMusicType.EOMT_UNKNOWN;

  /**
   * 执行者
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Common.POnlineInfo#ADAPTER"
  )
  private final POnlineInfo opUser;

  /**
   * 执行暂停/开始
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.PartyRoom.EOpKTvMusicType#ADAPTER"
  )
  private final EOpKTvMusicType opType;

  public PKTVStopMsg(POnlineInfo opUser, EOpKTvMusicType opType) {
    this(opUser, opType, ByteString.EMPTY);
  }

  public PKTVStopMsg(POnlineInfo opUser, EOpKTvMusicType opType, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.opUser = opUser;
    this.opType = opType;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.opUser = opUser;
    builder.opType = opType;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PKTVStopMsg)) return false;
    PKTVStopMsg o = (PKTVStopMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(opUser, o.opUser)
        && Internal.equals(opType, o.opType);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (opUser != null ? opUser.hashCode() : 0);
      result = result * 37 + (opType != null ? opType.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (opUser != null) builder.append(", opUser=").append(opUser);
    if (opType != null) builder.append(", opType=").append(opType);
    return builder.replace(0, 2, "PKTVStopMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PKTVStopMsg.ADAPTER.encode(this);
  }

  public static final PKTVStopMsg parseFrom(byte[] data) throws IOException {
    PKTVStopMsg c = null;
       c = PKTVStopMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 执行者
   */
  public POnlineInfo getOpUser() {
    if(opUser==null){
        return new POnlineInfo.Builder().build();
    }
    return opUser;
  }

  /**
   * 执行暂停/开始
   */
  public EOpKTvMusicType getOpType() {
    if(opType==null){
        return new EOpKTvMusicType.Builder().build();
    }
    return opType;
  }

  /**
   * 执行者
   */
  public boolean hasOpUser() {
    return opUser!=null;
  }

  /**
   * 执行暂停/开始
   */
  public boolean hasOpType() {
    return opType!=null;
  }

  public static final class Builder extends Message.Builder<PKTVStopMsg, Builder> {
    private POnlineInfo opUser;

    private EOpKTvMusicType opType;

    public Builder() {
    }

    /**
     * 执行者
     */
    public Builder setOpUser(POnlineInfo opUser) {
      this.opUser = opUser;
      return this;
    }

    /**
     * 执行暂停/开始
     */
    public Builder setOpType(EOpKTvMusicType opType) {
      this.opType = opType;
      return this;
    }

    @Override
    public PKTVStopMsg build() {
      return new PKTVStopMsg(opUser, opType, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PKTVStopMsg extends ProtoAdapter<PKTVStopMsg> {
    public ProtoAdapter_PKTVStopMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PKTVStopMsg.class);
    }

    @Override
    public int encodedSize(PKTVStopMsg value) {
      return POnlineInfo.ADAPTER.encodedSizeWithTag(1, value.opUser)
          + EOpKTvMusicType.ADAPTER.encodedSizeWithTag(2, value.opType)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PKTVStopMsg value) throws IOException {
      POnlineInfo.ADAPTER.encodeWithTag(writer, 1, value.opUser);
      EOpKTvMusicType.ADAPTER.encodeWithTag(writer, 2, value.opType);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PKTVStopMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setOpUser(POnlineInfo.ADAPTER.decode(reader)); break;
          case 2: {
            try {
              builder.setOpType(EOpKTvMusicType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
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
    public PKTVStopMsg redact(PKTVStopMsg value) {
      Builder builder = value.newBuilder();
      if (builder.opUser != null) builder.opUser = POnlineInfo.ADAPTER.redact(builder.opUser);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
