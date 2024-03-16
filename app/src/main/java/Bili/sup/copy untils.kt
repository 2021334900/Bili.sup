package Bili.sup

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.*


//拷贝
fun copyFromUri(context: Context, uri: Uri, dest: String) {

    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        val outputStream: OutputStream = FileOutputStream(dest)
//                    inputStream.copyTo(outputStream)
        //老方法,直接输入流输出流对拷,性能垃圾,弃用
        var temp = 0
        val data =
            if ((inputStream.available() / 1048576) > 1) {
                ByteArray(1048576)
            } else {
                ByteArray(1024)
            }
        // 进行判断,输入流文件大于1MB,每次传1MB数据,不大于每次传1KB数据,相当于加了个缓冲池
        temp = inputStream.read(data)
        while (temp != -1) {
            outputStream.write(data, 0, temp)
            temp = inputStream.read(data)

        }
        //判断是否拷贝完毕(data为拷贝一次数据的多少)
        inputStream.close()

        outputStream.close()
//关闭输入输出流
    }

}

//遍历
fun getFiles(context: Context, documentFile: DocumentFile?, path: String, dest: String) {

    for (file in documentFile!!.listFiles()) {
        val needcopy = "/" + file.name
//        println(needcopy)
        if (file.isDirectory) {
            getFiles(context, file, (path + needcopy), (dest + needcopy))
            //递归调用,性能较差,但是暂时没有更好的解决办法
        } else {
            if (!fileyesorno(dest)) File(dest).mkdirs()
//判断该文件的上级目录是否存在,不存在就创建目录
            when (file.name) {
                "danmaku.xml" -> println(" ")
                "index.json" -> println(" ")
                //没用的文件直接就舍弃了
                else -> {
//                    println(dest + needcopy)
                    if (!fileyesorno(dest + needcopy)) {
                        copyFromUri(
                            context,
                            changeToUri1(path + needcopy),
                            (dest + needcopy)
                        )
                        println(dest + needcopy)
                    } else {
                        println(dest + needcopy + "is")
                    }
                }
            }
            //如果目标不是文件夹,则拷贝文件
        }
    }
}


//fun getFiles(context: Context, documentFile: DocumentFile?, dest: String, path: String) {
//    if (documentFile!!.isDirectory) {
//        for (file in documentFile.listFiles()) {
//            val needcopy = "/" + file.name
//            if (file.isDirectory) {
//                val newDir = File(dest, file.name)
//                if (!newDir.exists()) {
//                    newDir.mkdir()
//                }
//                getFiles(context, file, (dest + needcopy), (path + needcopy)) //递归调用
//            } else {
//                if (!fileyesorno(dest + needcopy)) {
//                    copyFromUri(
//                        context,
//                        changeToUri1(path + needcopy),
//                        (dest + needcopy)
//                        //如果目标不是文件夹,则拷贝文件
//
//                    )
//                }
//            }
//        }
//    }
//}


////遍历示例，不进行额外逻辑处理
//fun getFiles(documentFile: DocumentFile?) {
//    if (documentFile != null) {
//        println(documentFile.name.toString())
//    }
//    if (documentFile != null) {
//        if (documentFile.isDirectory) {
//            for (file in documentFile.listFiles()) {
//                println(file.name.toString())
//                if (file.isDirectory) {
//                    getFiles(file) //递归调用
//                }
//            }
//        }
//    }
//}



