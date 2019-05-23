package com.any.netlibrary.cache

import android.text.TextUtils
import android.util.Log

import java.io.*
import java.security.MessageDigest

/**
 * @author any
 * @date 2017/11/30
 */
object FileUtils {


//    fun getResultByFilePath(filePath: String): String? {
//        try {
//            val file = File(filePath)
//            if (!file.exists()) return null
//            val input = FileInputStream(file)
//            return convertStreamToString(input)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }


    /**
     * 获取文件内容
     *
     * @param filePath
     * @return
     */
    fun getResultByFilePathInKotlin(filePath: String): String? {
        try {
            val file = File(filePath)
            if (!file.exists()) return null
            return file.readText()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun writeResultToFileInKotlin(filePath: String, result: String, isAppend: Boolean = false): Boolean {
        createFile(filePath)
        return try {
            val file = File(filePath)

            if (isAppend) {
                file.writeText(result)
            } else {
                file.appendText(result)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


//    /**
//     * 写入文件，覆盖写入
//     *
//     * @param filePath
//     * @param result
//     */
//    @Synchronized
//    fun writeResultToFile(filePath: String, result: String): Int {
//        createFile(filePath)
//        var fos: FileOutputStream? = null
//        try {
//            fos = FileOutputStream(filePath)
//            fos.write(result.toByteArray(charset("UTF-8")))
//            fos.flush()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return -1
//        } finally {
//            try {
//                fos?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        }
//        return 1
//    }


    fun createFile(filePath: String) {
        if (TextUtils.isEmpty(filePath))
            return
        val file = File(filePath)
        if (file.exists()) {
            return
        }
        val parenFile = file.parentFile
        if (parenFile != null) {
            val bool = parenFile.mkdirs()
            Log.e("msg", "boolean: $bool")
        }

    }

//    fun convertStreamToString(`is`: InputStream?): String? {
//        try {
//            if (`is` != null) {
//                val sb = StringBuilder()
//                var line: String
//                val reader = BufferedReader(
//                    InputStreamReader(`is`, "UTF-8")
//                )
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line)
//                }
//                return sb.toString()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } catch (error: OutOfMemoryError) {
//            error.printStackTrace()
//        } finally {
//            try {
//                `is`?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        }
//        return null
//    }


    /**
     * 处理文件名 key
     * @param key
     * @return
     */
    fun getMD5(key: String): String {
        val md5: MessageDigest
        try {
            md5 = MessageDigest.getInstance("MD5")
            val charArray = key.toCharArray()
            val byteArray = ByteArray(charArray.size)
            for (i in charArray.indices) {
                byteArray[i] = charArray[i].toByte()
            }
            val md5Bytes = md5.digest(byteArray)
            val hexValue = StringBuffer()
            for (i in md5Bytes.indices) {
                val `val` = md5Bytes[i].toInt() and 0xff
                if (`val` < 16) {
                    hexValue.append("0")
                }
                hexValue.append(Integer.toHexString(`val`))
            }
            return hexValue.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return key
    }

}
