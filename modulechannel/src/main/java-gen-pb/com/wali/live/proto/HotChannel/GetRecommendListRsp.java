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

public final class GetRecommendListRsp extends Message<GetRecommendListRsp, GetRecommendListRsp.Builder> {
  public static final ProtoAdapter<GetRecommendListRsp> ADAPTER = new ProtoAdapter_GetRecommendListRsp();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_RET_CODE = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer ret_code;

  @WireField(
      tag = 2,
      adapter = "com.wali.live.proto.CommonChannel.ChannelItem#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<ChannelItem> items;

  public GetRecommendListRsp(Integer ret_code, List<ChannelItem> items) {
    this(ret_code, items, ByteString.EMPTY);
  }

  public GetRecommendListRsp(Integer ret_code, List<ChannelItem> items, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.ret_code = ret_code;
    this.items = Internal.immutableCopyOf("items", items);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.ret_code = ret_code;
    builder.items = Internal.copyOf("items", items);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetRecommendListRsp)) return false;
    GetRecommendListRsp o = (GetRecommendListRsp) other;
    return unknownFields().equals(o.unknownFields())
        && ret_code.equals(o.ret_code)
        && items.equals(o.items);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + ret_code.hashCode();
      result = result * 37 + items.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", ret_code=").append(ret_code);
    if (!items.isEmpty()) builder.append(", items=").append(items);
    return builder.replace(0, 2, "GetRecommendListRsp{").append('}').toString();
  }

  public static final GetRecommendListRsp parseFrom(byte[] data) throws IOException {
    GetRecommendListRsp c = null;
       c = GetRecommendListRsp.ADAPTER.decode(data);
    return c;
  }

  public Integer getRetCode() {
    if(ret_code==null){
        return DEFAULT_RET_CODE;
    }
    return ret_code;
  }

  public List<ChannelItem> getItemsList() {
    if(items==null){
        return new java.util.ArrayList<ChannelItem>();
    }
    return items;
  }

  public boolean hasRetCode() {
    return ret_code!=null;
  }

  public boolean hasItemsList() {
    return items!=null;
  }

  public static final class Builder extends Message.Builder<GetRecommendListRsp, Builder> {
    public Integer ret_code;

    public List<ChannelItem> items;

    public Builder() {
      items = Internal.newMutableList();
    }

    public Builder setRetCode(Integer ret_code) {
      this.ret_code = ret_code;
      return this;
    }

    public Builder addAllItems(List<ChannelItem> items) {
      Internal.checkElementsNotNull(items);
      this.items = items;
      return this;
    }

    @Override
    public GetRecommendListRsp build() {
      return new GetRecommendListRsp(ret_code, items, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GetRecommendListRsp extends ProtoAdapter<GetRecommendListRsp> {
    public ProtoAdapter_GetRecommendListRsp() {
      super(FieldEncoding.LENGTH_DELIMITED, GetRecommendListRsp.class);
    }

    @Override
    public int encodedSize(GetRecommendListRsp value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.ret_code)
          + ChannelItem.ADAPTER.asRepeated().encodedSizeWithTag(2, value.items)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GetRecommendListRsp value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.ret_code);
      ChannelItem.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.items);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GetRecommendListRsp decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRetCode(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.items.add(ChannelItem.ADAPTER.decode(reader)); break;
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
    public GetRecommendListRsp redact(GetRecommendListRsp value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.items, ChannelItem.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
