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

public final class AllGoodsResp extends Message<AllGoodsResp, AllGoodsResp.Builder> {
  public static final ProtoAdapter<AllGoodsResp> ADAPTER = new ProtoAdapter_AllGoodsResp();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_ERR_CODE = 0;

  public static final String DEFAULT_ERR_MSG = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#INT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer err_code;

  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String err_msg;

  @WireField(
      tag = 3,
      adapter = "com.wali.live.proto.LiveMall.PkgInfoList#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<PkgInfoList> pkg_info_list;

  public AllGoodsResp(Integer err_code, String err_msg, List<PkgInfoList> pkg_info_list) {
    this(err_code, err_msg, pkg_info_list, ByteString.EMPTY);
  }

  public AllGoodsResp(Integer err_code, String err_msg, List<PkgInfoList> pkg_info_list,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.err_code = err_code;
    this.err_msg = err_msg;
    this.pkg_info_list = Internal.immutableCopyOf("pkg_info_list", pkg_info_list);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.err_code = err_code;
    builder.err_msg = err_msg;
    builder.pkg_info_list = Internal.copyOf("pkg_info_list", pkg_info_list);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof AllGoodsResp)) return false;
    AllGoodsResp o = (AllGoodsResp) other;
    return unknownFields().equals(o.unknownFields())
        && err_code.equals(o.err_code)
        && Internal.equals(err_msg, o.err_msg)
        && pkg_info_list.equals(o.pkg_info_list);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + err_code.hashCode();
      result = result * 37 + (err_msg != null ? err_msg.hashCode() : 0);
      result = result * 37 + pkg_info_list.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", err_code=").append(err_code);
    if (err_msg != null) builder.append(", err_msg=").append(err_msg);
    if (!pkg_info_list.isEmpty()) builder.append(", pkg_info_list=").append(pkg_info_list);
    return builder.replace(0, 2, "AllGoodsResp{").append('}').toString();
  }

  public byte[] toByteArray() {
    return AllGoodsResp.ADAPTER.encode(this);
  }

  public static final AllGoodsResp parseFrom(byte[] data) throws IOException {
    AllGoodsResp c = null;
       c = AllGoodsResp.ADAPTER.decode(data);
    return c;
  }

  public Integer getErrCode() {
    if(err_code==null){
        return DEFAULT_ERR_CODE;
    }
    return err_code;
  }

  public String getErrMsg() {
    if(err_msg==null){
        return DEFAULT_ERR_MSG;
    }
    return err_msg;
  }

  public List<PkgInfoList> getPkgInfoListList() {
    if(pkg_info_list==null){
        return new java.util.ArrayList<PkgInfoList>();
    }
    return pkg_info_list;
  }

  public boolean hasErrCode() {
    return err_code!=null;
  }

  public boolean hasErrMsg() {
    return err_msg!=null;
  }

  public boolean hasPkgInfoListList() {
    return pkg_info_list!=null;
  }

  public static final class Builder extends Message.Builder<AllGoodsResp, Builder> {
    public Integer err_code;

    public String err_msg;

    public List<PkgInfoList> pkg_info_list;

    public Builder() {
      pkg_info_list = Internal.newMutableList();
    }

    public Builder setErrCode(Integer err_code) {
      this.err_code = err_code;
      return this;
    }

    public Builder setErrMsg(String err_msg) {
      this.err_msg = err_msg;
      return this;
    }

    public Builder addAllPkgInfoList(List<PkgInfoList> pkg_info_list) {
      Internal.checkElementsNotNull(pkg_info_list);
      this.pkg_info_list = pkg_info_list;
      return this;
    }

    @Override
    public AllGoodsResp build() {
      return new AllGoodsResp(err_code, err_msg, pkg_info_list, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_AllGoodsResp extends ProtoAdapter<AllGoodsResp> {
    public ProtoAdapter_AllGoodsResp() {
      super(FieldEncoding.LENGTH_DELIMITED, AllGoodsResp.class);
    }

    @Override
    public int encodedSize(AllGoodsResp value) {
      return ProtoAdapter.INT32.encodedSizeWithTag(1, value.err_code)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.err_msg)
          + PkgInfoList.ADAPTER.asRepeated().encodedSizeWithTag(3, value.pkg_info_list)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, AllGoodsResp value) throws IOException {
      ProtoAdapter.INT32.encodeWithTag(writer, 1, value.err_code);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.err_msg);
      PkgInfoList.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.pkg_info_list);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public AllGoodsResp decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setErrCode(ProtoAdapter.INT32.decode(reader)); break;
          case 2: builder.setErrMsg(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.pkg_info_list.add(PkgInfoList.ADAPTER.decode(reader)); break;
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
    public AllGoodsResp redact(AllGoodsResp value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.pkg_info_list, PkgInfoList.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
