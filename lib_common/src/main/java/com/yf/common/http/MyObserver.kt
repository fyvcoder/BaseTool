package com.yf.common.http

import com.yf.common.utils.LogUtil
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import rxhttp.wrapper.exception.ParseException

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
 * @项目名称 kyTransfer
 * @路径 com.ky.transfer.myHttp
 * @简介 网络请求回调，也可以自己继承[Observer]类来重写自己的方法
 * @时间 2022/07/05
 * @email 8210@xue5678.com
 * @author Mr.Fu
 */
abstract class MyObserver<T : Any> : Observer<T> {
    override fun onSubscribe(d: Disposable) {
        onStart()
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        if (e is ParseException) {
            //token无效
            if (e.errorCode == "401") {
                LogUtil.printRunningLog("网络请求回调e.errorCode=401，重新获取token")
            }
        }
        onFail(e)
        onFinish()
    }

    override fun onComplete() {
        onFinish()
    }

    /**
     * 此方法可选
     * 网络请求之前
     */
    open fun onStart() {}

    /**
     * 成功的回调
     */
    abstract fun onSuccess(data: T)

    /**
     * 错误回调
     */
    abstract fun onFail(e: Throwable)

    /**
     * 此方法可选
     * 整个网络请求结束的操作
     */
    open fun onFinish() {}
}