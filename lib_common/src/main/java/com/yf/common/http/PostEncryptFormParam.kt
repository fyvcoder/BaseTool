package com.yf.common.http

import com.yf.common.utils.MyEncryptUtil
import okhttp3.RequestBody
import rxhttp.wrapper.annotation.Param
import rxhttp.wrapper.param.FormParam
import rxhttp.wrapper.param.Method
import rxhttp.wrapper.utils.GsonUtil

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
 * @简介
 * @时间 2022/07/07
 * @email 8210@xue5678.com
 * @author Mr.Fu
 */
@Param(methodName = "postEncryptForm")
class PostEncryptFormParam(url: String) : FormParam(url, Method.POST) {
    override fun getRequestBody(): RequestBody {
        //这里拿到你添加的所有参数
        val json = if (bodyParam == null) "" else GsonUtil.toJson(bodyParam)
        //根据上面拿到的参数，自行实现加密逻辑
        val encryptStr: String = MyEncryptUtil.encode(json)
        addHeader("encryptStr", encryptStr);
        return super.getRequestBody();
    }
}