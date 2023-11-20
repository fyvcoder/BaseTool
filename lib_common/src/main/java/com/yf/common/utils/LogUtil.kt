package com.yf.common.utils

import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.Printer
import com.yf.common.base.BaseApp

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
 * 项目名称：KyConsume
 * 包地址：com.lib.base.utils
 * 类描述：log打印，若用其他log三方库，只需修改此处log打印方法即可
 * 创建时间：2023/11/3
 * Email:8210@xue5678.com
 */
object LogUtil {
    private val printerRunningFile: Printer? by lazy { BaseApp.xlogRunningFilePrinter }

    /**
     * 打印操作记录日志到本地
     */
    fun printRunningLog(msg: Any) {
        if (BaseApp.isPrintLog) {
            //不打印边框
            XLog.disableBorder()
                //不打印线程信息
                .disableThreadInfo()
                //不打印堆栈信息
                .disableStackTrace()
                .logLevel(com.elvishew.xlog.LogLevel.DEBUG)
                .printers(printerRunningFile)
                .d(msg)
        }
    }

    /**
     * 普通打印log,
     */
    fun log(msg: Any, level: LogLevel = LogLevel.DEBUG) = when (level) {
        LogLevel.VERBOSE -> {
            XLog.v(msg)
        }

        LogLevel.DEBUG -> {
            XLog.d(msg)
        }

        LogLevel.INFO -> {
            XLog.i(msg)
        }

        LogLevel.WARN -> {
            XLog.w(msg)
        }

        LogLevel.ERROR -> {
            XLog.e(msg)
        }

        LogLevel.JSON -> {
            XLog.disableBorder()
                .json(msg.toString())
        }

        LogLevel.XML -> {
            XLog.xml(msg.toString())
        }
    }
}