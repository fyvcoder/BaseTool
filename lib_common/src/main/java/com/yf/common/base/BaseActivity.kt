package com.yf.common.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elvishew.xlog.XLog
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar
import com.yf.common.R


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
 * 包地址：com.xyd.consume.acts.base
 * 类描述：基类，所有的Activity都继承这个类
 * 创建时间：2022/02/21
 * Email:8210@xue5678.com
 */
abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var me: Activity
    protected val baseHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        me = this //引用me自己，便于子类调用
        XLog.i("onCreate -----> ${me.localClassName}")
        if (!isTaskRoot) {
            val intent = intent
            val intentAction = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction == Intent.ACTION_MAIN
            ) {
                finish()
                return
            }
        }
    }

    /**
     * 获取根布局
     *
     * @return 布局ID
     */
//    protected abstract val layoutId: Int

    /**
     * 加载数据
     */
    protected abstract fun initData()

    /**
     * 初始化点击事件
     */
    protected abstract fun initListener()

    /**
     * 可选方法，有列表时用到
     */
    open fun initAdapter() {

    }

    /**
     * 可选方法，协程使用
     */
    open fun initLifecycle() {

    }

    override fun onDestroy() {
        super.onDestroy()
        baseHandler.removeCallbacksAndMessages(null)
    }

    /**
     * 传递键盘点击事件
     * @param key 点击的按钮对应值
     */
    protected abstract fun onKeyClick(key: String)

    /**
     * 弹出提示框
     * @param title 标题
     * @param msg 弹窗显示内容
     * @param backgroundColor 背景颜色 0:默认颜色 1:成功 2:报错
     */
    fun showTopTips(msg: String, title: String = "提示", backgroundColor: Int = 0) {
        CookieBar.builder(me)
            .setTitle(title)
            .setMessage(msg)
            .setBackgroundColor(
                when (backgroundColor) {
                    1 -> {
                        R.color.color_00cc99
                    }

                    2 -> {
                        R.color.color_ef555f
                    }

                    else -> {
                        0
                    }
                }
            )
            .setDuration(2000)
            .setAction(
                "好的",
                View.OnClickListener {})
            .show()
    }

    //loadingDialog
    private var dialog: MiniLoadingDialog? = null

    /**
     * 显示加载框
     */
    fun showLoading(msg: String = "") {
        if (me.isFinishing) {
            return
        }
        if (dialog != null && dialog?.isShowing == true) {
            dialog?.updateMessage(msg)
            return
        }
        dialog = WidgetUtils.getMiniLoadingDialog(me).apply {
            setCancelable(false)
            if (msg.isNotEmpty()) {
                updateMessage(msg)
            }
            show()
        }
    }

    /**
     * 关闭加载框
     */
    fun dismissLoading() {
        if (me.isFinishing) {
            return
        }
        if (dialog != null && dialog?.isLoading == true) {
            dialog?.dismiss()
            dialog?.recycle()
        }
    }
}