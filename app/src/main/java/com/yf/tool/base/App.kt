package com.yf.tool.base

import androidx.lifecycle.ViewModelProvider
import com.yf.common.base.BaseApp
import com.yf.tool.db.ObjectBox
import com.yf.tool.viewmodels.BaseViewModel

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
 * 包地址：com.yf.tool.base
 * 类描述：
 * 创建时间：2023/11/20
 * Email:8210@xue5678.com
 */
class App : BaseApp() {
    companion object {
        //整个程序的全局viewModel
        lateinit var vm: BaseViewModel
    }

    override fun onCreate() {
        super.onCreate()
        //初始化viewModel
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
            .create(BaseViewModel::class.java)
//        初始化数据库
        ObjectBox.init(getContext())
    }
}