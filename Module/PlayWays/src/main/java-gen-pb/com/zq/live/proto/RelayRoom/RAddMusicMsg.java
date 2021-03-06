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
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class RAddMusicMsg extends Message<RAddMusicMsg, RAddMusicMsg.Builder> {
  public static final ProtoAdapter<RAddMusicMsg> ADAPTER = new ProtoAdapter_RAddMusicMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_MUSICCNT = 0;

  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.RelayRoom.RUserMusicDetail#ADAPTER"
  )
  private final RUserMusicDetail detail;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer musicCnt;

  public RAddMusicMsg(RUserMusicDetail detail, Integer musicCnt) {
    this(detail, musicCnt, ByteString.EMPTY);
  }

  public RAddMusicMsg(RUserMusicDetail detail, Integer musicCnt, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.detail = detail;
    this.musicCnt = musicCnt;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.detail = detail;
    builder.musicCnt = musicCnt;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RAddMusicMsg)) return false;
    RAddMusicMsg o = (RAddMusicMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(detail, o.detail)
        && Internal.equals(musicCnt, o.musicCnt);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (detail != null ? detail.hashCode() : 0);
      result = result * 37 + (musicCnt != null ? musicCnt.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (detail != null) builder.append(", detail=").append(detail);
    if (musicCnt != null) builder.append(", musicCnt=").append(musicCnt);
    return builder.replace(0, 2, "RAddMusicMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RAddMusicMsg.ADAPTER.encode(this);
  }

  public static final RAddMusicMsg parseFrom(byte[] data) throws IOException {
    RAddMusicMsg c = null;
       c = RAddMusicMsg.ADAPTER.decode(data);
    return c;
  }

  public RUserMusicDetail getDetail() {
    if(detail==null){
        return new RUserMusicDetail.Builder().build();
    }
    return detail;
  }

  public Integer getMusicCnt() {
    if(musicCnt==null){
        return DEFAULT_MUSICCNT;
    }
    return musicCnt;
  }

  public boolean hasDetail() {
    return detail!=null;
  }

  public boolean hasMusicCnt() {
    return musicCnt!=null;
  }

  public static final class Builder extends Message.Builder<RAddMusicMsg, Builder> {
    private RUserMusicDetail detail;

    private Integer musicCnt;

    public Builder() {
    }

    public Builder setDetail(RUserMusicDetail detail) {
      this.detail = detail;
      return this;
    }

    public Builder setMusicCnt(Integer musicCnt) {
      this.musicCnt = musicCnt;
      return this;
    }

    @Override
    public RAddMusicMsg build() {
      return new RAddMusicMsg(detail, musicCnt, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RAddMusicMsg extends ProtoAdapter<RAddMusicMsg> {
    public ProtoAdapter_RAddMusicMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, RAddMusicMsg.class);
    }

    @Override
    public int encodedSize(RAddMusicMsg value) {
      return RUserMusicDetail.ADAPTER.encodedSizeWithTag(1, value.detail)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.musicCnt)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RAddMusicMsg value) throws IOException {
      RUserMusicDetail.ADAPTER.encodeWithTag(writer, 1, value.detail);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.musicCnt);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RAddMusicMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setDetail(RUserMusicDetail.ADAPTER.decode(reader)); break;
          case 2: builder.setMusicCnt(ProtoAdapter.UINT32.decode(reader)); break;
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
    public RAddMusicMsg redact(RAddMusicMsg value) {
      Builder builder = value.newBuilder();
      if (builder.detail != null) builder.detail = RUserMusicDetail.ADAPTER.redact(builder.detail);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
