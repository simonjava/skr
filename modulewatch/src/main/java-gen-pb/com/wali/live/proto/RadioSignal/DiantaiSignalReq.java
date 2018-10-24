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
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

/**
 * java -jar -Dfile.encoding=UTF-8 ./proto/wire-compiler-2.3.0-SNAPSHOT-jar-with-dependencies.jar \
 * --proto_path=./proto --java_out=./modulewatch/src/main/java-gen-pb/ RadioSignal.proto
 * 信令相关命令字, pb协议通用
 * 1. cmd: zhibo.diantai.apply   用户申请连麦
 * 2. cmd: zhibo.diantai.invite  主播邀请用户连麦
 * 3. cmd: zhibo.diantai.approve 主播批准用户连麦
 * 4. cmd: zhibo.diantai.accept  用户接受主播邀请
 * 5. cmd: zhibo.diantai.quit    用户结束连麦
 * 6. cmd: zhibo.diantai.kick    踢出用户
 * 7. cmd: zhibo.diantai.cancel_apply  用户取消申请连麦
 * 8. cmd: zhibo.diantai.mute  禁言某个电台用户(嘉宾)
 * 9. cmd: zhibo.diantai.un_mute  取消禁言某个电台用户(嘉宾)
 */
public final class DiantaiSignalReq extends Message<DiantaiSignalReq, DiantaiSignalReq.Builder> {
  public static final ProtoAdapter<DiantaiSignalReq> ADAPTER = new ProtoAdapter_DiantaiSignalReq();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_FROM_ZUID = 0L;

  public static final Integer DEFAULT_DIANTAI_ID = 0;

  public static final String DEFAULT_ROOM_ID = "";

  /**
   * 发起方用户id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64",
      label = WireField.Label.REQUIRED
  )
  public final Long from_zuid;

  /**
   * 电台id, 没有则不用带
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer diantai_id;

  /**
   * 接收方用户id列表
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64",
      label = WireField.Label.REPEATED
  )
  public final List<Long> to_zuids;

  /**
   * 直播房间id
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String room_id;

  public DiantaiSignalReq(Long from_zuid, Integer diantai_id, List<Long> to_zuids, String room_id) {
    this(from_zuid, diantai_id, to_zuids, room_id, ByteString.EMPTY);
  }

  public DiantaiSignalReq(Long from_zuid, Integer diantai_id, List<Long> to_zuids, String room_id,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.from_zuid = from_zuid;
    this.diantai_id = diantai_id;
    this.to_zuids = Internal.immutableCopyOf("to_zuids", to_zuids);
    this.room_id = room_id;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.from_zuid = from_zuid;
    builder.diantai_id = diantai_id;
    builder.to_zuids = Internal.copyOf("to_zuids", to_zuids);
    builder.room_id = room_id;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof DiantaiSignalReq)) return false;
    DiantaiSignalReq o = (DiantaiSignalReq) other;
    return unknownFields().equals(o.unknownFields())
        && from_zuid.equals(o.from_zuid)
        && Internal.equals(diantai_id, o.diantai_id)
        && to_zuids.equals(o.to_zuids)
        && Internal.equals(room_id, o.room_id);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + from_zuid.hashCode();
      result = result * 37 + (diantai_id != null ? diantai_id.hashCode() : 0);
      result = result * 37 + to_zuids.hashCode();
      result = result * 37 + (room_id != null ? room_id.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", from_zuid=").append(from_zuid);
    if (diantai_id != null) builder.append(", diantai_id=").append(diantai_id);
    if (!to_zuids.isEmpty()) builder.append(", to_zuids=").append(to_zuids);
    if (room_id != null) builder.append(", room_id=").append(room_id);
    return builder.replace(0, 2, "DiantaiSignalReq{").append('}').toString();
  }

  public byte[] toByteArray() {
    return DiantaiSignalReq.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<DiantaiSignalReq, Builder> {
    public Long from_zuid;

    public Integer diantai_id;

    public List<Long> to_zuids;

    public String room_id;

    public Builder() {
      to_zuids = Internal.newMutableList();
    }

    /**
     * 发起方用户id
     */
    public Builder setFromZuid(Long from_zuid) {
      this.from_zuid = from_zuid;
      return this;
    }

    /**
     * 电台id, 没有则不用带
     */
    public Builder setDiantaiId(Integer diantai_id) {
      this.diantai_id = diantai_id;
      return this;
    }

    /**
     * 接收方用户id列表
     */
    public Builder addAllToZuids(List<Long> to_zuids) {
      Internal.checkElementsNotNull(to_zuids);
      this.to_zuids = to_zuids;
      return this;
    }

    /**
     * 直播房间id
     */
    public Builder setRoomId(String room_id) {
      this.room_id = room_id;
      return this;
    }

    @Override
    public DiantaiSignalReq build() {
      if (from_zuid == null) {
        throw Internal.missingRequiredFields(from_zuid, "from_zuid");
      }
      return new DiantaiSignalReq(from_zuid, diantai_id, to_zuids, room_id, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_DiantaiSignalReq extends ProtoAdapter<DiantaiSignalReq> {
    public ProtoAdapter_DiantaiSignalReq() {
      super(FieldEncoding.LENGTH_DELIMITED, DiantaiSignalReq.class);
    }

    @Override
    public int encodedSize(DiantaiSignalReq value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.from_zuid)
          + ProtoAdapter.UINT32.encodedSizeWithTag(2, value.diantai_id)
          + ProtoAdapter.UINT64.asRepeated().encodedSizeWithTag(3, value.to_zuids)
          + ProtoAdapter.STRING.encodedSizeWithTag(4, value.room_id)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, DiantaiSignalReq value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.from_zuid);
      ProtoAdapter.UINT32.encodeWithTag(writer, 2, value.diantai_id);
      ProtoAdapter.UINT64.asRepeated().encodeWithTag(writer, 3, value.to_zuids);
      ProtoAdapter.STRING.encodeWithTag(writer, 4, value.room_id);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public DiantaiSignalReq decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setFromZuid(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setDiantaiId(ProtoAdapter.UINT32.decode(reader)); break;
          case 3: builder.to_zuids.add(ProtoAdapter.UINT64.decode(reader)); break;
          case 4: builder.setRoomId(ProtoAdapter.STRING.decode(reader)); break;
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
    public DiantaiSignalReq redact(DiantaiSignalReq value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
