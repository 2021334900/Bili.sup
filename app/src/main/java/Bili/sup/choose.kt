package Bili.sup

import android.content.Context
import androidx.preference.PreferenceManager
import org.json.JSONObject
import java.io.File

fun selectName(context: Context, src: String): String {


    val o = File(src).readText()
    var jsonObject = JSONObject(o)
    //获取entry.json的内容并生成可操作的json对象

    val jsontitle = jsonObject.optString("title").toString()
    //获取视频的名字


    var jsonpart = ""
    var howmuch = ""
    if (jsonObject.optString("ep") == "")
    //通过检索文件中ep字段是否为空来判断该文件是否属于番剧的对象
    {
        jsonObject = JSONObject(jsonObject.optString("page_data"))
        howmuch = jsonObject.optString("page")
        jsonpart = jsonObject.optString("part")
        //获取视频的Bvid
        //获取page_data中的part(兼容多P视频,part是多P视频的真实名称,如果是单P视频,part则是up主乱设的)
        //例如 水浒传-13武松打虎,水浒传对应的是jsontitle,13对应的是howmuch,武松打虎对应的jsonpart,jsonbvid是这个视频在B站上可以检索到的一个途径
    } else {
        jsonObject = JSONObject(jsonObject.optString("ep"))
        howmuch = jsonObject.optString("index")
        jsonpart = jsonObject.optString("index_title")
        //获取番剧可跳转的Bvid(虽然番剧都有自己的Seasonid,但是无法通过Seasonid跳转到番剧页面(是个花瓶))
        //一一对应,不多阐述
    }

    var result = jsontitle + howmuch + jsonpart
    result = result.replace(" ", "").replace("/", "")
    //基于安卓文件命名规范(底层Linux,也就是linux文件命名规范,linux中空格和/不可以出现在文件名中)
    if (result.length > 83) {
        result = result.substring(0, 83)
    }
    //限制文件名最长长度
    //文件命名长度出错过(linux文件名(包括后缀)最长为255,一个汉字占3个字符,全中文最多85个中文字符),所以限制文件名长度为83,是因为后面要追加.mp4

    var formate = ".mp4"
    if (PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean("type", false)
    ) {
        formate = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("typelist", ".mp4").toString()
    }
    //获取自定义的视频格式
    result += formate

    return result

    /*这是另一种写法,比较易懂*/
//    通过遍历entry.json获取视频的名字
//    var file2 = File(src).readText()
//    when (file2.indexOf("index_title") > 0) {
//        //index_title只有番剧有
//        true -> file2 = "/" + file2.subSequence(
//            file2.indexOf("title") + 8,
//            file2.indexOf("type_tag") - 3
//        ).toString() + "-" + file2.subSequence(
//            file2.indexOf("index_title") + 14,
//            file2.indexOf("from") - 3
//        ).toString()
//        else -> file2 = if (file2.indexOf("part") > 0) {
//            "/" + file2.subSequence(
//                file2.indexOf("title") + 8,
//                file2.indexOf("type_tag") - 3
//            ).toString() + "-" + file2.subSequence(
//                file2.indexOf("part") + 7,
//                file2.indexOf("link") - 3
//            ).toString()
//        } else {
//            "/" + file2.subSequence(
//                file2.indexOf("title") + 8,
//                file2.indexOf("type_tag") - 3
//            ).toString()
//        }
//    }
//    //从entry.json中提取视频的名称
//    file2 = file2.replace(" ", "")
//
//    if (file2.length > 83) {
//        file2 = file2.substring(0, 83)
//    }
//
//    //限制文件名最长长度
//    //文件命名长度出错过(linux文件名(包括后缀)最长为255,一个汉字占3个字符,全中文最多85个中文字符),所以限制文件名长度为83,是因为后面要追加.mp4
//    file2 += ".mp4"
//    return file2

}

fun selectCover(src: String): String {
    val o = File(src).readText()
    val jsonObject = JSONObject(o)
    val jsoncover =
        jsonObject.optString("cover").replace("\\", "").replace("http", "https")
    return jsoncover

    /*易懂的写法*/
//    var file2 = File(src).readText()
//    file2 = file2.subSequence(
//        file2.indexOf("cover") + 8, file2.indexOf("video_quality") - 3
//    ).toString()
//    file2 = file2.replace("\\", "").replace("http", "https")
//    //不许明文访问,只能用https加密协议
//    return file2
}

fun selectBvid(src: String): String {
    val o = File(src).readText()
    var jsonObject = JSONObject(o)
    var jsonbvid = ""
    if (jsonObject.optString("ep") == "")
    //通过检索文件中ep字段是否为空来判断该文件是否属于番剧的对象
    {
        jsonbvid = jsonObject.optString("bvid")

    } else {
        jsonObject = JSONObject(jsonObject.optString("ep"))
        jsonbvid = jsonObject.optString("bvid")
    }
    return jsonbvid
}
