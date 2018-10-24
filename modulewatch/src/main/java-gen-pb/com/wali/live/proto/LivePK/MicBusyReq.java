// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LivePk.proto
package com.wali.live.proto.LivePK;

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
 * 通知占线状态
 * zhibo.live.micbusy
 */
public final class MicBusyReq extends Message<MicBusyReq, MicBusyReq.Builder> {
  public static final ProtoAdapter<MicBusyReq> ADAPTER = new ProtoAdapter_MicBusyReq();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_UUID = 0L;

  public static final String DEFAULT_LIVEID = "";

  public static final Long DEFAULT_INVITE_UUID = 0L;

  public static final Long DEFAULT_ZUID = 0L;

  /**
   * 处于busy状态的用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64",
      label = WireField.Label.REQUIRED
  )
  public final Long uuid;

  /**
   * 处于busy状态的liveid
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String liveid;

  /**
   * 发起用户id(例：场控邀请自己房间主播时，传场控id)
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long invite_uuid;

  /**
   * 发起用户所在房间主播id
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long zuid;

  public MicBusyReq(Long uuid, String liveid, Long invite_uuid, Long zuid) {
    this(uuid, liveid, invite_uuid, zuid, ByteString.EMPTY);
  }

  public MicBusyReq(Long uuid, String liveid, Long invite_uuid, Long zuid,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.uuid = uuid;
    this.liveid = liveid;
    this.invite_uuid = invite_uuid;
    this.zuid = zuid;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.uuid = uuid;
    builder.liveid = liveid;
    builder.invite_uuid = invite_uuid;
    builder.zuid = zuid;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MicBusyReq)) return false;
    MicBusyReq o = (MicBusyReq) other;
    return unknownFields().equals(o.unknownFields())
        && uuid.equals(o.uuid)
        && Internal.equals(liveid, o.liveid)
        && Internal.equals(invite_uuid, o.invite_uuid)
        && Internal.equals(zuid, o.zuid);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + uuid.hashCode();
      result = result * 37 + (liveid != null ? liveid.hashCode() : 0);
      result = result * 37 + (invite_uuid != null ? invite_uuid.hashCode() : 0);
      result = result * 37 + (zuid != null ? zuid.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", uuid=").append(uuid);
    if (liveid != null) builder.append(", liveid=").append(liveid);
    if (invite_uuid != null) builder.append(", invite_uuid=").append(invite_uuid);
    if (zuid != null) builder.append(", zuid=").append(zuid);
    return builder.replace(0, 2, "MicBusyReq{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MicBusyReq.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<MicBusyReq, Builder> {
    public Long uuid;

    public String liveid;

    public Long invite_uuid;

    public Long zuid;

    public Builder() {
    }

    /**
     * 处于busy状态的用户id
     */
    public Builder setUuid(Long uuid) {
      this.uuid = uuid;
      return this;
    }

    /**
     * 处于busy状态的liveid
     */
    public Builder setLiveid(String liveid) {
      this.liveid = liveid;
      return this;
    }

    /**
     * 发起用户id(例：场控邀请自己房间主播时，传场控id)
     */
    public Builder setInviteUuid(Long invite_uuid) {
      this.invite_uuid = invite_uuid;
      return this;
    }

    /**
     * 发起用户所在房间主播id
     */
    public Builder setZuid(Long zuid) {
      this.zuid = zuid;
      return this;
    }

    @Override
    public MicBusyReq build() {
      if (uuid == null) {
        throw Internal.missingRequiredFields(uuid, "uuid");
      }
      return new MicBusyReq(uuid, liveid, invite_uuid, zuid, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MicBusyReq extends ProtoAdapter<MicBusyReq> {
    public ProtoAdapter_MicBusyReq() {
      super(FieldEncoding.LENGTH_DELIMITED, MicBusyReq.class);
    }

    @Override
    public int encodedSize(MicBusyReq value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.uuid)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.liveid)
          + ProtoAdapter.UINT64.encodedSizeWithTag(3, value.invite_uuid)
          + ProtoAdapter.UINT64.encodedSizeWithTag(4, value.zuid)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MicBusyReq value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.uuid);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.liveid);
      ProtoAdapter.UINT64.encodeWithTag(writer, 3, value.invite_uuid);
      ProtoAdapter.UINT64.encodeWithTag(writer, 4, value.zuid);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MicBusyReq decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setUuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setLiveid(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setInviteUuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 4: builder.setZuid(ProtoAdapter.UINT64.decode(reader)); break;
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
    public MicBusyReq redact(MicBusyReq value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
