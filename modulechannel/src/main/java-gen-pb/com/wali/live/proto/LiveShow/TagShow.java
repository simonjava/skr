// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: LiveShow.proto
package com.wali.live.proto.LiveShow;

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
import java.util.List;
import okio.ByteString;

/**
 * 一组带标签的直播记录
 */
public final class TagShow extends Message<TagShow, TagShow.Builder> {
  public static final ProtoAdapter<TagShow> ADAPTER = new ProtoAdapter_TagShow();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_TAGNAME = "";

  /**
   * 标签名称
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String tagName;

  /**
   * 直播列表集
   */
  @WireField(
      tag = 2,
      adapter = "com.wali.live.proto.LiveShow.LiveShow#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<LiveShow> lives;

  public TagShow(String tagName, List<LiveShow> lives) {
    this(tagName, lives, ByteString.EMPTY);
  }

  public TagShow(String tagName, List<LiveShow> lives, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.tagName = tagName;
    this.lives = Internal.immutableCopyOf("lives", lives);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.tagName = tagName;
    builder.lives = Internal.copyOf("lives", lives);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof TagShow)) return false;
    TagShow o = (TagShow) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(tagName, o.tagName)
        && lives.equals(o.lives);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (tagName != null ? tagName.hashCode() : 0);
      result = result * 37 + lives.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (tagName != null) builder.append(", tagName=").append(tagName);
    if (!lives.isEmpty()) builder.append(", lives=").append(lives);
    return builder.replace(0, 2, "TagShow{").append('}').toString();
  }

  public byte[] toByteArray() {
    return TagShow.ADAPTER.encode(this);
  }

  public static final TagShow parseFrom(byte[] data) throws IOException {
    TagShow c = null;
       c = TagShow.ADAPTER.decode(data);
    return c;
  }

  /**
   * 标签名称
   */
  public String getTagName() {
    if(tagName==null){
        return DEFAULT_TAGNAME;
    }
    return tagName;
  }

  /**
   * 直播列表集
   */
  public List<LiveShow> getLivesList() {
    if(lives==null){
        return new java.util.ArrayList<LiveShow>();
    }
    return lives;
  }

  /**
   * 标签名称
   */
  public boolean hasTagName() {
    return tagName!=null;
  }

  /**
   * 直播列表集
   */
  public boolean hasLivesList() {
    return lives!=null;
  }

  public static final class Builder extends Message.Builder<TagShow, Builder> {
    public String tagName;

    public List<LiveShow> lives;

    public Builder() {
      lives = Internal.newMutableList();
    }

    /**
     * 标签名称
     */
    public Builder setTagName(String tagName) {
      this.tagName = tagName;
      return this;
    }

    /**
     * 直播列表集
     */
    public Builder addAllLives(List<LiveShow> lives) {
      Internal.checkElementsNotNull(lives);
      this.lives = lives;
      return this;
    }

    @Override
    public TagShow build() {
      return new TagShow(tagName, lives, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_TagShow extends ProtoAdapter<TagShow> {
    public ProtoAdapter_TagShow() {
      super(FieldEncoding.LENGTH_DELIMITED, TagShow.class);
    }

    @Override
    public int encodedSize(TagShow value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.tagName)
          + LiveShow.ADAPTER.asRepeated().encodedSizeWithTag(2, value.lives)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, TagShow value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.tagName);
      LiveShow.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.lives);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public TagShow decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setTagName(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.lives.add(LiveShow.ADAPTER.decode(reader)); break;
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
    public TagShow redact(TagShow value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.lives, LiveShow.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
