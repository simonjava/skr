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
 * 发送消息响应
 */
public final class ChatMessageResponse extends Message<ChatMessageResponse, ChatMessageResponse.Builder> {
  public static final ProtoAdapter<ChatMessageResponse> ADAPTER = new ProtoAdapter_ChatMessageResponse();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_RET = 0;

  public static final String DEFAULT_ERROR_MSG = "";

  public static final Integer DEFAULT_MSG_SEQ = 0;

  public static final Long DEFAULT_TIMESTAMP = 0L;

  public static final Long DEFAULT_CID = 0L;

  /**
   * 消息返回码
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32",
      label = WireField.Label.REQUIRED
  )
  public final Integer ret;

  /**
   * 错误信息
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String error_msg;

  /**
   * 私信消息seq，用来sync消息使用
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer msg_seq;

  /**
   * 时间戳，客户端排序使用
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long timestamp;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#UINT64"
  )
  public final Long cid;

  public ChatMessageResponse(Integer ret, String error_msg, Integer msg_seq, Long timestamp,
      Long cid) {
    this(ret, error_msg, msg_seq, timestamp, cid, ByteString.EMPTY);
  }

  public ChatMessageResponse(Integer ret, String error_msg, Integer msg_seq, Long timestamp,
      Long cid, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.ret = ret;
    this.error_msg = error_msg;
    this.msg_seq = msg_seq;
    this.timestamp = timestamp;
    this.cid = cid;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.ret = ret;
    builder.error_msg = error_msg;
    builder.msg_seq = msg_seq;
    builder.timestamp = timestamp;
    builder.cid = cid;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ChatMessageResponse)) return false;
    ChatMessageResponse o = (ChatMessageResponse) other;
    return unknownFields().equals(o.unknownFields())
        && ret.equals(o.ret)
        && Internal.equals(error_msg, o.error_msg)
        && Internal.equals(msg_seq, o.msg_seq)
        && Internal.equals(timestamp, o.timestamp)
        && Internal.equals(cid, o.cid);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + ret.hashCode();
      result = result * 37 + (error_msg != null ? error_msg.hashCode() : 0);
      result = result * 37 + (msg_seq != null ? msg_seq.hashCode() : 0);
      result = result * 37 + (timestamp != null ? timestamp.hashCode() : 0);
      result = result * 37 + (cid != null ? cid.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", ret=").append(ret);
    if (error_msg != null) builder.append(", error_msg=").append(error_msg);
    if (msg_seq != null) builder.append(", msg_seq=").append(msg_seq);
    if (timestamp != null) builder.append(", timestamp=").append(timestamp);
    if (cid != null) builder.append(", cid=").append(cid);
    return builder.replace(0, 2, "ChatMessageResponse{").append('}').toString();
  }

  public static final ChatMessageResponse parseFrom(byte[] data) throws IOException {
    ChatMessageResponse c = null;
       c = ChatMessageResponse.ADAPTER.decode(data);
    return c;
  }

  /**
   * 消息返回码
   */
  public Integer getRet() {
    if(ret==null){
        return DEFAULT_RET;
    }
    return ret;
  }

  /**
   * 错误信息
   */
  public String getErrorMsg() {
    if(error_msg==null){
        return DEFAULT_ERROR_MSG;
    }
    return error_msg;
  }

  /**
   * 私信消息seq，用来sync消息使用
   */
  public Integer getMsgSeq() {
    if(msg_seq==null){
        return DEFAULT_MSG_SEQ;
    }
    return msg_seq;
  }

  /**
   * 时间戳，客户端排序使用
   */
  public Long getTimestamp() {
    if(timestamp==null){
        return DEFAULT_TIMESTAMP;
    }
    return timestamp;
  }

  public Long getCid() {
    if(cid==null){
        return DEFAULT_CID;
    }
    return cid;
  }

  /**
   * 消息返回码
   */
  public boolean hasRet() {
    return ret!=null;
  }

  /**
   * 错误信息
   */
  public boolean hasErrorMsg() {
    return error_msg!=null;
  }

  /**
   * 私信消息seq，用来sync消息使用
   */
  public boolean hasMsgSeq() {
    return msg_seq!=null;
  }

  /**
   * 时间戳，客户端排序使用
   */
  public boolean hasTimestamp() {
    return timestamp!=null;
  }

  public boolean hasCid() {
    return cid!=null;
  }

  public static final class Builder extends Message.Builder<ChatMessageResponse, Builder> {
    public Integer ret;

    public String error_msg;

    public Integer msg_seq;

    public Long timestamp;

    public Long cid;

    public Builder() {
    }

    /**
     * 消息返回码
     */
    public Builder setRet(Integer ret) {
      this.ret = ret;
      return this;
    }

    /**
     * 错误信息
     */
    public Builder setErrorMsg(String error_msg) {
      this.error_msg = error_msg;
      return this;
    }

    /**
     * 私信消息seq，用来sync消息使用
     */
    public Builder setMsgSeq(Integer msg_seq) {
      this.msg_seq = msg_seq;
      return this;
    }

    /**
     * 时间戳，客户端排序使用
     */
    public Builder setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder setCid(Long cid) {
      this.cid = cid;
      return this;
    }

    @Override
    public ChatMessageResponse build() {
      return new ChatMessageResponse(ret, error_msg, msg_seq, timestamp, cid, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_ChatMessageResponse extends ProtoAdapter<ChatMessageResponse> {
    public ProtoAdapter_ChatMessageResponse() {
      super(FieldEncoding.LENGTH_DELIMITED, ChatMessageResponse.class);
    }

    @Override
    public int encodedSize(ChatMessageResponse value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.ret)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.error_msg)
          + ProtoAdapter.UINT32.encodedSizeWithTag(3, value.msg_seq)
          + ProtoAdapter.UINT64.encodedSizeWithTag(4, value.timestamp)
          + ProtoAdapter.UINT64.encodedSizeWithTag(5, value.cid)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, ChatMessageResponse value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.ret);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.error_msg);
      ProtoAdapter.UINT32.encodeWithTag(writer, 3, value.msg_seq);
      ProtoAdapter.UINT64.encodeWithTag(writer, 4, value.timestamp);
      ProtoAdapter.UINT64.encodeWithTag(writer, 5, value.cid);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public ChatMessageResponse decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRet(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setErrorMsg(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setMsgSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 4: builder.setTimestamp(ProtoAdapter.UINT64.decode(reader)); break;
          case 5: builder.setCid(ProtoAdapter.UINT64.decode(reader)); break;
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
    public ChatMessageResponse redact(ChatMessageResponse value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
