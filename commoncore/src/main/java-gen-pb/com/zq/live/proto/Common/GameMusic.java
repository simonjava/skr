// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: Common.proto
package com.zq.live.proto.Common;

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

public final class GameMusic extends Message<GameMusic, GameMusic.Builder> {
  public static final ProtoAdapter<GameMusic> ADAPTER = new ProtoAdapter_GameMusic();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_TITLE = "";

  public static final String DEFAULT_CONTENT = "";

  public static final String DEFAULT_EXAMPLE = "";

  /**
   * 标题
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String title;

  /**
   * 内容
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String content;

  /**
   * 举例
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String example;

  public GameMusic(String title, String content, String example) {
    this(title, content, example, ByteString.EMPTY);
  }

  public GameMusic(String title, String content, String example, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.title = title;
    this.content = content;
    this.example = example;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.title = title;
    builder.content = content;
    builder.example = example;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GameMusic)) return false;
    GameMusic o = (GameMusic) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(title, o.title)
        && Internal.equals(content, o.content)
        && Internal.equals(example, o.example);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (content != null ? content.hashCode() : 0);
      result = result * 37 + (example != null ? example.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (title != null) builder.append(", title=").append(title);
    if (content != null) builder.append(", content=").append(content);
    if (example != null) builder.append(", example=").append(example);
    return builder.replace(0, 2, "GameMusic{").append('}').toString();
  }

  public byte[] toByteArray() {
    return GameMusic.ADAPTER.encode(this);
  }

  public static final GameMusic parseFrom(byte[] data) throws IOException {
    GameMusic c = null;
       c = GameMusic.ADAPTER.decode(data);
    return c;
  }

  /**
   * 标题
   */
  public String getTitle() {
    if(title==null){
        return DEFAULT_TITLE;
    }
    return title;
  }

  /**
   * 内容
   */
  public String getContent() {
    if(content==null){
        return DEFAULT_CONTENT;
    }
    return content;
  }

  /**
   * 举例
   */
  public String getExample() {
    if(example==null){
        return DEFAULT_EXAMPLE;
    }
    return example;
  }

  /**
   * 标题
   */
  public boolean hasTitle() {
    return title!=null;
  }

  /**
   * 内容
   */
  public boolean hasContent() {
    return content!=null;
  }

  /**
   * 举例
   */
  public boolean hasExample() {
    return example!=null;
  }

  public static final class Builder extends Message.Builder<GameMusic, Builder> {
    private String title;

    private String content;

    private String example;

    public Builder() {
    }

    /**
     * 标题
     */
    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    /**
     * 内容
     */
    public Builder setContent(String content) {
      this.content = content;
      return this;
    }

    /**
     * 举例
     */
    public Builder setExample(String example) {
      this.example = example;
      return this;
    }

    @Override
    public GameMusic build() {
      return new GameMusic(title, content, example, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GameMusic extends ProtoAdapter<GameMusic> {
    public ProtoAdapter_GameMusic() {
      super(FieldEncoding.LENGTH_DELIMITED, GameMusic.class);
    }

    @Override
    public int encodedSize(GameMusic value) {
      return ProtoAdapter.STRING.encodedSizeWithTag(1, value.title)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.content)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.example)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GameMusic value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.title);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.content);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.example);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GameMusic decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setTitle(ProtoAdapter.STRING.decode(reader)); break;
          case 2: builder.setContent(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setExample(ProtoAdapter.STRING.decode(reader)); break;
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
    public GameMusic redact(GameMusic value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
