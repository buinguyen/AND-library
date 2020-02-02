package com.alan.app.mvvm.ui.first

import android.content.Intent
import android.view.View
import com.alan.app.mvvm.R
import com.alan.app.mvvm.base.BaseMVVMFragment
import com.alan.app.mvvm.base.HasFragmentResult
import com.alan.app.mvvm.di.ViewModelFactory
import com.alan.app.mvvm.ui.second.SecondFragment
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : BaseMVVMFragment<FirstVM, ViewModelFactory>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_first
    }

    override fun getViewModelType(): Class<FirstVM> {
        return FirstVM::class.java
    }

    override fun viewModelInActivityScope(): Boolean = true

    override fun setupViews(view: View) {
        btn_go.setOnClickListener {
            sendToNav { it.openFragment(SecondFragment.newInstance()) }
        }
        btn_go_with_result.setOnClickListener {
            sendToNav {
                it.openFragment(SecondFragment.newInstance(), forResult = Pair(this, CODE_SECOND_RESULT))
            }
        }
    }

    override fun onObserve(): (FirstVM.() -> Unit) = {
        doObserve(resultLiveData) { tv_result.text = "Result: $it" }
    }

    override fun onFragmentResult(requestCode: Int, action: Int, extraData: Intent?) {
        if (action == HasFragmentResult.ACTION_OK && requestCode == CODE_SECOND_RESULT) {
            val result = extraData?.getStringExtra(SecondFragment.KEY_EXTRA_DATA) ?: ""
            viewModel.setResult(result)
        }
    }

    companion object {
        private const val CODE_SECOND_RESULT = 1

        fun newInstance() = FirstFragment()
    }
}