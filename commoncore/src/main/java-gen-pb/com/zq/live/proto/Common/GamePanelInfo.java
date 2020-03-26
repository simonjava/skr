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
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

public final class GamePanelInfo extends Message<GamePanelInfo, GamePanelInfo.Builder> {
  public static final ProtoAdapter<GamePanelInfo> ADAPTER = new ProtoAdapter_GamePanelInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_PANELSEQ = 0;

  /**
   * 面板序号
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  private final Integer panelSeq;

  /**
   * 游戏条目信息
   */
  @WireField(
      tag = 2,
      adapter = "com.zq.live.proto.Common.GameItemInfo#ADAPTER",
      label = WireField.Label.REPEATED
  )
  private final List<GameItemInfo> items;

  public GamePanelInfo(Integer panelSeq, List<GameItemInfo> items) {
    this(panelSeq, items, ByteString.EMPTY);
  }

  public GamePanelInfo(Integer panelSeq, List<GameItemInfo> items, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.panelSeq = panelSeq;
    this.items = Internal.immutableCopyOf("items", items);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.panelSeq = panelSeq;
    builder.items = Internal.copyOf("items", items);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GamePanelInfo)) return false;
    GamePanelInfo o = (GamePanelInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(panelSeq, o.panelSeq)
        && items.equals(o.items);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (panelSeq != null ? panelSeq.hashCode() : 0);
      result = result * 37 + items.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (panelSeq != null) builder.append(", panelSeq=").append(panelSeq);
    if (!items.isEmpty()) builder.append(", items=").append(items);
    return builder.replace(0, 2, "GamePanelInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return GamePanelInfo.ADAPTER.encode(this);
  }

  public static final GamePanelInfo parseFrom(byte[] data) throws IOException {
    GamePanelInfo c = null;
       c = GamePanelInfo.ADAPTER.decode(data);
    return c;
  }

  /**
   * 面板序号
   */
  public Integer getPanelSeq() {
    if(panelSeq==null){
        return DEFAULT_PANELSEQ;
    }
    return panelSeq;
  }

  /**
   * 游戏条目信息
   */
  public List<GameItemInfo> getItemsList() {
    if(items==null){
        return new java.util.ArrayList<GameItemInfo>();
    }
    return items;
  }

  /**
   * 面板序号
   */
  public boolean hasPanelSeq() {
    return panelSeq!=null;
  }

  /**
   * 游戏条目信息
   */
  public boolean hasItemsList() {
    return items!=null;
  }

  public static final class Builder extends Message.Builder<GamePanelInfo, Builder> {
    private Integer panelSeq;

    private List<GameItemInfo> items;

    public Builder() {
      items = Internal.newMutableList();
    }

    /**
     * 面板序号
     */
    public Builder setPanelSeq(Integer panelSeq) {
      this.panelSeq = panelSeq;
      return this;
    }

    /**
     * 游戏条目信息
     */
    public Builder addAllItems(List<GameItemInfo> items) {
      Internal.checkElementsNotNull(items);
      this.items = items;
      return this;
    }

    @Override
    public GamePanelInfo build() {
      return new GamePanelInfo(panelSeq, items, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_GamePanelInfo extends ProtoAdapter<GamePanelInfo> {
    public ProtoAdapter_GamePanelInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, GamePanelInfo.class);
    }

    @Override
    public int encodedSize(GamePanelInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.panelSeq)
          + GameItemInfo.ADAPTER.asRepeated().encodedSizeWithTag(2, value.items)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, GamePanelInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.panelSeq);
      GameItemInfo.ADAPTER.asRepeated().encodeWithTag(writer, 2, value.items);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public GamePanelInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setPanelSeq(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.items.add(GameItemInfo.ADAPTER.decode(reader)); break;
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
    public GamePanelInfo redact(GamePanelInfo value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.items, GameItemInfo.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
