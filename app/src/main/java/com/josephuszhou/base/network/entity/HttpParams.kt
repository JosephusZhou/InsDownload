package com.josephuszhou.base.network.entity

import java.io.Serializable

/**
 * @author senfeng.zhou
 * @date 2019-07-11
 * @desc
 */
class HttpParams: Serializable {

    var urlParamsMap: LinkedHashMap<String, Any>

    constructor() {
        urlParamsMap = LinkedHashMap()
    }

    constructor(key: String, value: Any) {
        urlParamsMap = LinkedHashMap()
        urlParamsMap[key] = value
    }

    fun put(key: String, value: Any) {
        urlParamsMap[key] = value
    }

}