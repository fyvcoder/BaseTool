package com.yf.tool.db

import android.content.Context
import com.yf.common.utils.LogLevel
import com.yf.common.utils.LogUtil
import com.yf.tool.BuildConfig
import io.objectbox.BoxStore
import io.objectbox.android.Admin

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
 * 项目名称：YfBaseTool
 * 包地址：com.yf.tool.db
 * 类描述：
 * 创建时间：2023/11/20
 * Email:8210@xue5678.com
 */
object ObjectBox {
    lateinit var store: BoxStore
        private set

    /**
     * 初始化
     */
    fun init(context: Context) {
        store = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
        if (BuildConfig.DEBUG) {
            val started = Admin(store).start(context)
            LogUtil.log("ObjectBoxAdmin -----> Started: $started", level = LogLevel.INFO)
        }
    }
}