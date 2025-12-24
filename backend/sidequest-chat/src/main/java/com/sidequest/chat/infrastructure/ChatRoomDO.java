package com.sidequest.chat.infrastructure;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_chat_room")
public class ChatRoomDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String type; // PRIVATE, GROUP
    private LocalDateTime createTime;
}

