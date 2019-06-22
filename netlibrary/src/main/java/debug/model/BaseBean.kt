package debug.model

/**
 *
 * @author any
 * @time 2019/06/22 13.50
 * @details
 */
data class BaseBean<T>(val code: String?, val message: String?, val t: T)

inline fun <reified T> BaseBean<T>.isSuccess(): Boolean {
    return code == "200"
}