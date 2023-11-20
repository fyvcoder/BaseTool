package com.yf.common.http

import com.yf.common.utils.MyEncryptUtil
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import rxhttp.wrapper.annotation.Param
import rxhttp.wrapper.entity.KeyValuePair
import rxhttp.wrapper.param.Method
import rxhttp.wrapper.param.NoBodyParam
import java.net.URLEncoder


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
 * @简介 网络get加密请求
 * @时间 2022/07/07
 * @email 8210@xue5678.com
 * @author Mr.Fu
 */
@Param(methodName = "getEncrypt")
class GetEncryptParam(url: String) : NoBodyParam(url, Method.GET) {
    override fun getHttpUrl(): HttpUrl {
        //存储加密后的参数
        val paramsBuilder = StringBuilder()
        //拿到参数
        val params: MutableList<KeyValuePair>? = queryParam
        if (!params.isNullOrEmpty() && params.size > 0) {
            for (i in params.indices) {
                if (i == params.lastIndex) {
                    paramsBuilder.append(
                        "${params[i].key}=${
                            URLEncoder.encode(
                                MyEncryptUtil.encode(
                                    params[i].value.toString()
                                ), "UTF-8"
                            )
                        }"
                    )
                } else {
                    paramsBuilder.append(
                        "${params[i].key}=${
                            URLEncoder.encode(
                                MyEncryptUtil.encode(
                                    params[i].value.toString()
                                ), "UTF-8"
                            )
                        }&"
                    )
                }
            }
        }
        //拿到请求Url
        val simpleUrl = simpleUrl
        return if (paramsBuilder.isEmpty()) simpleUrl.toHttpUrl() else "${simpleUrl}?$paramsBuilder".toHttpUrl()
    }
}