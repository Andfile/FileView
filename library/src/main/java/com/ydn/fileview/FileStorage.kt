package com.ydn.fileview

import android.util.Log
import java.io.*
import java.util.*

class FileStorage internal constructor(internal var filename: String) {
    private val TAG  = "FileStorage"

    internal val numberOfLines: Int
        get() {
            try {
                val reader = BufferedReader(FileReader(filename))
                var lines = 0
                while (reader.readLine() != null) lines++
                reader.close()
                return lines
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                return 0
            }
        }

    internal fun getData(startPosition: Int, loadSize: Int): List<Item> {
        val lines = ArrayList<Item>()
        try {
            val fs = FileInputStream(filename)
            val br = BufferedReader(InputStreamReader(fs, "UTF-8"))

            for (i in 0 until startPosition + loadSize) {
                val line = br.readLine() ?: break

                if (i >= startPosition) {
                    lines.add(Item(line, true))
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        }

        return lines
    }
}
