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

public final class SyncSystemNotificationRequest extends Message<SyncSystemNotificationRequest, SyncSystemNotificationRequest.Builder> {
  public static final ProtoAdapter<SyncSystemNotificationRequest> ADAPTER = new ProtoAdapter_SyncSystemNotificationRequest();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_FROM = 0L;

  public static final Long DEFAULT_CID = 0L;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long from;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long cid;

  public SyncSystemNotificationRequest(Long from, Long cid) {
    this(from, cid, ByteString.EMPTY);
  }

  public SyncSystemNotificationRequest(Long from, Long cid, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.from = from;
    this.cid = cid;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.from = from;
    builder.cid = cid;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SyncSystemNotificationRequest)) return false;
    SyncSystemNotificationRequest o = (SyncSystemNotificationRequest) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(from, o.from)
        && Internal.equals(cid, o.cid);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (from != null ? from.hashCode() : 0);
      result = result * 37 + (cid != null ? cid.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (from != null) builder.append(", from=").append(from);
    if (cid != null) builder.append(", cid=").append(cid);
    return builder.replace(0, 2, "SyncSystemNotificationRequest{").append('}').toString();
  }

  public byte[] toByteArray() {
    return SyncSystemNotificationRequest.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<SyncSystemNotificationRequest, Builder> {
    public Long from;

    public Long cid;

    public Builder() {
    }

    public Builder setFrom(Long from) {
      this.from = from;
      return this;
    }

    public Builder setCid(Long cid) {
      this.cid = cid;
      return this;
    }

    @Override
    public SyncSystemNotificationRequest build() {
      return new SyncSystemNotificationRequest(from, cid, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_SyncSystemNotificationRequest extends ProtoAdapter<SyncSystemNotificationRequest> {
    public ProtoAdapter_SyncSystemNotificationRequest() {
      super(FieldEncoding.LENGTH_DELIMITED, SyncSystemNotificationRequest.class);
    }

    @Override
    public int encodedSize(SyncSystemNotificationRequest value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.from)
          + ProtoAdapter.UINT64.encodedSizeWithTag(2, value.cid)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, SyncSystemNotificationRequest value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.from);
      ProtoAdapter.UINT64.encodeWithTag(writer, 2, value.cid);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public SyncSystemNotificationRequest decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setFrom(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setCid(ProtoAdapter.UINT64.decode(reader)); break;
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
    public SyncSystemNotificationRequest redact(SyncSystemNotificationRequest value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
