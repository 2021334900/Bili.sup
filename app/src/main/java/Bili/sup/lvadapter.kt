package Bili.sup

import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startForegroundService
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


//Bean类
//自定义数组获取名字,BVid,封面
class Vd(Vd_name: String?, Vd_id: String?, Vd_pic: String?) {

    var name: String? = Vd_name
    var id: String? = Vd_id
    var pic: String? = Vd_pic

}

//适配器构造函数
class lvadapter(
    var context: Context,
    private var vdlist: ArrayList<Vd>,
    private val src: MutableList<String>,
) : BaseAdapter(), OnItemClickListener, OnItemLongClickListener {

    override fun getItem(position: Int) = vdlist[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = vdlist.size
    //简化函数获得Item的内容,ID和数量

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var vH: ViewHolder? = null
        var view: View? = null
        if (convertView == null) {
            //判断是否第一次加载

            view = View.inflate(context, R.layout.list_item, null)
            //通过LayoutInflater来创建View

            vH = ViewHolder()
            //声明视图持有者的实例

            vH.textView = view.findViewById(R.id.textView)
            vH.textView1 = view.findViewById(R.id.textView1)
            vH.imageView = view.findViewById(R.id.imageView)
            //vH.bt = view.findViewById(R.id.bt1)
            //依次获取内部各个控件对象

            view.tag = vH
            //传递数据

        } else {
            //直接更新列表视图中的数据
            view = convertView
            vH = view.tag as ViewHolder
        }
        Glide.with(context).load(vdlist[position].pic).into(vH.imageView)

        vH.textView.text = vdlist[position].name
        vH.textView1.text = vdlist[position].id
//        vH.bt?.setOnClickListener()
//        vH.bt.tag = position

        return view!!
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

//        println(position)
//        println(src[position])

        //打开视频播放界面并传入视频的路径和名称
        val openvideo = Intent(context, Video::class.java)
        openvideo.putExtra("src", src[position])
        openvideo.putExtra("name", vdlist[position].name)
        startActivity(context, openvideo, null)
    }

    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
//        doAsync { uiThread { context.toast("已复制" + vdlist[position].id.toString() + "到剪贴板") } }
        //获取系统的剪贴板
        val cmb: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //赋值Bvid给系统的剪贴板
        cmb.text = vdlist[position].id


        val intent = Intent()

        var cmp =
            ComponentName("tv.danmaku.bili", "tv.danmaku.bili.MainActivityV2")
        //这是拉起的B站启动的Activity
//        if (PreferenceManager.getDefaultSharedPreferences(context)
//                .getBoolean("play", false)
//        ) {
//            cmp = ComponentName(
//                "com.bilibili.app.in",
//                "tv.danmaku.bili.ui.videodownload.VideoDownloadListActivity"
//            )
//
//        }
//        println(cmp)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //为该Intent开辟一个新的任务栈
        intent.action = Intent.ACTION_MAIN
        //标识一个Activity作为程序来使用,因为是拉起的B站的Activity所以要加这个
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        //让应用显示在后台中,也就是拉起的单独APP而不是在此应用拉起APP,不加这个就不会显示在后台
        intent.putExtra("BVID", vdlist[position].id)
        intent.component = cmp
        startActivity(context, intent, null)

        println(intent)

        return true

    }

    //延迟初始化各个控件,提高性能
    inner class ViewHolder {

        lateinit var textView: TextView
        lateinit var textView1: TextView
        lateinit var imageView: ImageView
//        lateinit var bt: Button

    }

    //慢速不过稳定的图片载入方法
//            Thread {
//                val myurl = URL(selectCover(entry[h]))
//                val conn: HttpURLConnection = myurl.openConnection() as HttpURLConnection
////                    conn.connectTimeout = 6000
//                conn.doInput = true
//                conn.useCaches = false
//                conn.connect()
//
//
//                val imgin: InputStream = conn.inputStream
//                val bmp: Bitmap = BitmapFactory.decodeStream(imgin)
//                doAsync {
//                    uiThread {
//                        img1.setImageBitmap(bmp)
//                    }
//                }
//
//                imgin.close()
//
//
//            }.start()
}