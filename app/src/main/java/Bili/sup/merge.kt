package Bili.sup

import com.arthenica.mobileffmpeg.FFmpeg
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

fun merge(audiosrc: String, videosrc: String, destpath: String): Int {

    return FFmpeg.execute("-i $audiosrc -i $videosrc -acodec copy -vcodec copy $destpath")
    //和电脑端的FFmpeg 命令行执行效果一样

}

fun mergefile(file1: String, file2: String, dest: String) {
//用输入输出流转码,弃用
    val inputStream1: InputStream = FileInputStream(file1)
    val inputStream2: InputStream = FileInputStream(file2)
    val outputStream: OutputStream = FileOutputStream(dest)

    var temp = 0
    var data = ByteArray(1048576)

    temp = inputStream1.read(data)
    while (temp != -1) {
        outputStream.write(data, 0, temp)
        //从偏移量为0的地方开始写数据,每次写1048576字节的数据也就是1M
        temp = inputStream1.read(data)

    }
    temp = inputStream2.read(data)
    while (temp != -1) {
        outputStream.write(data, inputStream1.available(), temp)
        //从偏移量为第一个数据大小的地方开始写数据
        temp = inputStream2.read(data)

    }

    inputStream1.close()
    inputStream2.close()
    outputStream.close()
    //释放资源
}


//internal class MyExtractor(path: String) {
//    private var mediaExtractor: MediaExtractor? = null
//    private var videoTrackId = 0
//    private var audioTrackId = 0
//
//    /**
//     * 获取视频 MediaFormat
//     *
//     * @return
//     */
//    var videoFormat: MediaFormat? = null
//
//    /**
//     * 获取音频 MediaFormat
//     *
//     * @return
//     */
//    var audioFormat: MediaFormat? = null
//
//    /**
//     * 获取当前帧的时间戳
//     *
//     * @return
//     */
//    var curSampleTime: Long = 0
//        private set
//
//    /**
//     * 获取当前帧的标志位
//     *
//     * @return
//     */
//    var curSampleFlags = 0
//        private set
//
//    /**
//     * 释放资源
//     */
//    fun release() {
//        mediaExtractor!!.release()
//    }
//
//    /**
//     * 读取一帧的数据
//     *
//     * @param buffer
//     * @return #MyExtractor#readBuffer
//     */
//    fun readBuffer(buffer: ByteBuffer, video: Boolean): Int {
//        //先清空数据
//        buffer.clear()
//        //选择要解析的轨道
//
//        mediaExtractor!!.selectTrack(if (video) videoTrackId else audioTrackId)
//        //读取当前帧的数据
//        val bufferCount = mediaExtractor!!.readSampleData(buffer, 0)
//        if (bufferCount < 0) {
//            return -1
//        }
//        //记录当前时间戳
//        curSampleTime = mediaExtractor!!.sampleTime
//        //记录当前帧的标志位
//        curSampleFlags = mediaExtractor!!.sampleFlags
//        //进入下一帧
//        mediaExtractor!!.advance()
//        return bufferCount
//    }
//
//    init {
//        try {
//            mediaExtractor = MediaExtractor()
//            // 设置数据源
//            mediaExtractor!!.setDataSource(path!!)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        //拿到所有的轨道
//        val count = mediaExtractor!!.trackCount
//        for (i in 0 until count) {
//            //根据下标拿到 MediaFormat
//            val format = mediaExtractor!!.getTrackFormat(i)
//            //拿到 mime 类型
//            val mime = format.getString(MediaFormat.KEY_MIME)
//
//            //拿到视频轨
//            if (mime!!.startsWith("video")) {
//                videoTrackId = i
//                videoFormat = format
//            } else if (mime.startsWith("audio")) {
//                //拿到音频轨
//                audioTrackId = i
//                audioFormat = format
//            }
//        }
//    }
//}


//@Throws(Exception::class)
//fun merge(audioPath: String, videoPath: String, savePath: String) {
//    val starttime: Long = System.currentTimeMillis()
//    //创建音频的 MediaExtractor
//    val audioExtractor = MyExtractor(audioPath)
//    //创建视频的 MediaExtractor
//    val videoExtractor = MyExtractor(videoPath)
//    //拿到音频的 mediaFormat
//    val audioFormat = audioExtractor.audioFormat
//    //拿到音频的 mediaFormat
//    val videoFormat = videoExtractor.videoFormat
//
//
//    val mediaMuxer = MediaMuxer(savePath!!, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
//    //添加音频
//
//    val audioId = mediaMuxer.addTrack(audioFormat!!)
//
//    //添加视频
//
//    val videoId = mediaMuxer.addTrack(videoFormat!!)
//    //开始混合，等待写入
//    mediaMuxer.start()
//    val buffer = ByteBuffer.allocate(1048576)
//    val info = MediaCodec.BufferInfo()
//
//    //混合视频
//
//
//    var videoSize: Int
//    //读取视频帧的数据，直到结束
//    while (videoExtractor.readBuffer(buffer, true).also { videoSize = it } > 0) {
//        //从0帧开始读取
//        info.offset = 0
//        //本次读取的长度
//        info.size = videoSize
//        info.presentationTimeUs = videoExtractor.curSampleTime
//        info.flags = videoExtractor.curSampleFlags
//        mediaMuxer.writeSampleData(videoId, buffer, info)
//    }
//    //写完视频，再把音频混合进去
//
//
//    var audioSize: Int
//
//    //读取音频帧的数据，直到结束
//    while (audioExtractor.readBuffer(buffer, false).also { audioSize = it } > 0) {
//        info.offset = 0
//        info.size = audioSize
//        info.presentationTimeUs = audioExtractor.curSampleTime
//        info.flags = audioExtractor.curSampleFlags
//        mediaMuxer.writeSampleData(audioId, buffer, info)
//    }
//    val quittime: Long = System.currentTimeMillis()
//    //结束执行的时间
//
//
//    //释放资源
//    audioExtractor.release()
//    videoExtractor.release()
//    mediaMuxer.stop()
//    mediaMuxer.release()
//    println("耗时" + ((quittime - starttime).toFloat()) / 1000 + "秒")
//}






