package Bili.sup


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.io.File


class VideoList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//        )
        super.onCreate(savedInstanceState)
        getsetting(this, window)
        setContentView(R.layout.activity_video_list)
        var lv1: ListView = findViewById(R.id.vdlist)
        val bt1: Button = findViewById(R.id.bt1)


        val fileTree: FileTreeWalk =
            File("/storage/emulated/0/Android/data/${application.packageName}/").walk()
        var itemlist: ArrayList<Vd>? = ArrayList()


        var en: MutableList<String> = mutableListOf()
        var media: MutableList<String> = mutableListOf()
        var formate = ".mp4"
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("type", false)
        ) {
            formate = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("typelist", ".mp4").toString()
        }

        formate = formate.replace(".", "")

        fileTree.maxDepth(4)
            .filter { it.isFile }
            .forEach {
                when (true) {
                    it.extension == formate -> media.add(it.path)
                    it.name == "entry.json" -> en.add(it.path)
                    else -> println("")//测试用的...
                }
            }
        media = media.toSortedSet().toMutableList()
        en = en.toSortedSet().toMutableList()
//            for (o in 0 until media.size) {
//                onebyone[media[o]] = en[o]
////                println(media[o])
////                // key
////                println(onebyone[media[o]])
////                // value
//            }
//
//
//            for (o in onebyone.keys) {
//                val c: String = onebyone[o].toString()
//                println(selectCover(c))
//                println(selectName(c))
//            }
        if (media.size == 0) {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                .setPositiveButton("确定") { _, _ ->
                    this.finish()
                }
            if (en.size == 0) {
                builder.setMessage("您还没有拷贝离线缓存!")
            } else {
                builder.setMessage("您拷贝过来离线缓存还没有转码哦!")
            }

            builder.show()

        }
        for (i in en) {

            itemlist?.add(Vd(selectName(this, i), selectBvid(i), selectCover(i)))
            //绑定数据源
        }

        val pp = lvadapter(this, itemlist!!, media)

        lv1.adapter = pp
        //外部给列表视图设置对应的适配器
        lv1.onItemClickListener = pp
        //绑定列表视图item的单击事件
        lv1.onItemLongClickListener = pp
        //绑定列表视图item的长按事件

//        val data = arrayOf("A", "B", "C", "D", "E", "F")
//        val adapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_list_item_1, data
//        )
//        lv1.adapter = adapter
//MVC架构演示

        bt1.setOnClickListener {

            if (PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean("output", false)
            ) lv1.adapter = null

            val kongzhi = "copytopublicThread"
            val openorclose = Intent(this, BackInterface::class.java)
            openorclose.putExtra("kongzhi", kongzhi)
            startActivity(openorclose)
//            lv1.adapter = null
        }

    }


}