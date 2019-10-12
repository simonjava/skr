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
import java.util.List;
import okio.ByteString;

public final class QGameConfig extends Message<QGameConfig, QGameConfig.Builder> {
  public static final ProtoAdapter<QGameConfig> ADAPTER = new ProtoAdapter_QGameConfig();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_TOTALGAMEROUNDSEQ = 0;

  public static final Integer DEFAULT_ENABLESHOWBLIGHTWAITTIMEMS = 0;

  public static final Integer DEFAULT_ENABLESHOWMLIGHTWAITTIMEMS = 0;

  public static final Integer DEFAULT_WANTSINGDELAYTIMEMS = 0;

  public static final Integer DEFAULT_KICKUSERCONSUMCOINCNT = 0;

  public static final Integer DEFAULT_CHALLENGEROUNDCNT = 0;

  /**
   * 轮次总数
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer TotalGameRoundSeq;

  /**
   * 爆灯等待时间(毫秒)
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer EnableShowBLightWaitTimeMs;

  /**
   * 灭灯等待时间(毫秒)
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer EnableShowMLightWaitTimeMs;

  /**
   * 想唱延迟时间(毫秒)
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer WantSingDelayTimeMs;

  /**
   * 反馈分提示语
   */
  @WireField(
      tag = 5,
      adapter = "com.zq.live.proto.Room.QScoreTipMsg#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<QScoreTipMsg> qScoreTipMsg;

  /**
   * 踢人消耗金币数
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer KickUserConsumCoinCnt;

  /**
   * 反馈分提示语
   */
  @WireField(
      tag = 7,
      adapter = "com.zq.live.proto.Room.PKScoreTipMsg#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<PKScoreTipMsg> qPKScoreTipMsg;

  /**
   * 一个挑战的轮次数量
   */
  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer challengeRoundCnt;

  public QGameConfig(Integer TotalGameRoundSeq, Integer EnableShowBLightWaitTimeMs,
      Integer EnableShowMLightWaitTimeMs, Integer WantSingDelayTimeMs,
      List<QScoreTipMsg> qScoreTipMsg, Integer KickUserConsumCoinCnt,
      List<PKScoreTipMsg> qPKScoreTipMsg, Integer challengeRoundCnt) {
    this(TotalGameRoundSeq, EnableShowBLightWaitTimeMs, EnableShowMLightWaitTimeMs, WantSingDelayTimeMs, qScoreTipMsg, KickUserConsumCoinCnt, qPKScoreTipMsg, challengeRoundCnt, ByteString.EMPTY);
  }

  public QGameConfig(Integer TotalGameRoundSeq, Integer EnableShowBLightWaitTimeMs,
      Integer EnableShowMLightWaitTimeMs, Integer WantSingDelayTimeMs,
      List<QScoreTipMsg> qScoreTipMsg, Integer KickUserConsumCoinCnt,
      List<PKScoreTipMsg> qPKScoreTipMsg, Integer challengeRoundCnt, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.TotalGameRoundSeq = TotalGameRoundSeq;
    this.EnableShowBLightWaitTimeMs = EnableShowBLightWaitTimeMs;
    this.EnableShowMLightWaitTimeMs = EnableShowMLightWaitTimeMs;
    this.WantSingDelayTimeMs = WantSingDelayTimeMs;
    this.qScoreTipMsg = Internal.immutableCopyOf("qScoreTipMsg", qScoreTipMsg);
    this.KickUserConsumCoinCnt = KickUserConsumCoinCnt;
    this.qPKScoreTipMsg = Internal.immutableCopyOf("qPKScoreTipMsg", qPKScoreTipMsg);
    this.challengeRoundCnt = challengeRoundCnt;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.TotalGameRoundSeq = TotalGameRoundSeq;
    builder.EnableShowBLightWaitTimeMs = EnableShowBLightWaitTimeMs;
    builder.EnableShowMLightWaitTimeMs = EnableShowMLightWaitTimeMs;
    builder.WantSingDelayTimeMs = WantSingDelayTimeMs;
    builder.qScoreTipMsg = Internal.copyOf("qScoreTipMsg", qScoreTipMsg);
    builder.KickUserConsumCoinCnt = KickUserConsumCoinCnt;
    builder.qPKScoreTipMsg = Internal.copyOf("qPKScoreTipMsg", qPKScoreTipMsg);
    builder.challengeRoundCnt = challengeRoundCnt;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof QGameConfig)) return false;
    QGameConfig o = (QGameConfig) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(TotalGameRoundSeq, o.TotalGameRoundSeq)
        && Internal.equals(EnableShowBLightWaitTimeMs, o.EnableShowBLightWaitTimeMs)
        && Internal.equals(EnableShowMLightWaitTimeMs, o.EnableShowMLightWaitTimeMs)
        && Internal.equals(WantSingDelayTimeMs, o.WantSingDelayTimeMs)
        && qScoreTipMsg.equals(o.qScoreTipMsg)
        && Internal.equals(KickUserConsumCoinCnt, o.KickUserConsumCoinCnt)
        && qPKScoreTipMsg.equals(o.qPKScoreTipMsg)
        && Internal.equals(challengeRoundCnt, o.challengeRoundCnt);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (TotalGameRoundSeq != null ? TotalGameRoundSeq.hashCode() : 0);
      result = result * 37 + (EnableShowBLightWaitTimeMs != null ? EnableShowBLightWaitTimeMs.hashCode() : 0);
      result = result * 37 + (EnableShowMLightWaitTimeMs != null ? EnableShowMLightWaitTimeMs.hashCode() : 0);
      result = result * 37 + (WantSingDelayTimeMs != null ? WantSingDelayTimeMs.hashCode() : 0);
      result = result * 37 + qScoreTipMsg.hashCode();
      result = result * 37 + (KickUserConsumCoinCnt != null ? KickUserConsumCoinCnt.hashCode() : 0);
      result = result * 37 + qPKScoreTipMsg.hashCode();
      result = result * 37 + (challengeRoundCnt != null ? challengeRoundCnt.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (TotalGameRoundSeq != null) builder.append(", TotalGameRoundSeq=").append(TotalGameRoundSeq);
    if (EnableShowBLightWaitTimeMs != null) builder.append(", EnableShowBLightWaitTimeMs=").append(EnableShowBLightWaitTimeMs);
    if (EnableShowMLightWaitTimeMs != null) builder.append(", EnableShowMLightWaitTimeMs=").append(EnableShowMLightWaitTimeMs);
    if (WantSingDelayTimeMs != null) builder.append(", WantSingDelayTimeMs=").append(WantSingDelayTimeMs);
    if (!qScoreTipMsg.isEmpty()) builder.append(", qScoreTipMsg=").append(qScoreTipMsg);
    if (KickUserConsumCoinCnt != null) builder.append(", KickUserConsumCoinCnt=").append(KickUserConsumCoinCnt);
    if (!qPKScoreTipMsg.isEmpty()) builder.append(", qPKScoreTipMsg=").append(qPKScoreTipMsg);
    if (challengeRoundCnt != null) builder.append(", challengeRoundCnt=").append(challengeRoundCnt);
    return builder.replace(0, 2, "QGameConfig{").append('}').toString();
  }

  public byte[] toByteArray() {
    return QGameConfig.ADAPTER.encode(this);
  }

  public static final QGameConfig parseFrom(byte[] data) throws IOException {
    QGameConfig c = null;
       c = QGameConfig.ADAPTER.decode(data);
    return c;
  }

  /**
   * 轮次总数
   */
  public Integer getTotalGameRoundSeq() {
    if(TotalGameRoundSeq==null){
        return DEFAULT_TOTALGAMEROUNDSEQ;
    }
    return TotalGameRoundSeq;
  }

  /**
   * 爆灯等待时间(毫秒)
   */
  public Integer getEnableShowBLightWaitTimeMs() {
    if(EnableShowBLightWaitTimeMs==null){
        return DEFAULT_ENABLESHOWBLIGHTWAITTIMEMS;
    }
    return EnableShowBLightWaitTimeMs;
  }

  /**
   * 灭灯等待时间(毫秒)
   */
  public Integer getEnableShowMLightWaitTimeMs() {
    if(EnableShowMLightWaitTimeMs==null){
        return DEFAULT_ENABLESHOWMLIGHTWAITTIMEMS;
    }
    return EnableShowMLightWaitTimeMs;
  }

  /**
   * 想唱延迟时间(毫秒)
   */
  public Integer getWantSingDelayTimeMs() {
    if(WantSingDelayTimeMs==null){
        return DEFAULT_WANTSINGDELAYTIMEMS;
    }
    return WantSingDelayTimeMs;
  }

  /**
   * 反馈分提示语
   */
  public List<QScoreTipMsg> getQScoreTipMsgList() {
    if(qScoreTipMsg==null){
        return new java.util.ArrayList<QScoreTipMsg>();
    }
    return qScoreTipMsg;
  }

  /**
   * 踢人消耗金币数
   */
  public Integer getKickUserConsumCoinCnt() {
    if(KickUserConsumCoinCnt==null){
        return DEFAULT_KICKUSERCONSUMCOINCNT;
    }
    return KickUserConsumCoinCnt;
  }

  /**
   * 反馈分提示语
   */
  public List<PKScoreTipMsg> getQPKScoreTipMsgList() {
    if(qPKScoreTipMsg==null){
        return new java.util.ArrayList<PKScoreTipMsg>();
    }
    return qPKScoreTipMsg;
  }

  /**
   * 一个挑战的轮次数量
   */
  public Integer getChallengeRoundCnt() {
    if(challengeRoundCnt==null){
        return DEFAULT_CHALLENGEROUNDCNT;
    }
    return challengeRoundCnt;
  }

  /**
   * 轮次总数
   */
  public boolean hasTotalGameRoundSeq() {
    return TotalGameRoundSeq!=null;
  }

  /**
   * 爆灯等待时间(毫秒)
   */
  public boolean hasEnableShowBLightWaitTimeMs() {
    return EnableShowBLightWaitTimeMs!=null;
  }

  /**
   * 灭灯等待时间(毫秒)
   */
  public boolean hasEnableShowMLightWaitTimeMs() {
    return EnableShowMLightWaitTimeMs!=null;
  }

  /**
   * 想唱延迟时间(毫秒)
   */
  public boolean hasWantSingDelayTimeMs() {
    return WantSingDelayTimeMs!=null;
  }

  /**
   * 反馈分提示语
   */
  public boolean hasQScoreTipMsgList() {
    return qScoreTipMsg!=null;
  }

  /**
   * 踢人消耗金币数
   */
  public boolean hasKickUserConsumCoinCnt() {
    return KickUserConsumCoinCnt!=null;
  }

  /**
   * 反馈分提示语
   */
  public boolean hasQPKScoreTipMsgList() {
    return qPKScoreTipMsg!=null;
  }

  /**
   * 一个挑战的轮次数量
   */
  public boolean hasChallengeRoundCnt() {
    return challengeRoundCnt!=null;
  }

  public static final class Builder extends Message.Builder<QGameConfig, Builder> {
    private Integer TotalGameRoundSeq;

    private Integer EnableShowBLightWaitTimeMs;

    private Integer EnableShowMLightWaitTimeMs;

    private Integer WantSingDelayTimeMs;

    private List<QScoreTipMsg> qScoreTipMsg;

    private Integer KickUserConsumCoinCnt;

    private List<PKScoreTipMsg> qPKScoreTipMsg;

    private Integer challengeRoundCnt;

    public Builder() {
      qScoreTipMsg = Internal.newMutableList();
      qPKScoreTipMsg = Internal.newMutableList();
    }

    /**
     * 轮次总数
     */
    public Builder setTotalGameRoundSeq(Integer TotalGameRoundSeq) {
      this.TotalGameRoundSeq = TotalGameRoundSeq;
      return this;
    }

    /**
     * 爆灯等待时间(毫秒)
     */
    public Builder setEnableShowBLightWaitTimeMs(Integer EnableShowBLightWaitTimeMs) {
      this.EnableShowBLightWaitTimeMs = EnableShowBLightWaitTimeMs;
      return this;
    }

    /**
     * 灭灯等待时间(毫秒)
     */
    public Builder setEnableShowMLightWaitTimeMs(Integer EnableShowMLightWaitTimeMs) {
      this.EnableShowMLightWaitTimeMs = EnableShowMLightWaitTimeMs;
      return this;
    }

    /**
     * 想唱延迟时间(毫秒)
     */
    public Builder setWantSingDelayTimeMs(Integer WantSingDelayTimeMs) {
      this.WantSingDelayTimeMs = WantSingDelayTimeMs;
      return this;
    }

    /**
     * 反馈分提示语
     */
    public Builder addAllQScoreTipMsg(List<QScoreTipMsg> qScoreTipMsg) {
      Internal.checkElementsNotNull(qScoreTipMsg);
      this.qScoreTipMsg = qScoreTipMsg;
      return this;
    }

    /**
     * 踢人消耗金币数
     */
    public Builder setKickUserConsumCoinCnt(Integer KickUserConsumCoinCnt) {
      this.KickUserConsumCoinCnt = KickUserConsumCoinCnt;
      return this;
    }

    /**
     * 反馈分提示语
     */
    public Builder addAllQPKScoreTipMsg(List<PKScoreTipMsg> qPKScoreTipMsg) {
      Internal.checkElementsNotNull(qPKScoreTipMsg);
      this.qPKScoreTipMsg = qPKScoreTipMsg;
      return this;
    }

    /**
     * 一个挑战的轮次数量
     */
    public Builder setChallengeRoundCnt(Integer challengeRoundCnt) {
      this.challengeRoundCnt = challengeRoundCnt;
      return this;
    }

    @Override
    public QGameConfig build() {
      return new QGameConfig(TotalGameRoundSeq, EnableShowBLightWaitTimeMs, EnableShowMLightWaitTimeMs, WantSingDelayTimeMs, qScoreTipMsg, KickUserConsumCoinCnt, qPKScoreTipMsg, challengeRoundCnt, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_QGameConfig extends ProtoAdapter<QGameConfig> {
    public ProtoAdapter_QGameConfig() {
      super(FieldEncoding.LENGTH_DELIMITED, QGameConfig.class);
    }

    @Override
    public int encodedSize(QGameConfig value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.TotalGameRoundSeq)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.EnableShowBLightWaitTimeMs)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.EnableShowMLightWaitTimeMs)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.WantSingDelayTimeMs)
          + QScoreTipMsg.ADAPTER.asRepeated().encodedSizeWithTag(5, value.qScoreTipMsg)
          + ProtoAdapter.UINT32.encodedSizeWithTag(6, value.KickUserConsumCoinCnt)
          + PKScoreTipMsg.ADAPTER.asRepeated().encodedSizeWithTag(7, value.qPKScoreTipMsg)
          + ProtoAdapter.UINT32.encodedSizeWithTag(8, value.challengeRoundCnt)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, QGameConfig value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.TotalGameRoundSeq);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.EnableShowBLightWaitTimeMs);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.EnableShowMLightWaitTimeMs);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.WantSingDelayTimeMs);
      QScoreTipMsg.ADAPTER.asRepeated().encodeWithTag(writer, 5, value.qScoreTipMsg);
      ProtoAdapter.UINT32.encodeWithTag(writer, 6, value.KickUserConsumCoinCnt);
      PKScoreTipMsg.ADAPTER.asRepeated().encodeWithTag(writer, 7, value.qPKScoreTipMsg);
      ProtoAdapter.UINT32.encodeWithTag(writer, 8, value.challengeRoundCnt);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public QGameConfig decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setTotalGameRoundSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setEnableShowBLightWaitTimeMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setEnableShowMLightWaitTimeMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setWantSingDelayTimeMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.qScoreTipMsg.add(QScoreTipMsg.ADAPTER.decode(reader)); break;
          case 6: builder.setKickUserConsumCoinCnt(ProtoAdapter.UINT32.decode(reader)); break;
          case 7: builder.qPKScoreTipMsg.add(PKScoreTipMsg.ADAPTER.decode(reader)); break;
          case 8: builder.setChallengeRoundCnt(ProtoAdapter.UINT32.decode(reader)); break;
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
    public QGameConfig redact(QGameConfig value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.qScoreTipMsg, QScoreTipMsg.ADAPTER);
      Internal.redactElements(builder.qPKScoreTipMsg, PKScoreTipMsg.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
