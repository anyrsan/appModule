package debug.parseJson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.Exception

/**
 *
 * @author any
 * @time 2019/06/22 10.54
 * @details
 */

object ParseHandler {
    val gson: Gson? = GsonBuilder().create()

    inline fun <reified T> getModel(jsonString: String?): T? {
        try {
            jsonString?.let {
                return gson?.fromJson(jsonString, T::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    inline fun <reified T> getListModel(jsonArray: String?): List<T>? {
        try {
            jsonArray?.let {
                return gson?.fromJson<List<T>>(jsonArray, object : TypeToken<List<T>>() {}.type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}


