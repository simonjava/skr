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
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PBeginPunish extends Message<PBeginPunish, PBeginPunish.Builder> {
  public static final ProtoAdapter<PBeginPunish> ADAPTER = new ProtoAdapter_PBeginPunish();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_BEGINTIMEMS = 0L;

  public static final Long DEFAULT_ENDTIMEMS = 0L;

  /**
   * 惩罚信息
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.PartyRoom.PunishInfo#ADAPTER"
  )
  private final PunishInfo punishInfo;

  /**
   * 开始时间戳，绝对时间
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long beginTimeMs;

  /**
   * 结束时间戳，绝对时间
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long endTimeMs;

  public PBeginPunish(PunishInfo punishInfo, Long beginTimeMs, Long endTimeMs) {
    this(punishInfo, beginTimeMs, endTimeMs, ByteString.EMPTY);
  }

  public PBeginPunish(PunishInfo punishInfo, Long beginTimeMs, Long endTimeMs,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.punishInfo = punishInfo;
    this.beginTimeMs = beginTimeMs;
    this.endTimeMs = endTimeMs;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.punishInfo = punishInfo;
    builder.beginTimeMs = beginTimeMs;
    builder.endTimeMs = endTimeMs;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PBeginPunish)) return false;
    PBeginPunish o = (PBeginPunish) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(punishInfo, o.punishInfo)
        && Internal.equals(beginTimeMs, o.beginTimeMs)
        && Internal.equals(endTimeMs, o.endTimeMs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (punishInfo != null ? punishInfo.hashCode() : 0);
      result = result * 37 + (beginTimeMs != null ? beginTimeMs.hashCode() : 0);
      result = result * 37 + (endTimeMs != null ? endTimeMs.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (punishInfo != null) builder.append(", punishInfo=").append(punishInfo);
    if (beginTimeMs != null) builder.append(", beginTimeMs=").append(beginTimeMs);
    if (endTimeMs != null) builder.append(", endTimeMs=").append(endTimeMs);
    return builder.replace(0, 2, "PBeginPunish{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PBeginPunish.ADAPTER.encode(this);
  }

  public static final PBeginPunish parseFrom(byte[] data) throws IOException {
    PBeginPunish c = null;
       c = PBeginPunish.ADAPTER.decode(data);
    return c;
  }

  /**
   * 惩罚信息
   */
  public PunishInfo getPunishInfo() {
    if(punishInfo==null){
        return new PunishInfo.Builder().build();
    }
    return punishInfo;
  }

  /**
   * 开始时间戳，绝对时间
   */
  public Long getBeginTimeMs() {
    if(beginTimeMs==null){
        return DEFAULT_BEGINTIMEMS;
    }
    return beginTimeMs;
  }

  /**
   * 结束时间戳，绝对时间
   */
  public Long getEndTimeMs() {
    if(endTimeMs==null){
        return DEFAULT_ENDTIMEMS;
    }
    return endTimeMs;
  }

  /**
   * 惩罚信息
   */
  public boolean hasPunishInfo() {
    return punishInfo!=null;
  }

  /**
   * 开始时间戳，绝对时间
   */
  public boolean hasBeginTimeMs() {
    return beginTimeMs!=null;
  }

  /**
   * 结束时间戳，绝对时间
   */
  public boolean hasEndTimeMs() {
    return endTimeMs!=null;
  }

  public static final class Builder extends Message.Builder<PBeginPunish, Builder> {
    private PunishInfo punishInfo;

    private Long beginTimeMs;

    private Long endTimeMs;

    public Builder() {
    }

    /**
     * 惩罚信息
     */
    public Builder setPunishInfo(PunishInfo punishInfo) {
      this.punishInfo = punishInfo;
      return this;
    }

    /**
     * 开始时间戳，绝对时间
     */
    public Builder setBeginTimeMs(Long beginTimeMs) {
      this.beginTimeMs = beginTimeMs;
      return this;
    }

    /**
     * 结束时间戳，绝对时间
     */
    public Builder setEndTimeMs(Long endTimeMs) {
      this.endTimeMs = endTimeMs;
      return this;
    }

    @Override
    public PBeginPunish build() {
      return new PBeginPunish(punishInfo, beginTimeMs, endTimeMs, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PBeginPunish extends ProtoAdapter<PBeginPunish> {
    public ProtoAdapter_PBeginPunish() {
      super(FieldEncoding.LENGTH_DELIMITED, PBeginPunish.class);
    }

    @Override
    public int encodedSize(PBeginPunish value) {
      return PunishInfo.ADAPTER.encodedSizeWithTag(1, value.punishInfo)
          + ProtoAdapter.SINT64.encodedSizeWithTag(2, value.beginTimeMs)
          + ProtoAdapter.SINT64.encodedSizeWithTag(3, value.endTimeMs)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PBeginPunish value) throws IOException {
      PunishInfo.ADAPTER.encodeWithTag(writer, 1, value.punishInfo);
      ProtoAdapter.SINT64.encodeWithTag(writer, 2, value.beginTimeMs);
      ProtoAdapter.SINT64.encodeWithTag(writer, 3, value.endTimeMs);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PBeginPunish decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setPunishInfo(PunishInfo.ADAPTER.decode(reader)); break;
          case 2: builder.setBeginTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
          case 3: builder.setEndTimeMs(ProtoAdapter.SINT64.decode(reader)); break;
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
    public PBeginPunish redact(PBeginPunish value) {
      Builder builder = value.newBuilder();
      if (builder.punishInfo != null) builder.punishInfo = PunishInfo.ADAPTER.redact(builder.punishInfo);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
