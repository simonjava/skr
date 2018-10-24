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
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

public final class UserOrder extends Message<UserOrder, UserOrder.Builder> {
  public static final ProtoAdapter<UserOrder> ADAPTER = new ProtoAdapter_UserOrder();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_SHOP_TYPE = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#INT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer shop_type;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING",
      label = WireField.Label.REPEATED
  )
  public final List<String> img_url;

  public UserOrder(Integer shop_type, List<String> img_url) {
    this(shop_type, img_url, ByteString.EMPTY);
  }

  public UserOrder(Integer shop_type, List<String> img_url, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.shop_type = shop_type;
    this.img_url = Internal.immutableCopyOf("img_url", img_url);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.shop_type = shop_type;
    builder.img_url = Internal.copyOf("img_url", img_url);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof UserOrder)) return false;
    UserOrder o = (UserOrder) other;
    return unknownFields().equals(o.unknownFields())
        && shop_type.equals(o.shop_type)
        && img_url.equals(o.img_url);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + shop_type.hashCode();
      result = result * 37 + img_url.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", shop_type=").append(shop_type);
    if (!img_url.isEmpty()) builder.append(", img_url=").append(img_url);
    return builder.replace(0, 2, "UserOrder{").append('}').toString();
  }

  public byte[] toByteArray() {
    return UserOrder.ADAPTER.encode(this);
  }

  public static final UserOrder parseFrom(byte[] data) throws IOException {
    UserOrder c = null;
       c = UserOrder.ADAPTER.decode(data);
    return c;
  }

  public Integer getShopType() {
    if(shop_type==null){
        return DEFAULT_SHOP_TYPE;
    }
    return shop_type;
  }

  public List<String> getImgUrlList() {
    if(img_url==null){
        return new java.util.ArrayList<String>();
    }
    return img_url;
  }

  public boolean hasShopType() {
    return shop_type!=null;
  }

  public boolean hasImgUrlList() {
    return img_url!=null;
  }

  public static final class Builder extends Message.Builder<UserOrder, Builder> {
    public Integer shop_type;

    public List<String> img_url;

    public Builder() {
      img_url = Internal.newMutableList();
    }

    public Builder setShopType(Integer shop_type) {
      this.shop_type = shop_type;
      return this;
    }

    public Builder addAllImgUrl(List<String> img_url) {
      Internal.checkElementsNotNull(img_url);
      this.img_url = img_url;
      return this;
    }

    @Override
    public UserOrder build() {
      return new UserOrder(shop_type, img_url, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_UserOrder extends ProtoAdapter<UserOrder> {
    public ProtoAdapter_UserOrder() {
      super(FieldEncoding.LENGTH_DELIMITED, UserOrder.class);
    }

    @Override
    public int encodedSize(UserOrder value) {
      return ProtoAdapter.INT32.encodedSizeWithTag(1, value.shop_type)
          + ProtoAdapter.STRING.asRepeated().encodedSizeWithTag(2, value.img_url)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, UserOrder value) throws IOException {
      ProtoAdapter.INT32.encodeWithTag(writer, 1, value.shop_type);
      ProtoAdapter.STRING.asRepeated().encodeWithTag(writer, 2, value.img_url);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public UserOrder decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setShopType(ProtoAdapter.INT32.decode(reader)); break;
          case 2: builder.img_url.add(ProtoAdapter.STRING.decode(reader)); break;
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
    public UserOrder redact(UserOrder value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
