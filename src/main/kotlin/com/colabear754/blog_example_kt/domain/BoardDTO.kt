package com.colabear754.blog_example_kt.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class BoardDTO(
    var seq: Int?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    var reg_time: LocalDateTime?,
    var category_id: Int?,
    var subject: String,
    var content: String,
    var thumbnail: String?,
    var view_cnt: Int?,
    var like_cnt: Int?
) {
    constructor(content: String) : this(null, null, null, content, content, null, null, null)
    constructor(subject: String, content: String, category_id: Int?, thumbnail: String?) : this(null, null, category_id, subject, content, thumbnail, null, null)
}
