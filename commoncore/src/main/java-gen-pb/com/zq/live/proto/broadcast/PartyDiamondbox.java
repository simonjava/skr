// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: broadcast.proto
package com.zq.live.proto.broadcast;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.zq.live.proto.Common.PBeginDiamondbox;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PartyDiamondbox extends Message<PartyDiamondbox, PartyDiamondbox.Builder> {
  public static final ProtoAdapter<PartyDiamondbox> ADAPTER = new ProtoAdapter_PartyDiamondbox();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_ROOMID = 0;

  /**
   * 房间id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roomID;

  /**
   * 下发宝箱
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Common.PBeginDiamondbox#ADAPTER"
  )
  private final PBeginDiamondbox pBeginDiamondbox;

  public PartyDiamondbox(Integer roomID, PBeginDiamondbox pBeginDiamondbox) {
    this(roomID, pBeginDiamondbox, ByteString.EMPTY);
  }

  public PartyDiamondbox(Integer roomID, PBeginDiamondbox pBeginDiamondbox,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.roomID = roomID;
    this.pBeginDiamondbox = pBeginDiamondbox;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.roomID = roomID;
    builder.pBeginDiamondbox = pBeginDiamondbox;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PartyDiamondbox)) return false;
    PartyDiamondbox o = (PartyDiamondbox) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(roomID, o.roomID)
        && Internal.equals(pBeginDiamondbox, o.pBeginDiamondbox);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (roomID != null ? roomID.hashCode() : 0);
      result = result * 37 + (pBeginDiamondbox != null ? pBeginDiamondbox.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (roomID != null) builder.append(", roomID=").append(roomID);
    if (pBeginDiamondbox != null) builder.append(", pBeginDiamondbox=").append(pBeginDiamondbox);
    return builder.replace(0, 2, "PartyDiamondbox{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PartyDiamondbox.ADAPTER.encode(this);
  }

  public static final PartyDiamondbox parseFrom(byte[] data) throws IOException {
    PartyDiamondbox c = null;
       c = PartyDiamondbox.ADAPTER.decode(data);
    return c;
  }

  /**
   * 房间id
   */
  public Integer getRoomID() {
    if(roomID==null){
        return DEFAULT_ROOMID;
    }
    return roomID;
  }

  /**
   * 下发宝箱
   */
  public PBeginDiamondbox getPBeginDiamondbox() {
    if(pBeginDiamondbox==null){
        return new PBeginDiamondbox.Builder().build();
    }
    return pBeginDiamondbox;
  }

  /**
   * 房间id
   */
  public boolean hasRoomID() {
    return roomID!=null;
  }

  /**
   * 下发宝箱
   */
  public boolean hasPBeginDiamondbox() {
    return pBeginDiamondbox!=null;
  }

  public static final class Builder extends Message.Builder<PartyDiamondbox, Builder> {
    private Integer roomID;

    private PBeginDiamondbox pBeginDiamondbox;

    public Builder() {
    }

    /**
     * 房间id
     */
    public Builder setRoomID(Integer roomID) {
      this.roomID = roomID;
      return this;
    }

    /**
     * 下发宝箱
     */
    public Builder setPBeginDiamondbox(PBeginDiamondbox pBeginDiamondbox) {
      this.pBeginDiamondbox = pBeginDiamondbox;
      return this;
    }

    @Override
    public PartyDiamondbox build() {
      return new PartyDiamondbox(roomID, pBeginDiamondbox, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PartyDiamondbox extends ProtoAdapter<PartyDiamondbox> {
    public ProtoAdapter_PartyDiamondbox() {
      super(FieldEncoding.LENGTH_DELIMITED, PartyDiamondbox.class);
    }

    @Override
    public int encodedSize(PartyDiamondbox value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.roomID)
          + PBeginDiamondbox.ADAPTER.encodedSizeWithTag(2, value.pBeginDiamondbox)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PartyDiamondbox value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.roomID);
      PBeginDiamondbox.ADAPTER.encodeWithTag(writer, 2, value.pBeginDiamondbox);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PartyDiamondbox decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setPBeginDiamondbox(PBeginDiamondbox.ADAPTER.decode(reader)); break;
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
    public PartyDiamondbox redact(PartyDiamondbox value) {
      Builder builder = value.newBuilder();
      if (builder.pBeginDiamondbox != null) builder.pBeginDiamondbox = PBeginDiamondbox.ADAPTER.redact(builder.pBeginDiamondbox);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
