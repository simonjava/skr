// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: BigTurnTable.proto
package com.wali.live.proto.BigTurnTable;

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

/**
 * zhibo.turntable.start
 */
public final class StartTurntableReq extends Message<StartTurntableReq, StartTurntableReq.Builder> {
  public static final ProtoAdapter<StartTurntableReq> ADAPTER = new ProtoAdapter_StartTurntableReq();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_ZUID = 0L;

  public static final String DEFAULT_LIVEID = "";

  public static final TurntableType DEFAULT_TYPE = TurntableType.TYPE_128;

  public static final String DEFAULT_CUSTOMPRIZENAME = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64",
      label = WireField.Label.REQUIRED
  )
  public final Long zuid;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String liveId;

  /**
   * 转盘类型 0:128钻，1:500钻
   */
  @WireField(
      tag = 3,
      adapter = "com.wali.live.proto.BigTurnTable.TurntableType#ADAPTER"
  )
  public final TurntableType type;

  /**
   * 自定义奖项名字
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String customPrizeName;

  public StartTurntableReq(Long zuid, String liveId, TurntableType type, String customPrizeName) {
    this(zuid, liveId, type, customPrizeName, ByteString.EMPTY);
  }

  public StartTurntableReq(Long zuid, String liveId, TurntableType type, String customPrizeName,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.zuid = zuid;
    this.liveId = liveId;
    this.type = type;
    this.customPrizeName = customPrizeName;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.zuid = zuid;
    builder.liveId = liveId;
    builder.type = type;
    builder.customPrizeName = customPrizeName;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof StartTurntableReq)) return false;
    StartTurntableReq o = (StartTurntableReq) other;
    return unknownFields().equals(o.unknownFields())
        && zuid.equals(o.zuid)
        && Internal.equals(liveId, o.liveId)
        && Internal.equals(type, o.type)
        && Internal.equals(customPrizeName, o.customPrizeName);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + zuid.hashCode();
      result = result * 37 + (liveId != null ? liveId.hashCode() : 0);
      result = result * 37 + (type != null ? type.hashCode() : 0);
      result = result * 37 + (customPrizeName != null ? customPrizeName.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", zuid=").append(zuid);
    if (liveId != null) builder.append(", liveId=").append(liveId);
    if (type != null) builder.append(", type=").append(type);
    if (customPrizeName != null) builder.append(", customPrizeName=").append(customPrizeName);
    return builder.replace(0, 2, "StartTurntableReq{").append('}').toString();
  }

  public byte[] toByteArray() {
    return StartTurntableReq.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<StartTurntableReq, Builder> {
    public Long zuid;

    public String liveId;

    public TurntableType type;

    public String customPrizeName;

    public Builder() {
    }

    public Builder setZuid(Long zuid) {
      this.zuid = zuid;
      return this;
    }

    public Builder setLiveId(String liveId) {
      this.liveId = liveId;
      return this;
    }

    /**
     * 转盘类型 0:128钻，1:500钻
     */
    public Builder setType(TurntableType type) {
      this.type = type;
      return this;
    }

    /**
     * 自定义奖项名字
     */
    public Builder setCustomPrizeName(String customPrizeName) {
      this.customPrizeName = customPrizeName;
      return this;
    }

    @Override
    public StartTurntableReq build() {
      if (zuid == null) {
        throw Internal.missingRequiredFields(zuid, "zuid");
      }
      return new StartTurntableReq(zuid, liveId, type, customPrizeName, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_StartTurntableReq extends ProtoAdapter<StartTurntableReq> {
    public ProtoAdapter_StartTurntableReq() {
      super(FieldEncoding.LENGTH_DELIMITED, StartTurntableReq.class);
    }

    @Override
    public int encodedSize(StartTurntableReq value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.zuid)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.liveId)
          + TurntableType.ADAPTER.encodedSizeWithTag(3, value.type)
          + ProtoAdapter.STRING.encodedSizeWithTag(4, value.customPrizeName)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, StartTurntableReq value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.zuid);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.liveId);
      TurntableType.ADAPTER.encodeWithTag(writer, 3, value.type);
      ProtoAdapter.STRING.encodeWithTag(writer, 4, value.customPrizeName);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public StartTurntableReq decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setZuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setLiveId(ProtoAdapter.STRING.decode(reader)); break;
          case 3: {
            try {
              builder.setType(TurntableType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 4: builder.setCustomPrizeName(ProtoAdapter.STRING.decode(reader)); break;
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
    public StartTurntableReq redact(StartTurntableReq value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
