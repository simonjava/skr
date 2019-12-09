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
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PChangeRoomEnterPermissionMsg extends Message<PChangeRoomEnterPermissionMsg, PChangeRoomEnterPermissionMsg.Builder> {
  public static final ProtoAdapter<PChangeRoomEnterPermissionMsg> ADAPTER = new ProtoAdapter_PChangeRoomEnterPermissionMsg();

  private static final long serialVersionUID = 0L;

  public static final EEnterPermissionType DEFAULT_PERMISSION = EEnterPermissionType.EPT_UNKNOWNS;

  /**
   * 新的权限
   */
  @WireField(
      tag = 1,
      adapter = "com.zq.live.proto.PartyRoom.EEnterPermissionType#ADAPTER"
  )
  private final EEnterPermissionType permission;

  public PChangeRoomEnterPermissionMsg(EEnterPermissionType permission) {
    this(permission, ByteString.EMPTY);
  }

  public PChangeRoomEnterPermissionMsg(EEnterPermissionType permission, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.permission = permission;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.permission = permission;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PChangeRoomEnterPermissionMsg)) return false;
    PChangeRoomEnterPermissionMsg o = (PChangeRoomEnterPermissionMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(permission, o.permission);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (permission != null ? permission.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (permission != null) builder.append(", permission=").append(permission);
    return builder.replace(0, 2, "PChangeRoomEnterPermissionMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PChangeRoomEnterPermissionMsg.ADAPTER.encode(this);
  }

  public static final PChangeRoomEnterPermissionMsg parseFrom(byte[] data) throws IOException {
    PChangeRoomEnterPermissionMsg c = null;
       c = PChangeRoomEnterPermissionMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 新的权限
   */
  public EEnterPermissionType getPermission() {
    if(permission==null){
        return new EEnterPermissionType.Builder().build();
    }
    return permission;
  }

  /**
   * 新的权限
   */
  public boolean hasPermission() {
    return permission!=null;
  }

  public static final class Builder extends Message.Builder<PChangeRoomEnterPermissionMsg, Builder> {
    private EEnterPermissionType permission;

    public Builder() {
    }

    /**
     * 新的权限
     */
    public Builder setPermission(EEnterPermissionType permission) {
      this.permission = permission;
      return this;
    }

    @Override
    public PChangeRoomEnterPermissionMsg build() {
      return new PChangeRoomEnterPermissionMsg(permission, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PChangeRoomEnterPermissionMsg extends ProtoAdapter<PChangeRoomEnterPermissionMsg> {
    public ProtoAdapter_PChangeRoomEnterPermissionMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PChangeRoomEnterPermissionMsg.class);
    }

    @Override
    public int encodedSize(PChangeRoomEnterPermissionMsg value) {
      return EEnterPermissionType.ADAPTER.encodedSizeWithTag(1, value.permission)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PChangeRoomEnterPermissionMsg value) throws IOException {
      EEnterPermissionType.ADAPTER.encodeWithTag(writer, 1, value.permission);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PChangeRoomEnterPermissionMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: {
            try {
              builder.setPermission(EEnterPermissionType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
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
    public PChangeRoomEnterPermissionMsg redact(PChangeRoomEnterPermissionMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
