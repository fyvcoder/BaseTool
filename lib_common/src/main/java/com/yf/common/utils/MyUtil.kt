package com.yf.common.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Environment
import android.os.StatFs
import com.yf.common.base.BaseApp
import java.text.SimpleDateFormat
import java.util.*

/**
 *                            _ooOoo_
 *                           o8888888o
 *                           88" . "88
 *                           (| -_- |)
 *                            O\ = /O
 *                        ____/`---'\____
 *                      .   ' \\| |// `.
 *                       / \\||| : |||// \
 *                     / _||||| -:- |||||- \
 *                       | | \\\ - /// | |
 *                     | \_| ''\---/'' | |
 *                      \ .-\__ `-` ___/-. /
 *                   ___`. .' /--.--\ `. . __
 *                ."" '< `.___\_<|>_/___.' >'"".
 *               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 *                 \ \ `-. \_ __\ /__ _/ .-` / /
 *         ======`-.____`-.___\_____/___.-`____.-'======
 *                            `=---='
 *
 *         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *                  佛祖保佑             永无BUG
 *          佛曰:
 *                  写字楼里写字间，写字间里程序员；
 *                  程序人员写程序，又拿程序换酒钱。
 *                  酒醒只在网上坐，酒醉还来网下眠；
 *                  酒醉酒醒日复日，网上网下年复年。
 *                  但愿老死电脑间，不愿鞠躬老板前；
 *                  奔驰宝马贵者趣，公交自行程序员。
 *                  别人笑我忒疯癫，我笑自己命太贱；
 *                  不见满街漂亮妹，哪个归得程序员？
 * 项目名称：XYDConsume
 * 包地址：com.xyd.consume.utils
 * 类描述：字符串工具类
 * 创建时间：2022/03/17
 * Email:8210@xue5678.com
 */
object MyUtil {
    /**
     * 姓名显示加密
     * @param name
     * @return
     */
    fun formatName(name: String): String {
        if (name.isEmpty()) {
            return ""
        }
        var newStr = ""
        if (name.length == 2) {
            newStr = name.substring(0, 1) + "*"
        } else if (name.length > 2) {
            var a = ""
            for (i in 0 until (name.length - 2)) {
                a += "*"
            }
            newStr = name.substring(0, 1) + a + name.substring(name.length - 1)
        } else {
            newStr = name
        }
        return newStr
    }

    /**
     * @param nowTimeStr 当前时间(HH:mm)
     * @param mealTypeTime 餐别时间
     */
    fun isEffectiveDate(nowTimeStr: String, mealTypeTime: String): Boolean {
        if (mealTypeTime.isEmpty()) {
            return false
        }
        val format = "HH:mm"
        val nowTime: Date? = SimpleDateFormat(format, Locale.CHINESE).parse(nowTimeStr)
        val startTime: Date? =
            SimpleDateFormat(format, Locale.CHINESE).parse(mealTypeTime.split("-")[0])
        val endTime: Date? =
            SimpleDateFormat(format, Locale.CHINESE).parse(mealTypeTime.split("-")[1])
//        XLog.d("nowTime = ${nowTime}, startTime = ${startTime}, endTime = $endTime")
        return isEffectiveDate(nowTime, startTime, endTime)
    }

    /**
     * @param nowTimeStr 当前时间(HH:mm)
     * @param mealTypeTime 餐别时间
     */
    fun isEffectiveDate(
        nowTimeStr: String,
        start: String,
        end: String,
        format: String = "HH:mm"
    ): Boolean {
        if (nowTimeStr.isEmpty() || start.isEmpty() || end.isEmpty()) {
            return false
        }
        val nowTime: Date? = SimpleDateFormat(format, Locale.CHINESE).parse(nowTimeStr)
        val startTime: Date? = SimpleDateFormat(format, Locale.CHINESE).parse(start)
        val endTime: Date? = SimpleDateFormat(format, Locale.CHINESE).parse(end)
//        XLog.d("nowTime = ${nowTime}, startTime = ${startTime}, endTime = $endTime")
        return isEffectiveDate(nowTime, startTime, endTime)
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return true:在区间内  false:不在区间内
     */
    fun isEffectiveDate(nowTime: Date?, startTime: Date?, endTime: Date?): Boolean {
        if (nowTime?.time == startTime?.time
            || nowTime?.time == endTime?.time
        ) {
            return true
        }
        val date: Calendar = Calendar.getInstance()
        date.time = nowTime
        val begin: Calendar = Calendar.getInstance()
        begin.time = startTime
        val end: Calendar = Calendar.getInstance()
        end.time = endTime
        return date.after(begin) && date.before(end)
    }

    /**
     * 获取设备存储空间
     * @return 已用/总存储
     */
    fun getStorageSpace(): String {
        val path = Environment.getExternalStorageDirectory()
        val stat: StatFs = StatFs(path.path)
        var blockSize: Long = stat.blockSize.toLong()
        var availableBlocks: Long = stat.availableBlocks.toLong()
        var totalBlocks: Long = stat.blockCount.toLong()
        var available = availableBlocks * blockSize / (1024 * 1024)
        var total = totalBlocks * blockSize / (1024 * 1024)
        //XLog.d("getStorageSpace = ${available}")
        return "${total - available}/${total}"
    }

    /**
     * 获取设备运行内存
     * @return 可用/总运行内存
     */
    fun getRunningMemory(): String {
        val manager: ActivityManager =
            BaseApp.getContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()
        //设备内存
        manager.getMemoryInfo(mi)
        //val totalMem = Formatter.formatFileSize(BaseApp.context, mi.totalMem)
        val totalMem = mi.totalMem / (1024 * 1024)
        //XLog.d("设备运行总内存 totalMem：$totalMem")
        //val availMem = Formatter.formatFileSize(BaseApp.context, mi.availMem)
        val availMem = mi.availMem / (1024 * 1024)
        //XLog.d("设备运行剩余内存 availMem：$availMem")
        return "${totalMem - availMem}/${totalMem}"
    }
}