package com.colabear754.blog_example_kt.domain

class LikeDTO(val seq: Int, val id: String, result: Int, action: Boolean) {
    val action = if (action) "추천" else "추천 취소"
    val result = if (result == 1) "성공" else "실패"
}