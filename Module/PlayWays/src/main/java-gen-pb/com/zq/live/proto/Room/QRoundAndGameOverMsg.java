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
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

public final class QRoundAndGameOverMsg extends Message<QRoundAndGameOverMsg, QRoundAndGameOverMsg.Builder> {
  public static final ProtoAdapter<QRoundAndGameOverMsg> ADAPTER = new ProtoAdapter_QRoundAndGameOverMsg();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_ROUNDOVERTIMEMS = 0L;

  /**
   * 本轮次结束的毫秒时间戳
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long roundOverTimeMs;

  /**
   * 当前轮次信息
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Room.QRoundInfo#ADAPTER"
  )
  private final QRoundInfo currentRound;

  /**
   * 最终结果信息
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.Room.QResultInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<QResultInfo> resultInfo;

  /**
   * 有金币变化的用户列表
   */
  @WireField(
      tag = 4,
      adapter = "com.zq.live.proto.Room.QUserCoin#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<QUserCoin> qUserCoin;

  public QRoundAndGameOverMsg(Long roundOverTimeMs, QRoundInfo currentRound,
      List<QResultInfo> resultInfo, List<QUserCoin> qUserCoin) {
    this(roundOverTimeMs, currentRound, resultInfo, qUserCoin, ByteString.EMPTY);
  }

  public QRoundAndGameOverMsg(Long roundOverTimeMs, QRoundInfo currentRound,
      List<QResultInfo> resultInfo, List<QUserCoin> qUserCoin, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.roundOverTimeMs = roundOverTimeMs;
    this.currentRound = currentRound;
    this.resultInfo = Internal.immutableCopyOf("resultInfo", resultInfo);
    this.qUserCoin = Internal.immutableCopyOf("qUserCoin", qUserCoin);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.roundOverTimeMs = roundOverTimeMs;
    builder.currentRound = currentRound;
    builder.resultInfo = Internal.copyOf("resultInfo", resultInfo);
    builder.qUserCoin = Internal.copyOf("qUserCoin", qUserCoin);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof QRoundAndGameOverMsg)) return false;
    QRoundAndGameOverMsg o = (QRoundAndGameOverMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(roundOverTimeMs, o.roundOverTimeMs)
        && Internal.equals(currentRound, o.currentRound)
        && resultInfo.equals(o.resultInfo)
        && qUserCoin.equals(o.qUserCoin);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (roundOverTimeMs != null ? roundOverTimeMs.hashCode() : 0);
      result = result * 37 + (currentRound != null ? currentRound.hashCode() : 0);
      result = result * 37 + resultInfo.hashCode();
      result = result * 37 + qUserCoin.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (roundOverTimeMs != null) builder.append(", roundOverTimeMs=").append(roundOverTimeMs);
    if (currentRound != null) builder.append(", currentRound=").append(currentRound);
    if (!resultInfo.isEmpty()) builder.append(", resultInfo=").append(resultInfo);
    if (!qUserCoin.isEmpty()) builder.append(", qUserCoin=").append(qUserCoin);
    return builder.replace(0, 2, "QRoundAndGameOverMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return QRoundAndGameOverMsg.ADAPTER.encode(this);
  }

  public static final QRoundAndGameOverMsg parseFrom(byte[] data) throws IOException {
    QRoundAndGameOverMsg c = null;
       c = QRoundAndGameOverMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 本轮次结束的毫秒时间戳
   */
  public Long getRoundOverTimeMs() {
    if(roundOverTimeMs==null){
        return DEFAULT_ROUNDOVERTIMEMS;
    }
    return roundOverTimeMs;
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
   * 最终结果信息
   */
  public List<QResultInfo> getResultInfoList() {
    if(resultInfo==null){
        return new java.util.ArrayList<QResultInfo>();
    }
    return resultInfo;
  }

  /**
   * 有金币变化的用户列表
   */
  public List<QUserCoin> getQUserCoinList() {
    if(qUserCoin==null){
        return new java.util.ArrayList<QUserCoin>();
    }
    return qUserCoin;
  }

  /**
   * 本轮次结束的毫秒时间戳
   */
  public boolean hasRoundOverTimeMs() {
    return roundOverTimeMs!=null;
  }

  /**
   * 当前轮次信息
   */
  public boolean hasCurrentRound() {
    return currentRound!=null;
  }

  /**
   * 最终结果信息
   */
  public boolean hasResultInfoList() {
    return resultInfo!=null;
  }

  /**
   * 有金币变化的用户列表
   */
  public boolean hasQUserCoinList() {
    return qUserCoin!=null;
  }

  public static final class Builder extends Message.Builder<QRoundAndGameOverMsg, Builder> {
    private Long roundOverTimeMs;

    private QRoundInfo currentRound;

    private List<QResultInfo> resultInfo;

    private List<QUserCoin> qUserCoin;

    public Builder() {
      resultInfo = Internal.newMutableList();
      qUserCoin = Internal.newMutableList();
    }

    /**
     * 本轮次结束的毫秒时间戳
     */
    public Builder setRoundOverTimeMs(Long roundOverTimeMs) {
      this.roundOverTimeMs = roundOverTimeMs;
      return this;
    }

    /**
     * 当前轮次信息
     */
    public Builder setCurrentRound(QRoundInfo currentRound) {
      this.currentRound = currentRound;
      return this;
    }

    /**
     * 最终结果信息
     */
    public Builder addAllResultInfo(List<QResultInfo> resultInfo) {
      Internal.checkElementsNotNull(resultInfo);
      this.resultInfo = resultInfo;
      return this;
    }

    /**
     * 有金币变化的用户列表
     */
    public Builder addAllQUserCoin(List<QUserCoin> qUserCoin) {
      Internal.checkElementsNotNull(qUserCoin);
      this.qUserCoin = qUserCoin;
      return this;
    }

    @Override
    public QRoundAndGameOverMsg build() {
      return new QRoundAndGameOverMsg(roundOverTimeMs, currentRound, resultInfo, qUserCoin, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_QRoundAndGameOverMsg extends ProtoAdapter<QRoundAndGameOverMsg> {
    public ProtoAdapter_QRoundAndGameOverMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, QRoundAndGameOverMsg.class);
    }

    @Override
    public int encodedSize(QRoundAndGameOverMsg value) {
      return ProtoAdapter.SINT64.encodedSizeWithTag(1, value.roundOverTimeMs)
          + QRoundInfo.ADAPTER.encodedSizeWithTag(2, value.currentRound)
          + QResultInfo.ADAPTER.asRepeated().encodedSizeWithTag(3, value.resultInfo)
          + QUserCoin.ADAPTER.asRepeated().encodedSizeWithTag(4, value.qUserCoin)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, QRoundAndGameOverMsg value) throws IOException {
      ProtoAdapter.SINT64.encodeWithTag(writer, 1, value.roundOverTimeMs);
      QRoundInfo.ADAPTER.encodeWithTag(writer, 2, value.currentRound);
      QResultInfo.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.resultInfo);
      QUserCoin.ADAPTER.asRepeated().encodeWithTag(writer, 4, value.qUserCoin);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public QRoundAndGameOverMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRoundOverTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
          case 2: builder.setCurrentRound(QRoundInfo.ADAPTER.decode(reader)); break;
          case 3: builder.resultInfo.add(QResultInfo.ADAPTER.decode(reader)); break;
          case 4: builder.qUserCoin.add(QUserCoin.ADAPTER.decode(reader)); break;
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
    public QRoundAndGameOverMsg redact(QRoundAndGameOverMsg value) {
      Builder builder = value.newBuilder();
      if (builder.currentRound != null) builder.currentRound = QRoundInfo.ADAPTER.redact(builder.currentRound);
      Internal.redactElements(builder.resultInfo, QResultInfo.ADAPTER);
      Internal.redactElements(builder.qUserCoin, QUserCoin.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
