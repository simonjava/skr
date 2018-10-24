// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: RadioSignal.proto
package com.wali.live.proto.RadioSignal;

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

public final class DiantaiSignalRsp extends Message<DiantaiSignalRsp, DiantaiSignalRsp.Builder> {
  public static final ProtoAdapter<DiantaiSignalRsp> ADAPTER = new ProtoAdapter_DiantaiSignalRsp();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_RET = 0;

  public static final Integer DEFAULT_DIANTAI_ID = 0;

  /**
   * 错误码
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer ret;

  /**
   * 电台id
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer diantai_id;

  /**
   * 电台成员信息，包含自己
   */
  @WireField(
      tag = 3,
      adapter = "com.wali.live.proto.RadioSignal.DiantaiUser#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<DiantaiUser> members;

  public DiantaiSignalRsp(Integer ret, Integer diantai_id, List<DiantaiUser> members) {
    this(ret, diantai_id, members, ByteString.EMPTY);
  }

  public DiantaiSignalRsp(Integer ret, Integer diantai_id, List<DiantaiUser> members,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.ret = ret;
    this.diantai_id = diantai_id;
    this.members = Internal.immutableCopyOf("members", members);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.ret = ret;
    builder.diantai_id = diantai_id;
    builder.members = Internal.copyOf("members", members);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof DiantaiSignalRsp)) return false;
    DiantaiSignalRsp o = (DiantaiSignalRsp) other;
    return unknownFields().equals(o.unknownFields())
        && ret.equals(o.ret)
        && Internal.equals(diantai_id, o.diantai_id)
        && members.equals(o.members);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + ret.hashCode();
      result = result * 37 + (diantai_id != null ? diantai_id.hashCode() : 0);
      result = result * 37 + members.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", ret=").append(ret);
    if (diantai_id != null) builder.append(", diantai_id=").append(diantai_id);
    if (!members.isEmpty()) builder.append(", members=").append(members);
    return builder.replace(0, 2, "DiantaiSignalRsp{").append('}').toString();
  }

  public static final DiantaiSignalRsp parseFrom(byte[] data) throws IOException {
    DiantaiSignalRsp c = null;
       c = DiantaiSignalRsp.ADAPTER.decode(data);
    return c;
  }

  /**
   * 错误码
   */
  public Integer getRet() {
    if(ret==null){
        return DEFAULT_RET;
    }
    return ret;
  }

  /**
   * 电台id
   */
  public Integer getDiantaiId() {
    if(diantai_id==null){
        return DEFAULT_DIANTAI_ID;
    }
    return diantai_id;
  }

  /**
   * 电台成员信息，包含自己
   */
  public List<DiantaiUser> getMembersList() {
    if(members==null){
        return new java.util.ArrayList<DiantaiUser>();
    }
    return members;
  }

  /**
   * 错误码
   */
  public boolean hasRet() {
    return ret!=null;
  }

  /**
   * 电台id
   */
  public boolean hasDiantaiId() {
    return diantai_id!=null;
  }

  /**
   * 电台成员信息，包含自己
   */
  public boolean hasMembersList() {
    return members!=null;
  }

  public static final class Builder extends Message.Builder<DiantaiSignalRsp, Builder> {
    public Integer ret;

    public Integer diantai_id;

    public List<DiantaiUser> members;

    public Builder() {
      members = Internal.newMutableList();
    }

    /**
     * 错误码
     */
    public Builder setRet(Integer ret) {
      this.ret = ret;
      return this;
    }

    /**
     * 电台id
     */
    public Builder setDiantaiId(Integer diantai_id) {
      this.diantai_id = diantai_id;
      return this;
    }

    /**
     * 电台成员信息，包含自己
     */
    public Builder addAllMembers(List<DiantaiUser> members) {
      Internal.checkElementsNotNull(members);
      this.members = members;
      return this;
    }

    @Override
    public DiantaiSignalRsp build() {
      return new DiantaiSignalRsp(ret, diantai_id, members, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_DiantaiSignalRsp extends ProtoAdapter<DiantaiSignalRsp> {
    public ProtoAdapter_DiantaiSignalRsp() {
      super(FieldEncoding.LENGTH_DELIMITED, DiantaiSignalRsp.class);
    }

    @Override
    public int encodedSize(DiantaiSignalRsp value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.ret)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.diantai_id)
          + DiantaiUser.ADAPTER.asRepeated().encodedSizeWithTag(3, value.members)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, DiantaiSignalRsp value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.ret);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.diantai_id);
      DiantaiUser.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.members);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public DiantaiSignalRsp decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRet(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setDiantaiId(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.members.add(DiantaiUser.ADAPTER.decode(reader)); break;
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
    public DiantaiSignalRsp redact(DiantaiSignalRsp value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.members, DiantaiUser.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
