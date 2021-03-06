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
import com.zq.live.proto.Common.BLightShowInfo;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class QBLightMsg extends Message<QBLightMsg, QBLightMsg.Builder> {
  public static final ProtoAdapter<QBLightMsg> ADAPTER = new ProtoAdapter_QBLightMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Integer DEFAULT_ROUNDSEQ = 0;

  public static final Integer DEFAULT_BLIGHTCNT = 0;

  /**
   * 玩家id
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
   * 当前爆灯总数量
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer bLightCnt;

  @WireField(
      tag = 4,
      adapter = "com.zq.live.proto.Common.BLightShowInfo#ADAPTER"
  )
  private final BLightShowInfo showInfo;

  public QBLightMsg(Integer userID, Integer roundSeq, Integer bLightCnt, BLightShowInfo showInfo) {
    this(userID, roundSeq, bLightCnt, showInfo, ByteString.EMPTY);
  }

  public QBLightMsg(Integer userID, Integer roundSeq, Integer bLightCnt, BLightShowInfo showInfo,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.roundSeq = roundSeq;
    this.bLightCnt = bLightCnt;
    this.showInfo = showInfo;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.roundSeq = roundSeq;
    builder.bLightCnt = bLightCnt;
    builder.showInfo = showInfo;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof QBLightMsg)) return false;
    QBLightMsg o = (QBLightMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(roundSeq, o.roundSeq)
        && Internal.equals(bLightCnt, o.bLightCnt)
        && Internal.equals(showInfo, o.showInfo);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (roundSeq != null ? roundSeq.hashCode() : 0);
      result = result * 37 + (bLightCnt != null ? bLightCnt.hashCode() : 0);
      result = result * 37 + (showInfo != null ? showInfo.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (roundSeq != null) builder.append(", roundSeq=").append(roundSeq);
    if (bLightCnt != null) builder.append(", bLightCnt=").append(bLightCnt);
    if (showInfo != null) builder.append(", showInfo=").append(showInfo);
    return builder.replace(0, 2, "QBLightMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return QBLightMsg.ADAPTER.encode(this);
  }

  public static final QBLightMsg parseFrom(byte[] data) throws IOException {
    QBLightMsg c = null;
       c = QBLightMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 玩家id
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
   * 当前爆灯总数量
   */
  public Integer getBLightCnt() {
    if(bLightCnt==null){
        return DEFAULT_BLIGHTCNT;
    }
    return bLightCnt;
  }

  public BLightShowInfo getShowInfo() {
    if(showInfo==null){
        return new BLightShowInfo.Builder().build();
    }
    return showInfo;
  }

  /**
   * 玩家id
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
   * 当前爆灯总数量
   */
  public boolean hasBLightCnt() {
    return bLightCnt!=null;
  }

  public boolean hasShowInfo() {
    return showInfo!=null;
  }

  public static final class Builder extends Message.Builder<QBLightMsg, Builder> {
    private Integer userID;

    private Integer roundSeq;

    private Integer bLightCnt;

    private BLightShowInfo showInfo;

    public Builder() {
    }

    /**
     * 玩家id
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
     * 当前爆灯总数量
     */
    public Builder setBLightCnt(Integer bLightCnt) {
      this.bLightCnt = bLightCnt;
      return this;
    }

    public Builder setShowInfo(BLightShowInfo showInfo) {
      this.showInfo = showInfo;
      return this;
    }

    @Override
    public QBLightMsg build() {
      return new QBLightMsg(userID, roundSeq, bLightCnt, showInfo, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_QBLightMsg extends ProtoAdapter<QBLightMsg> {
    public ProtoAdapter_QBLightMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, QBLightMsg.class);
    }

    @Override
    public int encodedSize(QBLightMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.roundSeq)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.bLightCnt)
          + BLightShowInfo.ADAPTER.encodedSizeWithTag(4, value.showInfo)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, QBLightMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.roundSeq);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.bLightCnt);
      BLightShowInfo.ADAPTER.encodeWithTag(writer, 4, value.showInfo);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public QBLightMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setRoundSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setBLightCnt(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setShowInfo(BLightShowInfo.ADAPTER.decode(reader)); break;
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
    public QBLightMsg redact(QBLightMsg value) {
      Builder builder = value.newBuilder();
      if (builder.showInfo != null) builder.showInfo = BLightShowInfo.ADAPTER.redact(builder.showInfo);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
