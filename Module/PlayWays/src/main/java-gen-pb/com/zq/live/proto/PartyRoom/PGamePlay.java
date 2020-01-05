// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: party_room.proto
package com.zq.live.proto.PartyRoom;

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

public final class PGamePlay extends Message<PGamePlay, PGamePlay.Builder> {
  public static final ProtoAdapter<PGamePlay> ADAPTER = new ProtoAdapter_PGamePlay();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_PLAYID = 0;

  public static final String DEFAULT_PLAYNAME = "";

  public static final String DEFAULT_PLAYCONTENT = "";

  public static final String DEFAULT_PLAYCARD = "";

  public static final String DEFAULT_UPLOADER = "";

  /**
   * 剧本标识
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer playID;

  /**
   * 剧本名称
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String playName;

  /**
   * 剧本内容
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String playContent;

  /**
   * 剧本手卡
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String playCard;

  /**
   * 上传者
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String uploader;

  public PGamePlay(Integer playID, String playName, String playContent, String playCard,
      String uploader) {
    this(playID, playName, playContent, playCard, uploader, ByteString.EMPTY);
  }

  public PGamePlay(Integer playID, String playName, String playContent, String playCard,
      String uploader, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.playID = playID;
    this.playName = playName;
    this.playContent = playContent;
    this.playCard = playCard;
    this.uploader = uploader;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.playID = playID;
    builder.playName = playName;
    builder.playContent = playContent;
    builder.playCard = playCard;
    builder.uploader = uploader;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PGamePlay)) return false;
    PGamePlay o = (PGamePlay) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(playID, o.playID)
        && Internal.equals(playName, o.playName)
        && Internal.equals(playContent, o.playContent)
        && Internal.equals(playCard, o.playCard)
        && Internal.equals(uploader, o.uploader);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (playID != null ? playID.hashCode() : 0);
      result = result * 37 + (playName != null ? playName.hashCode() : 0);
      result = result * 37 + (playContent != null ? playContent.hashCode() : 0);
      result = result * 37 + (playCard != null ? playCard.hashCode() : 0);
      result = result * 37 + (uploader != null ? uploader.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (playID != null) builder.append(", playID=").append(playID);
    if (playName != null) builder.append(", playName=").append(playName);
    if (playContent != null) builder.append(", playContent=").append(playContent);
    if (playCard != null) builder.append(", playCard=").append(playCard);
    if (uploader != null) builder.append(", uploader=").append(uploader);
    return builder.replace(0, 2, "PGamePlay{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PGamePlay.ADAPTER.encode(this);
  }

  public static final PGamePlay parseFrom(byte[] data) throws IOException {
    PGamePlay c = null;
       c = PGamePlay.ADAPTER.decode(data);
    return c;
  }

  /**
   * 剧本标识
   */
  public Integer getPlayID() {
    if(playID==null){
        return DEFAULT_PLAYID;
    }
    return playID;
  }

  /**
   * 剧本名称
   */
  public String getPlayName() {
    if(playName==null){
        return DEFAULT_PLAYNAME;
    }
    return playName;
  }

  /**
   * 剧本内容
   */
  public String getPlayContent() {
    if(playContent==null){
        return DEFAULT_PLAYCONTENT;
    }
    return playContent;
  }

  /**
   * 剧本手卡
   */
  public String getPlayCard() {
    if(playCard==null){
        return DEFAULT_PLAYCARD;
    }
    return playCard;
  }

  /**
   * 上传者
   */
  public String getUploader() {
    if(uploader==null){
        return DEFAULT_UPLOADER;
    }
    return uploader;
  }

  /**
   * 剧本标识
   */
  public boolean hasPlayID() {
    return playID!=null;
  }

  /**
   * 剧本名称
   */
  public boolean hasPlayName() {
    return playName!=null;
  }

  /**
   * 剧本内容
   */
  public boolean hasPlayContent() {
    return playContent!=null;
  }

  /**
   * 剧本手卡
   */
  public boolean hasPlayCard() {
    return playCard!=null;
  }

  /**
   * 上传者
   */
  public boolean hasUploader() {
    return uploader!=null;
  }

  public static final class Builder extends Message.Builder<PGamePlay, Builder> {
    private Integer playID;

    private String playName;

    private String playContent;

    private String playCard;

    private String uploader;

    public Builder() {
    }

    /**
     * 剧本标识
     */
    public Builder setPlayID(Integer playID) {
      this.playID = playID;
      return this;
    }

    /**
     * 剧本名称
     */
    public Builder setPlayName(String playName) {
      this.playName = playName;
      return this;
    }

    /**
     * 剧本内容
     */
    public Builder setPlayContent(String playContent) {
      this.playContent = playContent;
      return this;
    }

    /**
     * 剧本手卡
     */
    public Builder setPlayCard(String playCard) {
      this.playCard = playCard;
      return this;
    }

    /**
     * 上传者
     */
    public Builder setUploader(String uploader) {
      this.uploader = uploader;
      return this;
    }

    @Override
    public PGamePlay build() {
      return new PGamePlay(playID, playName, playContent, playCard, uploader, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PGamePlay extends ProtoAdapter<PGamePlay> {
    public ProtoAdapter_PGamePlay() {
      super(FieldEncoding.LENGTH_DELIMITED, PGamePlay.class);
    }

    @Override
    public int encodedSize(PGamePlay value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.playID)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.playName)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.playContent)
          + ProtoAdapter.STRING.encodedSizeWithTag(4, value.playCard)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.uploader)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PGamePlay value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.playID);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.playName);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.playContent);
      ProtoAdapter.STRING.encodeWithTag(writer, 4, value.playCard);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.uploader);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PGamePlay decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setPlayID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setPlayName(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setPlayContent(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setPlayCard(ProtoAdapter.STRING.decode(reader)); break;
          case 5: builder.setUploader(ProtoAdapter.STRING.decode(reader)); break;
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
    public PGamePlay redact(PGamePlay value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
