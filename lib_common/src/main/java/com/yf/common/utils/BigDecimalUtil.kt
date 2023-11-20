package com.yf.common.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.regex.Pattern

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
 * 类描述：金额计算工具类
 * 创建时间：2022/03/02
 * Email:8210@xue5678.com
 */
object BigDecimalUtil {
    // 需要精确至小数点后几位
    private const val DECIMAL_POINT_NUMBER: Int = 2
    private val pattern = Pattern.compile("[^-0-9.]")

    /**
     * 加法运算
     */
    @JvmStatic
    fun add(d1: String, d2: String): String =
        BigDecimal(d1).add(BigDecimal(d2)).setScale(DECIMAL_POINT_NUMBER, RoundingMode.DOWN)
            .toString()

    /**
     * 加法运算
     */
    @JvmStatic
    fun add(d1: String, d2: String, pointNumber: Int): String =
        BigDecimal(d1).add(BigDecimal(d2)).setScale(pointNumber, RoundingMode.DOWN).toString()

    /**
     * 减法运算
     */
    @JvmStatic
    fun sub(d1: String, d2: String): String = BigDecimal(d1).subtract(BigDecimal(d2))
        .setScale(DECIMAL_POINT_NUMBER, RoundingMode.DOWN).toString()

    /**
     * 减法运算
     */
    @JvmStatic
    fun sub(d1: String, d2: String, pointNumber: Int): String =
        BigDecimal(d1).subtract(BigDecimal(d2)).setScale(pointNumber, RoundingMode.DOWN)
            .toString()

    /**
     * 乘法运算
     */
    @JvmStatic
    fun mul(d1: String, d2: String): String = BigDecimal(d1).multiply(BigDecimal(d2))
        .setScale(DECIMAL_POINT_NUMBER, RoundingMode.DOWN).toString()

    /**
     * 乘法运算
     */
    @JvmStatic
    fun mul(d1: String, d2: String, pointNumber: Int): String =
        BigDecimal(d1).multiply(BigDecimal(d2))
            .setScale(pointNumber, RoundingMode.DOWN).toString()

    /**
     * 除法运算
     */
    @JvmStatic
    fun div(d1: String, d2: String): String =
        BigDecimal(d1).divide(BigDecimal(d2)).setScale(DECIMAL_POINT_NUMBER, RoundingMode.DOWN)
            .toString()

    /**
     * 除法运算
     */
    @JvmStatic
    fun div(d1: String, d2: String, pointNumber: Int): String =
        BigDecimal(d1).divide(BigDecimal(d2)).setScale(pointNumber, RoundingMode.DOWN)
            .toString()

    /**
     * 格式化金额,
     * @method
     * @date: 2020/5/21 19:48
     * @author: moran
     * @param value 金额内容
     * @param pattern 金额格式化形式
     * @return 金额
     */
    @JvmStatic
    fun decimalFormatMoney(value: String, pattern: String = "0.00"): String {
        val format = DecimalFormat(pattern)
        return format.format(BigDecimal(formatMoney(value)))
    }

    @JvmStatic
    fun decimalFormat(value: String): String {
        val format = DecimalFormat("0.00")
        return format.format(BigDecimal(formatMoney(value)))
    }

    /**
     * 去除非数字内容，如：0.12元，0,01，1,11
     * @date: 2020/5/21 19:16
     * @author: moran
     * @param value 格式化金额类
     * @return
     */
    private fun formatMoney(value: String): String {
        if ("" == value) {
            return "0.00"
        }
        val money = pattern.matcher(value).replaceAll("").trim()
        if ("" == money) {
            return "0.00"
        }
        return money
    }
}