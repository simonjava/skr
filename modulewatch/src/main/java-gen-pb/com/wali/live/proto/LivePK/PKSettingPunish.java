// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LivePk.proto
package com.wali.live.proto.LivePK;

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

public final class PKSettingPunish extends Message<PKSettingPunish, PKSettingPunish.Builder> {
  public static final ProtoAdapter<PKSettingPunish> ADAPTER = new ProtoAdapter_PKSettingPunish();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_ID = 0;

  public static final String DEFAULT_NAME = "";

  public static final Integer DEFAULT_TYPE = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer id;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String name;

  /**
   * 0:普通 1：自定义
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer type;

  public PKSettingPunish(Integer id, String name, Integer type) {
    this(id, name, type, ByteString.EMPTY);
  }

  public PKSettingPunish(Integer id, String name, Integer type, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.id = id;
    this.name = name;
    this.type = type;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.id = id;
    builder.name = name;
    builder.type = type;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PKSettingPunish)) return false;
    PKSettingPunish o = (PKSettingPunish) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(id, o.id)
        && Internal.equals(name, o.name)
        && Internal.equals(type, o.type);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (id != null ? id.hashCode() : 0);
      result = result * 37 + (name != null ? name.hashCode() : 0);
      result = result * 37 + (type != null ? type.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (id != null) builder.append(", id=").append(id);
    if (name != null) builder.append(", name=").append(name);
    if (type != null) builder.append(", type=").append(type);
    return builder.replace(0, 2, "PKSettingPunish{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PKSettingPunish.ADAPTER.encode(this);
  }

  public static final PKSettingPunish parseFrom(byte[] data) throws IOException {
    PKSettingPunish c = null;
       c = PKSettingPunish.ADAPTER.decode(data);
    return c;
  }

  public Integer getId() {
    if(id==null){
        return DEFAULT_ID;
    }
    return id;
  }

  public String getName() {
    if(name==null){
        return DEFAULT_NAME;
    }
    return name;
  }

  /**
   * 0:普通 1：自定义
   */
  public Integer getType() {
    if(type==null){
        return DEFAULT_TYPE;
    }
    return type;
  }

  public boolean hasId() {
    return id!=null;
  }

  public boolean hasName() {
    return name!=null;
  }

  /**
   * 0:普通 1：自定义
   */
  public boolean hasType() {
    return type!=null;
  }

  public static final class Builder extends Message.Builder<PKSettingPunish, Builder> {
    public Integer id;

    public String name;

    public Integer type;

    public Builder() {
    }

    public Builder setId(Integer id) {
      this.id = id;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    /**
     * 0:普通 1：自定义
     */
    public Builder setType(Integer type) {
      this.type = type;
      return this;
    }

    @Override
    public PKSettingPunish build() {
      return new PKSettingPunish(id, name, type, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PKSettingPunish extends ProtoAdapter<PKSettingPunish> {
    public ProtoAdapter_PKSettingPunish() {
      super(FieldEncoding.LENGTH_DELIMITED, PKSettingPunish.class);
    }

    @Override
    public int encodedSize(PKSettingPunish value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.id)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.name)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.type)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PKSettingPunish value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.id);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.name);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.type);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PKSettingPunish decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setId(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setName(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setType(ProtoAdapter.UINT32.decode(reader)); break;
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
    public PKSettingPunish redact(PKSettingPunish value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
