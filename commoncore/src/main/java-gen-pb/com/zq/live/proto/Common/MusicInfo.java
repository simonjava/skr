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
import okio.ByteString;

/**
 * 音乐信息
 */
public final class MusicInfo extends Message<MusicInfo, MusicInfo.Builder> {
  public static final ProtoAdapter<MusicInfo> ADAPTER = new ProtoAdapter_MusicInfo();

  private static final long serialVersionUID = 0L;

  public static final Integer DEFAULT_ITEMID = 0;

  public static final String DEFAULT_ITEMNAME = "";

  public static final String DEFAULT_COVER = "";

  public static final String DEFAULT_OWNER = "";

  public static final String DEFAULT_LYRIC = "";

  public static final String DEFAULT_ORI = "";

  public static final String DEFAULT_ACC = "";

  public static final String DEFAULT_MIDI = "";

  public static final String DEFAULT_ZIP = "";

  public static final Integer DEFAULT_TOTALTIMEMS = 0;

  public static final Integer DEFAULT_BEGINTIMEMS = 0;

  public static final Integer DEFAULT_ENDTIMEMS = 0;

  /**
   * 音乐条目标识
   */
  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer itemID;

  /**
   * 音乐条目名称
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String itemName;

  /**
   * 音乐条目封页图片
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String cover;

  /**
   * 音乐条目所属（歌手）
   */
  @WireField(
      tag = 4,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String owner;

  /**
   * 音乐条目对应歌词
   */
  @WireField(
      tag = 5,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String lyric;

  /**
   * 原唱:origin
   */
  @WireField(
      tag = 6,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String ori;

  /**
   * 伴奏:accompany
   */
  @WireField(
      tag = 7,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String acc;

  /**
   * 音乐条目对应midi文件
   */
  @WireField(
      tag = 8,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String midi;

  /**
   * 资源压缩文件
   */
  @WireField(
      tag = 9,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String zip;

  /**
   * 共计多少毫秒
   */
  @WireField(
      tag = 10,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer totalTimeMs;

  /**
   * 开始毫秒
   */
  @WireField(
      tag = 11,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer beginTimeMs;

  /**
   * 结束毫秒
   */
  @WireField(
      tag = 12,
      adapter = "com.squareup.wire.ProtoAdapter#UINT32"
  )
  public final Integer endTimeMs;

  public MusicInfo(Integer itemID, String itemName, String cover, String owner, String lyric,
      String ori, String acc, String midi, String zip, Integer totalTimeMs, Integer beginTimeMs,
      Integer endTimeMs) {
    this(itemID, itemName, cover, owner, lyric, ori, acc, midi, zip, totalTimeMs, beginTimeMs, endTimeMs, ByteString.EMPTY);
  }

  public MusicInfo(Integer itemID, String itemName, String cover, String owner, String lyric,
      String ori, String acc, String midi, String zip, Integer totalTimeMs, Integer beginTimeMs,
      Integer endTimeMs, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.itemID = itemID;
    this.itemName = itemName;
    this.cover = cover;
    this.owner = owner;
    this.lyric = lyric;
    this.ori = ori;
    this.acc = acc;
    this.midi = midi;
    this.zip = zip;
    this.totalTimeMs = totalTimeMs;
    this.beginTimeMs = beginTimeMs;
    this.endTimeMs = endTimeMs;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.itemID = itemID;
    builder.itemName = itemName;
    builder.cover = cover;
    builder.owner = owner;
    builder.lyric = lyric;
    builder.ori = ori;
    builder.acc = acc;
    builder.midi = midi;
    builder.zip = zip;
    builder.totalTimeMs = totalTimeMs;
    builder.beginTimeMs = beginTimeMs;
    builder.endTimeMs = endTimeMs;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MusicInfo)) return false;
    MusicInfo o = (MusicInfo) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(itemID, o.itemID)
        && Internal.equals(itemName, o.itemName)
        && Internal.equals(cover, o.cover)
        && Internal.equals(owner, o.owner)
        && Internal.equals(lyric, o.lyric)
        && Internal.equals(ori, o.ori)
        && Internal.equals(acc, o.acc)
        && Internal.equals(midi, o.midi)
        && Internal.equals(zip, o.zip)
        && Internal.equals(totalTimeMs, o.totalTimeMs)
        && Internal.equals(beginTimeMs, o.beginTimeMs)
        && Internal.equals(endTimeMs, o.endTimeMs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (itemID != null ? itemID.hashCode() : 0);
      result = result * 37 + (itemName != null ? itemName.hashCode() : 0);
      result = result * 37 + (cover != null ? cover.hashCode() : 0);
      result = result * 37 + (owner != null ? owner.hashCode() : 0);
      result = result * 37 + (lyric != null ? lyric.hashCode() : 0);
      result = result * 37 + (ori != null ? ori.hashCode() : 0);
      result = result * 37 + (acc != null ? acc.hashCode() : 0);
      result = result * 37 + (midi != null ? midi.hashCode() : 0);
      result = result * 37 + (zip != null ? zip.hashCode() : 0);
      result = result * 37 + (totalTimeMs != null ? totalTimeMs.hashCode() : 0);
      result = result * 37 + (beginTimeMs != null ? beginTimeMs.hashCode() : 0);
      result = result * 37 + (endTimeMs != null ? endTimeMs.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (itemID != null) builder.append(", itemID=").append(itemID);
    if (itemName != null) builder.append(", itemName=").append(itemName);
    if (cover != null) builder.append(", cover=").append(cover);
    if (owner != null) builder.append(", owner=").append(owner);
    if (lyric != null) builder.append(", lyric=").append(lyric);
    if (ori != null) builder.append(", ori=").append(ori);
    if (acc != null) builder.append(", acc=").append(acc);
    if (midi != null) builder.append(", midi=").append(midi);
    if (zip != null) builder.append(", zip=").append(zip);
    if (totalTimeMs != null) builder.append(", totalTimeMs=").append(totalTimeMs);
    if (beginTimeMs != null) builder.append(", beginTimeMs=").append(beginTimeMs);
    if (endTimeMs != null) builder.append(", endTimeMs=").append(endTimeMs);
    return builder.replace(0, 2, "MusicInfo{").append('}').toString();
  }

  public byte[] toByteArray() {
    return MusicInfo.ADAPTER.encode(this);
  }

  public static final MusicInfo parseFrom(byte[] data) throws IOException {
    MusicInfo c = null;
       c = MusicInfo.ADAPTER.decode(data);
    return c;
  }

  /**
   * 音乐条目标识
   */
  public Integer getItemID() {
    if(itemID==null){
        return DEFAULT_ITEMID;
    }
    return itemID;
  }

  /**
   * 音乐条目名称
   */
  public String getItemName() {
    if(itemName==null){
        return DEFAULT_ITEMNAME;
    }
    return itemName;
  }

  /**
   * 音乐条目封页图片
   */
  public String getCover() {
    if(cover==null){
        return DEFAULT_COVER;
    }
    return cover;
  }

  /**
   * 音乐条目所属（歌手）
   */
  public String getOwner() {
    if(owner==null){
        return DEFAULT_OWNER;
    }
    return owner;
  }

  /**
   * 音乐条目对应歌词
   */
  public String getLyric() {
    if(lyric==null){
        return DEFAULT_LYRIC;
    }
    return lyric;
  }

  /**
   * 原唱:origin
   */
  public String getOri() {
    if(ori==null){
        return DEFAULT_ORI;
    }
    return ori;
  }

  /**
   * 伴奏:accompany
   */
  public String getAcc() {
    if(acc==null){
        return DEFAULT_ACC;
    }
    return acc;
  }

  /**
   * 音乐条目对应midi文件
   */
  public String getMidi() {
    if(midi==null){
        return DEFAULT_MIDI;
    }
    return midi;
  }

  /**
   * 资源压缩文件
   */
  public String getZip() {
    if(zip==null){
        return DEFAULT_ZIP;
    }
    return zip;
  }

  /**
   * 共计多少毫秒
   */
  public Integer getTotalTimeMs() {
    if(totalTimeMs==null){
        return DEFAULT_TOTALTIMEMS;
    }
    return totalTimeMs;
  }

  /**
   * 开始毫秒
   */
  public Integer getBeginTimeMs() {
    if(beginTimeMs==null){
        return DEFAULT_BEGINTIMEMS;
    }
    return beginTimeMs;
  }

  /**
   * 结束毫秒
   */
  public Integer getEndTimeMs() {
    if(endTimeMs==null){
        return DEFAULT_ENDTIMEMS;
    }
    return endTimeMs;
  }

  /**
   * 音乐条目标识
   */
  public boolean hasItemID() {
    return itemID!=null;
  }

  /**
   * 音乐条目名称
   */
  public boolean hasItemName() {
    return itemName!=null;
  }

  /**
   * 音乐条目封页图片
   */
  public boolean hasCover() {
    return cover!=null;
  }

  /**
   * 音乐条目所属（歌手）
   */
  public boolean hasOwner() {
    return owner!=null;
  }

  /**
   * 音乐条目对应歌词
   */
  public boolean hasLyric() {
    return lyric!=null;
  }

  /**
   * 原唱:origin
   */
  public boolean hasOri() {
    return ori!=null;
  }

  /**
   * 伴奏:accompany
   */
  public boolean hasAcc() {
    return acc!=null;
  }

  /**
   * 音乐条目对应midi文件
   */
  public boolean hasMidi() {
    return midi!=null;
  }

  /**
   * 资源压缩文件
   */
  public boolean hasZip() {
    return zip!=null;
  }

  /**
   * 共计多少毫秒
   */
  public boolean hasTotalTimeMs() {
    return totalTimeMs!=null;
  }

  /**
   * 开始毫秒
   */
  public boolean hasBeginTimeMs() {
    return beginTimeMs!=null;
  }

  /**
   * 结束毫秒
   */
  public boolean hasEndTimeMs() {
    return endTimeMs!=null;
  }

  public static final class Builder extends Message.Builder<MusicInfo, Builder> {
    public Integer itemID;

    public String itemName;

    public String cover;

    public String owner;

    public String lyric;

    public String ori;

    public String acc;

    public String midi;

    public String zip;

    public Integer totalTimeMs;

    public Integer beginTimeMs;

    public Integer endTimeMs;

    public Builder() {
    }

    /**
     * 音乐条目标识
     */
    public Builder setItemID(Integer itemID) {
      this.itemID = itemID;
      return this;
    }

    /**
     * 音乐条目名称
     */
    public Builder setItemName(String itemName) {
      this.itemName = itemName;
      return this;
    }

    /**
     * 音乐条目封页图片
     */
    public Builder setCover(String cover) {
      this.cover = cover;
      return this;
    }

    /**
     * 音乐条目所属（歌手）
     */
    public Builder setOwner(String owner) {
      this.owner = owner;
      return this;
    }

    /**
     * 音乐条目对应歌词
     */
    public Builder setLyric(String lyric) {
      this.lyric = lyric;
      return this;
    }

    /**
     * 原唱:origin
     */
    public Builder setOri(String ori) {
      this.ori = ori;
      return this;
    }

    /**
     * 伴奏:accompany
     */
    public Builder setAcc(String acc) {
      this.acc = acc;
      return this;
    }

    /**
     * 音乐条目对应midi文件
     */
    public Builder setMidi(String midi) {
      this.midi = midi;
      return this;
    }

    /**
     * 资源压缩文件
     */
    public Builder setZip(String zip) {
      this.zip = zip;
      return this;
    }

    /**
     * 共计多少毫秒
     */
    public Builder setTotalTimeMs(Integer totalTimeMs) {
      this.totalTimeMs = totalTimeMs;
      return this;
    }

    /**
     * 开始毫秒
     */
    public Builder setBeginTimeMs(Integer beginTimeMs) {
      this.beginTimeMs = beginTimeMs;
      return this;
    }

    /**
     * 结束毫秒
     */
    public Builder setEndTimeMs(Integer endTimeMs) {
      this.endTimeMs = endTimeMs;
      return this;
    }

    @Override
    public MusicInfo build() {
      return new MusicInfo(itemID, itemName, cover, owner, lyric, ori, acc, midi, zip, totalTimeMs, beginTimeMs, endTimeMs, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_MusicInfo extends ProtoAdapter<MusicInfo> {
    public ProtoAdapter_MusicInfo() {
      super(FieldEncoding.LENGTH_DELIMITED, MusicInfo.class);
    }

    @Override
    public int encodedSize(MusicInfo value) {
      return ProtoAdapter.UINT32.encodedSizeWithTag(1, value.itemID)
          + ProtoAdapter.STRING.encodedSizeWithTag(2, value.itemName)
          + ProtoAdapter.STRING.encodedSizeWithTag(3, value.cover)
          + ProtoAdapter.STRING.encodedSizeWithTag(4, value.owner)
          + ProtoAdapter.STRING.encodedSizeWithTag(5, value.lyric)
          + ProtoAdapter.STRING.encodedSizeWithTag(6, value.ori)
          + ProtoAdapter.STRING.encodedSizeWithTag(7, value.acc)
          + ProtoAdapter.STRING.encodedSizeWithTag(8, value.midi)
          + ProtoAdapter.STRING.encodedSizeWithTag(9, value.zip)
          + ProtoAdapter.UINT32.encodedSizeWithTag(10, value.totalTimeMs)
          + ProtoAdapter.UINT32.encodedSizeWithTag(11, value.beginTimeMs)
          + ProtoAdapter.UINT32.encodedSizeWithTag(12, value.endTimeMs)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MusicInfo value) throws IOException {
      ProtoAdapter.UINT32.encodeWithTag(writer, 1, value.itemID);
      ProtoAdapter.STRING.encodeWithTag(writer, 2, value.itemName);
      ProtoAdapter.STRING.encodeWithTag(writer, 3, value.cover);
      ProtoAdapter.STRING.encodeWithTag(writer, 4, value.owner);
      ProtoAdapter.STRING.encodeWithTag(writer, 5, value.lyric);
      ProtoAdapter.STRING.encodeWithTag(writer, 6, value.ori);
      ProtoAdapter.STRING.encodeWithTag(writer, 7, value.acc);
      ProtoAdapter.STRING.encodeWithTag(writer, 8, value.midi);
      ProtoAdapter.STRING.encodeWithTag(writer, 9, value.zip);
      ProtoAdapter.UINT32.encodeWithTag(writer, 10, value.totalTimeMs);
      ProtoAdapter.UINT32.encodeWithTag(writer, 11, value.beginTimeMs);
      ProtoAdapter.UINT32.encodeWithTag(writer, 12, value.endTimeMs);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MusicInfo decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.setItemID(ProtoAdapter.UINT32.decode(reader)); break;
          case 2: builder.setItemName(ProtoAdapter.STRING.decode(reader)); break;
          case 3: builder.setCover(ProtoAdapter.STRING.decode(reader)); break;
          case 4: builder.setOwner(ProtoAdapter.STRING.decode(reader)); break;
          case 5: builder.setLyric(ProtoAdapter.STRING.decode(reader)); break;
          case 6: builder.setOri(ProtoAdapter.STRING.decode(reader)); break;
          case 7: builder.setAcc(ProtoAdapter.STRING.decode(reader)); break;
          case 8: builder.setMidi(ProtoAdapter.STRING.decode(reader)); break;
          case 9: builder.setZip(ProtoAdapter.STRING.decode(reader)); break;
          case 10: builder.setTotalTimeMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 11: builder.setBeginTimeMs(ProtoAdapter.UINT32.decode(reader)); break;
          case 12: builder.setEndTimeMs(ProtoAdapter.UINT32.decode(reader)); break;
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
    public MusicInfo redact(MusicInfo value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
