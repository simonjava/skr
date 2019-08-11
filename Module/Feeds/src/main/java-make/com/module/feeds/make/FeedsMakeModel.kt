package com.module.feeds.make

import com.common.utils.U
import java.io.Serializable
import com.module.feeds.watch.model.FeedSongModel
import com.module.feeds.watch.model.FeedSongTpl

class FeedsMakeModel : Serializable {

    val composeSavePath: String = U.getAppInfoUtils().getFilePathInSubDir("feeds", "feeds_compose.m4a")
    val recordSavePath: String = U.getAppInfoUtils().getFilePathInSubDir("feeds", "feeds_make.m4a")

    var challengeID: Long = 0L
    var firstLyricShiftTs = 0
    var songModel: FeedSongModel? = null
    //var uploadFeedsId: String? = null // 上传后生成的feedsid
    //var uploadSongName: String? = null // 上传后歌曲名称
    //var uploadSongDesc: String? = null // 上传后歌曲描述

    var bgmDownloadProgress: Float = 0f

    var recordDuration: Long = 0 // 录音时间
    var recording: Boolean = false
    var beginRecordTs: Long = Long.MAX_VALUE

    var recordFirstFrameTs: Long = Long.MAX_VALUE // 录制获取到第一个音频包时的时间
    var musicFirstFrameTs: Long = Long.MAX_VALUE  // 伴奏第一帧播放出来的时间
    var recordOffsetTs: Long = 0 // 录音的偏移

    var recordingClick: Boolean = false
    var withBgm = false
    var hasChangeLyric = false // 是否改变了歌词

    @Transient
    var hasChangeLyricThisTime = false // 本次是否改变了歌词，因为可能从草稿箱进去
    var audioUploadUrl:String? = null

    var draftUpdateTs = 0L // 对应的草稿箱更新睡哪
    var draftID = 0L //草稿箱ID

    override fun toString(): String {
        return "FeedsMakeModel(challengeID=$challengeID, songModel=$songModel, composeSavePath='$composeSavePath', bgmDownloadProgress=$bgmDownloadProgress, recordDuration=$recordDuration, recording=$recording, beginRecordTs=$beginRecordTs, recordSavePath='$recordSavePath', recordingClick=$recordingClick, withBgm=$withBgm)"
    }
}

var sFeedsMakeModelHolder: FeedsMakeModel? = null