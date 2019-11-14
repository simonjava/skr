// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: race_room.proto
package com.zq.live.proto.RaceRoom;

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

public final class RaceRoundInfo extends Message<RaceRoundInfo, RaceRoundInfo.Builder> {
  public static final ProtoAdapter<RaceRoundInfo> ADAPTER = new ProtoAdapter_RaceRoundInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_ROUNDSEQ = 0;

  public static final Integer DEFAULT_SUBROUNDSEQ = 0;

  public static final ERaceRoundStatus DEFAULT_STATUS = ERaceRoundStatus.ERRS_UNKNOWN;

  public static final ERaceRoundOverReason DEFAULT_OVERREASON = ERaceRoundOverReason.ERROR_UNKNOWN;

  public static final Integer DEFAULT_INTROBEGINMS = 0;

  public static final Integer DEFAULT_INTROENDMS = 0;

  public static final Integer DEFAULT_CURRENTROUNDCHOICEUSERCNT = 0;

  public static final Integer DEFAULT_AUDIENCEUSERCNT = 0;

  /**
   * 轮次顺序
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roundSeq;

  /**
   * 子轮次
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer subRoundSeq;

  /**
   * 轮次状态
   */
  @WireField(
      tag = 3,
      adapter = "com.zq.live.proto.RaceRoom.ERaceRoundStatus#ADAPTER"
  )
  private final ERaceRoundStatus status;

  /**
   * 切换轮次原因
   */
  @WireField(
      tag = 4,
      adapter = "com.zq.live.proto.RaceRoom.ERaceRoundOverReason#ADAPTER"
  )
  private final ERaceRoundOverReason overReason;

  /**
   * 子轮次信息
   */
  @WireField(
      tag = 5,
      adapter = "com.zq.live.proto.RaceRoom.SubRoundInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<SubRoundInfo> subRoundInfo;

  /**
   * 得分信息
   */
  @WireField(
      tag = 6,
      adapter = "com.zq.live.proto.RaceRoom.RoundScoreInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<RoundScoreInfo> scores;

  /**
   * 等待中用户列表
   */
  @WireField(
      tag = 7,
      adapter = "com.zq.live.proto.RaceRoom.ROnlineInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<ROnlineInfo> waitUsers;

  /**
   * 当局玩的用户列表
   */
  @WireField(
      tag = 8,
      adapter = "com.zq.live.proto.RaceRoom.ROnlineInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<ROnlineInfo> playUsers;

  /**
   * 竞选开始相对时间（相对于createTimeMs时间）
   */
  @WireField(
      tag = 9,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer introBeginMs;

  /**
   * 竞选结束相对时间（相对于createTimeMs时间）
   */
  @WireField(
      tag = 10,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer introEndMs;

  /**
   * [废弃]竞选选择列表(当状态为ERRS_CHOCING时有效)
   */
  @WireField(
      tag = 11,
      adapter = "com.zq.live.proto.RaceRoom.RWantSingInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<RWantSingInfo> wantSingInfos;

  /**
   * 轮次报名人数
   */
  @WireField(
      tag = 12,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer currentRoundChoiceUserCnt;

  /**
   * 观众人数
   */
  @WireField(
      tag = 13,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer audienceUserCnt;

  public RaceRoundInfo(Integer roundSeq, Integer subRoundSeq, ERaceRoundStatus status,
      ERaceRoundOverReason overReason, List<SubRoundInfo> subRoundInfo, List<RoundScoreInfo> scores,
      List<ROnlineInfo> waitUsers, List<ROnlineInfo> playUsers, Integer introBeginMs,
      Integer introEndMs, List<RWantSingInfo> wantSingInfos, Integer currentRoundChoiceUserCnt,
      Integer audienceUserCnt) {
    this(roundSeq, subRoundSeq, status, overReason, subRoundInfo, scores, waitUsers, playUsers, introBeginMs, introEndMs, wantSingInfos, currentRoundChoiceUserCnt, audienceUserCnt, ByteString.EMPTY);
  }

  public RaceRoundInfo(Integer roundSeq, Integer subRoundSeq, ERaceRoundStatus status,
      ERaceRoundOverReason overReason, List<SubRoundInfo> subRoundInfo, List<RoundScoreInfo> scores,
      List<ROnlineInfo> waitUsers, List<ROnlineInfo> playUsers, Integer introBeginMs,
      Integer introEndMs, List<RWantSingInfo> wantSingInfos, Integer currentRoundChoiceUserCnt,
      Integer audienceUserCnt, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.roundSeq = roundSeq;
    this.subRoundSeq = subRoundSeq;
    this.status = status;
    this.overReason = overReason;
    this.subRoundInfo = Internal.immutableCopyOf("subRoundInfo", subRoundInfo);
    this.scores = Internal.immutableCopyOf("scores", scores);
    this.waitUsers = Internal.immutableCopyOf("waitUsers", waitUsers);
    this.playUsers = Internal.immutableCopyOf("playUsers", playUsers);
    this.introBeginMs = introBeginMs;
    this.introEndMs = introEndMs;
    this.wantSingInfos = Internal.immutableCopyOf("wantSingInfos", wantSingInfos);
    this.currentRoundChoiceUserCnt = currentRoundChoiceUserCnt;
    this.audienceUserCnt = audienceUserCnt;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.roundSeq = roundSeq;
    builder.subRoundSeq = subRoundSeq;
    builder.status = status;
    builder.overReason = overReason;
    builder.subRoundInfo = Internal.copyOf("subRoundInfo", subRoundInfo);
    builder.scores = Internal.copyOf("scores", scores);
    builder.waitUsers = Internal.copyOf("waitUsers", waitUsers);
    builder.playUsers = Internal.copyOf("playUsers", playUsers);
    builder.introBeginMs = introBeginMs;
    builder.introEndMs = introEndMs;
    builder.wantSingInfos = Internal.copyOf("wantSingInfos", wantSingInfos);
    builder.currentRoundChoiceUserCnt = currentRoundChoiceUserCnt;
    builder.audienceUserCnt = audienceUserCnt;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RaceRoundInfo)) return false;
    RaceRoundInfo o = (RaceRoundInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(roundSeq, o.roundSeq)
        && Internal.equals(subRoundSeq, o.subRoundSeq)
        && Internal.equals(status, o.status)
        && Internal.equals(overReason, o.overReason)
        && subRoundInfo.equals(o.subRoundInfo)
        && scores.equals(o.scores)
        && waitUsers.equals(o.waitUsers)
        && playUsers.equals(o.playUsers)
        && Internal.equals(introBeginMs, o.introBeginMs)
        && Internal.equals(introEndMs, o.introEndMs)
        && wantSingInfos.equals(o.wantSingInfos)
        && Internal.equals(currentRoundChoiceUserCnt, o.currentRoundChoiceUserCnt)
        && Internal.equals(audienceUserCnt, o.audienceUserCnt);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (roundSeq != null ? roundSeq.hashCode() : 0);
      result = result * 37 + (subRoundSeq != null ? subRoundSeq.hashCode() : 0);
      result = result * 37 + (status != null ? status.hashCode() : 0);
      result = result * 37 + (overReason != null ? overReason.hashCode() : 0);
      result = result * 37 + subRoundInfo.hashCode();
      result = result * 37 + scores.hashCode();
      result = result * 37 + waitUsers.hashCode();
      result = result * 37 + playUsers.hashCode();
      result = result * 37 + (introBeginMs != null ? introBeginMs.hashCode() : 0);
      result = result * 37 + (introEndMs != null ? introEndMs.hashCode() : 0);
      result = result * 37 + wantSingInfos.hashCode();
      result = result * 37 + (currentRoundChoiceUserCnt != null ? currentRoundChoiceUserCnt.hashCode() : 0);
      result = result * 37 + (audienceUserCnt != null ? audienceUserCnt.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (roundSeq != null) builder.append(", roundSeq=").append(roundSeq);
    if (subRoundSeq != null) builder.append(", subRoundSeq=").append(subRoundSeq);
    if (status != null) builder.append(", status=").append(status);
    if (overReason != null) builder.append(", overReason=").append(overReason);
    if (!subRoundInfo.isEmpty()) builder.append(", subRoundInfo=").append(subRoundInfo);
    if (!scores.isEmpty()) builder.append(", scores=").append(scores);
    if (!waitUsers.isEmpty()) builder.append(", waitUsers=").append(waitUsers);
    if (!playUsers.isEmpty()) builder.append(", playUsers=").append(playUsers);
    if (introBeginMs != null) builder.append(", introBeginMs=").append(introBeginMs);
    if (introEndMs != null) builder.append(", introEndMs=").append(introEndMs);
    if (!wantSingInfos.isEmpty()) builder.append(", wantSingInfos=").append(wantSingInfos);
    if (currentRoundChoiceUserCnt != null) builder.append(", currentRoundChoiceUserCnt=").append(currentRoundChoiceUserCnt);
    if (audienceUserCnt != null) builder.append(", audienceUserCnt=").append(audienceUserCnt);
    return builder.replace(0, 2, "RaceRoundInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RaceRoundInfo.ADAPTER.encode(this);
  }

  public static final RaceRoundInfo parseFrom(byte[] data) throws IOException {
    RaceRoundInfo c = null;
       c = RaceRoundInfo.ADAPTER.decode(data);
    return c;
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
   * 子轮次
   */
  public Integer getSubRoundSeq() {
    if(subRoundSeq==null){
        return DEFAULT_SUBROUNDSEQ;
    }
    return subRoundSeq;
  }

  /**
   * 轮次状态
   */
  public ERaceRoundStatus getStatus() {
    if(status==null){
        return new ERaceRoundStatus.Builder().build();
    }
    return status;
  }

  /**
   * 切换轮次原因
   */
  public ERaceRoundOverReason getOverReason() {
    if(overReason==null){
        return new ERaceRoundOverReason.Builder().build();
    }
    return overReason;
  }

  /**
   * 子轮次信息
   */
  public List<SubRoundInfo> getSubRoundInfoList() {
    if(subRoundInfo==null){
        return new java.util.ArrayList<SubRoundInfo>();
    }
    return subRoundInfo;
  }

  /**
   * 得分信息
   */
  public List<RoundScoreInfo> getScoresList() {
    if(scores==null){
        return new java.util.ArrayList<RoundScoreInfo>();
    }
    return scores;
  }

  /**
   * 等待中用户列表
   */
  public List<ROnlineInfo> getWaitUsersList() {
    if(waitUsers==null){
        return new java.util.ArrayList<ROnlineInfo>();
    }
    return waitUsers;
  }

  /**
   * 当局玩的用户列表
   */
  public List<ROnlineInfo> getPlayUsersList() {
    if(playUsers==null){
        return new java.util.ArrayList<ROnlineInfo>();
    }
    return playUsers;
  }

  /**
   * 竞选开始相对时间（相对于createTimeMs时间）
   */
  public Integer getIntroBeginMs() {
    if(introBeginMs==null){
        return DEFAULT_INTROBEGINMS;
    }
    return introBeginMs;
  }

  /**
   * 竞选结束相对时间（相对于createTimeMs时间）
   */
  public Integer getIntroEndMs() {
    if(introEndMs==null){
        return DEFAULT_INTROENDMS;
    }
    return introEndMs;
  }

  /**
   * [废弃]竞选选择列表(当状态为ERRS_CHOCING时有效)
   */
  public List<RWantSingInfo> getWantSingInfosList() {
    if(wantSingInfos==null){
        return new java.util.ArrayList<RWantSingInfo>();
    }
    return wantSingInfos;
  }

  /**
   * 轮次报名人数
   */
  public Integer getCurrentRoundChoiceUserCnt() {
    if(currentRoundChoiceUserCnt==null){
        return DEFAULT_CURRENTROUNDCHOICEUSERCNT;
    }
    return currentRoundChoiceUserCnt;
  }

  /**
   * 观众人数
   */
  public Integer getAudienceUserCnt() {
    if(audienceUserCnt==null){
        return DEFAULT_AUDIENCEUSERCNT;
    }
    return audienceUserCnt;
  }

  /**
   * 轮次顺序
   */
  public boolean hasRoundSeq() {
    return roundSeq!=null;
  }

  /**
   * 子轮次
   */
  public boolean hasSubRoundSeq() {
    return subRoundSeq!=null;
  }

  /**
   * 轮次状态
   */
  public boolean hasStatus() {
    return status!=null;
  }

  /**
   * 切换轮次原因
   */
  public boolean hasOverReason() {
    return overReason!=null;
  }

  /**
   * 子轮次信息
   */
  public boolean hasSubRoundInfoList() {
    return subRoundInfo!=null;
  }

  /**
   * 得分信息
   */
  public boolean hasScoresList() {
    return scores!=null;
  }

  /**
   * 等待中用户列表
   */
  public boolean hasWaitUsersList() {
    return waitUsers!=null;
  }

  /**
   * 当局玩的用户列表
   */
  public boolean hasPlayUsersList() {
    return playUsers!=null;
  }

  /**
   * 竞选开始相对时间（相对于createTimeMs时间）
   */
  public boolean hasIntroBeginMs() {
    return introBeginMs!=null;
  }

  /**
   * 竞选结束相对时间（相对于createTimeMs时间）
   */
  public boolean hasIntroEndMs() {
    return introEndMs!=null;
  }

  /**
   * [废弃]竞选选择列表(当状态为ERRS_CHOCING时有效)
   */
  public boolean hasWantSingInfosList() {
    return wantSingInfos!=null;
  }

  /**
   * 轮次报名人数
   */
  public boolean hasCurrentRoundChoiceUserCnt() {
    return currentRoundChoiceUserCnt!=null;
  }

  /**
   * 观众人数
   */
  public boolean hasAudienceUserCnt() {
    return audienceUserCnt!=null;
  }

  public static final class Builder extends Message.Builder<RaceRoundInfo, Builder> {
    private Integer roundSeq;

    private Integer subRoundSeq;

    private ERaceRoundStatus status;

    private ERaceRoundOverReason overReason;

    private List<SubRoundInfo> subRoundInfo;

    private List<RoundScoreInfo> scores;

    private List<ROnlineInfo> waitUsers;

    private List<ROnlineInfo> playUsers;

    private Integer introBeginMs;

    private Integer introEndMs;

    private List<RWantSingInfo> wantSingInfos;

    private Integer currentRoundChoiceUserCnt;

    private Integer audienceUserCnt;

    public Builder() {
      subRoundInfo = Internal.newMutableList();
      scores = Internal.newMutableList();
      waitUsers = Internal.newMutableList();
      playUsers = Internal.newMutableList();
      wantSingInfos = Internal.newMutableList();
    }

    /**
     * 轮次顺序
     */
    public Builder setRoundSeq(Integer roundSeq) {
      this.roundSeq = roundSeq;
      return this;
    }

    /**
     * 子轮次
     */
    public Builder setSubRoundSeq(Integer subRoundSeq) {
      this.subRoundSeq = subRoundSeq;
      return this;
    }

    /**
     * 轮次状态
     */
    public Builder setStatus(ERaceRoundStatus status) {
      this.status = status;
      return this;
    }

    /**
     * 切换轮次原因
     */
    public Builder setOverReason(ERaceRoundOverReason overReason) {
      this.overReason = overReason;
      return this;
    }

    /**
     * 子轮次信息
     */
    public Builder addAllSubRoundInfo(List<SubRoundInfo> subRoundInfo) {
      Internal.checkElementsNotNull(subRoundInfo);
      this.subRoundInfo = subRoundInfo;
      return this;
    }

    /**
     * 得分信息
     */
    public Builder addAllScores(List<RoundScoreInfo> scores) {
      Internal.checkElementsNotNull(scores);
      this.scores = scores;
      return this;
    }

    /**
     * 等待中用户列表
     */
    public Builder addAllWaitUsers(List<ROnlineInfo> waitUsers) {
      Internal.checkElementsNotNull(waitUsers);
      this.waitUsers = waitUsers;
      return this;
    }

    /**
     * 当局玩的用户列表
     */
    public Builder addAllPlayUsers(List<ROnlineInfo> playUsers) {
      Internal.checkElementsNotNull(playUsers);
      this.playUsers = playUsers;
      return this;
    }

    /**
     * 竞选开始相对时间（相对于createTimeMs时间）
     */
    public Builder setIntroBeginMs(Integer introBeginMs) {
      this.introBeginMs = introBeginMs;
      return this;
    }

    /**
     * 竞选结束相对时间（相对于createTimeMs时间）
     */
    public Builder setIntroEndMs(Integer introEndMs) {
      this.introEndMs = introEndMs;
      return this;
    }

    /**
     * [废弃]竞选选择列表(当状态为ERRS_CHOCING时有效)
     */
    public Builder addAllWantSingInfos(List<RWantSingInfo> wantSingInfos) {
      Internal.checkElementsNotNull(wantSingInfos);
      this.wantSingInfos = wantSingInfos;
      return this;
    }

    /**
     * 轮次报名人数
     */
    public Builder setCurrentRoundChoiceUserCnt(Integer currentRoundChoiceUserCnt) {
      this.currentRoundChoiceUserCnt = currentRoundChoiceUserCnt;
      return this;
    }

    /**
     * 观众人数
     */
    public Builder setAudienceUserCnt(Integer audienceUserCnt) {
      this.audienceUserCnt = audienceUserCnt;
      return this;
    }

    @Override
    public RaceRoundInfo build() {
      return new RaceRoundInfo(roundSeq, subRoundSeq, status, overReason, subRoundInfo, scores, waitUsers, playUsers, introBeginMs, introEndMs, wantSingInfos, currentRoundChoiceUserCnt, audienceUserCnt, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RaceRoundInfo extends ProtoAdapter<RaceRoundInfo> {
    public ProtoAdapter_RaceRoundInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, RaceRoundInfo.class);
    }

    @Override
    public int encodedSize(RaceRoundInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.roundSeq)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.subRoundSeq)
          + ERaceRoundStatus.ADAPTER.encodedSizeWithTag(3, value.status)
          + ERaceRoundOverReason.ADAPTER.encodedSizeWithTag(4, value.overReason)
          + SubRoundInfo.ADAPTER.asRepeated().encodedSizeWithTag(5, value.subRoundInfo)
          + RoundScoreInfo.ADAPTER.asRepeated().encodedSizeWithTag(6, value.scores)
          + ROnlineInfo.ADAPTER.asRepeated().encodedSizeWithTag(7, value.waitUsers)
          + ROnlineInfo.ADAPTER.asRepeated().encodedSizeWithTag(8, value.playUsers)
          + ProtoAdapter.UINT32.encodedSizeWithTag(9, value.introBeginMs)
          + ProtoAdapter.UINT32.encodedSizeWithTag(10, value.introEndMs)
          + RWantSingInfo.ADAPTER.asRepeated().encodedSizeWithTag(11, value.wantSingInfos)
          + ProtoAdapter.UINT32.encodedSizeWithTag(12, value.currentRoundChoiceUserCnt)
          + ProtoAdapter.UINT32.encodedSizeWithTag(13, value.audienceUserCnt)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RaceRoundInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.roundSeq);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.subRoundSeq);
      ERaceRoundStatus.ADAPTER.encodeWithTag(writer, 3, value.status);
      ERaceRoundOverReason.ADAPTER.encodeWithTag(writer, 4, value.overReason);
      SubRoundInfo.ADAPTER.asRepeated().encodeWithTag(writer, 5, value.subRoundInfo);
      RoundScoreInfo.ADAPTER.asRepeated().encodeWithTag(writer, 6, value.scores);
      ROnlineInfo.ADAPTER.asRepeated().encodeWithTag(writer, 7, value.waitUsers);
      ROnlineInfo.ADAPTER.asRepeated().encodeWithTag(writer, 8, value.playUsers);
      ProtoAdapter.UINT32.encodeWithTag(writer, 9, value.introBeginMs);
      ProtoAdapter.UINT32.encodeWithTag(writer, 10, value.introEndMs);
      RWantSingInfo.ADAPTER.asRepeated().encodeWithTag(writer, 11, value.wantSingInfos);
      ProtoAdapter.UINT32.encodeWithTag(writer, 12, value.currentRoundChoiceUserCnt);
      ProtoAdapter.UINT32.encodeWithTag(writer, 13, value.audienceUserCnt);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RaceRoundInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRoundSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setSubRoundSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: {
            try {
              builder.setStatus(ERaceRoundStatus.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 4: {
            try {
              builder.setOverReason(ERaceRoundOverReason.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 5: builder.subRoundInfo.add(SubRoundInfo.ADAPTER.decode(reader)); break;
          case 6: builder.scores.add(RoundScoreInfo.ADAPTER.decode(reader)); break;
          case 7: builder.waitUsers.add(ROnlineInfo.ADAPTER.decode(reader)); break;
          case 8: builder.playUsers.add(ROnlineInfo.ADAPTER.decode(reader)); break;
          case 9: builder.setIntroBeginMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 10: builder.setIntroEndMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 11: builder.wantSingInfos.add(RWantSingInfo.ADAPTER.decode(reader)); break;
          case 12: builder.setCurrentRoundChoiceUserCnt(ProtoAdapter.UINT32.decode(reader)); break;
          case 13: builder.setAudienceUserCnt(ProtoAdapter.UINT32.decode(reader)); break;
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
    public RaceRoundInfo redact(RaceRoundInfo value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.subRoundInfo, SubRoundInfo.ADAPTER);
      Internal.redactElements(builder.scores, RoundScoreInfo.ADAPTER);
      Internal.redactElements(builder.waitUsers, ROnlineInfo.ADAPTER);
      Internal.redactElements(builder.playUsers, ROnlineInfo.ADAPTER);
      Internal.redactElements(builder.wantSingInfos, RWantSingInfo.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
