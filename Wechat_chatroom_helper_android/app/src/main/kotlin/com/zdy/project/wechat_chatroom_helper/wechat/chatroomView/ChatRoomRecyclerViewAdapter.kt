package com.zdy.project.wechat_chatroom_helper.wechat.chatroomView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.ViewGroup
import com.zdy.project.wechat_chatroom_helper.ChatInfoModel
import com.zdy.project.wechat_chatroom_helper.io.AppSaveInfo
import com.zdy.project.wechat_chatroom_helper.plugins.main.adapter.ConversationItemHandler
import com.zdy.project.wechat_chatroom_helper.plugins.main.adapter.MainAdapter
import com.zdy.project.wechat_chatroom_helper.plugins.main.main.MainLauncherUI
import com.zdy.project.wechat_chatroom_helper.wechat.WXObject
import de.robv.android.xposed.XposedHelpers
import java.util.*

@Suppress("DEPRECATION")
/**
 * Created by Mr.Zdy on 2017/8/27.
 */

class ChatRoomRecyclerViewAdapter constructor(private val mContext: Context) : RecyclerView.Adapter<ChatRoomViewHolder>() {


    private lateinit var onDialogItemClickListener: OnDialogItemClickListener

    var data = ArrayList<ChatInfoModel>()

    fun setOnDialogItemClickListener(onDialogItemClickListener: OnDialogItemClickListener) {
        this.onDialogItemClickListener = onDialogItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(ChatRoomViewFactory.getItemView(mContext))
    }

    private fun getObject(position: Int): ChatInfoModel {
        return data[position]
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {

        val item = getObject(position)

        holder.nickname.text = if (item.nickname.isEmpty()) "群聊" else item.nickname
        holder.content.text = Html.fromHtml((ConversationItemHandler.getConversationContent(MainAdapter.originAdapter, item, position)
                ?: (item.content)).toString())
        holder.time.text = ConversationItemHandler.getConversationTimeString(MainAdapter.originAdapter, item.conversationTime)

        ConversationItemHandler.getConversationAvatar(item.username.toString(), holder.avatar)

        if (item.unReadCount > 0)
            holder.unread.background = ShapeDrawable(object : Shape() {
                override fun draw(canvas: Canvas, paint: Paint) {
                    val size = (canvas.width / 2).toFloat()

                    paint.isAntiAlias = true
                    paint.color = -0x10000
                    paint.style = Paint.Style.FILL_AND_STROKE
                    canvas.drawCircle(size, size, size, paint)
                }
            })
        else
            holder.unread.background = BitmapDrawable(mContext.resources)

        holder.itemView.background = ChatRoomViewFactory.getItemViewBackground(mContext)
        holder.itemView.setOnClickListener {
            XposedHelpers.callMethod(MainLauncherUI.launcherUI, WXObject.MainUI.M.StartChattingOfLauncherUI, item.username, null, true)
        }
        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener true
        }

        holder.nickname.setTextColor(Color.parseColor("#" + AppSaveInfo.nicknameColorInfo()))
        holder.content.setTextColor(Color.parseColor("#" + AppSaveInfo.contentColorInfo()))
        holder.time.setTextColor(Color.parseColor("#" + AppSaveInfo.timeColorInfo()))
        holder.divider.setBackgroundColor(Color.parseColor("#" + AppSaveInfo.dividerColorInfo()))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnDialogItemClickListener {
        fun onItemClick(relativePosition: Int)

        fun onItemLongClick(relativePosition: Int)
    }


}
