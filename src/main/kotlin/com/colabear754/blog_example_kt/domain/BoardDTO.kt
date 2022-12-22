package com.colabear754.blog_example_kt.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class BoardDTO(
    var seq: Int,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    var reg_time: LocalDateTime,
    var category_id: Int?,
    var subject: String,
    var content: String,
    var thumbnail: String?,
    var view_cnt: Int,
    var like_cnt: Int
)
