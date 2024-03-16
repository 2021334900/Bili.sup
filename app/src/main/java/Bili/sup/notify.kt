package Bili.sup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat

fun notify(context: Context, time: Int) {
    //通知

    var ContentTitle = "休息一下吧!"
    var ContentText = "您已经学习了$time" + "小时."
    if (time >= 3) {
        ContentTitle = "卷王求你了,休息一下吧!!!"
        ContentText = "卷王,是你吗?你已经学习$time" + "小时了.休息一下吧!!!"
    }

    val channelID = "1"
    val channelName = "休息一下吧!!!"
    //自定义通知名称和通知ID,通知ID没卵用,但是一定要有,设置成啥都行

    val importance = NotificationManager.IMPORTANCE_HIGH
    //通知紧急程度,最高级(发出响声)

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    //获取系统的通知管理

    val channel = NotificationChannel(channelID, channelName, importance)
    //给通知赋值

    channel.vibrationPattern = longArrayOf(0, 100, 50, 1000)
    //震动模式  停顿时长震动时长依次排列,停顿0,震动100ms,停顿50ms,震动1000ms

    channel.enableVibration(true)
    //让通知震动可以震动

    manager.createNotificationChannel(channel)
    //通知初始化


//    val Floatnotify = PendingIntent.getActivity(
//        context,
//        R.drawable.ic_launcher_foreground,
//        null,
//        PendingIntent.FLAG_UPDATE_CURRENT
//    )
    //想做成浮动通知(例如微信收到了一条消息,显示到状态栏下面)来着,技术不允许,bug一堆,舍弃

    val notification = NotificationCompat.Builder(context, channelID)

        .setContentTitle(ContentTitle)
        .setContentText(ContentText)
        .setSmallIcon(R.drawable.square)
        .setWhen(System.currentTimeMillis())
        .setShowWhen(true)
        .setTicker(ContentTitle)
        //在状态栏中提示的信息
        .setSubText(System.currentTimeMillis().toString())
        //附加内容显示在通知的右侧(APP名字后)
        .setAutoCancel(true)
        .build()
    //设置通知样式

    manager.notify(1, notification)
    //使用系统的通知管理发送通知,id是通知的唯一标识符,如果要清理该通知,调用.cacel方法

    val vibrationPattern = channel.vibrationPattern //震动模式
    val sound = channel.sound   // 通知声音
    val import = channel.importance //通知优先级
    //没卵用,测试用的


    val bl1 = AlertDialog.Builder(context)
        .setMessage("您已经学习了$time" + "个小时了哦,整理整理去休息一下吧!")
        .setPositiveButton("OK") { _, _ -> }
    bl1.show()
    //嫌弃通知不够显眼?声音和震动无法引起用户注意,接一个啥都不执行的对话框
}