// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: broadcast.proto
package com.zq.live.proto.broadcast;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import com.zq.live.proto.Common.EGameModeType;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class PresentGift extends Message<PresentGift, PresentGift.Builder> {
  public static final ProtoAdapter<PresentGift> ADAPTER = new ProtoAdapter_PresentGift();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_ROOMID = 0;

  public static final EGameModeType DEFAULT_MODE = EGameModeType.UnknownMode;

  public static final String DEFAULT_CONTENT = "";

  public static final Boolean DEFAULT_COULDENTER = false;

  public static final String DEFAULT_SOURCEURL = "";

  public static final String DEFAULT_FROMUSERNICKNAME = "";

  public static final String DEFAULT_TOUSERNICKNAME = "";

  public static final String DEFAULT_ENTERSCHEME = "";

  /**
   * 房间id
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer roomID;

  /**
   * 游戏模式
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Common.EGameModeType#ADAPTER"
  )
  private final EGameModeType mode;

  /**
   * 通知内容
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String content;

  /**
   * 是否可以进入
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  private final Boolean couldEnter;

  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String sourceURL;

  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String fromUserNickname;

  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String toUserNickname;

  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  private final String enterScheme;

  public PresentGift(Integer roomID, EGameModeType mode, String content, Boolean couldEnter,
      String sourceURL, String fromUserNickname, String toUserNickname, String enterScheme) {
    this(roomID, mode, content, couldEnter, sourceURL, fromUserNickname, toUserNickname, enterScheme, ByteString.EMPTY);
  }

  public PresentGift(Integer roomID, EGameModeType mode, String content, Boolean couldEnter,
      String sourceURL, String fromUserNickname, String toUserNickname, String enterScheme,
      ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.roomID = roomID;
    this.mode = mode;
    this.content = content;
    this.couldEnter = couldEnter;
    this.sourceURL = sourceURL;
    this.fromUserNickname = fromUserNickname;
    this.toUserNickname = toUserNickname;
    this.enterScheme = enterScheme;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.roomID = roomID;
    builder.mode = mode;
    builder.content = content;
    builder.couldEnter = couldEnter;
    builder.sourceURL = sourceURL;
    builder.fromUserNickname = fromUserNickname;
    builder.toUserNickname = toUserNickname;
    builder.enterScheme = enterScheme;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PresentGift)) return false;
    PresentGift o = (PresentGift) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(roomID, o.roomID)
        && Internal.equals(mode, o.mode)
        && Internal.equals(content, o.content)
        && Internal.equals(couldEnter, o.couldEnter)
        && Internal.equals(sourceURL, o.sourceURL)
        && Internal.equals(fromUserNickname, o.fromUserNickname)
        && Internal.equals(toUserNickname, o.toUserNickname)
        && Internal.equals(enterScheme, o.enterScheme);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (roomID != null ? roomID.hashCode() : 0);
      result = result * 37 + (mode != null ? mode.hashCode() : 0);
      result = result * 37 + (content != null ? content.hashCode() : 0);
      result = result * 37 + (couldEnter != null ? couldEnter.hashCode() : 0);
      result = result * 37 + (sourceURL != null ? sourceURL.hashCode() : 0);
      result = result * 37 + (fromUserNickname != null ? fromUserNickname.hashCode() : 0);
      result = result * 37 + (toUserNickname != null ? toUserNickname.hashCode() : 0);
      result = result * 37 + (enterScheme != null ? enterScheme.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (roomID != null) builder.append(", roomID=").append(roomID);
    if (mode != null) builder.append(", mode=").append(mode);
    if (content != null) builder.append(", content=").append(content);
    if (couldEnter != null) builder.append(", couldEnter=").append(couldEnter);
    if (sourceURL != null) builder.append(", sourceURL=").append(sourceURL);
    if (fromUserNickname != null) builder.append(", fromUserNickname=").append(fromUserNickname);
    if (toUserNickname != null) builder.append(", toUserNickname=").append(toUserNickname);
    if (enterScheme != null) builder.append(", enterScheme=").append(enterScheme);
    return builder.replace(0, 2, "PresentGift{").append('}').toString();
  }

  public byte[] toByteArray() {
    return PresentGift.ADAPTER.encode(this);
  }

  public static final PresentGift parseFrom(byte[] data) throws IOException {
    PresentGift c = null;
       c = PresentGift.ADAPTER.decode(data);
    return c;
  }

  /**
   * 房间id
   */
  public Integer getRoomID() {
    if(roomID==null){
        return DEFAULT_ROOMID;
    }
    return roomID;
  }

  /**
   * 游戏模式
   */
  public EGameModeType getMode() {
    if(mode==null){
        return new EGameModeType.Builder().build();
    }
    return mode;
  }

  /**
   * 通知内容
   */
  public String getContent() {
    if(content==null){
        return DEFAULT_CONTENT;
    }
    return content;
  }

  /**
   * 是否可以进入
   */
  public Boolean getCouldEnter() {
    if(couldEnter==null){
        return DEFAULT_COULDENTER;
    }
    return couldEnter;
  }

  public String getSourceURL() {
    if(sourceURL==null){
        return DEFAULT_SOURCEURL;
    }
    return sourceURL;
  }

  public String getFromUserNickname() {
    if(fromUserNickname==null){
        return DEFAULT_FROMUSERNICKNAME;
    }
    return fromUserNickname;
  }

  public String getToUserNickname() {
    if(toUserNickname==null){
        return DEFAULT_TOUSERNICKNAME;
    }
    return toUserNickname;
  }

  public String getEnterScheme() {
    if(enterScheme==null){
        return DEFAULT_ENTERSCHEME;
    }
    return enterScheme;
  }

  /**
   * 房间id
   */
  public boolean hasRoomID() {
    return roomID!=null;
  }

  /**
   * 游戏模式
   */
  public boolean hasMode() {
    return mode!=null;
  }

  /**
   * 通知内容
   */
  public boolean hasContent() {
    return content!=null;
  }

  /**
   * 是否可以进入
   */
  public boolean hasCouldEnter() {
    return couldEnter!=null;
  }

  public boolean hasSourceURL() {
    return sourceURL!=null;
  }

  public boolean hasFromUserNickname() {
    return fromUserNickname!=null;
  }

  public boolean hasToUserNickname() {
    return toUserNickname!=null;
  }

  public boolean hasEnterScheme() {
    return enterScheme!=null;
  }

  public static final class Builder extends Message.Builder<PresentGift, Builder> {
    private Integer roomID;

    private EGameModeType mode;

    private String content;

    private Boolean couldEnter;

    private String sourceURL;

    private String fromUserNickname;

    private String toUserNickname;

    private String enterScheme;

    public Builder() {
    }

    /**
     * 房间id
     */
    public Builder setRoomID(Integer roomID) {
      this.roomID = roomID;
      return this;
    }

    /**
     * 游戏模式
     */
    public Builder setMode(EGameModeType mode) {
      this.mode = mode;
      return this;
    }

    /**
     * 通知内容
     */
    public Builder setContent(String content) {
      this.content = content;
      return this;
    }

    /**
     * 是否可以进入
     */
    public Builder setCouldEnter(Boolean couldEnter) {
      this.couldEnter = couldEnter;
      return this;
    }

    public Builder setSourceURL(String sourceURL) {
      this.sourceURL = sourceURL;
      return this;
    }

    public Builder setFromUserNickname(String fromUserNickname) {
      this.fromUserNickname = fromUserNickname;
      return this;
    }

    public Builder setToUserNickname(String toUserNickname) {
      this.toUserNickname = toUserNickname;
      return this;
    }

    public Builder setEnterScheme(String enterScheme) {
      this.enterScheme = enterScheme;
      return this;
    }

    @Override
    public PresentGift build() {
      return new PresentGift(roomID, mode, content, couldEnter, sourceURL, fromUserNickname, toUserNickname, enterScheme, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_PresentGift extends ProtoAdapter<PresentGift> {
    public ProtoAdapter_PresentGift() {
      super(FieldEncoding.LENGTH_DELIMITED, PresentGift.class);
    }

    @Override
    public int encodedSize(PresentGift value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.roomID)
          + EGameModeType.ADAPTER.encodedSizeWithTag(2, value.mode)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.content)
          + ProtoAdapter.BOOL.encodedSizeWithTag(4, value.couldEnter)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.sourceURL)
          + ProtoAdapter.STRING.encodedSizeWithTag(6, value.fromUserNickname)
          + ProtoAdapter.STRING.encodedSizeWithTag(7, value.toUserNickname)
          + ProtoAdapter.STRING.encodedSizeWithTag(8, value.enterScheme)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, PresentGift value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.roomID);
      EGameModeType.ADAPTER.encodeWithTag(writer, 2, value.mode);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.content);
      ProtoAdapter.BOOL.encodeWithTag(writer, 4, value.couldEnter);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.sourceURL);
      ProtoAdapter.STRING.encodeWithTag(writer, 6, value.fromUserNickname);
      ProtoAdapter.STRING.encodeWithTag(writer, 7, value.toUserNickname);
      ProtoAdapter.STRING.encodeWithTag(writer, 8, value.enterScheme);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public PresentGift decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setRoomID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: {
            try {
              builder.setMode(EGameModeType.ADAPTER.decode(reader));
            } catch (ProtoAdapter.EnumConstantNotFoundException e) {
              builder.addUnknownField(tag, FieldEncoding.VARINT, (long) e.value);
            }
            break;
          }
          case 3: builder.setContent(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setCouldEnter(ProtoAdapter.BOOL.decode(reader)); break;
          case 5: builder.setSourceURL(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.setFromUserNickname(ProtoAdapter.STRING.decode(reader)); break;
          case 7: builder.setToUserNickname(ProtoAdapter.STRING.decode(reader)); break;
          case 8: builder.setEnterScheme(ProtoAdapter.STRING.decode(reader)); break;
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
    public PresentGift redact(PresentGift value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
