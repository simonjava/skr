// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveShow.proto
package com.wali.live.proto.LiveShow;

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

/**
 * 1 拉关注列表 请求和返回
 */
public final class GetConcernsReq extends Message<GetConcernsReq, GetConcernsReq.Builder> {
  public static final ProtoAdapter<GetConcernsReq> ADAPTER = new ProtoAdapter_GetConcernsReq();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_UID = 0L;

  /**
   * 用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64",
      label = WireField.Label.REQUIRED
  )
  public final Long uId;

  public GetConcernsReq(Long uId) {
    this(uId, ByteString.EMPTY);
  }

  public GetConcernsReq(Long uId, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.uId = uId;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.uId = uId;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetConcernsReq)) return false;
    GetConcernsReq o = (GetConcernsReq) other;
    return unknownFields().equals(o.unknownFields())
        && uId.equals(o.uId);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + uId.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", uId=").append(uId);
    return builder.replace(0, 2, "GetConcernsReq{").append('}').toString();
  }

  public byte[] toByteArray() {
    return GetConcernsReq.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<GetConcernsReq, Builder> {
    public Long uId;

    public Builder() {
    }

    /**
     * 用户id
     */
    public Builder setUId(Long uId) {
      this.uId = uId;
      return this;
    }

    @Override
    public GetConcernsReq build() {
      if (uId == null) {
        throw Internal.missingRequiredFields(uId, "uId");
      }
      return new GetConcernsReq(uId, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GetConcernsReq extends ProtoAdapter<GetConcernsReq> {
    public ProtoAdapter_GetConcernsReq() {
      super(FieldEncoding.LENGTH_DELIMITED, GetConcernsReq.class);
    }

    @Override
    public int encodedSize(GetConcernsReq value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.uId)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GetConcernsReq value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.uId);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GetConcernsReq decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUId(ProtoAdapter.UINT64.decode(reader)); break;
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
    public GetConcernsReq redact(GetConcernsReq value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
