package com.josephuszhou.base.network.entity

import com.google.gson.annotations.SerializedName

/**
 * @author senfeng.zhou
 * @date 2019-07-11
 * @desc
 */
class ApiResult<T> {

    @SerializedName("status")
    public var code: Int? = null
    @SerializedName(value = "msg", alternate = ["message"])
    public var msg: String? = null
    @SerializedName("data")
    public var data: T? = null

    public var success = false
        get() {
        code?.let {
            return code == 1
        }
        return false
    }


}