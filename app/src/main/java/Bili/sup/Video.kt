package Bili.sup

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import Bili.sup.R


class Video : AppCompatActivity() {


    override fun onBackPressed() {
        //复写返回键,如果视频是全屏状态则返回到上一级(普通窗口)
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //页面内无顶部标题栏,向下兼容安卓版本
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //无导航栏
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        getsetting(this, window)
        setContentView(R.layout.activity_video)


        val jzvdStd: JzvdStd = findViewById(R.id.jz_video)
        val src = this.intent.getStringExtra("src")
        val name = this.intent.getStringExtra("name")
        //获得上个页面传给该页面的数据

        jzvdStd.setUp(
            src, name
        )


        jzvdStd.startVideo()
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("autoplay", true)
        ) jzvdStd.gotoFullscreen()


        //适配竖屏视频,有BUG适配不了.
//        val retriever = MediaMetadataRetriever()
//        retriever.setDataSource(src)
//宽
//        val width =
//            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toString()
//                .toInt()
//高
//        val height =
//            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toString()
//                .toInt()
//        if (height > width) {
//        Jzvd.setTextureViewRotation(90);
//        }


        //全屏播放,已知bug,不打开自动旋转全屏播放不可以旋转(解决)
//        jzvdStd.setScreenFullscreen()


//        val vd1: VideoView = findViewById(R.id.videoView)
//        val src = this.intent.getStringExtra("src")
//        vd1.setVideoPath(src)
//
//        vd1.setMediaController(MediaController(this))
//
//        vd1.start()

    }


}