package com.alan.app.mvvm.ui.second

import android.content.Intent
import android.view.View
import com.alan.app.mvvm.R
import com.alan.app.mvvm.base.BaseMVVMFragment
import com.alan.app.mvvm.base.HasFragmentResult
import com.alan.app.mvvm.di.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : BaseMVVMFragment<SecondVM, ViewModelFactory>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_second
    }

    override fun getViewModelType(): Class<SecondVM> {
        return SecondVM::class.java
    }

    override fun viewModelInActivityScope(): Boolean = true

    override fun setupViews(view: View) {
        btn_back.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(KEY_EXTRA_DATA, edt_result.text.toString())
            }
            setFragmentResult(HasFragmentResult.ACTION_OK, resultIntent)
            sendToNav { it.goBack() }
        }
    }

    companion object {
        const val KEY_EXTRA_DATA = "extra_data"

        fun newInstance() = SecondFragment()
    }
}