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
import com.zq.live.proto.Common.POnlineInfo;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PVoteInfo extends Message<PVoteInfo, PVoteInfo.Builder> {
  public static final ProtoAdapter<PVoteInfo> ADAPTER = new ProtoAdapter_PVoteInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_VOTECNT = 0;

  /**
   * 投票数
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer voteCnt;

  /**
   * 用户信息
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Common.POnlineInfo#ADAPTER"
  )
  private final POnlineInfo user;

  public PVoteInfo(Integer voteCnt, POnlineInfo user) {
    this(voteCnt, user, ByteString.EMPTY);
  }

  public PVoteInfo(Integer voteCnt, POnlineInfo user, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.voteCnt = voteCnt;
    this.user = user;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.voteCnt = voteCnt;
    builder.user = user;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PVoteInfo)) return false;
    PVoteInfo o = (PVoteInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(voteCnt, o.voteCnt)
        && Internal.equals(user, o.user);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (voteCnt != null ? voteCnt.hashCode() : 0);
      result = result * 37 + (user != null ? user.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (voteCnt != null) builder.append(", voteCnt=").append(voteCnt);
    if (user != null) builder.append(", user=").append(user);
    return builder.replace(0, 2, "PVoteInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PVoteInfo.ADAPTER.encode(this);
  }

  public static final PVoteInfo parseFrom(byte[] data) throws IOException {
    PVoteInfo c = null;
       c = PVoteInfo.ADAPTER.decode(data);
    return c;
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
   * 用户信息
   */
  public POnlineInfo getUser() {
    if(user==null){
        return new POnlineInfo.Builder().build();
    }
    return user;
  }

  /**
   * 投票数
   */
  public boolean hasVoteCnt() {
    return voteCnt!=null;
  }

  /**
   * 用户信息
   */
  public boolean hasUser() {
    return user!=null;
  }

  public static final class Builder extends Message.Builder<PVoteInfo, Builder> {
    private Integer voteCnt;

    private POnlineInfo user;

    public Builder() {
    }

    /**
     * 投票数
     */
    public Builder setVoteCnt(Integer voteCnt) {
      this.voteCnt = voteCnt;
      return this;
    }

    /**
     * 用户信息
     */
    public Builder setUser(POnlineInfo user) {
      this.user = user;
      return this;
    }

    @Override
    public PVoteInfo build() {
      return new PVoteInfo(voteCnt, user, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PVoteInfo extends ProtoAdapter<PVoteInfo> {
    public ProtoAdapter_PVoteInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, PVoteInfo.class);
    }

    @Override
    public int encodedSize(PVoteInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.voteCnt)
          + POnlineInfo.ADAPTER.encodedSizeWithTag(2, value.user)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PVoteInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.voteCnt);
      POnlineInfo.ADAPTER.encodeWithTag(writer, 2, value.user);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PVoteInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setVoteCnt(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setUser(POnlineInfo.ADAPTER.decode(reader)); break;
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
    public PVoteInfo redact(PVoteInfo value) {
      Builder builder = value.newBuilder();
      if (builder.user != null) builder.user = POnlineInfo.ADAPTER.redact(builder.user);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
