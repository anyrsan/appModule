package com.any.netlibrary.model

/**
 *
 * @author any
 * @time 2019/05/23 16.59
 * @details
 */

open class BaseModel<R> {
    var code: String? = null
    var message: String? = null
    var data: R? = null
    fun isSuccess(): Boolean = code == "200"
}