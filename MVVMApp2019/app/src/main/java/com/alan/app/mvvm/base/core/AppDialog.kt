package com.alan.app.mvvm.base.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.alan.app.mvvm.R
import kotlinx.android.synthetic.main.fragment_dialog.*

class AppDialog
private constructor(var title: String? = null,
                    var message: String? = null,
                    var positiveText: String? = null,
                    var negativeText: String? = null,
                    var onPositive: (() -> Unit)? = null,
                    var onNegative: (() -> Unit)? = null,
                    var cancelable: Boolean? = null) : DialogFragment() {

    private constructor() : this(null)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_dialog, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        if (title == null) {
            tvTitle.visibility = View.GONE
        } else {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = title
        }
        if (tvMessage == null) {
            tvMessage.visibility = View.GONE
        } else {
            tvMessage.visibility = View.VISIBLE
            tvMessage.text = message
        }
        if (positiveText != null) {
            btnPositive.text = positiveText
        }
        if (negativeText != null) {
            btnNegative.text = negativeText
        }
        if (onNegative == null) {
            btnNegative.visibility = View.GONE
        }
        btnPositive.setOnClickListener {
            onPositive?.invoke()
            dismiss()
        }
        btnNegative.setOnClickListener {
            onNegative?.invoke()
            dismiss()
        }
        if (cancelable == null) {
            dialog?.setCancelable(true)
        } else {
            dialog?.setCancelable(cancelable!!)
        }
    }

    companion object {
        fun newInstance(title: String?,
                        message: String?,
                        positiveText: String? = null,
                        negativeText: String? = null,
                        cancelable: Boolean? = null,
                        onPositive: (() -> Unit)? = null,
                        onNegative: (() -> Unit)? = null
        ) = AppDialog(title, message, positiveText, negativeText, onPositive, onNegative, cancelable)
    }
}