package com.module.playways.room.prepare;


import com.module.playways.room.msg.event.JoinActionEvent;

public interface IRankMatchingView {
    /**
     * 匹配成功
     */
    void matchRankSucess(JoinActionEvent event);
}
