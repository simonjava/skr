// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: CommonChannel.proto
package com.wali.live.proto.CommonChannel;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * 连麦
 */
public final class MicBrief extends Message<MicBrief, MicBrief.Builder> {
  public static final ProtoAdapter<MicBrief> ADAPTER = new ProtoAdapter_MicBrief();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_MIC_UID = 0L;

  public static final Integer DEFAULT_MIC_TYPE = 0;

  /**
   * 连麦的观众id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long mic_uid;

  /**
   * 0:主播与观众连麦 1：主播与主播连麦
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer mic_type;

  public MicBrief(Long mic_uid, Integer mic_type) {
    this(mic_uid, mic_type, ByteString.EMPTY);
  }

  public MicBrief(Long mic_uid, Integer mic_type, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.mic_uid = mic_uid;
    this.mic_type = mic_type;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.mic_uid = mic_uid;
    builder.mic_type = mic_type;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MicBrief)) return false;
    MicBrief o = (MicBrief) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(mic_uid, o.mic_uid)
        && Internal.equals(mic_type, o.mic_type);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (mic_uid != null ? mic_uid.hashCode() : 0);
      result = result * 37 + (mic_type != null ? mic_type.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (mic_uid != null) builder.append(", mic_uid=").append(mic_uid);
    if (mic_type != null) builder.append(", mic_type=").append(mic_type);
    return builder.replace(0, 2, "MicBrief{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MicBrief.ADAPTER.encode(this);
  }

  public static final MicBrief parseFrom(byte[] data) throws IOException {
    MicBrief c = null;
       c = MicBrief.ADAPTER.decode(data);
    return c;
  }

  /**
   * 连麦的观众id
   */
  public Long getMicUid() {
    if(mic_uid==null){
        return DEFAULT_MIC_UID;
    }
    return mic_uid;
  }

  /**
   * 0:主播与观众连麦 1：主播与主播连麦
   */
  public Integer getMicType() {
    if(mic_type==null){
        return DEFAULT_MIC_TYPE;
    }
    return mic_type;
  }

  /**
   * 连麦的观众id
   */
  public boolean hasMicUid() {
    return mic_uid!=null;
  }

  /**
   * 0:主播与观众连麦 1：主播与主播连麦
   */
  public boolean hasMicType() {
    return mic_type!=null;
  }

  public static final class Builder extends Message.Builder<MicBrief, Builder> {
    public Long mic_uid;

    public Integer mic_type;

    public Builder() {
    }

    /**
     * 连麦的观众id
     */
    public Builder setMicUid(Long mic_uid) {
      this.mic_uid = mic_uid;
      return this;
    }

    /**
     * 0:主播与观众连麦 1：主播与主播连麦
     */
    public Builder setMicType(Integer mic_type) {
      this.mic_type = mic_type;
      return this;
    }

    @Override
    public MicBrief build() {
      return new MicBrief(mic_uid, mic_type, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MicBrief extends ProtoAdapter<MicBrief> {
    public ProtoAdapter_MicBrief() {
      super(FieldEncoding.LENGTH_DELIMITED, MicBrief.class);
    }

    @Override
    public int encodedSize(MicBrief value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.mic_uid)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.mic_type)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MicBrief value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.mic_uid);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.mic_type);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MicBrief decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setMicUid(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setMicType(ProtoAdapter.UINT32.decode(reader)); break;
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
    public MicBrief redact(MicBrief value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
