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
import com.zq.live.proto.Common.CdnStreamURL;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

/**
 * sprint35
 */
public final class PSetCDNStream extends Message<PSetCDNStream, PSetCDNStream.Builder> {
  public static final ProtoAdapter<PSetCDNStream> ADAPTER = new ProtoAdapter_PSetCDNStream();

  private static final long serialVersionUID = 0L;

  public static final Boolean DEFAULT_ENABLE = false;

  /**
   * 是否开启,true:开始， false:关闭
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  private final Boolean enable;

  /**
   * 开启后的cdn拉流地址, 关闭后此字段为空
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Common.CdnStreamURL#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<CdnStreamURL> pulCdnURLs;

  public PSetCDNStream(Boolean enable, List<CdnStreamURL> pulCdnURLs) {
    this(enable, pulCdnURLs, ByteString.EMPTY);
  }

  public PSetCDNStream(Boolean enable, List<CdnStreamURL> pulCdnURLs, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.enable = enable;
    this.pulCdnURLs = Internal.immutableCopyOf("pulCdnURLs", pulCdnURLs);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.enable = enable;
    builder.pulCdnURLs = Internal.copyOf("pulCdnURLs", pulCdnURLs);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PSetCDNStream)) return false;
    PSetCDNStream o = (PSetCDNStream) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(enable, o.enable)
        && pulCdnURLs.equals(o.pulCdnURLs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (enable != null ? enable.hashCode() : 0);
      result = result * 37 + pulCdnURLs.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (enable != null) builder.append(", enable=").append(enable);
    if (!pulCdnURLs.isEmpty()) builder.append(", pulCdnURLs=").append(pulCdnURLs);
    return builder.replace(0, 2, "PSetCDNStream{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PSetCDNStream.ADAPTER.encode(this);
  }

  public static final PSetCDNStream parseFrom(byte[] data) throws IOException {
    PSetCDNStream c = null;
       c = PSetCDNStream.ADAPTER.decode(data);
    return c;
  }

  /**
   * 是否开启,true:开始， false:关闭
   */
  public Boolean getEnable() {
    if(enable==null){
        return DEFAULT_ENABLE;
    }
    return enable;
  }

  /**
   * 开启后的cdn拉流地址, 关闭后此字段为空
   */
  public List<CdnStreamURL> getPulCdnURLsList() {
    if(pulCdnURLs==null){
        return new java.util.ArrayList<CdnStreamURL>();
    }
    return pulCdnURLs;
  }

  /**
   * 是否开启,true:开始， false:关闭
   */
  public boolean hasEnable() {
    return enable!=null;
  }

  /**
   * 开启后的cdn拉流地址, 关闭后此字段为空
   */
  public boolean hasPulCdnURLsList() {
    return pulCdnURLs!=null;
  }

  public static final class Builder extends Message.Builder<PSetCDNStream, Builder> {
    private Boolean enable;

    private List<CdnStreamURL> pulCdnURLs;

    public Builder() {
      pulCdnURLs = Internal.newMutableList();
    }

    /**
     * 是否开启,true:开始， false:关闭
     */
    public Builder setEnable(Boolean enable) {
      this.enable = enable;
      return this;
    }

    /**
     * 开启后的cdn拉流地址, 关闭后此字段为空
     */
    public Builder addAllPulCdnURLs(List<CdnStreamURL> pulCdnURLs) {
      Internal.checkElementsNotNull(pulCdnURLs);
      this.pulCdnURLs = pulCdnURLs;
      return this;
    }

    @Override
    public PSetCDNStream build() {
      return new PSetCDNStream(enable, pulCdnURLs, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PSetCDNStream extends ProtoAdapter<PSetCDNStream> {
    public ProtoAdapter_PSetCDNStream() {
      super(FieldEncoding.LENGTH_DELIMITED, PSetCDNStream.class);
    }

    @Override
    public int encodedSize(PSetCDNStream value) {
      return ProtoAdapter.BOOL.encodedSizeWithTag(1, value.enable)
          + CdnStreamURL.ADAPTER.asRepeated().encodedSizeWithTag(2, value.pulCdnURLs)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PSetCDNStream value) throws IOException {
      ProtoAdapter.BOOL.encodeWithTag(writer, 1, value.enable);
      CdnStreamURL.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.pulCdnURLs);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PSetCDNStream decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setEnable(ProtoAdapter.BOOL.decode(reader)); break;
          case 2: builder.pulCdnURLs.add(CdnStreamURL.ADAPTER.decode(reader)); break;
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
    public PSetCDNStream redact(PSetCDNStream value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.pulCdnURLs, CdnStreamURL.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
