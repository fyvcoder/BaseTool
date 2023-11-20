package com.yf.common.utils

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


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
 * @路径 com.ky.transfer.utils
 * @简介 加密工具类，对应lib中的 hutool-all-4.1.1.jar 使用
 * @时间 2022/07/07
 * @email 8210@xue5678.com
 * @author Mr.Fu
 */
object MyEncryptUtil {
    private const val pubKeyBase64 =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNMhrmwlRstvWHndgNmvhF8tVgh6uo7pPAbxz/RQj8JzyPCQv8DlMU+GzROAinS8zvknKLvVZXT9d6j1x4d+0MY9i9H8IRg6GDoX0ud9erFt6sB6NMkv+cgdUpDIatTl7vAoBCURpbWVB0ZCV90CjXq8TXnfDL3lxGBN77H/xtnQIDAQAB"
    //私钥，仅供卡加密用
    private const val privateKey =
        "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL+0hi8JR0UEi4KqZJUeMaF7ZflMPpb1GNaHqsPqtDVi+W0n/DvfVo2DSWTrbkEqhJ4AeMGJqxFeLHcfNoOvFTlS2dfPh0pDeeNR7YmoTQiuF8RVsKwwwz8lTUclFq6A8cUkQCX+3FUcJtctrjm8cvIYNmt1l7dNeYGd1Y40O4fnAgMBAAECgYEAqoexnYHJbjG+0Hs2gheb2qPkcEFMpAhfEecZFrJfn+XtLxaUn8XqPnP9A9MR/tINqmeNA8ys6gQ11blkSvuiChF8z+2YL+G5hycgDcVNficUMi7h/1fTKXYQFm+1w/urYlKA6gkMH9Jr6R5oaGbT+trcRdayI6QU0Xd++oA6F3ECQQD3r9fQQ4YX7/LyzU4ZotFO6Mr6MUqvglx/j4R/pJihagbKHfP0YwMM91uybcLA2iQojqQCyEc7qLGN3DG086sNAkEAxiOt3ttfF9Vk3jqsNndDSX+A8fL7wsb2KBwv6yayuL2q/LxFyvebySNCWaJ29/WTMgK+qWk1W5zZUVcI+r3xwwJAY2poJEbGDtuX108ClxWcD6Euv4eDY7jeabDwTM+DF97XxM6K+b+4HL84RgD6V6/3LXQ7boZ22QJX6gnExc28DQJALe7tiuF+TQ8qYB5n+ARz8+D8Kpry5HetbyUBstj4y70km4yl/WSjl+B1rEPuopMquYhLXQjH1uIouyObME3CcwJBAMnqGgU4e/nhBQWIadqeTRYAPzBLU9ER/GmyJqEzCLLrCOggAzfpAILlOUV/Q2O60c8qjo5DcLjpxhTxU3yGtvw="
    //编码格式
    private const val CHSR_SET = "UTF-8"

    //密钥长度
    private const val KEY_SIZE = 1024

    //最大加密数据长度
    private const val MAX_ENC_SIZE = KEY_SIZE / 8 - 11

    /**
     * RSA加密
     * @param str 要加密的内容
     * @return 加密后的数据
     */
    fun encode(str: String): String {
        //XLog.d("要加密的内容：$str")
        var outStr = ""
        try {
            // base64编码的公钥
            val decoded: ByteArray = Base64.decode(pubKeyBase64, android.util.Base64.NO_WRAP)
            val pubKey = KeyFactory.getInstance("RSA")
                .generatePublic(X509EncodedKeySpec(decoded)) as RSAPublicKey
            // RSA加密
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            //要加密内容转换为byteArray
            val data: ByteArray = str.toByteArray(charset(CHSR_SET))
            val inputLen = data.size
            val out: ByteArrayOutputStream = ByteArrayOutputStream()
            var offSet = 0
            var cache: ByteArray
            var i = 0
            //对数据分段加密
            while (inputLen - offSet > 0) {
                cache = if (inputLen - offSet > MAX_ENC_SIZE) {
                    cipher.doFinal(data, offSet, MAX_ENC_SIZE);
                } else {
                    cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.size);
                i++;
                offSet = i * MAX_ENC_SIZE;
            }
            val encryptedData = out.toByteArray()
            out.close()

            outStr = Base64.encodeToString(encryptedData, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //XLog.d("加密：$outStr")
        return outStr
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    fun decrypt(str: String, privateKey: String = MyEncryptUtil.privateKey): String {
        if (str.isEmpty()){
            return ""
        }
        var outStr = ""
        try {
            //64位解码加密后的字符串
            val inputByte = Base64.decode(str, Base64.NO_WRAP);
            //base64编码的私钥
            val decoded = Base64.decode(privateKey, Base64.NO_WRAP);
            val priKey: RSAPrivateKey =
                KeyFactory.getInstance("RSA")
                    .generatePrivate(PKCS8EncodedKeySpec(decoded)) as RSAPrivateKey;
            //RSA解密
            val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = String(cipher.doFinal(inputByte));
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outStr;
    }
}