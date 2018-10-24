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
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * zhibo.send.roommsg
 */
public final class RoomMessageRequest extends Message<RoomMessageRequest, RoomMessageRequest.Builder> {
  public static final ProtoAdapter<RoomMessageRequest> ADAPTER = new ProtoAdapter_RoomMessageRequest();

  private static final long serialVersionUID = 0L;

  public static final Long DEFAULT_FROM_USER = 0L;

  public static final String DEFAULT_ROOM_ID = "";

  public static final Long DEFAULT_CID = 0L;

  public static final Integer DEFAULT_MSG_TYPE = 0;

  public static final String DEFAULT_MSG_BODY = "";

  public static final ByteString DEFAULT_MSG_EXT = ByteString.EMPTY;

  public static final Long DEFAULT_ANCHOR_ID = 0L;

  public static final String DEFAULT_SUPPORT_TXT = "";

  public static final Integer DEFAULT_ROOM_TYPE = 0;

  public static final Long DEFAULT_REQUEST_TS = 0L;

  public static final String DEFAULT_UNION_ROOM_ID = "";

  /**
   * 弹幕消息发送者
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64",
      label = WireField.Label.REQUIRED
  )
  public final Long from_user;

  /**
   * 房间ID
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String room_id;

  /**
   * 客户端消息ID
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long cid;

  /**
   * 根据msg_type扩展msg_ext字段结构
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer msg_type;

  /**
   * 文本消息
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String msg_body;

  /**
   * 扩展消息pb，与msg_type对应
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#BYTES"
  )
  public final ByteString msg_ext;

  /**
   * 主播ID
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long anchor_id;

  /**
   * 版本兼容文案
   */
  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String support_txt;

  /**
   * 房间类型,0代表正常房间 1代表PK房间
   */
  @WireField(
      tag = 9,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer room_type;

  /**
   * PK房间信息,房间发送弹幕使用
   */
  @WireField(
      tag = 10,
      adapter = "com.wali.live.proto.LiveMessage.PKRoomInfo#ADAPTER"
  )
  public final PKRoomInfo pk_room_info;

  /**
   * 多语言文案
   */
  @WireField(
      tag = 11,
      adapter = "com.wali.live.proto.LiveMessage.MultiLanguage#ADAPTER"
  )
  public final MultiLanguage multi_language;

  /**
   * 进出房间时间戳支持
   */
  @WireField(
      tag = 12,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long request_ts;

  /**
   * 所有类型弹幕扩展字段(针对多种类型的弹幕)
   */
  @WireField(
      tag = 13,
      adapter = "com.wali.live.proto.LiveMessage.GlobalRoomMessageExt#ADAPTER"
  )
  public final GlobalRoomMessageExt global_room_msg_ext;

  /**
   * 工会房间ID
   */
  @WireField(
      tag = 14,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String union_room_id;

  public RoomMessageRequest(Long from_user, String room_id, Long cid, Integer msg_type,
      String msg_body, ByteString msg_ext, Long anchor_id, String support_txt, Integer room_type,
      PKRoomInfo pk_room_info, MultiLanguage multi_language, Long request_ts,
      GlobalRoomMessageExt global_room_msg_ext, String union_room_id) {
    this(from_user, room_id, cid, msg_type, msg_body, msg_ext, anchor_id, support_txt, room_type, pk_room_info, multi_language, request_ts, global_room_msg_ext, union_room_id, ByteString.EMPTY);
  }

  public RoomMessageRequest(Long from_user, String room_id, Long cid, Integer msg_type,
      String msg_body, ByteString msg_ext, Long anchor_id, String support_txt, Integer room_type,
      PKRoomInfo pk_room_info, MultiLanguage multi_language, Long request_ts,
      GlobalRoomMessageExt global_room_msg_ext, String union_room_id, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.from_user = from_user;
    this.room_id = room_id;
    this.cid = cid;
    this.msg_type = msg_type;
    this.msg_body = msg_body;
    this.msg_ext = msg_ext;
    this.anchor_id = anchor_id;
    this.support_txt = support_txt;
    this.room_type = room_type;
    this.pk_room_info = pk_room_info;
    this.multi_language = multi_language;
    this.request_ts = request_ts;
    this.global_room_msg_ext = global_room_msg_ext;
    this.union_room_id = union_room_id;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.from_user = from_user;
    builder.room_id = room_id;
    builder.cid = cid;
    builder.msg_type = msg_type;
    builder.msg_body = msg_body;
    builder.msg_ext = msg_ext;
    builder.anchor_id = anchor_id;
    builder.support_txt = support_txt;
    builder.room_type = room_type;
    builder.pk_room_info = pk_room_info;
    builder.multi_language = multi_language;
    builder.request_ts = request_ts;
    builder.global_room_msg_ext = global_room_msg_ext;
    builder.union_room_id = union_room_id;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RoomMessageRequest)) return false;
    RoomMessageRequest o = (RoomMessageRequest) other;
    return unknownFields().equals(o.unknownFields())
        && from_user.equals(o.from_user)
        && Internal.equals(room_id, o.room_id)
        && Internal.equals(cid, o.cid)
        && Internal.equals(msg_type, o.msg_type)
        && Internal.equals(msg_body, o.msg_body)
        && Internal.equals(msg_ext, o.msg_ext)
        && Internal.equals(anchor_id, o.anchor_id)
        && Internal.equals(support_txt, o.support_txt)
        && Internal.equals(room_type, o.room_type)
        && Internal.equals(pk_room_info, o.pk_room_info)
        && Internal.equals(multi_language, o.multi_language)
        && Internal.equals(request_ts, o.request_ts)
        && Internal.equals(global_room_msg_ext, o.global_room_msg_ext)
        && Internal.equals(union_room_id, o.union_room_id);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + from_user.hashCode();
      result = result * 37 + (room_id != null ? room_id.hashCode() : 0);
      result = result * 37 + (cid != null ? cid.hashCode() : 0);
      result = result * 37 + (msg_type != null ? msg_type.hashCode() : 0);
      result = result * 37 + (msg_body != null ? msg_body.hashCode() : 0);
      result = result * 37 + (msg_ext != null ? msg_ext.hashCode() : 0);
      result = result * 37 + (anchor_id != null ? anchor_id.hashCode() : 0);
      result = result * 37 + (support_txt != null ? support_txt.hashCode() : 0);
      result = result * 37 + (room_type != null ? room_type.hashCode() : 0);
      result = result * 37 + (pk_room_info != null ? pk_room_info.hashCode() : 0);
      result = result * 37 + (multi_language != null ? multi_language.hashCode() : 0);
      result = result * 37 + (request_ts != null ? request_ts.hashCode() : 0);
      result = result * 37 + (global_room_msg_ext != null ? global_room_msg_ext.hashCode() : 0);
      result = result * 37 + (union_room_id != null ? union_room_id.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", from_user=").append(from_user);
    if (room_id != null) builder.append(", room_id=").append(room_id);
    if (cid != null) builder.append(", cid=").append(cid);
    if (msg_type != null) builder.append(", msg_type=").append(msg_type);
    if (msg_body != null) builder.append(", msg_body=").append(msg_body);
    if (msg_ext != null) builder.append(", msg_ext=").append(msg_ext);
    if (anchor_id != null) builder.append(", anchor_id=").append(anchor_id);
    if (support_txt != null) builder.append(", support_txt=").append(support_txt);
    if (room_type != null) builder.append(", room_type=").append(room_type);
    if (pk_room_info != null) builder.append(", pk_room_info=").append(pk_room_info);
    if (multi_language != null) builder.append(", multi_language=").append(multi_language);
    if (request_ts != null) builder.append(", request_ts=").append(request_ts);
    if (global_room_msg_ext != null) builder.append(", global_room_msg_ext=").append(global_room_msg_ext);
    if (union_room_id != null) builder.append(", union_room_id=").append(union_room_id);
    return builder.replace(0, 2, "RoomMessageRequest{").append('}').toString();
  }

  public byte[] toByteArray() {
    return RoomMessageRequest.ADAPTER.encode(this);
  }

  public static final class Builder extends Message.Builder<RoomMessageRequest, Builder> {
    public Long from_user;

    public String room_id;

    public Long cid;

    public Integer msg_type;

    public String msg_body;

    public ByteString msg_ext;

    public Long anchor_id;

    public String support_txt;

    public Integer room_type;

    public PKRoomInfo pk_room_info;

    public MultiLanguage multi_language;

    public Long request_ts;

    public GlobalRoomMessageExt global_room_msg_ext;

    public String union_room_id;

    public Builder() {
    }

    /**
     * 弹幕消息发送者
     */
    public Builder setFromUser(Long from_user) {
      this.from_user = from_user;
      return this;
    }

    /**
     * 房间ID
     */
    public Builder setRoomId(String room_id) {
      this.room_id = room_id;
      return this;
    }

    /**
     * 客户端消息ID
     */
    public Builder setCid(Long cid) {
      this.cid = cid;
      return this;
    }

    /**
     * 根据msg_type扩展msg_ext字段结构
     */
    public Builder setMsgType(Integer msg_type) {
      this.msg_type = msg_type;
      return this;
    }

    /**
     * 文本消息
     */
    public Builder setMsgBody(String msg_body) {
      this.msg_body = msg_body;
      return this;
    }

    /**
     * 扩展消息pb，与msg_type对应
     */
    public Builder setMsgExt(ByteString msg_ext) {
      this.msg_ext = msg_ext;
      return this;
    }

    /**
     * 主播ID
     */
    public Builder setAnchorId(Long anchor_id) {
      this.anchor_id = anchor_id;
      return this;
    }

    /**
     * 版本兼容文案
     */
    public Builder setSupportTxt(String support_txt) {
      this.support_txt = support_txt;
      return this;
    }

    /**
     * 房间类型,0代表正常房间 1代表PK房间
     */
    public Builder setRoomType(Integer room_type) {
      this.room_type = room_type;
      return this;
    }

    /**
     * PK房间信息,房间发送弹幕使用
     */
    public Builder setPkRoomInfo(PKRoomInfo pk_room_info) {
      this.pk_room_info = pk_room_info;
      return this;
    }

    /**
     * 多语言文案
     */
    public Builder setMultiLanguage(MultiLanguage multi_language) {
      this.multi_language = multi_language;
      return this;
    }

    /**
     * 进出房间时间戳支持
     */
    public Builder setRequestTs(Long request_ts) {
      this.request_ts = request_ts;
      return this;
    }

    /**
     * 所有类型弹幕扩展字段(针对多种类型的弹幕)
     */
    public Builder setGlobalRoomMsgExt(GlobalRoomMessageExt global_room_msg_ext) {
      this.global_room_msg_ext = global_room_msg_ext;
      return this;
    }

    /**
     * 工会房间ID
     */
    public Builder setUnionRoomId(String union_room_id) {
      this.union_room_id = union_room_id;
      return this;
    }

    @Override
    public RoomMessageRequest build() {
      if (from_user == null) {
        throw Internal.missingRequiredFields(from_user, "from_user");
      }
      return new RoomMessageRequest(from_user, room_id, cid, msg_type, msg_body, msg_ext, anchor_id, support_txt, room_type, pk_room_info, multi_language, request_ts, global_room_msg_ext, union_room_id, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_RoomMessageRequest extends ProtoAdapter<RoomMessageRequest> {
    public ProtoAdapter_RoomMessageRequest() {
      super(FieldEncoding.LENGTH_DELIMITED, RoomMessageRequest.class);
    }

    @Override
    public int encodedSize(RoomMessageRequest value) {
      return ProtoAdapter.UINT64.encodedSizeWithTag(1, value.from_user)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.room_id)
          + ProtoAdapter.UINT64.encodedSizeWithTag(3, value.cid)
          + ProtoAdapter.UINT32.encodedSizeWithTag(4, value.msg_type)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.msg_body)
          + ProtoAdapter.BYTES.encodedSizeWithTag(6, value.msg_ext)
          + ProtoAdapter.UINT64.encodedSizeWithTag(7, value.anchor_id)
          + ProtoAdapter.STRING.encodedSizeWithTag(8, value.support_txt)
          + ProtoAdapter.UINT32.encodedSizeWithTag(9, value.room_type)
          + PKRoomInfo.ADAPTER.encodedSizeWithTag(10, value.pk_room_info)
          + MultiLanguage.ADAPTER.encodedSizeWithTag(11, value.multi_language)
          + ProtoAdapter.UINT64.encodedSizeWithTag(12, value.request_ts)
          + GlobalRoomMessageExt.ADAPTER.encodedSizeWithTag(13, value.global_room_msg_ext)
          + ProtoAdapter.STRING.encodedSizeWithTag(14, value.union_room_id)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, RoomMessageRequest value) throws IOException {
      ProtoAdapter.UINT64.encodeWithTag(writer, 1, value.from_user);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.room_id);
      ProtoAdapter.UINT64.encodeWithTag(writer, 3, value.cid);
      ProtoAdapter.UINT32.encodeWithTag(writer, 4, value.msg_type);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.msg_body);
      ProtoAdapter.BYTES.encodeWithTag(writer, 6, value.msg_ext);
      ProtoAdapter.UINT64.encodeWithTag(writer, 7, value.anchor_id);
      ProtoAdapter.STRING.encodeWithTag(writer, 8, value.support_txt);
      ProtoAdapter.UINT32.encodeWithTag(writer, 9, value.room_type);
      PKRoomInfo.ADAPTER.encodeWithTag(writer, 10, value.pk_room_info);
      MultiLanguage.ADAPTER.encodeWithTag(writer, 11, value.multi_language);
      ProtoAdapter.UINT64.encodeWithTag(writer, 12, value.request_ts);
      GlobalRoomMessageExt.ADAPTER.encodeWithTag(writer, 13, value.global_room_msg_ext);
      ProtoAdapter.STRING.encodeWithTag(writer, 14, value.union_room_id);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public RoomMessageRequest decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setFromUser(ProtoAdapter.UINT64.decode(reader)); break;
          case 2: builder.setRoomId(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setCid(ProtoAdapter.UINT64.decode(reader)); break;
          case 4: builder.setMsgType(ProtoAdapter.UINT32.decode(reader)); break;
          case 5: builder.setMsgBody(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.setMsgExt(ProtoAdapter.BYTES.decode(reader)); break;
          case 7: builder.setAnchorId(ProtoAdapter.UINT64.decode(reader)); break;
          case 8: builder.setSupportTxt(ProtoAdapter.STRING.decode(reader)); break;
          case 9: builder.setRoomType(ProtoAdapter.UINT32.decode(reader)); break;
          case 10: builder.setPkRoomInfo(PKRoomInfo.ADAPTER.decode(reader)); break;
          case 11: builder.setMultiLanguage(MultiLanguage.ADAPTER.decode(reader)); break;
          case 12: builder.setRequestTs(ProtoAdapter.UINT64.decode(reader)); break;
          case 13: builder.setGlobalRoomMsgExt(GlobalRoomMessageExt.ADAPTER.decode(reader)); break;
          case 14: builder.setUnionRoomId(ProtoAdapter.STRING.decode(reader)); break;
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
    public RoomMessageRequest redact(RoomMessageRequest value) {
      Builder builder = value.newBuilder();
      if (builder.pk_room_info != null) builder.pk_room_info = PKRoomInfo.ADAPTER.redact(builder.pk_room_info);
      if (builder.multi_language != null) builder.multi_language = MultiLanguage.ADAPTER.redact(builder.multi_language);
      if (builder.global_room_msg_ext != null) builder.global_room_msg_ext = GlobalRoomMessageExt.ADAPTER.redact(builder.global_room_msg_ext);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
