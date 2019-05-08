package     com.zdy.project.wechat_chatroom_helper.plugins

import android.annotation.SuppressLint
import com.gh0u1l5.wechatmagician.spellbook.SpellBook
import com.gh0u1l5.wechatmagician.spellbook.util.BasicUtil
import com.zdy.project.wechat_chatroom_helper.plugins.log.LogRecord
import com.zdy.project.wechat_chatroom_helper.wechat.chatroomView.ChatRoomViewPresenter
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by Mr.Zdy on 2018/3/31.
 */

@SuppressLint("StaticFieldLeak")
class PluginEntry : IXposedHookLoadPackage {


    companion object {

        lateinit var classloader: ClassLoader

        lateinit var chatRoomViewPresenter: ChatRoomViewPresenter

        lateinit var officialViewPresenter: ChatRoomViewPresenter
    }


    override fun handleLoadPackage(p0: XC_LoadPackage.LoadPackageParam) {


        BasicUtil.tryVerbosely {
            if (SpellBook.isImportantWechatProcess(p0)) {
                classloader = p0.classLoader
                SpellBook.startup(p0, listOf(/*MainLauncherUI, MessageHandler, MainAdapter,*/ LogRecord))

            }
        }

    }
}