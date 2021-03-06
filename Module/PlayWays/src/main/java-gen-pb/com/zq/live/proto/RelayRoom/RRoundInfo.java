// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: relay_room.proto
package com.zq.live.proto.RelayRoom;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.zq.live.proto.Common.MusicInfo;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class RRoundInfo extends Message<RRoundInfo, RRoundInfo.Builder> {
  public static final ProtoAdapter<RRoundInfo> ADAPTER = new ProtoAdapter_RRoundInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Integer DEFAULT_ROUNDSEQ = 0;

  public static final Integer DEFAULT_INTROBEGINMS = 0;

  public static final Integer DEFAULT_INTROENDMS = 0;

  public static final Integer DEFAULT_SINGBEGINMS = 0;

  public static final Integer DEFAULT_SINGENDMS = 0;

  public static final ERRoundStatus DEFAULT_STATUS = ERRoundStatus.RRS_UNKNOWN;

  public static final ERRoundOverReason DEFAULT_OVERREASON = ERRoundOverReason.RROR_UNKNOWN;

  /**
   * 发起者
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * 轮次序号
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roundSeq;

  /**
   * 导唱开始相对时间（相对于createdTimeMs时间） p.s.导唱为等待阶段
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer introBeginMs;

  /**
   * 导唱结束相对时间（相对于createdTimeMs时间） p.s.导唱为等待阶段
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer introEndMs;

  /**
   * 演唱开始相对时间（相对于createdTimeMs时间）
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer singBeginMs;

  /**
   * 演唱结束相对时间（相对于createdTimeMs时间）
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer singEndMs;

  /**
   * 轮次状态
   */
  @WireField(
      tag = 7,
      adapter = "com.zq.live.proto.RelayRoom.ERRoundStatus#ADAPTER"
  )
  private final ERRoundStatus status;

  /**
   * 切换轮次原因
   */
  @WireField(
      tag = 8,
      adapter = "com.zq.live.proto.RelayRoom.ERRoundOverReason#ADAPTER"
  )
  private final ERRoundOverReason overReason;

  /**
   * 本轮次的歌曲信息
   */
  @WireField(
      tag = 9,
      adapter = "com.zq.live.proto.Common.MusicInfo#ADAPTER"
  )
  private final MusicInfo music;

  public RRoundInfo(Integer userID, Integer roundSeq, Integer introBeginMs, Integer introEndMs,
      Integer singBeginMs, Integer singEndMs, ERRoundStatus status, ERRoundOverReason overReason,
      MusicInfo music) {
    this(userID, roundSeq, introBeginMs, introEndMs, singBeginMs, singEndMs, status, overReason, music, ByteString.EMPTY);
  }

  public RRoundInfo(Integer userID, Integer roundSeq, Integer introBeginMs, Integer introEndMs,
      Integer singBeginMs, Integer singEndMs, ERRoundStatus status, ERRoundOverReason overReason,
      MusicInfo music, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.roundSeq = roundSeq;
    this.introBeginMs = introBeginMs;
    this.introEndMs = introEndMs;
    this.singBeginMs = singBeginMs;
    this.singEndMs = singEndMs;
    this.status = status;
    this.overReason = overReason;
    this.music = music;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.roundSeq = roundSeq;
    builder.introBeginMs = introBeginMs;
    builder.introEndMs = introEndMs;
    builder.singBeginMs = singBeginMs;
    builder.singEndMs = singEndMs;
    builder.status = status;
    builder.overReason = overReason;
    builder.music = music;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RRoundInfo)) return false;
    RRoundInfo o = (RRoundInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(roundSeq, o.roundSeq)
        && Internal.equals(introBeginMs, o.introBeginMs)
        && Internal.equals(introEndMs, o.introEndMs)
        && Internal.equals(singBeginMs, o.singBeginMs)
        && Internal.equals(singEndMs, o.singEndMs)
        && Internal.equals(status, o.status)
        && Internal.equals(overReason, o.overReason)
        && Internal.equals(music, o.music);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (roundSeq != null ? roundSeq.hashCode() : 0);
      result = result * 37 + (introBeginMs != null ? introBeginMs.hashCode() : 0);
      result = result * 37 + (introEndMs != null ? introEndMs.hashCode() : 0);
      result = result * 37 + (singBeginMs != null ? singBeginMs.hashCode() : 0);
      result = result * 37 + (singEndMs != null ? singEndMs.hashCode() : 0);
      result = result * 37 + (status != null ? status.hashCode() : 0);
      result = result * 37 + (overReason != null ? overReason.hashCode() : 0);
      result = result * 37 + (music != null ? music.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (roundSeq != null) builder.append(", roundSeq=").append(roundSeq);
    if (introBeginMs != null) builder.append(", introBeginMs=").append(introBeginMs);
    if (introEndMs != null) builder.append(", introEndMs=").append(introEndMs);
    if (singBeginMs != null) builder.append(", singBeginMs=").append(singBeginMs);
    if (singEndMs != null) builder.append(", singEndMs=").append(singEndMs);
    if (status != null) builder.append(", status=").append(status);
    if (overReason != null) builder.append(", overReason=").append(overReason);
    if (music != null) builder.append(", music=").append(music);
    return builder.replace(0, 2, "RRoundInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RRoundInfo.ADAPTER.encode(this);
  }

  public static final RRoundInfo parseFrom(byte[] data) throws IOException {
    RRoundInfo c = null;
       c = RRoundInfo.ADAPTER.decode(data);
    return c;
  }

  /**
   * 发起者
   */
  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  /**
   * 轮次序号
   */
  public Integer getRoundSeq() {
    if(roundSeq==null){
        return DEFAULT_ROUNDSEQ;
    }
    return roundSeq;
  }

  /**
   * 导唱开始相对时间（相对于createdTimeMs时间） p.s.导唱为等待阶段
   */
  public Integer getIntroBeginMs() {
    if(introBeginMs==null){
        return DEFAULT_INTROBEGINMS;
    }
    return introBeginMs;
  }

  /**
   * 导唱结束相对时间（相对于createdTimeMs时间） p.s.导唱为等待阶段
   */
  public Integer getIntroEndMs() {
    if(introEndMs==null){
        return DEFAULT_INTROENDMS;
    }
    return introEndMs;
  }

  /**
   * 演唱开始相对时间（相对于createdTimeMs时间）
   */
  public Integer getSingBeginMs() {
    if(singBeginMs==null){
        return DEFAULT_SINGBEGINMS;
    }
    return singBeginMs;
  }

  /**
   * 演唱结束相对时间（相对于createdTimeMs时间）
   */
  public Integer getSingEndMs() {
    if(singEndMs==null){
        return DEFAULT_SINGENDMS;
    }
    return singEndMs;
  }

  /**
   * 轮次状态
   */
  public ERRoundStatus getStatus() {
    if(status==null){
        return new ERRoundStatus.Builder().build();
    }
    return status;
  }

  /**
   * 切换轮次原因
   */
  public ERRoundOverReason getOverReason() {
    if(overReason==null){
        return new ERRoundOverReason.Builder().build();
    }
    return overReason;
  }

  /**
   * 本轮次的歌曲信息
   */
  public MusicInfo getMusic() {
    if(music==null){
        return new MusicInfo.Builder().build();
    }
    return music;
  }

  /**
   * 发起者
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 轮次序号
   */
  public boolean hasRoundSeq() {
    return roundSeq!=null;
  }

  /**
   * 导唱开始相对时间（相对于createdTimeMs时间） p.s.导唱为等待阶段
   */
  public boolean hasIntroBeginMs() {
    return introBeginMs!=null;
  }

  /**
   * 导唱结束相对时间（相对于createdTimeMs时间） p.s.导唱为等待阶段
   */
  public boolean hasIntroEndMs() {
    return introEndMs!=null;
  }

  /**
   * 演唱开始相对时间（相对于createdTimeMs时间）
   */
  public boolean hasSingBeginMs() {
    return singBeginMs!=null;
  }

  /**
   * 演唱结束相对时间（相对于createdTimeMs时间）
   */
  public boolean hasSingEndMs() {
    return singEndMs!=null;
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
   * 本轮次的歌曲信息
   */
  public boolean hasMusic() {
    return music!=null;
  }

  public static final class Builder extends Message.Builder<RRoundInfo, Builder> {
    private Integer userID;

    private Integer roundSeq;

    private Integer introBeginMs;

    private Integer introEndMs;

    private Integer singBeginMs;

    private Integer singEndMs;

    private ERRoundStatus status;

    private ERRoundOverReason overReason;

    private MusicInfo music;

    public Builder() {
    }

    /**
     * 发起者
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    /**
     * 轮次序号
     */
    public Builder setRoundSeq(Integer roundSeq) {
      this.roundSeq = roundSeq;
      return this;
    }

    /**
     * 导唱开始相对时间（相对于createdTimeMs时间） p.s.导唱为等待阶段
     */
    public Builder setIntroBeginMs(Integer introBeginMs) {
      this.introBeginMs = introBeginMs;
      return this;
    }

    /**
     * 导唱结束相对时间（相对于createdTimeMs时间） p.s.导唱为等待阶段
     */
    public Builder setIntroEndMs(Integer introEndMs) {
      this.introEndMs = introEndMs;
      return this;
    }

    /**
     * 演唱开始相对时间（相对于createdTimeMs时间）
     */
    public Builder setSingBeginMs(Integer singBeginMs) {
      this.singBeginMs = singBeginMs;
      return this;
    }

    /**
     * 演唱结束相对时间（相对于createdTimeMs时间）
     */
    public Builder setSingEndMs(Integer singEndMs) {
      this.singEndMs = singEndMs;
      return this;
    }

    /**
     * 轮次状态
     */
    public Builder setStatus(ERRoundStatus status) {
      this.status = status;
      return this;
    }

    /**
     * 切换轮次原因
     */
    public Builder setOverReason(ERRoundOverReason overReason) {
      this.overReason = overReason;
      return this;
    }

    /**
     * 本轮次的歌曲信息
     */
    public Builder setMusic(MusicInfo music) {
      this.music = music;
      return this;
    }

    @Override
    public RRoundInfo build() {
      return new RRoundInfo(userID, roundSeq, introBeginMs, introEndMs, singBeginMs, singEndMs, status, overReason, music, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RRoundInfo extends ProtoAdapter<RRoundInfo> {
    public ProtoAdapter_RRoundInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, RRoundInfo.class);
    }

    @Override
    public int encodedSize(RRoundInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.roundSeq)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.introBeginMs)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.introEndMs)
          + ProtoAdapter.UINT32.encodedSizeWithTag(5, value.singBeginMs)
          + ProtoAdapter.UINT32.encodedSizeWithTag(6, value.singEndMs)
          + ERRoundStatus.ADAPTER.encodedSizeWithTag(7, value.status)
          + ERRoundOverReason.ADAPTER.encodedSizeWithTag(8, value.overReason)
          + MusicInfo.ADAPTER.encodedSizeWithTag(9, value.music)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RRoundInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.roundSeq);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.introBeginMs);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.introEndMs);
      ProtoAdapter.UINT32.encodeWithTag(writer, 5, value.singBeginMs);
      ProtoAdapter.UINT32.encodeWithTag(writer, 6, value.singEndMs);
      ERRoundStatus.ADAPTER.encodeWithTag(writer, 7, value.status);
      ERRoundOverReason.ADAPTER.encodeWithTag(writer, 8, value.overReason);
      MusicInfo.ADAPTER.encodeWithTag(writer, 9, value.music);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RRoundInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setRoundSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setIntroBeginMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setIntroEndMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setSingBeginMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 6: builder.setSingEndMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 7: {
            try {
              builder.setStatus(ERRoundStatus.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 8: {
            try {
              builder.setOverReason(ERRoundOverReason.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 9: builder.setMusic(MusicInfo.ADAPTER.decode(reader)); break;
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
    public RRoundInfo redact(RRoundInfo value) {
      Builder builder = value.newBuilder();
      if (builder.music != null) builder.music = MusicInfo.ADAPTER.redact(builder.music);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
