package com.josephuszhou.base.util

import okhttp3.ResponseBody
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

/**
 * @author senfeng.zhou
 * @date 2019-07-12
 * @desc
 */
class ClassUtil {

    companion object {
        fun <R> findNeedType(cls: Class<R>): Type {
            val typeList = getMethodTypes(cls)
            return if (typeList == null || typeList.isEmpty()) {
                ResponseBody::class.java
            } else typeList[0]
        }

        fun <T> getMethodTypes(cls: Class<T>): List<Type>? {
            val typeOri = cls.genericSuperclass
            var needTypes: MutableList<Type>? = null
            // if Type is T
            if (typeOri is ParameterizedType) {
                needTypes = ArrayList()
                val parenTypes = typeOri.actualTypeArguments
                for (childType in parenTypes) {
                    needTypes.add(childType)
                    if (childType is ParameterizedType) {
                        val childTypes = childType.actualTypeArguments
                        Collections.addAll(needTypes, *childTypes)
                    }
                }
            }
            return needTypes
        }
    }

}