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
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PResponseVote extends Message<PResponseVote, PResponseVote.Builder> {
  public static final ProtoAdapter<PResponseVote> ADAPTER = new ProtoAdapter_PResponseVote();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_VOTETAG = "";

  public static final Integer DEFAULT_USERID = 0;

  public static final Integer DEFAULT_VOTECNT = 0;

  /**
   * 投票标识
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String voteTag;

  /**
   * 用户标识
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * 投票数
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer voteCnt;

  public PResponseVote(String voteTag, Integer userID, Integer voteCnt) {
    this(voteTag, userID, voteCnt, ByteString.EMPTY);
  }

  public PResponseVote(String voteTag, Integer userID, Integer voteCnt, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.voteTag = voteTag;
    this.userID = userID;
    this.voteCnt = voteCnt;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.voteTag = voteTag;
    builder.userID = userID;
    builder.voteCnt = voteCnt;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PResponseVote)) return false;
    PResponseVote o = (PResponseVote) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(voteTag, o.voteTag)
        && Internal.equals(userID, o.userID)
        && Internal.equals(voteCnt, o.voteCnt);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (voteTag != null ? voteTag.hashCode() : 0);
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (voteCnt != null ? voteCnt.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (voteTag != null) builder.append(", voteTag=").append(voteTag);
    if (userID != null) builder.append(", userID=").append(userID);
    if (voteCnt != null) builder.append(", voteCnt=").append(voteCnt);
    return builder.replace(0, 2, "PResponseVote{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PResponseVote.ADAPTER.encode(this);
  }

  public static final PResponseVote parseFrom(byte[] data) throws IOException {
    PResponseVote c = null;
       c = PResponseVote.ADAPTER.decode(data);
    return c;
  }

  /**
   * 投票标识
   */
  public String getVoteTag() {
    if(voteTag==null){
        return DEFAULT_VOTETAG;
    }
    return voteTag;
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
   * 投票数
   */
  public Integer getVoteCnt() {
    if(voteCnt==null){
        return DEFAULT_VOTECNT;
    }
    return voteCnt;
  }

  /**
   * 投票标识
   */
  public boolean hasVoteTag() {
    return voteTag!=null;
  }

  /**
   * 用户标识
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 投票数
   */
  public boolean hasVoteCnt() {
    return voteCnt!=null;
  }

  public static final class Builder extends Message.Builder<PResponseVote, Builder> {
    private String voteTag;

    private Integer userID;

    private Integer voteCnt;

    public Builder() {
    }

    /**
     * 投票标识
     */
    public Builder setVoteTag(String voteTag) {
      this.voteTag = voteTag;
      return this;
    }

    /**
     * 用户标识
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    /**
     * 投票数
     */
    public Builder setVoteCnt(Integer voteCnt) {
      this.voteCnt = voteCnt;
      return this;
    }

    @Override
    public PResponseVote build() {
      return new PResponseVote(voteTag, userID, voteCnt, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PResponseVote extends ProtoAdapter<PResponseVote> {
    public ProtoAdapter_PResponseVote() {
      super(FieldEncoding.LENGTH_DELIMITED, PResponseVote.class);
    }

    @Override
    public int encodedSize(PResponseVote value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.voteTag)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.userID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.voteCnt)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PResponseVote value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.voteTag);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.userID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.voteCnt);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PResponseVote decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setVoteTag(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setVoteCnt(ProtoAdapter.UINT32.decode(reader)); break;
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
    public PResponseVote redact(PResponseVote value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
