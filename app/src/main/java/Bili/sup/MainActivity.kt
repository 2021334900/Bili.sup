package Bili.sup


import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import net.arvin.permissionhelper.PermissionUtil
import net.arvin.permissionhelper.PermissionUtil.RequestPermissionListener
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import java.io.File


// 这里anko库要去setting/Plugins下载


class MainActivity : AppCompatActivity() {
    //    companion object {
//        const val houtai = "${application.packageName}.BackInterface"
//    }
    private var timeReceiver: TimeReceiver? = null
    //用来接收每分钟系统发送的定时

    var nowtime: Int = 0
    var settime: Int = 2147483647
    //设置应用使用时间和自定义设置的提醒时间,默认最大值(Int取值的最大值...)

    inner class TimeReceiver : BroadcastReceiver() {
        //定义一个时间广播的接收器
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                nowtime += 1
                //每接收到一个系统发出的Intent.ACTION_TIME_TICK广播,就让现在的时间+1
                if (nowtime == settime * 60) {
                    //因为接受的是每个整点的分钟广播,所以要让settime(你自己设置的提醒时长)x60变成小时的
                    notify(context!!, settime)
                    nowtime = 0
                    //重置用户使用APP的时间
                }

            }
        }
    }


    override fun onStart() {
        super.onStart()
        if (timeReceiver == null) {
            timeReceiver = TimeReceiver()
            val guolv = IntentFilter(Intent.ACTION_TIME_TICK)
            //声明一个过滤器,只接受Intent.ACTION_TIME_TICK的广播,该广播是系统在每分钟的整点发的广播 例如系统时间为13:33:00发出这个广播
            registerReceiver(timeReceiver, guolv)
            //活动启动是注册广播接收器
        }
    }
    //    @RequiresApi(Build.VERSION_CODES.R)


    override fun onCreate(savedInstanceState: Bundle?) {

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//        )
        super.onCreate(savedInstanceState)

        settime = getsetting(this, window)
        setContentView(R.layout.activity_main)
//        fun getcontrol() :Array{
//            val button1: Button = findViewById(R.id.button1)
//            val button2: Button = findViewById(R.id.button2)
//            val button3: Button = findViewById(R.id.button3)
//            val button4: Button = findViewById(R.id.button4)
//            val button5: Button = findViewById(R.id.button5)
//            val button6: Button = findViewById(R.id.button6)
//            val text1: TextView = findViewById(R.id.text1)
//            val text2: TextView = findViewById(R.id.text2)
//
//        }
//        getcontrol()
        //想做一个自定义控件赋值的方法,没实现了

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
//        val text1: TextView = findViewById(R.id.text1)
//        val text2: TextView = findViewById(R.id.text2)


        getExternalFilesDir(null)
//创建app私有目录


        button1.setOnClickListener {

            val permissionUtil = PermissionUtil.Builder()
                .with(this) //必传：可使用FragmentActivity或v4.Fragment实例
                .setTitleText("提示") //弹框标题
                .setEnsureBtnText("确定") //权限说明弹框授权按钮文字
                .setCancelBtnText("取消") //权限说明弹框取消授权按钮文字
                .setSettingEnsureText("设置") //打开设置说明弹框打开按钮文字
                .setSettingCancelText("取消") //打开设置说明弹框关闭按钮文字
                .setSettingMsg("当前应用缺少必要权限。\n请点击\"设置\"-\"权限\"-打开文件管理权限。") //打开设置说明弹框内容文字
                .setInstallAppMsg("允许安装来自此来源的应用") //打开允许安装此来源的应用设置
                .setShowRequest(true) //是否显示申请权限弹框
                .setShowSetting(true) //是否显示设置弹框
                .setShowInstall(true) //是否显示允许安装此来源弹框
                .setRequestCancelable(true) //申请权限说明弹款是否cancelable
                .setSettingCancelable(true) //打开设置界面弹款是否cancelable
                .setInstallCancelable(true) //打开允许安装此来源引用弹款是否cancelable
                .setTitleColor(Color.BLACK) //弹框标题文本颜色
                .setMsgColor(Color.GRAY) //弹框内容文本颜色
                .setEnsureBtnColor(Color.BLACK) //弹框确定文本颜色
                .setCancelBtnColor(Color.BLACK) //弹框取消文本颜色
                .build()


            fun shouquan() {
                permissionUtil.request("需要读写权限",
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    RequestPermissionListener { granted, _ ->
                        if (granted) {
                            toast("准备冻手准备冻手")
                        } else {
                            longToast("你刚才肯定点错了对不对?\n 我还没有获得访问文件的权限呢!")
                        }
                    }
                )
            }
//安卓6.0及其以上的授权读写权限的方法


            toast("正在检查应用是否有所有文件管理权限")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R
                || Environment.isExternalStorageManager()
            //如果安卓版本小于11,则用动态授权,若>=11则拉起11的所有文件权限管理窗口,并动态授权
            ) {
                shouquan()
            } else {
                val builder = AlertDialog.Builder(this)
                    .setMessage("因为您是安卓" + Build.VERSION.RELEASE + ",\n所以需要授权所有文件管理权限,\n请在即将打开的授权页面授权.")
                    .setPositiveButton("确定") { _, _ ->
                        shouquan()
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        intent.data = Uri.parse("package:" + application.packageName)
//                        intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                        //这个动作跳转到全部应用的所有文件管理权限页面(弃用,可直接跳转到该应用的授权界面)
                        //国际版wps中pritln出来这一项
                        this.startActivityForResult(intent, 0)
                        println(intent)
//                        startActivityForResult(intent, 1)
                    }
                builder.show()

            }
            //方法1


//            val intent2 = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//            startActivity(intent2)
            //方法2 已弃用(无法判断安卓版本)
        }


        button2.setOnClickListener {
            toast("正在检查是否获得data访问权限")
            if (isGrant(this)) toast("您已获得data访问权限") else {
//也是访问contentResolver共享数据得到APP是否权限持久化
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    .setMessage("请您在即将出现的界面点击使用此文件夹\n如果您是鸿蒙系统,则点击允许访问\"DATA\"")
                    .setPositiveButton("确定") { _, _ ->
                        startForRoot(this, 11)
                        //startFor("/storage/emulated/0/Android/data/", this, 11)
                        //同理
                    }
                builder.show()
                if (isGrant(this)) {
                    toast("您已获得data访问权限")
                }
            }
            //判断是否获得/Android/data权限,如果没获得就打开授权界面,如果获得了就toast提示一下
        }


        button3.setOnClickListener {
            longToast("如果您B站下载的离线缓存太多了,拷贝时间可能会有点长,请您稍等.")
            val kongzhi = "copyThread"
            val openorclose = Intent(this, BackInterface::class.java)
            openorclose.putExtra("kongzhi", kongzhi)
            startActivity(openorclose)

//            fun copy(): Boolean {
//                val starttime: Long = System.currentTimeMillis()
//                //记录耗时
//                val documentFile = DocumentFile.fromTreeUri(
//                    this,
//                    changeToUri1("/storage/emulated/0/Android/data/tv.danmaku.bili/download")
//                )
//                getFiles(
//                    this,
//                    documentFile,
//                    "/storage/emulated/0/Android/data/${application.packageName}",
//                    "/storage/emulated/0/Android/data/tv.danmaku.bili/download"
//                )
////                toast("拷贝完成")
//                val quittime: Long = System.currentTimeMillis()
//                //结束执行的时间
//                val end = ((quittime - starttime).toFloat()) / 1000
////                toast("耗时$end" + "秒")
//                kongzhi = "true"
//                return true
//
//            }
//            Thread {
//                if (copyThread()) {
//                    println(kongzhi)
//                }
//            }.start()


        }

        val fileTree: FileTreeWalk =
            File("/storage/emulated/0/Android/data/${application.packageName}/").walk()
//遍历,获得文件目录树结构
        button4.setOnClickListener {


            var fileParents: MutableList<String> = mutableListOf()
            fileTree.maxDepth(4)
                .filter { it.extension in listOf("m4s", "blv") }
                .forEach { fileParents.add(it.parent) }
            if (fileParents.size == 0) {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                    .setMessage("您已处理完所有视频")
                    .setPositiveButton("OK") { _, _ -> }
                builder.show()
            } else {
                longToast("转码中,请您稍等.")
                val kongzhi = "mergeThread"
                val openorclose = Intent(this, BackInterface::class.java)
                openorclose.putExtra("kongzhi", kongzhi)
                startActivity(openorclose)
            }

//            if (j >= fileParents.size) {
//                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
//                    .setMessage("您已处理完所有视频")
//                    .setPositiveButton("OK") { _, _ -> }
//                builder.show()
//            }

        }


        button5.setOnClickListener {

            var en: MutableList<String> = mutableListOf()
            var media: MutableList<String> = mutableListOf()
            var onebyone: MutableMap<String, String> = mutableMapOf()
            val uri =
                changeToUri1("/storage/emulated/0/Android/data/${application.packageName}/")
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
            startActivity(intent)
            //拉起系统文件管理器,并打开至本应用的数据目录/storage/emulated/0/Android/data/${application.packageName}/
            File("/storage/emulated/0/Android/data/${application.packageName}/点击退出该页面").createNewFile()

        }

        var i = 0
//播放
        button6.setOnClickListener {
            val openorclose = Intent(this, VideoList::class.java)
            startActivity(openorclose)


//            val kongzhi = "player"
//            val openorclose = Intent(this, BackInterface::class.java)
//            openorclose.putExtra("kongzhi", kongzhi)
//            startActivity(openorclose)


//            vd.visibility = View.VISIBLE
//            var mediaList: MutableList<String> = mutableListOf()
//            fileTree.maxDepth(4)
//                .filter { it.isFile }
//                .filter { it.extension == "mp4" }
//                .forEach { mediaList.add(it.path) }
//            mediaList = mediaList.toSortedSet().toMutableList()
//            //按名称排序
//            if (i < mediaList.size) {
//                vd.setVideoPath(mediaList[i])
//                vd.setMediaController(MediaController(this))
//                vd.start()
//
//
//            }
//            i++
//            vd.setOnCompletionListener { vd.visibility = View.GONE }
        }


        button7.setOnClickListener {
            val openorclose = Intent(this, Setting::class.java)
            startActivity(openorclose)
        }


        button8.setOnClickListener {

            //测试通知
            notify(this, 3)
//            PreferenceManager.getDefaultSharedPreferences(this).getString("hour", "1")
            //自定义间隔时常
            println(nowtime)
            println(settime)
            println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES))
        }

    }


    //返回授权状态,检测窗口关闭并持久化权限

    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri: Uri?
        uri = data?.data

        if (requestCode == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                toast("bingo")
            } else {
                toast("请重新授权")
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + application.packageName)
//                        intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                //这个动作跳转到全部应用的所有文件管理权限页面(弃用,可直接跳转到该应用的授权界面)
                //国际版wps中pritln出来这一项
                this.startActivityForResult(intent, 0)
            }
        }

        if (data == null) {
            return
        }
        if (requestCode == 11 && data.data.also { uri = it } != null) {
            //Activity的返回值=11时,给uri赋值为Intent中的data
            contentResolver.takePersistableUriPermission(
                uri!!, data.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            )
            //保存这个目录的访问权限,权限持久化
            toast("您已获得data访问权限")
        }
    }

    override fun onStop() {
        super.onStop()
//        println("im stop")
//        unregisterReceiver(timeReceiver)
        //活动停止时注销广播接受器

    }

    override fun onDestroy() {
        super.onDestroy()

        val nManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        nManager.cancelAll()
        //清除所有通知

        unregisterReceiver(timeReceiver)
        //活动停止时注销广播接受器
    }
}







