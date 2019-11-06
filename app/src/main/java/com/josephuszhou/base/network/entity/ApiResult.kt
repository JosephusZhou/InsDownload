package com.josephuszhou.base.network.entity

import com.google.gson.annotations.SerializedName

/**
 * @author senfeng.zhou
 * @date 2019-07-11
 * @desc
 */
class ApiResult<T> {

    @SerializedName("status")
    var code: Int? = null
    @SerializedName(value = "msg", alternate = ["message"])
    var msg: String? = null
    @SerializedName("data")
    var data: T? = null

    val success: Boolean
        get() {
        code?.let {
            return it == 1
        }
        return false
    }
}