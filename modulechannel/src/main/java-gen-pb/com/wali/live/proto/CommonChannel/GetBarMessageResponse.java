// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: CommonChannel.proto
package com.wali.live.proto.CommonChannel;

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

public final class GetBarMessageResponse extends Message<GetBarMessageResponse, GetBarMessageResponse.Builder> {
  public static final ProtoAdapter<GetBarMessageResponse> ADAPTER = new ProtoAdapter_GetBarMessageResponse();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_RET = 0;

  public static final String DEFAULT_ERR_MSG = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer ret;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String err_msg;

  @WireField(
      tag = 3,
      adapter = "com.wali.live.proto.CommonChannel.BarMessage#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<BarMessage> bar_msg;

  public GetBarMessageResponse(Integer ret, String err_msg, List<BarMessage> bar_msg) {
    this(ret, err_msg, bar_msg, ByteString.EMPTY);
  }

  public GetBarMessageResponse(Integer ret, String err_msg, List<BarMessage> bar_msg,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.ret = ret;
    this.err_msg = err_msg;
    this.bar_msg = Internal.immutableCopyOf("bar_msg", bar_msg);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.ret = ret;
    builder.err_msg = err_msg;
    builder.bar_msg = Internal.copyOf("bar_msg", bar_msg);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetBarMessageResponse)) return false;
    GetBarMessageResponse o = (GetBarMessageResponse) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(ret, o.ret)
        && Internal.equals(err_msg, o.err_msg)
        && bar_msg.equals(o.bar_msg);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (ret != null ? ret.hashCode() : 0);
      result = result * 37 + (err_msg != null ? err_msg.hashCode() : 0);
      result = result * 37 + bar_msg.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (ret != null) builder.append(", ret=").append(ret);
    if (err_msg != null) builder.append(", err_msg=").append(err_msg);
    if (!bar_msg.isEmpty()) builder.append(", bar_msg=").append(bar_msg);
    return builder.replace(0, 2, "GetBarMessageResponse{").append('}').toString();
  }

  public static final GetBarMessageResponse parseFrom(byte[] data) throws IOException {
    GetBarMessageResponse c = null;
       c = GetBarMessageResponse.ADAPTER.decode(data);
    return c;
  }

  public Integer getRet() {
    if(ret==null){
        return DEFAULT_RET;
    }
    return ret;
  }

  public String getErrMsg() {
    if(err_msg==null){
        return DEFAULT_ERR_MSG;
    }
    return err_msg;
  }

  public List<BarMessage> getBarMsgList() {
    if(bar_msg==null){
        return new java.util.ArrayList<BarMessage>();
    }
    return bar_msg;
  }

  public boolean hasRet() {
    return ret!=null;
  }

  public boolean hasErrMsg() {
    return err_msg!=null;
  }

  public boolean hasBarMsgList() {
    return bar_msg!=null;
  }

  public static final class Builder extends Message.Builder<GetBarMessageResponse, Builder> {
    public Integer ret;

    public String err_msg;

    public List<BarMessage> bar_msg;

    public Builder() {
      bar_msg = Internal.newMutableList();
    }

    public Builder setRet(Integer ret) {
      this.ret = ret;
      return this;
    }

    public Builder setErrMsg(String err_msg) {
      this.err_msg = err_msg;
      return this;
    }

    public Builder addAllBarMsg(List<BarMessage> bar_msg) {
      Internal.checkElementsNotNull(bar_msg);
      this.bar_msg = bar_msg;
      return this;
    }

    @Override
    public GetBarMessageResponse build() {
      return new GetBarMessageResponse(ret, err_msg, bar_msg, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GetBarMessageResponse extends ProtoAdapter<GetBarMessageResponse> {
    public ProtoAdapter_GetBarMessageResponse() {
      super(FieldEncoding.LENGTH_DELIMITED, GetBarMessageResponse.class);
    }

    @Override
    public int encodedSize(GetBarMessageResponse value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.ret)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.err_msg)
          + BarMessage.ADAPTER.asRepeated().encodedSizeWithTag(3, value.bar_msg)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GetBarMessageResponse value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.ret);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.err_msg);
      BarMessage.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.bar_msg);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GetBarMessageResponse decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRet(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setErrMsg(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.bar_msg.add(BarMessage.ADAPTER.decode(reader)); break;
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
    public GetBarMessageResponse redact(GetBarMessageResponse value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.bar_msg, BarMessage.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
