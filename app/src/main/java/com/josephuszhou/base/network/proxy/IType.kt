package com.josephuszhou.base.network.proxy

import java.lang.reflect.Type

/**
 * @author senfeng.zhou
 * @date 2019-07-24
 * @desc
 */
interface IType<T> {

    fun getIType(): Type

}