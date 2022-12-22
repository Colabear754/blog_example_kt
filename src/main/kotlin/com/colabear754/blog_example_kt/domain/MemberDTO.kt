package com.colabear754.blog_example_kt.domain

import io.swagger.annotations.ApiModelProperty
import java.time.LocalDate

data class MemberDTO(
    @ApiModelProperty(value = "계정", required = true)
    var id: String,
    @ApiModelProperty(value = "비밀번호", required = true)
    var password: String,
    @ApiModelProperty(value = "이름", required = true)
    var name: String,
    @ApiModelProperty(value = "전화번호", allowEmptyValue = true)
    var phone: String,
    @ApiModelProperty(value = "부서명", allowEmptyValue = true)
    var department: String,
    @ApiModelProperty(hidden = true)
    val sign_up_date: LocalDate
)
