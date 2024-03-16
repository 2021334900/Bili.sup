package Bili.sup

import android.content.Context
import java.io.File

fun fileyesorno(path: String): Boolean {
    val pd = File(path)
    return pd.exists()
    //存在就true,不存在就false
}

fun delfile(path: String) {
    File(path).delete()
    //删除文件
}


