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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

/**
 * 投票结果
 */
public final class VoteResultMsg extends Message<VoteResultMsg, VoteResultMsg.Builder> {
  public static final ProtoAdapter<VoteResultMsg> ADAPTER = new ProtoAdapter_VoteResultMsg();

  private static final long serialVersionUID = 0L;

  /**
   * 投票打分信息
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.Room.VoteInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<VoteInfo> voteInfo;

  /**
   * 所有参与者的评分结果，应该使用这个结构
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Room.UserScoreResult#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<UserScoreResult> scoreResults;

  public VoteResultMsg(List<VoteInfo> voteInfo, List<UserScoreResult> scoreResults) {
    this(voteInfo, scoreResults, ByteString.EMPTY);
  }

  public VoteResultMsg(List<VoteInfo> voteInfo, List<UserScoreResult> scoreResults,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.voteInfo = Internal.immutableCopyOf("voteInfo", voteInfo);
    this.scoreResults = Internal.immutableCopyOf("scoreResults", scoreResults);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.voteInfo = Internal.copyOf("voteInfo", voteInfo);
    builder.scoreResults = Internal.copyOf("scoreResults", scoreResults);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof VoteResultMsg)) return false;
    VoteResultMsg o = (VoteResultMsg) other;
    return unknownFields().equals(o.unknownFields())
        && voteInfo.equals(o.voteInfo)
        && scoreResults.equals(o.scoreResults);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + voteInfo.hashCode();
      result = result * 37 + scoreResults.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (!voteInfo.isEmpty()) builder.append(", voteInfo=").append(voteInfo);
    if (!scoreResults.isEmpty()) builder.append(", scoreResults=").append(scoreResults);
    return builder.replace(0, 2, "VoteResultMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return VoteResultMsg.ADAPTER.encode(this);
  }

  public static final VoteResultMsg parseFrom(byte[] data) throws IOException {
    VoteResultMsg c = null;
       c = VoteResultMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 投票打分信息
   */
  public List<VoteInfo> getVoteInfoList() {
    if(voteInfo==null){
        return new java.util.ArrayList<VoteInfo>();
    }
    return voteInfo;
  }

  /**
   * 所有参与者的评分结果，应该使用这个结构
   */
  public List<UserScoreResult> getScoreResultsList() {
    if(scoreResults==null){
        return new java.util.ArrayList<UserScoreResult>();
    }
    return scoreResults;
  }

  /**
   * 投票打分信息
   */
  public boolean hasVoteInfoList() {
    return voteInfo!=null;
  }

  /**
   * 所有参与者的评分结果，应该使用这个结构
   */
  public boolean hasScoreResultsList() {
    return scoreResults!=null;
  }

  public static final class Builder extends Message.Builder<VoteResultMsg, Builder> {
    public List<VoteInfo> voteInfo;

    public List<UserScoreResult> scoreResults;

    public Builder() {
      voteInfo = Internal.newMutableList();
      scoreResults = Internal.newMutableList();
    }

    /**
     * 投票打分信息
     */
    public Builder addAllVoteInfo(List<VoteInfo> voteInfo) {
      Internal.checkElementsNotNull(voteInfo);
      this.voteInfo = voteInfo;
      return this;
    }

    /**
     * 所有参与者的评分结果，应该使用这个结构
     */
    public Builder addAllScoreResults(List<UserScoreResult> scoreResults) {
      Internal.checkElementsNotNull(scoreResults);
      this.scoreResults = scoreResults;
      return this;
    }

    @Override
    public VoteResultMsg build() {
      return new VoteResultMsg(voteInfo, scoreResults, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_VoteResultMsg extends ProtoAdapter<VoteResultMsg> {
    public ProtoAdapter_VoteResultMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, VoteResultMsg.class);
    }

    @Override
    public int encodedSize(VoteResultMsg value) {
      return VoteInfo.ADAPTER.asRepeated().encodedSizeWithTag(1, value.voteInfo)
          + UserScoreResult.ADAPTER.asRepeated().encodedSizeWithTag(2, value.scoreResults)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, VoteResultMsg value) throws IOException {
      VoteInfo.ADAPTER.asRepeated().encodeWithTag(writer, 1, value.voteInfo);
      UserScoreResult.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.scoreResults);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public VoteResultMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.voteInfo.add(VoteInfo.ADAPTER.decode(reader)); break;
          case 2: builder.scoreResults.add(UserScoreResult.ADAPTER.decode(reader)); break;
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
    public VoteResultMsg redact(VoteResultMsg value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.voteInfo, VoteInfo.ADAPTER);
      Internal.redactElements(builder.scoreResults, UserScoreResult.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
