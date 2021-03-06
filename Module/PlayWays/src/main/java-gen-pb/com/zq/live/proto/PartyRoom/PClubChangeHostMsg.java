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

public final class PClubChangeHostMsg extends Message<PClubChangeHostMsg, PClubChangeHostMsg.Builder> {
  public static final ProtoAdapter<PClubChangeHostMsg> ADAPTER = new ProtoAdapter_PClubChangeHostMsg();

  private static final long serialVersionUID = 0L;

  public static final EClubChangeHostType DEFAULT_CHANGETYPE = EClubChangeHostType.ECCHT_UNKNOWN;

  /**
   * 主持身份移交人
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Common.POnlineInfo#ADAPTER"
  )
  private final POnlineInfo fromUser;

  /**
   * 主持身份接收人
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Common.POnlineInfo#ADAPTER"
  )
  private final POnlineInfo toUser;

  /**
   * 更改方式
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.PartyRoom.EClubChangeHostType#ADAPTER"
  )
  private final EClubChangeHostType changeType;

  public PClubChangeHostMsg(POnlineInfo fromUser, POnlineInfo toUser,
      EClubChangeHostType changeType) {
    this(fromUser, toUser, changeType, ByteString.EMPTY);
  }

  public PClubChangeHostMsg(POnlineInfo fromUser, POnlineInfo toUser,
      EClubChangeHostType changeType, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.fromUser = fromUser;
    this.toUser = toUser;
    this.changeType = changeType;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.fromUser = fromUser;
    builder.toUser = toUser;
    builder.changeType = changeType;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PClubChangeHostMsg)) return false;
    PClubChangeHostMsg o = (PClubChangeHostMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(fromUser, o.fromUser)
        && Internal.equals(toUser, o.toUser)
        && Internal.equals(changeType, o.changeType);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (fromUser != null ? fromUser.hashCode() : 0);
      result = result * 37 + (toUser != null ? toUser.hashCode() : 0);
      result = result * 37 + (changeType != null ? changeType.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (fromUser != null) builder.append(", fromUser=").append(fromUser);
    if (toUser != null) builder.append(", toUser=").append(toUser);
    if (changeType != null) builder.append(", changeType=").append(changeType);
    return builder.replace(0, 2, "PClubChangeHostMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PClubChangeHostMsg.ADAPTER.encode(this);
  }

  public static final PClubChangeHostMsg parseFrom(byte[] data) throws IOException {
    PClubChangeHostMsg c = null;
       c = PClubChangeHostMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 主持身份移交人
   */
  public POnlineInfo getFromUser() {
    if(fromUser==null){
        return new POnlineInfo.Builder().build();
    }
    return fromUser;
  }

  /**
   * 主持身份接收人
   */
  public POnlineInfo getToUser() {
    if(toUser==null){
        return new POnlineInfo.Builder().build();
    }
    return toUser;
  }

  /**
   * 更改方式
   */
  public EClubChangeHostType getChangeType() {
    if(changeType==null){
        return new EClubChangeHostType.Builder().build();
    }
    return changeType;
  }

  /**
   * 主持身份移交人
   */
  public boolean hasFromUser() {
    return fromUser!=null;
  }

  /**
   * 主持身份接收人
   */
  public boolean hasToUser() {
    return toUser!=null;
  }

  /**
   * 更改方式
   */
  public boolean hasChangeType() {
    return changeType!=null;
  }

  public static final class Builder extends Message.Builder<PClubChangeHostMsg, Builder> {
    private POnlineInfo fromUser;

    private POnlineInfo toUser;

    private EClubChangeHostType changeType;

    public Builder() {
    }

    /**
     * 主持身份移交人
     */
    public Builder setFromUser(POnlineInfo fromUser) {
      this.fromUser = fromUser;
      return this;
    }

    /**
     * 主持身份接收人
     */
    public Builder setToUser(POnlineInfo toUser) {
      this.toUser = toUser;
      return this;
    }

    /**
     * 更改方式
     */
    public Builder setChangeType(EClubChangeHostType changeType) {
      this.changeType = changeType;
      return this;
    }

    @Override
    public PClubChangeHostMsg build() {
      return new PClubChangeHostMsg(fromUser, toUser, changeType, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PClubChangeHostMsg extends ProtoAdapter<PClubChangeHostMsg> {
    public ProtoAdapter_PClubChangeHostMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PClubChangeHostMsg.class);
    }

    @Override
    public int encodedSize(PClubChangeHostMsg value) {
      return POnlineInfo.ADAPTER.encodedSizeWithTag(1, value.fromUser)
          + POnlineInfo.ADAPTER.encodedSizeWithTag(2, value.toUser)
          + EClubChangeHostType.ADAPTER.encodedSizeWithTag(3, value.changeType)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PClubChangeHostMsg value) throws IOException {
      POnlineInfo.ADAPTER.encodeWithTag(writer, 1, value.fromUser);
      POnlineInfo.ADAPTER.encodeWithTag(writer, 2, value.toUser);
      EClubChangeHostType.ADAPTER.encodeWithTag(writer, 3, value.changeType);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PClubChangeHostMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setFromUser(POnlineInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setToUser(POnlineInfo.ADAPTER.decode(reader)); break;
          case 3: {
            try {
              builder.setChangeType(EClubChangeHostType.ADAPTER.decode(reader));
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
    public PClubChangeHostMsg redact(PClubChangeHostMsg value) {
      Builder builder = value.newBuilder();
      if (builder.fromUser != null) builder.fromUser = POnlineInfo.ADAPTER.redact(builder.fromUser);
      if (builder.toUser != null) builder.toUser = POnlineInfo.ADAPTER.redact(builder.toUser);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
