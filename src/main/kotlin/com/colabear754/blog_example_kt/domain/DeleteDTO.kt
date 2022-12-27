package com.colabear754.blog_example_kt.domain

class DeleteDTO(val seq: Int, result: Int) {
    val result = if (result == 1) "삭제 성공" else "삭제 실패"
}