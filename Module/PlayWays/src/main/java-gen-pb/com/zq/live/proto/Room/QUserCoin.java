// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.zq.live.proto.Room;

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

public final class QUserCoin extends Message<QUserCoin, QUserCoin.Builder> {
  public static final ProtoAdapter<QUserCoin> ADAPTER = new ProtoAdapter_QUserCoin();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Long DEFAULT_COIN = 0L;

  /**
   * 用户ID
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * 金币数量
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#SINT64"
  )
  private final Long coin;

  public QUserCoin(Integer userID, Long coin) {
    this(userID, coin, ByteString.EMPTY);
  }

  public QUserCoin(Integer userID, Long coin, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.coin = coin;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.coin = coin;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof QUserCoin)) return false;
    QUserCoin o = (QUserCoin) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(coin, o.coin);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (coin != null ? coin.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (coin != null) builder.append(", coin=").append(coin);
    return builder.replace(0, 2, "QUserCoin{").append('}').toString();
  }

  public byte[] toByteArray() {
    return QUserCoin.ADAPTER.encode(this);
  }

  public static final QUserCoin parseFrom(byte[] data) throws IOException {
    QUserCoin c = null;
       c = QUserCoin.ADAPTER.decode(data);
    return c;
  }

  /**
   * 用户ID
   */
  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  /**
   * 金币数量
   */
  public Long getCoin() {
    if(coin==null){
        return DEFAULT_COIN;
    }
    return coin;
  }

  /**
   * 用户ID
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 金币数量
   */
  public boolean hasCoin() {
    return coin!=null;
  }

  public static final class Builder extends Message.Builder<QUserCoin, Builder> {
    private Integer userID;

    private Long coin;

    public Builder() {
    }

    /**
     * 用户ID
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    /**
     * 金币数量
     */
    public Builder setCoin(Long coin) {
      this.coin = coin;
      return this;
    }

    @Override
    public QUserCoin build() {
      return new QUserCoin(userID, coin, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_QUserCoin extends ProtoAdapter<QUserCoin> {
    public ProtoAdapter_QUserCoin() {
      super(FieldEncoding.LENGTH_DELIMITED, QUserCoin.class);
    }

    @Override
    public int encodedSize(QUserCoin value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.SINT64.encodedSizeWithTag(2, value.coin)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, QUserCoin value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.SINT64.encodeWithTag(writer, 2, value.coin);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public QUserCoin decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setCoin(ProtoAdapter.SINT64.decode(reader)); break;
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
    public QUserCoin redact(QUserCoin value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
