// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: race_room.proto
package com.zq.live.proto.RaceRoom;

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

public final class RWantSingChanceMsg extends Message<RWantSingChanceMsg, RWantSingChanceMsg.Builder> {
  public static final ProtoAdapter<RWantSingChanceMsg> ADAPTER = new ProtoAdapter_RWantSingChanceMsg();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_USERID = 0;

  public static final Integer DEFAULT_ROUNDSEQ = 0;

  public static final Integer DEFAULT_CHOICEID = 0;

  public static final ERWantSingType DEFAULT_WANTSINGTYPE = ERWantSingType.ERWST_DEFAULT;

  /**
   * 用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer userID;

  /**
   * 轮次顺序
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roundSeq;

  /**
   * 选择标识
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer choiceID;

  /**
   * 想唱方式
   */
  @WireField(
      tag = 4,
      adapter = "com.zq.live.proto.RaceRoom.ERWantSingType#ADAPTER"
  )
  private final ERWantSingType wantSingType;

  /**
   * 选择的条目
   */
  @WireField(
      tag = 5,
      adapter = "com.zq.live.proto.RaceRoom.ItemInfo#ADAPTER"
  )
  private final ItemInfo choiceItem;

  public RWantSingChanceMsg(Integer userID, Integer roundSeq, Integer choiceID,
      ERWantSingType wantSingType, ItemInfo choiceItem) {
    this(userID, roundSeq, choiceID, wantSingType, choiceItem, ByteString.EMPTY);
  }

  public RWantSingChanceMsg(Integer userID, Integer roundSeq, Integer choiceID,
      ERWantSingType wantSingType, ItemInfo choiceItem, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.userID = userID;
    this.roundSeq = roundSeq;
    this.choiceID = choiceID;
    this.wantSingType = wantSingType;
    this.choiceItem = choiceItem;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.userID = userID;
    builder.roundSeq = roundSeq;
    builder.choiceID = choiceID;
    builder.wantSingType = wantSingType;
    builder.choiceItem = choiceItem;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RWantSingChanceMsg)) return false;
    RWantSingChanceMsg o = (RWantSingChanceMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(userID, o.userID)
        && Internal.equals(roundSeq, o.roundSeq)
        && Internal.equals(choiceID, o.choiceID)
        && Internal.equals(wantSingType, o.wantSingType)
        && Internal.equals(choiceItem, o.choiceItem);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (userID != null ? userID.hashCode() : 0);
      result = result * 37 + (roundSeq != null ? roundSeq.hashCode() : 0);
      result = result * 37 + (choiceID != null ? choiceID.hashCode() : 0);
      result = result * 37 + (wantSingType != null ? wantSingType.hashCode() : 0);
      result = result * 37 + (choiceItem != null ? choiceItem.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (userID != null) builder.append(", userID=").append(userID);
    if (roundSeq != null) builder.append(", roundSeq=").append(roundSeq);
    if (choiceID != null) builder.append(", choiceID=").append(choiceID);
    if (wantSingType != null) builder.append(", wantSingType=").append(wantSingType);
    if (choiceItem != null) builder.append(", choiceItem=").append(choiceItem);
    return builder.replace(0, 2, "RWantSingChanceMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RWantSingChanceMsg.ADAPTER.encode(this);
  }

  public static final RWantSingChanceMsg parseFrom(byte[] data) throws IOException {
    RWantSingChanceMsg c = null;
       c = RWantSingChanceMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 用户id
   */
  public Integer getUserID() {
    if(userID==null){
        return DEFAULT_USERID;
    }
    return userID;
  }

  /**
   * 轮次顺序
   */
  public Integer getRoundSeq() {
    if(roundSeq==null){
        return DEFAULT_ROUNDSEQ;
    }
    return roundSeq;
  }

  /**
   * 选择标识
   */
  public Integer getChoiceID() {
    if(choiceID==null){
        return DEFAULT_CHOICEID;
    }
    return choiceID;
  }

  /**
   * 想唱方式
   */
  public ERWantSingType getWantSingType() {
    if(wantSingType==null){
        return new ERWantSingType.Builder().build();
    }
    return wantSingType;
  }

  /**
   * 选择的条目
   */
  public ItemInfo getChoiceItem() {
    if(choiceItem==null){
        return new ItemInfo.Builder().build();
    }
    return choiceItem;
  }

  /**
   * 用户id
   */
  public boolean hasUserID() {
    return userID!=null;
  }

  /**
   * 轮次顺序
   */
  public boolean hasRoundSeq() {
    return roundSeq!=null;
  }

  /**
   * 选择标识
   */
  public boolean hasChoiceID() {
    return choiceID!=null;
  }

  /**
   * 想唱方式
   */
  public boolean hasWantSingType() {
    return wantSingType!=null;
  }

  /**
   * 选择的条目
   */
  public boolean hasChoiceItem() {
    return choiceItem!=null;
  }

  public static final class Builder extends Message.Builder<RWantSingChanceMsg, Builder> {
    private Integer userID;

    private Integer roundSeq;

    private Integer choiceID;

    private ERWantSingType wantSingType;

    private ItemInfo choiceItem;

    public Builder() {
    }

    /**
     * 用户id
     */
    public Builder setUserID(Integer userID) {
      this.userID = userID;
      return this;
    }

    /**
     * 轮次顺序
     */
    public Builder setRoundSeq(Integer roundSeq) {
      this.roundSeq = roundSeq;
      return this;
    }

    /**
     * 选择标识
     */
    public Builder setChoiceID(Integer choiceID) {
      this.choiceID = choiceID;
      return this;
    }

    /**
     * 想唱方式
     */
    public Builder setWantSingType(ERWantSingType wantSingType) {
      this.wantSingType = wantSingType;
      return this;
    }

    /**
     * 选择的条目
     */
    public Builder setChoiceItem(ItemInfo choiceItem) {
      this.choiceItem = choiceItem;
      return this;
    }

    @Override
    public RWantSingChanceMsg build() {
      return new RWantSingChanceMsg(userID, roundSeq, choiceID, wantSingType, choiceItem, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RWantSingChanceMsg extends ProtoAdapter<RWantSingChanceMsg> {
    public ProtoAdapter_RWantSingChanceMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, RWantSingChanceMsg.class);
    }

    @Override
    public int encodedSize(RWantSingChanceMsg value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.userID)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.roundSeq)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.choiceID)
          + ERWantSingType.ADAPTER.encodedSizeWithTag(4, value.wantSingType)
          + ItemInfo.ADAPTER.encodedSizeWithTag(5, value.choiceItem)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RWantSingChanceMsg value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.userID);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.roundSeq);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.choiceID);
      ERWantSingType.ADAPTER.encodeWithTag(writer, 4, value.wantSingType);
      ItemInfo.ADAPTER.encodeWithTag(writer, 5, value.choiceItem);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RWantSingChanceMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUserID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setRoundSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.setChoiceID(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: {
            try {
              builder.setWantSingType(ERWantSingType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 5: builder.setChoiceItem(ItemInfo.ADAPTER.decode(reader)); break;
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
    public RWantSingChanceMsg redact(RWantSingChanceMsg value) {
      Builder builder = value.newBuilder();
      if (builder.choiceItem != null) builder.choiceItem = ItemInfo.ADAPTER.redact(builder.choiceItem);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
