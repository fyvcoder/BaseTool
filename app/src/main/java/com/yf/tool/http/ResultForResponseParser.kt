package com.yf.tool.http

import okhttp3.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.convertTo
import java.io.IOException
import java.lang.reflect.Type

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
 * 包地址：com.xyd.consume.myHttp
 * 类描述：
 * 创建时间：2022/03/09
 * Email:8210@xue5678.com
 */
@Parser(name = "ResultByResp")
open class ResultForResponseParser<T> : TypeParser<ResponseBean<T>> {
    //该构造方法是必须的
    protected constructor() : super()

    //如果依赖了RxJava，该构造方法也是必须的
    constructor(type: Type) : super(type)

    @Throws(IOException::class)
    override fun onParse(response: Response): ResponseBean<T> {
        if (response.code == 200) {
            //这里的types就是自定义Response<T>里面的泛型类型
            val data: ResponseBean<T> = response.convertTo(ResponseBean::class.java, *types)
            return if (data.success) {
                data
            } else {
                if (data.data == null) {
                    throw ParseException("${data.status}", data.message, response)
                } else {
                    data
                }
            }
        } else {
            when (response.code) {
                //Not found
                404 -> {
                    throw ParseException("${response.code}", "请求的资源路径不存在，请稍后重试", response)
                }
                //Internal Server Error
                500 -> {
                    throw ParseException("${response.code}", "服务器错误，请稍后重试", response)
                }
                //Bad Gateway
                502 -> {
                    throw ParseException("${response.code}", "服务器网关错误，请稍后重试", response)
                }
                //Service Unavailable
                503 -> {
                    throw ParseException("${response.code}", "服务器服务暂时不可用，请稍后重试", response)
                }
                else -> {
                    throw ParseException("${response.code}", "网络故障，请稍后重试", response)
                }
            }
        }
    }
}