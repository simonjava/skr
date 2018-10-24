// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveMall.proto
package com.wali.live.proto.LiveMall;

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
import java.util.List;
import okio.ByteString;

/**
 * 命令字: zhibo.shopping.shopping_sales_list
 * 存储
 */
public final class RoomGoodsListStore extends Message<RoomGoodsListStore, RoomGoodsListStore.Builder> {
  public static final ProtoAdapter<RoomGoodsListStore> ADAPTER = new ProtoAdapter_RoomGoodsListStore();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_ROOM_ID = "";

  public static final Long DEFAULT_TIMESTAMP = 0L;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING",
      label = WireField.Label.REQUIRED
  )
  public final String room_id;

  @WireField(
      tag = 2,
      adapter = "com.wali.live.proto.LiveMall.GoodsInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<GoodsInfo> goods_info;

  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#INT64",
      label = WireField.Label.REQUIRED
  )
  public final Long timestamp;

  public RoomGoodsListStore(String room_id, List<GoodsInfo> goods_info, Long timestamp) {
    this(room_id, goods_info, timestamp, ByteString.EMPTY);
  }

  public RoomGoodsListStore(String room_id, List<GoodsInfo> goods_info, Long timestamp,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.room_id = room_id;
    this.goods_info = Internal.immutableCopyOf("goods_info", goods_info);
    this.timestamp = timestamp;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.room_id = room_id;
    builder.goods_info = Internal.copyOf("goods_info", goods_info);
    builder.timestamp = timestamp;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RoomGoodsListStore)) return false;
    RoomGoodsListStore o = (RoomGoodsListStore) other;
    return unknownFields().equals(o.unknownFields())
        && room_id.equals(o.room_id)
        && goods_info.equals(o.goods_info)
        && timestamp.equals(o.timestamp);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + room_id.hashCode();
      result = result * 37 + goods_info.hashCode();
      result = result * 37 + timestamp.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", room_id=").append(room_id);
    if (!goods_info.isEmpty()) builder.append(", goods_info=").append(goods_info);
    builder.append(", timestamp=").append(timestamp);
    return builder.replace(0, 2, "RoomGoodsListStore{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RoomGoodsListStore.ADAPTER.encode(this);
  }

  public static final RoomGoodsListStore parseFrom(byte[] data) throws IOException {
    RoomGoodsListStore c = null;
       c = RoomGoodsListStore.ADAPTER.decode(data);
    return c;
  }

  public String getRoomId() {
    if(room_id==null){
        return DEFAULT_ROOM_ID;
    }
    return room_id;
  }

  public List<GoodsInfo> getGoodsInfoList() {
    if(goods_info==null){
        return new java.util.ArrayList<GoodsInfo>();
    }
    return goods_info;
  }

  public Long getTimestamp() {
    if(timestamp==null){
        return DEFAULT_TIMESTAMP;
    }
    return timestamp;
  }

  public boolean hasRoomId() {
    return room_id!=null;
  }

  public boolean hasGoodsInfoList() {
    return goods_info!=null;
  }

  public boolean hasTimestamp() {
    return timestamp!=null;
  }

  public static final class Builder extends Message.Builder<RoomGoodsListStore, Builder> {
    public String room_id;

    public List<GoodsInfo> goods_info;

    public Long timestamp;

    public Builder() {
      goods_info = Internal.newMutableList();
    }

    public Builder setRoomId(String room_id) {
      this.room_id = room_id;
      return this;
    }

    public Builder addAllGoodsInfo(List<GoodsInfo> goods_info) {
      Internal.checkElementsNotNull(goods_info);
      this.goods_info = goods_info;
      return this;
    }

    public Builder setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    @Override
    public RoomGoodsListStore build() {
      return new RoomGoodsListStore(room_id, goods_info, timestamp, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RoomGoodsListStore extends ProtoAdapter<RoomGoodsListStore> {
    public ProtoAdapter_RoomGoodsListStore() {
      super(FieldEncoding.LENGTH_DELIMITED, RoomGoodsListStore.class);
    }

    @Override
    public int encodedSize(RoomGoodsListStore value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.room_id)
          + GoodsInfo.ADAPTER.asRepeated().encodedSizeWithTag(2, value.goods_info)
          + ProtoAdapter.INT64.encodedSizeWithTag(3, value.timestamp)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RoomGoodsListStore value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.room_id);
      GoodsInfo.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.goods_info);
      ProtoAdapter.INT64.encodeWithTag(writer, 3, value.timestamp);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RoomGoodsListStore decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRoomId(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.goods_info.add(GoodsInfo.ADAPTER.decode(reader)); break;
          case 3: builder.setTimestamp(ProtoAdapter.INT64.decode(reader)); break;
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
    public RoomGoodsListStore redact(RoomGoodsListStore value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.goods_info, GoodsInfo.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
