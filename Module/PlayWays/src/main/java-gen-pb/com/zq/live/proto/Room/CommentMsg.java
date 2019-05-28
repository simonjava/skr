// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Room.proto
package com.zq.live.proto.Room;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.zq.live.proto.Common.UserInfo;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

/**
 * 普通评论消息
 */
public final class CommentMsg extends Message<CommentMsg, CommentMsg.Builder> {
  public static final ProtoAdapter<CommentMsg> ADAPTER = new ProtoAdapter_CommentMsg();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_TEXT = "";

  /**
   * 发表内容
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String text;

  /**
   * 接收者信息
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Common.UserInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<UserInfo> receiver;

  public CommentMsg(String text, List<UserInfo> receiver) {
    this(text, receiver, ByteString.EMPTY);
  }

  public CommentMsg(String text, List<UserInfo> receiver, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.text = text;
    this.receiver = Internal.immutableCopyOf("receiver", receiver);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.text = text;
    builder.receiver = Internal.copyOf("receiver", receiver);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CommentMsg)) return false;
    CommentMsg o = (CommentMsg) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(text, o.text)
        && receiver.equals(o.receiver);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (text != null ? text.hashCode() : 0);
      result = result * 37 + receiver.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (text != null) builder.append(", text=").append(text);
    if (!receiver.isEmpty()) builder.append(", receiver=").append(receiver);
    return builder.replace(0, 2, "CommentMsg{").append('}').toString();
  }

  public byte[] toByteArray() {
    return CommentMsg.ADAPTER.encode(this);
  }

  public static final CommentMsg parseFrom(byte[] data) throws IOException {
    CommentMsg c = null;
       c = CommentMsg.ADAPTER.decode(data);
    return c;
  }

  /**
   * 发表内容
   */
  public String getText() {
    if(text==null){
        return DEFAULT_TEXT;
    }
    return text;
  }

  /**
   * 接收者信息
   */
  public List<UserInfo> getReceiverList() {
    if(receiver==null){
        return new java.util.ArrayList<UserInfo>();
    }
    return receiver;
  }

  /**
   * 发表内容
   */
  public boolean hasText() {
    return text!=null;
  }

  /**
   * 接收者信息
   */
  public boolean hasReceiverList() {
    return receiver!=null;
  }

  public static final class Builder extends Message.Builder<CommentMsg, Builder> {
    private String text;

    private List<UserInfo> receiver;

    public Builder() {
      receiver = Internal.newMutableList();
    }

    /**
     * 发表内容
     */
    public Builder setText(String text) {
      this.text = text;
      return this;
    }

    /**
     * 接收者信息
     */
    public Builder addAllReceiver(List<UserInfo> receiver) {
      Internal.checkElementsNotNull(receiver);
      this.receiver = receiver;
      return this;
    }

    @Override
    public CommentMsg build() {
      return new CommentMsg(text, receiver, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_CommentMsg extends ProtoAdapter<CommentMsg> {
    public ProtoAdapter_CommentMsg() {
      super(FieldEncoding.LENGTH_DELIMITED, CommentMsg.class);
    }

    @Override
    public int encodedSize(CommentMsg value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.text)
          + UserInfo.ADAPTER.asRepeated().encodedSizeWithTag(2, value.receiver)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, CommentMsg value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.text);
      UserInfo.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.receiver);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public CommentMsg decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setText(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.receiver.add(UserInfo.ADAPTER.decode(reader)); break;
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
    public CommentMsg redact(CommentMsg value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.receiver, UserInfo.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
