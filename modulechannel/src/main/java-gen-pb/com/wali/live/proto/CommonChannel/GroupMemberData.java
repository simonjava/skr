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
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class GroupMemberData extends Message<GroupMemberData, GroupMemberData.Builder> {
  public static final ProtoAdapter<GroupMemberData> ADAPTER = new ProtoAdapter_GroupMemberData();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_LIVECOVER = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String liveCover;

  public GroupMemberData(String liveCover) {
    this(liveCover, ByteString.EMPTY);
  }

  public GroupMemberData(String liveCover, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.liveCover = liveCover;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.liveCover = liveCover;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GroupMemberData)) return false;
    GroupMemberData o = (GroupMemberData) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(liveCover, o.liveCover);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (liveCover != null ? liveCover.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (liveCover != null) builder.append(", liveCover=").append(liveCover);
    return builder.replace(0, 2, "GroupMemberData{").append('}').toString();
  }

  public byte[] toByteArray() {
    return GroupMemberData.ADAPTER.encode(this);
  }

  public static final GroupMemberData parseFrom(byte[] data) throws IOException {
    GroupMemberData c = null;
       c = GroupMemberData.ADAPTER.decode(data);
    return c;
  }

  public String getLiveCover() {
    if(liveCover==null){
        return DEFAULT_LIVECOVER;
    }
    return liveCover;
  }

  public boolean hasLiveCover() {
    return liveCover!=null;
  }

  public static final class Builder extends Message.Builder<GroupMemberData, Builder> {
    public String liveCover;

    public Builder() {
    }

    public Builder setLiveCover(String liveCover) {
      this.liveCover = liveCover;
      return this;
    }

    @Override
    public GroupMemberData build() {
      return new GroupMemberData(liveCover, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GroupMemberData extends ProtoAdapter<GroupMemberData> {
    public ProtoAdapter_GroupMemberData() {
      super(FieldEncoding.LENGTH_DELIMITED, GroupMemberData.class);
    }

    @Override
    public int encodedSize(GroupMemberData value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.liveCover)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GroupMemberData value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.liveCover);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GroupMemberData decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setLiveCover(ProtoAdapter.STRING.decode(reader)); break;
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
    public GroupMemberData redact(GroupMemberData value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
