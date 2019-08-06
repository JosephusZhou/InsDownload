package com.josephuszhou.base.network.proxy

import okhttp3.ResponseBody
import com.josephuszhou.base.util.ClassUtil
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import com.google.gson.internal.`$Gson$Types`
import com.josephuszhou.base.network.entity.ApiResult

/**
 * @author senfeng.zhou
 * @date 2019-07-24
 * @desc
 */
abstract class CallClazzProxy<T: ApiResult<R>, R>(private var type: Type):
    IType<T> {

    public fun getCallType(): Type = type

    override fun getIType(): Type {
        //CallClazz代理方式，获取需要解析的Type
        var typeArguments: Type? = type
        if (typeArguments == null)
            typeArguments = ResponseBody::class.java

        var rawType = ClassUtil.findNeedType(javaClass)
        if (rawType is ParameterizedType) {
            rawType = rawType.rawType
        }
        return `$Gson$Types`.newParameterizedTypeWithOwner(null, rawType, typeArguments)
    }

}