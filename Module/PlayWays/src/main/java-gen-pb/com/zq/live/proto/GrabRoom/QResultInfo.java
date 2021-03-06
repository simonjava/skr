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
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class QResultInfo extends Message<QResultInfo, QResultInfo.Builder> {
  public static final ProtoAdapter<QResultInfo> ADAPTER = new ProtoAdapter_QResultInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Integer DEFAULT_WANTSINGCHANCECNT = 0;

  public static final Integer DEFAULT_GETSINGCHANCECNT = 0;

  public static final Integer DEFAULT_WHOLETIMESINGCNT = 0;

  public static final Float DEFAULT_WHOLETIMESINGRATIO = 0.0f;

  public static final Float DEFAULT_BEYONDSKRERRATIO = 0.0f;

  public static final Integer DEFAULT_OTHERBLIGHTCNTTOTAL = 0;

  /**
   * 用户标识
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * 想唱歌数量
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer wantSingChanceCnt;

  /**
   * 演唱机会数量
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer getSingChanceCnt;

  /**
   * 一唱到底数量
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer wholeTimeSingCnt;

  /**
   * 一唱到底成功率
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  private final Float wholeTimeSingRatio;

  /**
   * 超过同段位
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#FLOAT"
  )
  private final Float beyondSkrerRatio;

  /**
   * 其他人给予爆灯的数量
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer otherBlightCntTotal;

  public QResultInfo(Integer userID, Integer wantSingChanceCnt, Integer getSingChanceCnt,
      Integer wholeTimeSingCnt, Float wholeTimeSingRatio, Float beyondSkrerRatio,
      Integer otherBlightCntTotal) {
    this(userID, wantSingChanceCnt, getSingChanceCnt, wholeTimeSingCnt, wholeTimeSingRatio, beyondSkrerRatio, otherBlightCntTotal, ByteString.EMPTY);
  }

  public QResultInfo(Integer userID, Integer wantSingChanceCnt, Integer getSingChanceCnt,
      Integer wholeTimeSingCnt, Float wholeTimeSingRatio, Float beyondSkrerRatio,
      Integer otherBlightCntTotal, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.wantSingChanceCnt = wantSingChanceCnt;
    this.getSingChanceCnt = getSingChanceCnt;
    this.wholeTimeSingCnt = wholeTimeSingCnt;
    this.wholeTimeSingRatio = wholeTimeSingRatio;
    this.beyondSkrerRatio = beyondSkrerRatio;
    this.otherBlightCntTotal = otherBlightCntTotal;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.wantSingChanceCnt = wantSingChanceCnt;
    builder.getSingChanceCnt = getSingChanceCnt;
    builder.wholeTimeSingCnt = wholeTimeSingCnt;
    builder.wholeTimeSingRatio = wholeTimeSingRatio;
    builder.beyondSkrerRatio = beyondSkrerRatio;
    builder.otherBlightCntTotal = otherBlightCntTotal;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof QResultInfo)) return false;
    QResultInfo o = (QResultInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(wantSingChanceCnt, o.wantSingChanceCnt)
        && Internal.equals(getSingChanceCnt, o.getSingChanceCnt)
        && Internal.equals(wholeTimeSingCnt, o.wholeTimeSingCnt)
        && Internal.equals(wholeTimeSingRatio, o.wholeTimeSingRatio)
        && Internal.equals(beyondSkrerRatio, o.beyondSkrerRatio)
        && Internal.equals(otherBlightCntTotal, o.otherBlightCntTotal);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (wantSingChanceCnt != null ? wantSingChanceCnt.hashCode() : 0);
      result = result * 37 + (getSingChanceCnt != null ? getSingChanceCnt.hashCode() : 0);
      result = result * 37 + (wholeTimeSingCnt != null ? wholeTimeSingCnt.hashCode() : 0);
      result = result * 37 + (wholeTimeSingRatio != null ? wholeTimeSingRatio.hashCode() : 0);
      result = result * 37 + (beyondSkrerRatio != null ? beyondSkrerRatio.hashCode() : 0);
      result = result * 37 + (otherBlightCntTotal != null ? otherBlightCntTotal.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (wantSingChanceCnt != null) builder.append(", wantSingChanceCnt=").append(wantSingChanceCnt);
    if (getSingChanceCnt != null) builder.append(", getSingChanceCnt=").append(getSingChanceCnt);
    if (wholeTimeSingCnt != null) builder.append(", wholeTimeSingCnt=").append(wholeTimeSingCnt);
    if (wholeTimeSingRatio != null) builder.append(", wholeTimeSingRatio=").append(wholeTimeSingRatio);
    if (beyondSkrerRatio != null) builder.append(", beyondSkrerRatio=").append(beyondSkrerRatio);
    if (otherBlightCntTotal != null) builder.append(", otherBlightCntTotal=").append(otherBlightCntTotal);
    return builder.replace(0, 2, "QResultInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return QResultInfo.ADAPTER.encode(this);
  }

  public static final QResultInfo parseFrom(byte[] data) throws IOException {
    QResultInfo c = null;
       c = QResultInfo.ADAPTER.decode(data);
    return c;
  }

  /**
   * 用户标识
   */
  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  /**
   * 想唱歌数量
   */
  public Integer getWantSingChanceCnt() {
    if(wantSingChanceCnt==null){
        return DEFAULT_WANTSINGCHANCECNT;
    }
    return wantSingChanceCnt;
  }

  /**
   * 演唱机会数量
   */
  public Integer getGetSingChanceCnt() {
    if(getSingChanceCnt==null){
        return DEFAULT_GETSINGCHANCECNT;
    }
    return getSingChanceCnt;
  }

  /**
   * 一唱到底数量
   */
  public Integer getWholeTimeSingCnt() {
    if(wholeTimeSingCnt==null){
        return DEFAULT_WHOLETIMESINGCNT;
    }
    return wholeTimeSingCnt;
  }

  /**
   * 一唱到底成功率
   */
  public Float getWholeTimeSingRatio() {
    if(wholeTimeSingRatio==null){
        return DEFAULT_WHOLETIMESINGRATIO;
    }
    return wholeTimeSingRatio;
  }

  /**
   * 超过同段位
   */
  public Float getBeyondSkrerRatio() {
    if(beyondSkrerRatio==null){
        return DEFAULT_BEYONDSKRERRATIO;
    }
    return beyondSkrerRatio;
  }

  /**
   * 其他人给予爆灯的数量
   */
  public Integer getOtherBlightCntTotal() {
    if(otherBlightCntTotal==null){
        return DEFAULT_OTHERBLIGHTCNTTOTAL;
    }
    return otherBlightCntTotal;
  }

  /**
   * 用户标识
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 想唱歌数量
   */
  public boolean hasWantSingChanceCnt() {
    return wantSingChanceCnt!=null;
  }

  /**
   * 演唱机会数量
   */
  public boolean hasGetSingChanceCnt() {
    return getSingChanceCnt!=null;
  }

  /**
   * 一唱到底数量
   */
  public boolean hasWholeTimeSingCnt() {
    return wholeTimeSingCnt!=null;
  }

  /**
   * 一唱到底成功率
   */
  public boolean hasWholeTimeSingRatio() {
    return wholeTimeSingRatio!=null;
  }

  /**
   * 超过同段位
   */
  public boolean hasBeyondSkrerRatio() {
    return beyondSkrerRatio!=null;
  }

  /**
   * 其他人给予爆灯的数量
   */
  public boolean hasOtherBlightCntTotal() {
    return otherBlightCntTotal!=null;
  }

  public static final class Builder extends Message.Builder<QResultInfo, Builder> {
    private Integer userID;

    private Integer wantSingChanceCnt;

    private Integer getSingChanceCnt;

    private Integer wholeTimeSingCnt;

    private Float wholeTimeSingRatio;

    private Float beyondSkrerRatio;

    private Integer otherBlightCntTotal;

    public Builder() {
    }

    /**
     * 用户标识
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    /**
     * 想唱歌数量
     */
    public Builder setWantSingChanceCnt(Integer wantSingChanceCnt) {
      this.wantSingChanceCnt = wantSingChanceCnt;
      return this;
    }

    /**
     * 演唱机会数量
     */
    public Builder setGetSingChanceCnt(Integer getSingChanceCnt) {
      this.getSingChanceCnt = getSingChanceCnt;
      return this;
    }

    /**
     * 一唱到底数量
     */
    public Builder setWholeTimeSingCnt(Integer wholeTimeSingCnt) {
      this.wholeTimeSingCnt = wholeTimeSingCnt;
      return this;
    }

    /**
     * 一唱到底成功率
     */
    public Builder setWholeTimeSingRatio(Float wholeTimeSingRatio) {
      this.wholeTimeSingRatio = wholeTimeSingRatio;
      return this;
    }

    /**
     * 超过同段位
     */
    public Builder setBeyondSkrerRatio(Float beyondSkrerRatio) {
      this.beyondSkrerRatio = beyondSkrerRatio;
      return this;
    }

    /**
     * 其他人给予爆灯的数量
     */
    public Builder setOtherBlightCntTotal(Integer otherBlightCntTotal) {
      this.otherBlightCntTotal = otherBlightCntTotal;
      return this;
    }

    @Override
    public QResultInfo build() {
      return new QResultInfo(userID, wantSingChanceCnt, getSingChanceCnt, wholeTimeSingCnt, wholeTimeSingRatio, beyondSkrerRatio, otherBlightCntTotal, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_QResultInfo extends ProtoAdapter<QResultInfo> {
    public ProtoAdapter_QResultInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, QResultInfo.class);
    }

    @Override
    public int encodedSize(QResultInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.wantSingChanceCnt)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.getSingChanceCnt)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.wholeTimeSingCnt)
          + ProtoAdapter.FLOAT.encodedSizeWithTag(5, value.wholeTimeSingRatio)
          + ProtoAdapter.FLOAT.encodedSizeWithTag(6, value.beyondSkrerRatio)
          + ProtoAdapter.UINT32.encodedSizeWithTag(7, value.otherBlightCntTotal)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, QResultInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.wantSingChanceCnt);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.getSingChanceCnt);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.wholeTimeSingCnt);
      ProtoAdapter.FLOAT.encodeWithTag(writer, 5, value.wholeTimeSingRatio);
      ProtoAdapter.FLOAT.encodeWithTag(writer, 6, value.beyondSkrerRatio);
      ProtoAdapter.UINT32.encodeWithTag(writer, 7, value.otherBlightCntTotal);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public QResultInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setWantSingChanceCnt(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setGetSingChanceCnt(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setWholeTimeSingCnt(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setWholeTimeSingRatio(ProtoAdapter.FLOAT.decode(reader)); break;
          case 6: builder.setBeyondSkrerRatio(ProtoAdapter.FLOAT.decode(reader)); break;
          case 7: builder.setOtherBlightCntTotal(ProtoAdapter.UINT32.decode(reader)); break;
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
    public QResultInfo redact(QResultInfo value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
