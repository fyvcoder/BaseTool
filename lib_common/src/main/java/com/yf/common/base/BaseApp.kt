package com.yf.common.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Display
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.ClassicFlattener
import com.elvishew.xlog.flattener.PatternFlattener
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.ConsolePrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.BackupStrategy2
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.ChangelessFileNameGenerator
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import com.qiniu.android.storage.UploadManager
import com.xuexiang.constant.DateFormatConstants
import com.xuexiang.xutil.XUtil
import com.xuexiang.xutil.data.DateUtils
import com.xuexiang.xutil.file.FileUtils
import com.yf.common.BuildConfig
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.onAdaptListener
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.callback.Consumer
import rxhttp.wrapper.converter.GsonConverter
import rxhttp.wrapper.ssl.HttpsUtils
import xcrash.ICrashCallback
import xcrash.XCrash
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


/**
 * ░░░░░░░░░░░░░░░░░░░░░░░░▄░░
 * ░░░░░░░░░▐█░░░░░░░░░░░▄▀▒▌░
 * ░░░░░░░░▐▀▒█░░░░░░░░▄▀▒▒▒▐
 * ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐
 * ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐
 * ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌
 * ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒
 * ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐
 * ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄
 * ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒
 * ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒
 * 单身狗就这样默默地看着你，一句话也不说。
 *
 *
 * 项目名称： 学有道消费
 * 类描述：
 * 创建时间： 2021/08/27 17:38
 */
open class BaseApp : Application() {

    companion object {
        private lateinit var _baseContext: BaseApp

        fun getContext(): Context = _baseContext

        //log文件夹
        const val dir_log = "/logs/"

        //app下载文件夹
        const val dir_app = "/app/"

        //崩溃日志文件夹
        const val dir_crash = "/crash/"

        //七牛云上传
        lateinit var uploadManager: UploadManager

        //旷视人脸库存放地址
        val faceDir: String
            get() {
                return if (this::_baseContext.isInitialized) {
                    "${_baseContext.filesDir}/face/"
                } else {
                    ""
                }
            }

        /**
         * 获取缓存路径下文件夹
         * @param dir 文件夹名称
         */
        fun getCacheDir(dir: String): String? {
            return if (FileUtils.createOrExistsDir("${XUtil.getContext().externalCacheDir?.absolutePath}$dir")) {
                "${XUtil.getContext().externalCacheDir?.absolutePath}$dir"
            } else {
                null
            }
        }

        var xlogRunningFilePrinter: Printer? = null
        //是否打印本地日志记录
        var isPrintLog:Boolean = true

    }

    override fun onCreate() {
        super.onCreate()
        _baseContext = this

        uploadManager = UploadManager()

        initAutoSize()
        initXLog()
        //XUtil全局初始化
        XUtil.init(this)
        initXLogRunningFilePrinter()
        //崩溃工具初始化
        initXCrash(this)

        initRxHttp()
        initCoil()
    }

    /**
     * 屏幕适配
     */
    private fun initAutoSize() {
        //如果在某些特殊情况下出现 InitProvider 未能正常实例化, 导致 AndroidAutoSize 未能完成初始化
        //可以主动调用 AutoSize.checkAndInit(this) 方法, 完成 AndroidAutoSize 的初始化后即可正常使用
        AutoSize.checkAndInit(this)
        //        如何控制 AndroidAutoSize 的初始化，让 AndroidAutoSize 在某些设备上不自动启动？https://github.com/JessYanCoding/AndroidAutoSize/issues/249
        //以下是 AndroidAutoSize 可以自定义的参数, [AutoSizeConfig] 的每个方法的注释都写的很详细
        //使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!

        AutoSizeConfig.getInstance() //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
            //如果没有这个需求建议不开启
            .setCustomFragment(false)
            //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变;如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
            .setExcludeFontScale(true)
            //区别于系统字体大小的放大比例, AndroidAutoSize 允许 APP 内部可以独立于系统字体大小之外，独自拥有全局调节 APP 字体大小的能力
            //当然, 在 APP 内您必须使用 sp 来作为字体的单位, 否则此功能无效, 不设置或将此值设为 0 则取消此功能
            //                .setPrivateFontScale(0.8f)
            //屏幕适配监听器
            .setOnAdaptListener(object : onAdaptListener {
                override fun onAdaptBefore(target: Any?, activity: Activity?) {
                    //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
                    //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                    //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会主动重绘当前页面, 所以这时您需要自行重绘当前页面的参数一定要不要传 Application!!!
//                        ScreenUtils.getScreenSize(activity);
//                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
//                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
//                        AutoSizeConfig.getInstance().stop(activity);//停止框架
                    //log打印
//                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target?.javaClass?.name))
//                    LogUtil.log("%s onAdaptBefore!", target?.javaClass?.name)
                }

                override fun onAdaptAfter(target: Any?, activity: Activity?) {
                    //log打印
//                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target?.javaClass?.name))
//                    LogUtil.log("%s onAdaptAfter!", target?.javaClass?.name)
                }
            }) //是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
            //                .setLog(false)
            //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
            //AutoSize 会将屏幕总高度减去状态栏高度来做适配
            //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
            //在全面屏或刘海屏幕设备中, 获取到的屏幕高度可能不包含状态栏高度, 所以在全面屏设备中不需要减去状态栏高度，所以可以 setUseDeviceSize(true)
            .setUseDeviceSize(true)
            .isBaseOnWidth = true //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好

        Log.e("App", "初始化AutoSize屏幕适配")
    }

    /**
     * 爱奇艺carsh捕获
     */
    private fun initXCrash(context: Context) {
        // callback for java crash, native crash and ANR
        val callback = ICrashCallback { logPath, emergency ->
            Log.e(
                "crash",
                "log path: " + (logPath ?: "(null)") + ", emergency: " + (emergency ?: "(null)")
            )
            if (emergency != null) {
//                debug(logPath, emergency)

                // Disk is exhausted, send crash report immediately.
                //磁盘耗尽，立即发送崩溃报告
//                sendThenDeleteCrashLog(logPath, emergency)
            } else {
                // Add some expanded sections. Send crash report at the next time APP startup.
                //添加一些展开的部分。在下次应用程序启动时发送崩溃报告。

                // OK
                /*TombstoneManager.appendSection(logPath, "expanded_key_1", "expanded_content")
                TombstoneManager.appendSection(
                    logPath,
                    "expanded_key_2",
                    "expanded_content_row_1\nexpanded_content_row_2"
                )*/

                // Invalid. (Do NOT include multiple consecutive newline characters ("\n\n") in the content string.)
                //无效。(内容字符串中不要包含多个连续换行符(“\n\n”)。)
                // TombstoneManager.appendSection(logPath, "expanded_key_3", "expanded_content_row_1\n\nexpanded_content_row_2");
//                debug(logPath, null)
            }
        }
        // Initialize xCrash.
        XCrash.init(
            context, XCrash.InitParameters()
                .setPlaceholderCountMax(3)
                .setPlaceholderSizeKb(512)
                .setLogDir(getCacheDir(dir_crash))
                .setAnrFastCallback(callback)
                .setAnrCallback(callback)
                .setJavaCallback(callback)
                .setNativeCallback(callback)
        )
//        XCrash.init(this)
        Log.e("App", "初始化XCrash")
    }

    /**
     * 日志打印
     */
    private fun initXLog() {
        val config = LogConfiguration.Builder()
            // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
            .logLevel(if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.NONE)
            //.logLevel(LogLevel.DEBUG)
            // 指定 TAG，默认为 "X-LOG"
            .tag("ky-consume")
            // 允许打印线程信息，默认禁止
//            .enableThreadInfo()
            // 允许打印深度为 2 的调用栈信息，默认禁止
            .enableStackTrace(2)
            // 允许打印日志边框，默认禁止
            .enableBorder()
            // 指定 JSON 格式化器，默认为 DefaultJsonFormatter
//            .jsonFormatter(MyJsonFormatter())
            // 指定 XML 格式化器，默认为 DefaultXmlFormatter
//            .xmlFormatter(MyXmlFormatter())
            // 指定可抛出异常格式化器，默认为 DefaultThrowableFormatter
//            .throwableFormatter(MyThrowableFormatter())
            // 指定线程信息格式化器，默认为 DefaultThreadFormatter
//            .threadFormatter(MyThreadFormatter())
            // 指定调用栈信息格式化器，默认为 DefaultStackTraceFormatter
//            .stackTraceFormatter(MyStackTraceFormatter())
            // 指定边框格式化器，默认为 DefaultBorderFormatter
//            .borderFormatter(MyBoardFormatter())
            // 为指定类型添加对象格式化器; 默认使用 Object.toString()
//            .addObjectFormatter(AnyClass::class.java, AnyClassObjectFormatter())
            // 添加黑名单 TAG 过滤器
            //.addInterceptor(
            //    BlacklistTagsFilterInterceptor(
            //        "blacklist1",
            //        "blacklist2",
            //        "blacklist3"
            //    )
            //)
            // 添加一个日志拦截器
//            .addInterceptor(MyInterceptor())
            .build()

        // 通过 android.util.Log 打印日志的打印器
        val androidPrinter: Printer = AndroidPrinter(false)
        // 通过 System.out 打印日志到控制台的打印器
        val consolePrinter: Printer = ConsolePrinter()

        val filePrinter: Printer =
            FilePrinter.Builder(getCacheDir(dir_log)) // 指定保存日志文件的路径
                // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                .fileNameGenerator(DateFileNameGenerator())
                // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
                .backupStrategy(NeverBackupStrategy())
                // 指定日志文件清除策略，默认为 NeverCleanStrategy()
                .cleanStrategy(FileLastModifiedCleanStrategy(5 * 24 * 60 * 60 * 1000))
                // 指定日志平铺器，默认为 DefaultFlattener
                .flattener(ClassicFlattener())
                // 指定日志写入器，默认为 SimpleWriter
//            .writer(MyWriter())
                .build()

        XLog.init(
            // 初始化 XLog
            config,  // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
            androidPrinter,  // 添加任意多的打印器。如果没有添加任何打印器，会默认使用 AndroidPrinter(Android)/ConsolePrinter(java)
//            consolePrinter,
//            filePrinter
        )
        Log.e("App", "初始化XLog")
    }

    /**
     * 初始化操作记录日志的FilePrinter
     */
    private fun initXLogRunningFilePrinter() {
        xlogRunningFilePrinter = FilePrinter
            // 指定保存日志文件的路径
            .Builder(getCacheDir("/logs/"))
            // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
            .fileNameGenerator(
                ChangelessFileNameGenerator(
                    DateUtils.getNowString(
                        SimpleDateFormat(
                            DateFormatConstants.yyyyMMddNoSep,
                            Locale.CHINESE
                        )
                    )
                )
            )
            // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
            .backupStrategy(object : BackupStrategy2 {
                override fun shouldBackup(file: File?): Boolean {
                    //文件大于5兆时触发
                    return (file?.length() ?: 0) > (3 * 1024 * 1024)
                }

                override fun getMaxBackupIndex(): Int {
                    return 0
                }

                override fun getBackupFileName(fileName: String?, backupIndex: Int): String {
                    return "${fileName}_${System.currentTimeMillis()}.log"
                }
            })
            // 指定日志文件清除策略，默认为 NeverCleanStrategy()
            .cleanStrategy(FileLastModifiedCleanStrategy(5 * 24 * 60 * 60 * 1000))
            // 指定日志平铺器，默认为 DefaultFlattener
            .flattener(PatternFlattener("{d} -----> {m}"))
            // 指定日志写入器，默认为 SimpleWriter
            //.writer(MyWriter())
            .build()
    }

    /**
     * 初始化RxHttp
     */
    private fun initRxHttp() {
        val sslParams = HttpsUtils.getSslSocketFactory()
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .sslSocketFactory(
                sslParams.sSLSocketFactory,
                sslParams.trustManager
            ) //添加信任证书
            .hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true }) //忽略host验证
            .build()

        //设置缓存目录为：Android/data/{app包名目录}/cache/RxHttpCache
        val cacheDir = File(this.externalCacheDir, "RxHttpCache")
        //设置最大缓存为10M，缓存有效时长为60秒
        /*RxHttpPlugins.init(okHttpClient)      //自定义OkHttpClient对象
            .setDebug(true)                //是否开启调试模式，开启后，logcat过滤RxHttp，即可看到整个请求流程日志
//            .setCache(File, long, CacheMode)  //配置缓存目录，最大size及缓存模式
//            .setExcludeCacheKeys(String...)   //设置一些key，不参与cacheKey的组拼
//        .setResultDecoder(Function)       //设置数据解密/解码器，非必须
//            .setConverter(IConverter)         //设置全局的转换器，非必须
            .setOnParamAssembly(Function)    //设置公共参数/请求头回调*/
        RxHttpPlugins.init(okHttpClient)
            .setDebug(BuildConfig.DEBUG, false, 2)//调试模式/分段打印/json数据格式化输出
            .setConverter(GsonConverter.create())
//            .setOnParamAssembly(Consumer { it.addHeader("token", deviceToken) })
        Log.e("BaseApp", "初始化RxHttp")
    }

    /**
     * 初始化图片加载
     */
    private fun initCoil() {
        //全局实例化图片加载器的gif显示
        val imageLoder = ImageLoader.Builder(this).components {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //ImageDecoderDecoder由Android的ImageDecoder API提供支持，该API仅适用于API 28 及更高版本
                add(ImageDecoderDecoder.Factory())
            } else {
                //GifDecoder支持所有 API 级别，但速度较慢
                add(GifDecoder.Factory())
            }
        }.build()
        Coil.setImageLoader(imageLoder)
    }
}