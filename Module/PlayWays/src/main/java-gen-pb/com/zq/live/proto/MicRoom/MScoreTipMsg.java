// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: mic_room.proto
package com.zq.live.proto.MicRoom;

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

public final class MScoreTipMsg extends Message<MScoreTipMsg, MScoreTipMsg.Builder> {
  public static final ProtoAdapter<MScoreTipMsg> ADAPTER = new ProtoAdapter_MScoreTipMsg();

  private static final long serialVersionUID = 0L;

  public static final MScoreTipType DEFAULT_TIPTYPE = MScoreTipType.MST_UNKNOWN;

  public static final String DEFAULT_TIPDESC = "";

  public static final Integer DEFAULT_FROMSCORE = 0;

  public static final Integer DEFAULT_TOSCORE = 0;

  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.MicRoom.MScoreTipType#ADAPTER"
  )
  private final MScoreTipType tipType;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String tipDesc;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer fromScore;

  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer toScore;

  public MScoreTipMsg(MScoreTipType tipType, String tipDesc, Integer fromScore, Integer toScore) {
    this(tipType, tipDesc, fromScore, toScore, ByteString.EMPTY);
  }

  public MScoreTipMsg(MScoreTipType tipType, String tipDesc, Integer fromScore, Integer toScore,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.tipType = tipType;
    this.tipDesc = tipDesc;
    this.fromScore = fromScore;
    this.toScore = toScore;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.tipType = tipType;
    builder.tipDesc = tipDesc;
    builder.fromScore = fromScore;
    builder.toScore = toScore;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MScoreTipMsg)) return false;
    MScoreTipMsg o = (MScoreTipMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(tipType, o.tipType)
        && Internal.equals(tipDesc, o.tipDesc)
        && Internal.equals(fromScore, o.fromScore)
        && Internal.equals(toScore, o.toScore);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (tipType != null ? tipType.hashCode() : 0);
      result = result * 37 + (tipDesc != null ? tipDesc.hashCode() : 0);
      result = result * 37 + (fromScore != null ? fromScore.hashCode() : 0);
      result = result * 37 + (toScore != null ? toScore.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (tipType != null) builder.append(", tipType=").append(tipType);
    if (tipDesc != null) builder.append(", tipDesc=").append(tipDesc);
    if (fromScore != null) builder.append(", fromScore=").append(fromScore);
    if (toScore != null) builder.append(", toScore=").append(toScore);
    return builder.replace(0, 2, "MScoreTipMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MScoreTipMsg.ADAPTER.encode(this);
  }

  public static final MScoreTipMsg parseFrom(byte[] data) throws IOException {
    MScoreTipMsg c = null;
       c = MScoreTipMsg.ADAPTER.decode(data);
    return c;
  }

  public MScoreTipType getTipType() {
    if(tipType==null){
        return new MScoreTipType.Builder().build();
    }
    return tipType;
  }

  public String getTipDesc() {
    if(tipDesc==null){
        return DEFAULT_TIPDESC;
    }
    return tipDesc;
  }

  public Integer getFromScore() {
    if(fromScore==null){
        return DEFAULT_FROMSCORE;
    }
    return fromScore;
  }

  public Integer getToScore() {
    if(toScore==null){
        return DEFAULT_TOSCORE;
    }
    return toScore;
  }

  public boolean hasTipType() {
    return tipType!=null;
  }

  public boolean hasTipDesc() {
    return tipDesc!=null;
  }

  public boolean hasFromScore() {
    return fromScore!=null;
  }

  public boolean hasToScore() {
    return toScore!=null;
  }

  public static final class Builder extends Message.Builder<MScoreTipMsg, Builder> {
    private MScoreTipType tipType;

    private String tipDesc;

    private Integer fromScore;

    private Integer toScore;

    public Builder() {
    }

    public Builder setTipType(MScoreTipType tipType) {
      this.tipType = tipType;
      return this;
    }

    public Builder setTipDesc(String tipDesc) {
      this.tipDesc = tipDesc;
      return this;
    }

    public Builder setFromScore(Integer fromScore) {
      this.fromScore = fromScore;
      return this;
    }

    public Builder setToScore(Integer toScore) {
      this.toScore = toScore;
      return this;
    }

    @Override
    public MScoreTipMsg build() {
      return new MScoreTipMsg(tipType, tipDesc, fromScore, toScore, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MScoreTipMsg extends ProtoAdapter<MScoreTipMsg> {
    public ProtoAdapter_MScoreTipMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, MScoreTipMsg.class);
    }

    @Override
    public int encodedSize(MScoreTipMsg value) {
      return MScoreTipType.ADAPTER.encodedSizeWithTag(1, value.tipType)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.tipDesc)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.fromScore)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.toScore)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MScoreTipMsg value) throws IOException {
      MScoreTipType.ADAPTER.encodeWithTag(writer, 1, value.tipType);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.tipDesc);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.fromScore);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.toScore);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MScoreTipMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: {
            try {
              builder.setTipType(MScoreTipType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 2: builder.setTipDesc(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setFromScore(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setToScore(ProtoAdapter.UINT32.decode(reader)); break;
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
    public MScoreTipMsg redact(MScoreTipMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
