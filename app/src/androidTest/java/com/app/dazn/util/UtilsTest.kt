package com.app.dazn.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.dazn.data.VideoItem
import com.app.dazn.utils.readJsonFile
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UtilsTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test(expected = NullPointerException::class)
    fun read_json_from_assets_with_empty_file_name() {
        val list: List<VideoItem> = readJsonFile(context, "")
        assertEquals("HD (MP4, H264)", list[0].name)
        // this fun expect exception
    }

    @Test
    fun read_json_from_assets_with_valid_file_name() {
        val list: List<VideoItem> = readJsonFile(context, "video_list.json")
        assertEquals("HD (MP4, H264)", list[0].name)
    }
}