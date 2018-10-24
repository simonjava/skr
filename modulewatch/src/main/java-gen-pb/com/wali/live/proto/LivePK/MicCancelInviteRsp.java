// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LivePk.proto
package com.wali.live.proto.LivePK;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class MicCancelInviteRsp extends Message<MicCancelInviteRsp, MicCancelInviteRsp.Builder> {
  public static final ProtoAdapter<MicCancelInviteRsp> ADAPTER = new ProtoAdapter_MicCancelInviteRsp();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_RET_CODE = 0;

  /**
   * 0 代表成功
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer ret_code;

  public MicCancelInviteRsp(Integer ret_code) {
    this(ret_code, ByteString.EMPTY);
  }

  public MicCancelInviteRsp(Integer ret_code, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.ret_code = ret_code;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.ret_code = ret_code;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MicCancelInviteRsp)) return false;
    MicCancelInviteRsp o = (MicCancelInviteRsp) other;
    return unknownFields().equals(o.unknownFields())
        && ret_code.equals(o.ret_code);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + ret_code.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", ret_code=").append(ret_code);
    return builder.replace(0, 2, "MicCancelInviteRsp{").append('}').toString();
  }

  public static final MicCancelInviteRsp parseFrom(byte[] data) throws IOException {
    MicCancelInviteRsp c = null;
       c = MicCancelInviteRsp.ADAPTER.decode(data);
    return c;
  }

  /**
   * 0 代表成功
   */
  public Integer getRetCode() {
    if(ret_code==null){
        return DEFAULT_RET_CODE;
    }
    return ret_code;
  }

  /**
   * 0 代表成功
   */
  public boolean hasRetCode() {
    return ret_code!=null;
  }

  public static final class Builder extends Message.Builder<MicCancelInviteRsp, Builder> {
    public Integer ret_code;

    public Builder() {
    }

    /**
     * 0 代表成功
     */
    public Builder setRetCode(Integer ret_code) {
      this.ret_code = ret_code;
      return this;
    }

    @Override
    public MicCancelInviteRsp build() {
      return new MicCancelInviteRsp(ret_code, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MicCancelInviteRsp extends ProtoAdapter<MicCancelInviteRsp> {
    public ProtoAdapter_MicCancelInviteRsp() {
      super(FieldEncoding.LENGTH_DELIMITED, MicCancelInviteRsp.class);
    }

    @Override
    public int encodedSize(MicCancelInviteRsp value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.ret_code)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MicCancelInviteRsp value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.ret_code);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MicCancelInviteRsp decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRetCode(ProtoAdapter.UINT32.decode(reader)); break;
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
    public MicCancelInviteRsp redact(MicCancelInviteRsp value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
