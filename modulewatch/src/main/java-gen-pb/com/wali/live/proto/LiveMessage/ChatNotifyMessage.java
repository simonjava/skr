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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

/**
 * 私信通知消息
 */
public final class ChatNotifyMessage extends Message<ChatNotifyMessage, ChatNotifyMessage.Builder> {
  public static final ProtoAdapter<ChatNotifyMessage> ADAPTER = new ProtoAdapter_ChatNotifyMessage();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_FOLLOW_TYPE = 0;

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer follow_type;

  public ChatNotifyMessage(Integer follow_type) {
    this(follow_type, ByteString.EMPTY);
  }

  public ChatNotifyMessage(Integer follow_type, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.follow_type = follow_type;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.follow_type = follow_type;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ChatNotifyMessage)) return false;
    ChatNotifyMessage o = (ChatNotifyMessage) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(follow_type, o.follow_type);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (follow_type != null ? follow_type.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (follow_type != null) builder.append(", follow_type=").append(follow_type);
    return builder.replace(0, 2, "ChatNotifyMessage{").append('}').toString();
  }

  public byte[] toByteArray() {
    return ChatNotifyMessage.ADAPTER.encode(this);
  }

  public static final ChatNotifyMessage parseFrom(byte[] data) throws IOException {
    ChatNotifyMessage c = null;
       c = ChatNotifyMessage.ADAPTER.decode(data);
    return c;
  }

  public Integer getFollowType() {
    if(follow_type==null){
        return DEFAULT_FOLLOW_TYPE;
    }
    return follow_type;
  }

  public boolean hasFollowType() {
    return follow_type!=null;
  }

  public static final class Builder extends Message.Builder<ChatNotifyMessage, Builder> {
    public Integer follow_type;

    public Builder() {
    }

    public Builder setFollowType(Integer follow_type) {
      this.follow_type = follow_type;
      return this;
    }

    @Override
    public ChatNotifyMessage build() {
      return new ChatNotifyMessage(follow_type, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_ChatNotifyMessage extends ProtoAdapter<ChatNotifyMessage> {
    public ProtoAdapter_ChatNotifyMessage() {
      super(FieldEncoding.LENGTH_DELIMITED, ChatNotifyMessage.class);
    }

    @Override
    public int encodedSize(ChatNotifyMessage value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.follow_type)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, ChatNotifyMessage value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.follow_type);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public ChatNotifyMessage decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setFollowType(ProtoAdapter.UINT32.decode(reader)); break;
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
    public ChatNotifyMessage redact(ChatNotifyMessage value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
