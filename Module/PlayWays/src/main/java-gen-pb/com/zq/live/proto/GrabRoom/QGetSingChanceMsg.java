// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: grab_room.proto
package com.zq.live.proto.GrabRoom;

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

public final class QGetSingChanceMsg extends Message<QGetSingChanceMsg, QGetSingChanceMsg.Builder> {
  public static final ProtoAdapter<QGetSingChanceMsg> ADAPTER = new ProtoAdapter_QGetSingChanceMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Integer DEFAULT_ROUNDSEQ = 0;

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
   * 当前轮次信息
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.GrabRoom.QRoundInfo#ADAPTER"
  )
  private final QRoundInfo currentRound;

  public QGetSingChanceMsg(Integer userID, Integer roundSeq, QRoundInfo currentRound) {
    this(userID, roundSeq, currentRound, ByteString.EMPTY);
  }

  public QGetSingChanceMsg(Integer userID, Integer roundSeq, QRoundInfo currentRound,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.roundSeq = roundSeq;
    this.currentRound = currentRound;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.roundSeq = roundSeq;
    builder.currentRound = currentRound;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof QGetSingChanceMsg)) return false;
    QGetSingChanceMsg o = (QGetSingChanceMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(roundSeq, o.roundSeq)
        && Internal.equals(currentRound, o.currentRound);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (roundSeq != null ? roundSeq.hashCode() : 0);
      result = result * 37 + (currentRound != null ? currentRound.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (roundSeq != null) builder.append(", roundSeq=").append(roundSeq);
    if (currentRound != null) builder.append(", currentRound=").append(currentRound);
    return builder.replace(0, 2, "QGetSingChanceMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return QGetSingChanceMsg.ADAPTER.encode(this);
  }

  public static final QGetSingChanceMsg parseFrom(byte[] data) throws IOException {
    QGetSingChanceMsg c = null;
       c = QGetSingChanceMsg.ADAPTER.decode(data);
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
   * 当前轮次信息
   */
  public QRoundInfo getCurrentRound() {
    if(currentRound==null){
        return new QRoundInfo.Builder().build();
    }
    return currentRound;
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
   * 当前轮次信息
   */
  public boolean hasCurrentRound() {
    return currentRound!=null;
  }

  public static final class Builder extends Message.Builder<QGetSingChanceMsg, Builder> {
    private Integer userID;

    private Integer roundSeq;

    private QRoundInfo currentRound;

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
     * 当前轮次信息
     */
    public Builder setCurrentRound(QRoundInfo currentRound) {
      this.currentRound = currentRound;
      return this;
    }

    @Override
    public QGetSingChanceMsg build() {
      return new QGetSingChanceMsg(userID, roundSeq, currentRound, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_QGetSingChanceMsg extends ProtoAdapter<QGetSingChanceMsg> {
    public ProtoAdapter_QGetSingChanceMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, QGetSingChanceMsg.class);
    }

    @Override
    public int encodedSize(QGetSingChanceMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.roundSeq)
          + QRoundInfo.ADAPTER.encodedSizeWithTag(3, value.currentRound)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, QGetSingChanceMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.roundSeq);
      QRoundInfo.ADAPTER.encodeWithTag(writer, 3, value.currentRound);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public QGetSingChanceMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setRoundSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setCurrentRound(QRoundInfo.ADAPTER.decode(reader)); break;
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
    public QGetSingChanceMsg redact(QGetSingChanceMsg value) {
      Builder builder = value.newBuilder();
      if (builder.currentRound != null) builder.currentRound = QRoundInfo.ADAPTER.redact(builder.currentRound);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
