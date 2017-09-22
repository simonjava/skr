package com.wali.live.watchsdk.longtext.model.interior;

import com.base.log.MyLog;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wali.live.proto.Feeds;
import com.wali.live.proto.Feeds.BlogFeed;
import com.wali.live.proto.Feeds.MultiMedia;
import com.wali.live.proto.Feeds.UGCFeed;
import com.wali.live.watchsdk.longtext.model.interior.item.BaseFeedItemModel;
import com.wali.live.watchsdk.longtext.model.interior.item.CoverFeedItemModel;
import com.wali.live.watchsdk.longtext.model.interior.item.PictureFeedItemModel;
import com.wali.live.watchsdk.longtext.model.interior.item.TextFeedItemModel;
import com.wali.live.watchsdk.longtext.model.interior.item.TitleFeedItemModel;
import com.wali.live.watchsdk.longtext.model.interior.item.VideoFeedItemModel;

import java.util.ArrayList;
import java.util.List;

public class BlogFeedModel extends UgcFeedModel {
    private List<BaseFeedItemModel> mItemList;

    private CoverFeedItemModel mCoverItem;
    private TitleFeedItemModel mTitleItem;

    public BlogFeedModel(UGCFeed protoUgcFeed) throws Exception {
        super(protoUgcFeed);
    }

    @Override
    protected void parseExtData(ByteString extData) throws Exception {
        if (mExtType == UGC_EXT_TYPE_LONG_TEXT) {
            parseBlogFeed(BlogFeed.parseFrom(extData));
            return;
        }
        throw new Exception("extType is not matched=" + mExtType);
    }

    private void parseBlogFeed(BlogFeed protoBlogFeed) {
        if (mItemList == null) {
            mItemList = new ArrayList();
        }
        for (MultiMedia multiMedia : protoBlogFeed.getMultiMediaList()) {
            try {
                BaseFeedItemModel itemModel = parseBlogFeedItem(multiMedia);
                if (itemModel != null) {
                    mItemList.add(itemModel);
                }
            } catch (Exception e) {
                MyLog.e(TAG, e);
            }
        }
    }

    private BaseFeedItemModel parseBlogFeedItem(MultiMedia protoMultiMedia) throws InvalidProtocolBufferException {
        switch (protoMultiMedia.getMediaType()) {
            case BaseFeedItemModel.FEEDS_MULTI_MEDIA_TYPE_PIC:
                return new PictureFeedItemModel(Feeds.Picture.parseFrom(protoMultiMedia.getMediaData()));
            case BaseFeedItemModel.FEEDS_MULTI_MEDIA_TYPE_VIDEO:
                return new VideoFeedItemModel(Feeds.Video.parseFrom(protoMultiMedia.getMediaData()));
            case BaseFeedItemModel.FEEDS_MULTI_MEDIA_TYPE_TEXT:
                return new TextFeedItemModel(Feeds.Text.parseFrom(protoMultiMedia.getMediaData()));
            default:
                return null;
        }
    }

    public CoverFeedItemModel getCoverItem() {
        if (mCoverItem == null) {
            mCoverItem = new CoverFeedItemModel(mCoverUrl);
        }
        return mCoverItem;
    }

    public TitleFeedItemModel getTitleItem() {
        if (mTitleItem == null) {
            mTitleItem = new TitleFeedItemModel(mTitle);
        }
        return mTitleItem;
    }

    public List<BaseFeedItemModel> getItemList() {
        return mItemList;
    }

    @Override
    public String toPrint() {
        return mExtType + ":" + mItemList.size();
    }
}