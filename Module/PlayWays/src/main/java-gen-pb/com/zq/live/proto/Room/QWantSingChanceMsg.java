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
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class QWantSingChanceMsg extends Message<QWantSingChanceMsg, QWantSingChanceMsg.Builder> {
  public static final ProtoAdapter<QWantSingChanceMsg> ADAPTER = new ProtoAdapter_QWantSingChanceMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Integer DEFAULT_ROUNDSEQ = 0;

  public static final EWantSingType DEFAULT_WANTSINGTYPE = EWantSingType.EWST_DEFAULT;

  /**
   * 用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * 轮次顺序
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roundSeq;

  /**
   * 抢唱方式
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.Room.EWantSingType#ADAPTER"
  )
  private final EWantSingType wantSingType;

  public QWantSingChanceMsg(Integer userID, Integer roundSeq, EWantSingType wantSingType) {
    this(userID, roundSeq, wantSingType, ByteString.EMPTY);
  }

  public QWantSingChanceMsg(Integer userID, Integer roundSeq, EWantSingType wantSingType,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.roundSeq = roundSeq;
    this.wantSingType = wantSingType;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.roundSeq = roundSeq;
    builder.wantSingType = wantSingType;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof QWantSingChanceMsg)) return false;
    QWantSingChanceMsg o = (QWantSingChanceMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(roundSeq, o.roundSeq)
        && Internal.equals(wantSingType, o.wantSingType);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (roundSeq != null ? roundSeq.hashCode() : 0);
      result = result * 37 + (wantSingType != null ? wantSingType.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (roundSeq != null) builder.append(", roundSeq=").append(roundSeq);
    if (wantSingType != null) builder.append(", wantSingType=").append(wantSingType);
    return builder.replace(0, 2, "QWantSingChanceMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return QWantSingChanceMsg.ADAPTER.encode(this);
  }

  public static final QWantSingChanceMsg parseFrom(byte[] data) throws IOException {
    QWantSingChanceMsg c = null;
       c = QWantSingChanceMsg.ADAPTER.decode(data);
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
   * 轮次顺序
   */
  public Integer getRoundSeq() {
    if(roundSeq==null){
        return DEFAULT_ROUNDSEQ;
    }
    return roundSeq;
  }

  /**
   * 抢唱方式
   */
  public EWantSingType getWantSingType() {
    if(wantSingType==null){
        return new EWantSingType.Builder().build();
    }
    return wantSingType;
  }

  /**
   * 用户id
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 轮次顺序
   */
  public boolean hasRoundSeq() {
    return roundSeq!=null;
  }

  /**
   * 抢唱方式
   */
  public boolean hasWantSingType() {
    return wantSingType!=null;
  }

  public static final class Builder extends Message.Builder<QWantSingChanceMsg, Builder> {
    private Integer userID;

    private Integer roundSeq;

    private EWantSingType wantSingType;

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
     * 轮次顺序
     */
    public Builder setRoundSeq(Integer roundSeq) {
      this.roundSeq = roundSeq;
      return this;
    }

    /**
     * 抢唱方式
     */
    public Builder setWantSingType(EWantSingType wantSingType) {
      this.wantSingType = wantSingType;
      return this;
    }

    @Override
    public QWantSingChanceMsg build() {
      return new QWantSingChanceMsg(userID, roundSeq, wantSingType, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_QWantSingChanceMsg extends ProtoAdapter<QWantSingChanceMsg> {
    public ProtoAdapter_QWantSingChanceMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, QWantSingChanceMsg.class);
    }

    @Override
    public int encodedSize(QWantSingChanceMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.roundSeq)
          + EWantSingType.ADAPTER.encodedSizeWithTag(3, value.wantSingType)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, QWantSingChanceMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.roundSeq);
      EWantSingType.ADAPTER.encodeWithTag(writer, 3, value.wantSingType);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public QWantSingChanceMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setRoundSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: {
            try {
              builder.setWantSingType(EWantSingType.ADAPTER.decode(reader));
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
    public QWantSingChanceMsg redact(QWantSingChanceMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
