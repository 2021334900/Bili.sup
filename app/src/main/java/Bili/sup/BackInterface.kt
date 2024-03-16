package Bili.sup


import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.preference.PreferenceManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileInputStream


class BackInterface : AppCompatActivity() {
    //该界面无法单独启动,只能被别的界面拉起

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        getsetting(this, window)
        setContentView(R.layout.activity_back_interface)

        val yuan: ProgressBar = findViewById(R.id.yuan)
        val text1: TextView = findViewById(R.id.text)
//        val vd: VideoView = findViewById(R.id.player)

        val kz = this.intent.getStringExtra("kongzhi")
        //获取上个Activity传入kongzhi的值
        val fileTree: FileTreeWalk =
            File("/storage/emulated/0/Android/data/${application.packageName}/").walk()


        fun copyThread(): Boolean {
            yuan.visibility = View.VISIBLE

            //让圆形进度条显示
            text1.visibility = View.VISIBLE

            doAsync { uiThread { text1.text = "拷贝中,请稍等..." } }


            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                    "ch",
                    false
                )
            ) {
                //判断设置中支持的版本

                var documentFile = DocumentFile.fromTreeUri(
                    this,
                    changeToUri1("/storage/emulated/0/Android/data/com.bilibili.app.in/download")
                )
                getFiles(
                    this,
                    documentFile, "/storage/emulated/0/Android/data/com.bilibili.app.in/download",
                    "/storage/emulated/0/Android/data/${application.packageName}"
                )

                if (!(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                        "play",
                        false
                    ))
                ) {
                    documentFile = DocumentFile.fromTreeUri(
                        this,
                        changeToUri1("/storage/emulated/0/Android/data/tv.danmaku.bili/download")
                    )
                    getFiles(
                        this,
                        documentFile, "/storage/emulated/0/Android/data/tv.danmaku.bili/download",
                        "/storage/emulated/0/Android/data/${application.packageName}"

                    )

                }


//                println("guojiban")
            } else {
                val documentFile = DocumentFile.fromTreeUri(
                    this,
                    changeToUri1("/storage/emulated/0/Android/data/tv.danmaku.bili/download")
                )
                getFiles(
                    this,
                    documentFile, "/storage/emulated/0/Android/data/tv.danmaku.bili/download",
                    "/storage/emulated/0/Android/data/${application.packageName}"
                )
            }

//            toast("拷贝完成")
//            toast("耗时" + ((System.currentTimeMillis() - (kz?.toLong()!!)).toFloat()) / 1000 + "秒")
            doAsync { uiThread { toast("完成") } }
            return true

        }

        fun mergeThread(): Boolean {
            yuan.visibility = View.VISIBLE

            //让圆形进度条显示
            text1.visibility = View.VISIBLE
            var fileNames: MutableList<String> = mutableListOf()
            var fileParents: MutableList<String> = mutableListOf()
            var normalEntry: MutableList<String> = mutableListOf()


            fileTree.maxDepth(4)
                .filter { it.extension in listOf("m4s", "blv") }
                .forEach { fileNames.add(it.path); fileParents.add(it.parent!! + "/") }
            fileTree.maxDepth(4)
                .filter { it.name == ("entry.json") }
                .forEach { normalEntry.add(it.path) }
//遍历所有的m4s和blv文件,添加到列表中,并将父目录添加到另一个列表中(深度为4)
            fileNames = fileNames.toSortedSet().toMutableList()
            fileParents = fileParents.toSortedSet().toMutableList()
            normalEntry = normalEntry.toSortedSet().toMutableList()
            //父列表去重


            var j = 0
            var blvflag = 0


            while (j < fileParents.size) {
                doAsync {
                    uiThread {
                        text1.text = "共${fileParents.size}视频,正在处理第${j + 1}个"
                    }
                }
//                text1.text = "共" + fileParents.size + "个视频,正在处理第$j+个"
                if (fileNames[j * 2 - blvflag].endsWith("blv")) {

                    if (fileyesorno(fileParents[j] + selectName(this, normalEntry[j]))
                    ) {
                        delfile(fileNames[j * 2 - blvflag])
                    } else {
                        merge(
                            fileNames[j * 2 - blvflag],
                            fileNames[j * 2 - blvflag],
                            (fileParents[j] + selectName(this, normalEntry[j]))
                        )
                        delfile(fileNames[j * 2 - blvflag])
                    }
//                    println(fileNames[j * 2 - blvflag])
                    blvflag += 1
                } else {
                    if (fileyesorno(fileParents[j] + selectName(this, normalEntry[j]))
                    ) {
                        delfile(fileNames[j * 2 - blvflag])
                        delfile(fileNames[j * 2 - blvflag + 1])
                    } else {
                        merge(
                            fileNames[j * 2 - blvflag],
                            fileNames[j * 2 - blvflag + 1],
                            (fileParents[j] + selectName(this, normalEntry[j]))
                        )
                        delfile(fileNames[j * 2 - blvflag])
                        delfile(fileNames[j * 2 - blvflag + 1])
                    }
//                    println(fileNames[j * 2 - blvflag] + fileNames[j * 2 + 1 - blvflag])
                    //测试
                }
                j += 1
//                toast("共${fileParents.size}个视频,已经处理了${j}个")
            }


            doAsync { uiThread { toast("完成") } }
            return true
        }

        fun delOtherFile(): Boolean {
            yuan.visibility = View.VISIBLE

            //让圆形进度条显示
            text1.visibility = View.VISIBLE
            doAsync {
                uiThread {
                    yuan.scaleX = "-1".toFloat()
                    text1.text = "删除中,请稍后..."
                }
            }
            var dic: MutableList<String> = mutableListOf()
            //记录本目录下所有文件夹(除本目录),不包含子目录
            var needcopymap: MutableMap<String, String> = mutableMapOf()



            fileTree.maxDepth(4)
                .forEach {
                    when (true) {
                        it.isFile -> needcopymap[it.path] = it.name
                        it.isDirectory -> dic.add(it.path)
                        else -> println("empty")
                    }
                }
            dic.removeAt(0)
            //把父目录去掉
//            for (i in 0 until dic.size) {
//                val fileneedcopy: FileTreeWalk = File(dic[i]).walk()
//                fileneedcopy.maxDepth(3).filter { it.isFile }
//                    .filter { it.extension == "mp4" }
//                    .forEach {
//                        needcopymap[it.path] = it.name
//                    }
//                fileneedcopy.maxDepth(3).filter { it.isFile }
//                    .filter { it.extension != "mp4" }
//                    .forEach {
//                        it.delete()
//                    }
//            }

            if (needcopymap.isNotEmpty()) {
                for (key in needcopymap.keys) {
//                    File(key).copyTo(
//                        File(
//                            "/storage/emulated/0/Android/data/${application.packageName}/" + needcopymap[key]
//                        )
//                    )
//                    if (fileyesorno(
//                            "/storage/emulated/0/Android/data/${application.packageName}/" + needcopymap[key]
//                        )
//                    ) {
//                        delfile(key)
//                    }
                    println(key)
                    delfile(key)

                }
                needcopymap.clear()
            } else {
//                this.finish()
//                toast("没有需要删除的视频了.")
            }
            dic = dic.toSortedSet().toMutableList()

            for (i in dic.size downTo 1) {
                println(dic[i - 1])
                delfile(dic[i - 1])
            }
            //把所有拷贝过来的文件夹删掉,因为获得的目录列表是最底层,然后才是最外层
            //从最里层往外删除目录

            getExternalFilesDir(null)
            //可有可无,在应用目录下再创建一个files文件夹,防止bug出现
            return true
        }

        fun copytopublicThread(): Boolean {
            yuan.scaleX = "-1".toFloat()
            //让圆形进度条倒着转
            yuan.visibility = View.VISIBLE
            text1.visibility = View.VISIBLE

//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            //获取系统的Moives文件夹
//            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//与上同理,不过返回的是个content://media/external/video/media   URI对象
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
                        else -> println()//测试用的...
                    }
                }

            media = media.toSortedSet().toMutableList()
            en = en.toSortedSet().toMutableList()

            doAsync { uiThread { text1.text = "导出中,请稍等..." } }
            for (i in 0 until en.size) {
                val fn0 = selectName(this, en[i])
                if (!fileyesorno(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                            .toString() + "/" + fn0
                    )
                //检查文件是否已存在,不存在执行拷贝操作,存在忽略
                ) {
                    println(media[i])
                    println(en[i])

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        //安卓9及其以下版本直接对拷

                        File(media[i]).copyTo(
                            File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                                    .toString() + "/" + fn0
                            )
                        )
                    } else {
                        //安卓10及其以上版本使用MediaStore访问公共目录

                        val insertUri = this.contentResolver.insert(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            ContentValues().apply {
                                put(MediaStore.Video.Media.MIME_TYPE, "video")
                                put(
                                    MediaStore.Video.Media.DISPLAY_NAME,
                                    fn0
                                )
                            })
                        //获取公共目录的Uri,并设置保存的文件名及文件格式

                        insertUri?.let {
                            this.contentResolver.openOutputStream(insertUri).use { outputStrem ->
                                FileInputStream(media[i]).copyTo(outputStrem!!)
                            }
                        }
                        //拷贝本应用下视频到公共目录


                    }

                }
            }

            val uri =
                changeToUri1(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
                        .toString()
                )
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)

            if (PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean("output", false)
            ) {
                if (delOtherFile()) startActivity(intent)
            } else startActivity(intent)
            //导出视频时,判断是否删除原视频


            return true
        }

        Thread {

            when (kz) {
                "copyThread" -> if (copyThread()) {
                    this.finish()
                }
                "mergeThread" -> if (mergeThread()) {
                    this.finish()
                }
//                "delOtherFile" -> if (delOtherFile()) {
//                    this.finish()
//                }
                "player" -> println()
//                    vd.start()//弃用了
                "copytopublicThread" -> if (copytopublicThread()) {
                    this.finish()
                }
                else -> toast("出错啦")

            }

        }.start()

    }
}


