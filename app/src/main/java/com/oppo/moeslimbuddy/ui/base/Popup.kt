package com.oppo.moeslimbuddy.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.oppo.moeslimbuddy.R
import com.oppo.moeslimbuddy.databinding.DialogPopupBinding

class Popup private constructor(context: Context, private val view: View?, themeResId: Int) :
    Dialog(context, themeResId) {
    private var dismissible = false

    enum class Orientation {
        Horizontal, Vertical
    }

    @SuppressLint("StaticFieldLeak")
    companion object {
        fun show(
            activity: FragmentActivity, title: String?, message: String,
            positiveButton: String?, positiveClickListener: PopupDelegate?,
            gravity: Int = Gravity.START, themeResId: Int = 0, dismissible: Boolean = false,
            isHtml: Boolean = false,
            negativeButtonStyle: ButtonView.ButtonVariant = ButtonView.ButtonVariant.Outline
        ) =
            show(
                activity, title, message, positiveButton, null,
                positiveClickListener, null, themeResId =
                themeResId, gravity = gravity, dismissible = dismissible, isHtml = isHtml,
                negativeButtonStyle = negativeButtonStyle
            )

        fun show(
            activity: FragmentActivity, title: String?, message: String,
            positiveButton: String?, negativeButton: String?,
            positiveClickListener: PopupDelegate?, negativeClickListener: PopupDelegate?,
            orientation: Orientation = Orientation.Horizontal, gravity: Int = Gravity.START,
            themeResId: Int = 0, dismissible: Boolean = false, isHtml: Boolean = false,
            negativeButtonStyle: ButtonView.ButtonVariant = ButtonView.ButtonVariant.Outline
        ): Popup {

            val popup = Popup(activity, null, themeResId)

            popup.dismissible = dismissible

            popup.binding.tvTitle.isVisible = title?.isNotEmpty() == true
            popup.binding.tvTitle.text = if (isHtml && title != null) HtmlCompat.fromHtml(
                title,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ) else title
            popup.binding.tvTitle.gravity = gravity

            popup.binding.tvMessage.text = if (isHtml) HtmlCompat.fromHtml(
                message,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            ) else message
            popup.binding.tvMessage.gravity = gravity

            popup.binding.bvNegative.isVisible = negativeButton != null
            popup.binding.bvNegative.text = negativeButton
            popup.binding.bvNegative.variant = negativeButtonStyle


            popup.binding.bvNegative.setOnClickListener {
                if (negativeClickListener == null) {
                    popup.dismiss()
                } else {
                    negativeClickListener.onClick(popup, it)
                }
            }

            popup.binding.bvPositive.text = positiveButton
            popup.binding.bvPositive.setOnClickListener {
                if (positiveClickListener == null) {
                    popup.dismiss()
                } else {
                    positiveClickListener.onClick(popup, it)
                }
            }


            if (orientation == Orientation.Vertical) {
                popup.binding.llButton.orientation = LinearLayoutCompat.VERTICAL

                val lp1 = popup.binding.bvNegative.layoutParams as LinearLayoutCompat.LayoutParams?
                lp1?.width = LinearLayoutCompat.LayoutParams.MATCH_PARENT
                lp1?.marginEnd = 0

                val lp2 = popup.binding.bvPositive.layoutParams as LinearLayoutCompat.LayoutParams?
                lp2?.width = LinearLayoutCompat.LayoutParams.MATCH_PARENT
                lp2?.marginStart = 0
                lp2?.topMargin = activity.resources.getDimensionPixelSize(R.dimen.size_8dp)

                // tukar posisi negative dengan positive

                popup.binding.bvNegative.variant = ButtonView.ButtonVariant.Primary
                popup.binding.bvNegative.text = positiveButton
                popup.binding.bvNegative.setOnClickListener {
                    if (positiveClickListener == null) {
                        popup.dismiss()
                    } else {
                        positiveClickListener.onClick(popup, it)
                    }
                }

                popup.binding.bvPositive.variant = ButtonView.ButtonVariant.Outline
                popup.binding.bvPositive.text = negativeButton
                popup.binding.bvPositive.setOnClickListener {
                    if (negativeClickListener == null) {
                        popup.dismiss()
                    } else {
                        negativeClickListener.onClick(popup, it)
                    }
                }

            }

            popup.show()

            val width = Resources.getSystem().displayMetrics.widthPixels * 0.9f
            popup.window?.setLayout(
                width.toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            return popup
        }

        fun showFullScreen(view: View, themeResId: Int = 0, dismissible: Boolean = false) =
            show(
                view, width = WindowManager.LayoutParams.MATCH_PARENT.toFloat(),
                height = WindowManager.LayoutParams.MATCH_PARENT, themeResId = themeResId,
                dismissible = dismissible
            )

        fun show(
            view: View, width: Float = 0.9f,
            height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
            themeResId: Int = 0, dismissible: Boolean = false
        ): Popup {
            val popup = Popup(view.context, view, themeResId)
            popup.dismissible = dismissible
            popup.show()

            val w = when (width) {
                WindowManager.LayoutParams.WRAP_CONTENT.toFloat() -> WindowManager.LayoutParams.WRAP_CONTENT
                WindowManager.LayoutParams.MATCH_PARENT.toFloat() -> WindowManager.LayoutParams.MATCH_PARENT
                else -> (Resources.getSystem().displayMetrics.widthPixels * 0.9f).toInt()
            }

            popup.window?.setLayout(
                w,
                height
            )

            return popup
        }
    }

    val binding = DialogPopupBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(view ?: binding.root)
        window?.decorView?.setBackgroundResource(android.R.color.transparent)

        setCancelable(dismissible)
    }
}