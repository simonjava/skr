// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: HotChannel.proto
package com.wali.live.proto.HotChannel;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.wali.live.proto.CommonChannel.ChannelItem;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

public final class GetRecListRsp extends Message<GetRecListRsp, GetRecListRsp.Builder> {
  public static final ProtoAdapter<GetRecListRsp> ADAPTER = new ProtoAdapter_GetRecListRsp();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_RETCODE = 0;

  public static final Integer DEFAULT_CHANNELID = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer retCode;

  @WireField(
      tag = 2,
      adapter = "com.wali.live.proto.CommonChannel.ChannelItem#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<ChannelItem> items;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer channelId;

  public GetRecListRsp(Integer retCode, List<ChannelItem> items, Integer channelId) {
    this(retCode, items, channelId, ByteString.EMPTY);
  }

  public GetRecListRsp(Integer retCode, List<ChannelItem> items, Integer channelId,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.retCode = retCode;
    this.items = Internal.immutableCopyOf("items", items);
    this.channelId = channelId;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.retCode = retCode;
    builder.items = Internal.copyOf("items", items);
    builder.channelId = channelId;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetRecListRsp)) return false;
    GetRecListRsp o = (GetRecListRsp) other;
    return unknownFields().equals(o.unknownFields())
        && retCode.equals(o.retCode)
        && items.equals(o.items)
        && Internal.equals(channelId, o.channelId);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + retCode.hashCode();
      result = result * 37 + items.hashCode();
      result = result * 37 + (channelId != null ? channelId.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", retCode=").append(retCode);
    if (!items.isEmpty()) builder.append(", items=").append(items);
    if (channelId != null) builder.append(", channelId=").append(channelId);
    return builder.replace(0, 2, "GetRecListRsp{").append('}').toString();
  }

  public static final GetRecListRsp parseFrom(byte[] data) throws IOException {
    GetRecListRsp c = null;
       c = GetRecListRsp.ADAPTER.decode(data);
    return c;
  }

  public Integer getRetCode() {
    if(retCode==null){
        return DEFAULT_RETCODE;
    }
    return retCode;
  }

  public List<ChannelItem> getItemsList() {
    if(items==null){
        return new java.util.ArrayList<ChannelItem>();
    }
    return items;
  }

  public Integer getChannelId() {
    if(channelId==null){
        return DEFAULT_CHANNELID;
    }
    return channelId;
  }

  public boolean hasRetCode() {
    return retCode!=null;
  }

  public boolean hasItemsList() {
    return items!=null;
  }

  public boolean hasChannelId() {
    return channelId!=null;
  }

  public static final class Builder extends Message.Builder<GetRecListRsp, Builder> {
    public Integer retCode;

    public List<ChannelItem> items;

    public Integer channelId;

    public Builder() {
      items = Internal.newMutableList();
    }

    public Builder setRetCode(Integer retCode) {
      this.retCode = retCode;
      return this;
    }

    public Builder addAllItems(List<ChannelItem> items) {
      Internal.checkElementsNotNull(items);
      this.items = items;
      return this;
    }

    public Builder setChannelId(Integer channelId) {
      this.channelId = channelId;
      return this;
    }

    @Override
    public GetRecListRsp build() {
      return new GetRecListRsp(retCode, items, channelId, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GetRecListRsp extends ProtoAdapter<GetRecListRsp> {
    public ProtoAdapter_GetRecListRsp() {
      super(FieldEncoding.LENGTH_DELIMITED, GetRecListRsp.class);
    }

    @Override
    public int encodedSize(GetRecListRsp value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.retCode)
          + ChannelItem.ADAPTER.asRepeated().encodedSizeWithTag(2, value.items)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.channelId)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GetRecListRsp value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.retCode);
      ChannelItem.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.items);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.channelId);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GetRecListRsp decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRetCode(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.items.add(ChannelItem.ADAPTER.decode(reader)); break;
          case 3: builder.setChannelId(ProtoAdapter.UINT32.decode(reader)); break;
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
    public GetRecListRsp redact(GetRecListRsp value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.items, ChannelItem.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
