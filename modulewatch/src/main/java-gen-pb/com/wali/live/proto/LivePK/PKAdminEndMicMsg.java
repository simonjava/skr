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

public final class PKAdminEndMicMsg extends Message<PKAdminEndMicMsg, PKAdminEndMicMsg.Builder> {
  public static final ProtoAdapter<PKAdminEndMicMsg> ADAPTER = new ProtoAdapter_PKAdminEndMicMsg();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_ZUID = 0L;

  public static final String DEFAULT_LIVEID = "";

  public static final Long DEFAULT_MICUID = 0L;

  public static final Long DEFAULT_ADMIN_UUID = 0L;

  /**
   * 主播用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long zuid;

  /**
   * 房间号
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String liveId;

  /**
   * 对应的用户id
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long micuid;

  /**
   * 场控id
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long admin_uuid;

  public PKAdminEndMicMsg(Long zuid, String liveId, Long micuid, Long admin_uuid) {
    this(zuid, liveId, micuid, admin_uuid, ByteString.EMPTY);
  }

  public PKAdminEndMicMsg(Long zuid, String liveId, Long micuid, Long admin_uuid,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.zuid = zuid;
    this.liveId = liveId;
    this.micuid = micuid;
    this.admin_uuid = admin_uuid;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.zuid = zuid;
    builder.liveId = liveId;
    builder.micuid = micuid;
    builder.admin_uuid = admin_uuid;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PKAdminEndMicMsg)) return false;
    PKAdminEndMicMsg o = (PKAdminEndMicMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(zuid, o.zuid)
        && Internal.equals(liveId, o.liveId)
        && Internal.equals(micuid, o.micuid)
        && Internal.equals(admin_uuid, o.admin_uuid);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (zuid != null ? zuid.hashCode() : 0);
      result = result * 37 + (liveId != null ? liveId.hashCode() : 0);
      result = result * 37 + (micuid != null ? micuid.hashCode() : 0);
      result = result * 37 + (admin_uuid != null ? admin_uuid.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (zuid != null) builder.append(", zuid=").append(zuid);
    if (liveId != null) builder.append(", liveId=").append(liveId);
    if (micuid != null) builder.append(", micuid=").append(micuid);
    if (admin_uuid != null) builder.append(", admin_uuid=").append(admin_uuid);
    return builder.replace(0, 2, "PKAdminEndMicMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PKAdminEndMicMsg.ADAPTER.encode(this);
  }

  public static final PKAdminEndMicMsg parseFrom(byte[] data) throws IOException {
    PKAdminEndMicMsg c = null;
       c = PKAdminEndMicMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 主播用户id
   */
  public Long getZuid() {
    if(zuid==null){
        return DEFAULT_ZUID;
    }
    return zuid;
  }

  /**
   * 房间号
   */
  public String getLiveId() {
    if(liveId==null){
        return DEFAULT_LIVEID;
    }
    return liveId;
  }

  /**
   * 对应的用户id
   */
  public Long getMicuid() {
    if(micuid==null){
        return DEFAULT_MICUID;
    }
    return micuid;
  }

  /**
   * 场控id
   */
  public Long getAdminUuid() {
    if(admin_uuid==null){
        return DEFAULT_ADMIN_UUID;
    }
    return admin_uuid;
  }

  /**
   * 主播用户id
   */
  public boolean hasZuid() {
    return zuid!=null;
  }

  /**
   * 房间号
   */
  public boolean hasLiveId() {
    return liveId!=null;
  }

  /**
   * 对应的用户id
   */
  public boolean hasMicuid() {
    return micuid!=null;
  }

  /**
   * 场控id
   */
  public boolean hasAdminUuid() {
    return admin_uuid!=null;
  }

  public static final class Builder extends Message.Builder<PKAdminEndMicMsg, Builder> {
    public Long zuid;

    public String liveId;

    public Long micuid;

    public Long admin_uuid;

    public Builder() {
    }

    /**
     * 主播用户id
     */
    public Builder setZuid(Long zuid) {
      this.zuid = zuid;
      return this;
    }

    /**
     * 房间号
     */
    public Builder setLiveId(String liveId) {
      this.liveId = liveId;
      return this;
    }

    /**
     * 对应的用户id
     */
    public Builder setMicuid(Long micuid) {
      this.micuid = micuid;
      return this;
    }

    /**
     * 场控id
     */
    public Builder setAdminUuid(Long admin_uuid) {
      this.admin_uuid = admin_uuid;
      return this;
    }

    @Override
    public PKAdminEndMicMsg build() {
      return new PKAdminEndMicMsg(zuid, liveId, micuid, admin_uuid, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PKAdminEndMicMsg extends ProtoAdapter<PKAdminEndMicMsg> {
    public ProtoAdapter_PKAdminEndMicMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, PKAdminEndMicMsg.class);
    }

    @Override
    public int encodedSize(PKAdminEndMicMsg value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.zuid)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.liveId)
          + ProtoAdapter.UINT64.encodedSizeWithTag(3, value.micuid)
          + ProtoAdapter.UINT64.encodedSizeWithTag(4, value.admin_uuid)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PKAdminEndMicMsg value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.zuid);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.liveId);
      ProtoAdapter.UINT64.encodeWithTag(writer, 3, value.micuid);
      ProtoAdapter.UINT64.encodeWithTag(writer, 4, value.admin_uuid);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PKAdminEndMicMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setZuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setLiveId(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setMicuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 4: builder.setAdminUuid(ProtoAdapter.UINT64.decode(reader)); break;
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
    public PKAdminEndMicMsg redact(PKAdminEndMicMsg value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
