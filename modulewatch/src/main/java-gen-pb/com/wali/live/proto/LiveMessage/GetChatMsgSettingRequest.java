// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveMessage.proto
package com.wali.live.proto.LiveMessage;

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

public final class GetChatMsgSettingRequest extends Message<GetChatMsgSettingRequest, GetChatMsgSettingRequest.Builder> {
  public static final ProtoAdapter<GetChatMsgSettingRequest> ADAPTER = new ProtoAdapter_GetChatMsgSettingRequest();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_UUID = 0L;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long uuid;

  public GetChatMsgSettingRequest(Long uuid) {
    this(uuid, ByteString.EMPTY);
  }

  public GetChatMsgSettingRequest(Long uuid, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.uuid = uuid;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.uuid = uuid;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetChatMsgSettingRequest)) return false;
    GetChatMsgSettingRequest o = (GetChatMsgSettingRequest) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(uuid, o.uuid);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (uuid != null ? uuid.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (uuid != null) builder.append(", uuid=").append(uuid);
    return builder.replace(0, 2, "GetChatMsgSettingRequest{").append('}').toString();
  }

  public byte[] toByteArray() {
    return GetChatMsgSettingRequest.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<GetChatMsgSettingRequest, Builder> {
    public Long uuid;

    public Builder() {
    }

    public Builder setUuid(Long uuid) {
      this.uuid = uuid;
      return this;
    }

    @Override
    public GetChatMsgSettingRequest build() {
      return new GetChatMsgSettingRequest(uuid, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GetChatMsgSettingRequest extends ProtoAdapter<GetChatMsgSettingRequest> {
    public ProtoAdapter_GetChatMsgSettingRequest() {
      super(FieldEncoding.LENGTH_DELIMITED, GetChatMsgSettingRequest.class);
    }

    @Override
    public int encodedSize(GetChatMsgSettingRequest value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.uuid)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GetChatMsgSettingRequest value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.uuid);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GetChatMsgSettingRequest decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUuid(ProtoAdapter.UINT64.decode(reader)); break;
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
    public GetChatMsgSettingRequest redact(GetChatMsgSettingRequest value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
