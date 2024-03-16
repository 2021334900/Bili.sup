package Bili.sup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.view.Window
import android.view.WindowManager
import androidx.documentfile.provider.DocumentFile
import androidx.preference.PreferenceManager


fun getsetting(context: Context, window: Window): Int {

    var resulttime = 2147483647
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    if (preferences.getBoolean("night", true)) {
        context.setTheme(R.style.follow)
    } else {
        context.setTheme(R.style.keep)
    }
    if (preferences.getBoolean("keepscreen", true)) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    if (preferences.getBoolean("remind", false)) {
        resulttime = preferences.getString("hour", "1").toString().toInt()
        println(preferences.getString("hour", "1"))
    }
    return resulttime

}


fun changeToUri1(path: String): Uri {
    var path = path
    if (path.endsWith("/")) {
        path = path.substring(0, path.length - 1)
    }
    //如果路径的最后一位为"/",则删除
    val path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
    //传路径进来后替换,并赋值给path2
    return Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A$path2")
    //返回该uri(以uri的形式)
}


fun changeToUri(path: String): String? {
    var path = path
    if (path.endsWith("/")) {
        path = path.substring(0, path.length - 1)
    }
    //如果路径的最后一位为"/",则删除
    val path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
    //传路径进来后替换,转为安卓可以运行的URI格式
    return "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A$path2"
    //返回该uri(以字符串的形式)
}


//授权/Android/data目录访问权限
fun startForRoot(context: Activity, REQUEST_CODE_FOR_DIR: Int) {
    val uri1: Uri =
        Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata")
    //        DocumentFile documentFile = DocumentFile.fromTreeUri(context, uri1);
//    var uri = changeToUri(Environment.getExternalStorageDirectory().path)
//    uri = "$uri/document/primary%3A" + Environment.getExternalStorageDirectory().path.replace(
//        "/storage/emulated/0/",
//        ""
//    ).replace("/", "%2F")

//    val parse: Uri = Uri.parse(uri)
    val documentFile = DocumentFile.fromTreeUri(context, uri1)
    val intent1 = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    //允许应用通过document对象遍历
    intent1.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            //通过URI实现可读写
            or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            //通过URI请求系统保存权限(权限持久化)
            or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
            //通过URI请求权限针对关联的Uri和子类
            )
    intent1.putExtra(DocumentsContract.EXTRA_INITIAL_URI, documentFile!!.uri)

    context.startActivityForResult(intent1, REQUEST_CODE_FOR_DIR)
}


//授权指定目录data访问权限
fun startFor(path: String, context: Activity, REQUEST_CODE_FOR_DIR: Int) {

    val uri = changeToUri(path!!)

//    Toast.makeText(context, uri, Toast.LENGTH_SHORT).show()
//    测试changeToUri返回什么
    val parse: Uri = Uri.parse(uri)
    val intent = Intent("android.intent.action.OPEN_DOCUMENT_TREE")
    intent.addFlags(
        Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
    )
//      授权

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, parse)
    }
    context.startActivityForResult(intent, REQUEST_CODE_FOR_DIR)
}

//检查Android/data是否可以访问
fun isGrant(context: Context): Boolean {
    for (persistedUriPermission in context.contentResolver.persistedUriPermissions) {
        if (persistedUriPermission.isReadPermission && persistedUriPermission.uri.toString() == "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata") {
            return true
            //查找权限中是否有读的权限
        }
    }
    return false
}