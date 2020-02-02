package com.alan.app.mvvm.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alan.app.mvvm.utils.injectVM
import com.alan.app.mvvm.utils.injectVMWithActivityScope
import com.alan.app.mvvm.base.core.AppDialog
import com.alan.app.mvvm.base.core.HasLoadingFragment
import com.alan.app.mvvm.data.model.LoadingStatus
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseMVVMFragment<V : BaseViewModel, VMF : ViewModelProvider.Factory> : HasLoadingFragment() {

    @Inject
    lateinit var viewModelFactory: VMF

    internal lateinit var viewModel: V

    private var mDialog: AppDialog? = null

    protected abstract fun getLayoutRes(): Int

    protected abstract fun getViewModelType(): Class<V>

    protected abstract fun setupViews(view: View)

    protected open fun extractData(bundle: Bundle) {}

    protected open fun onObserve(): (V.() -> Unit)? = null

    /** Allow to create ViewModel in activity scope.
     * Default is false */
    protected open fun viewModelInActivityScope(): Boolean = false

    protected fun getAppContext(): Context? = context?.applicationContext

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        if (::viewModelFactory.isInitialized) {
            viewModel = if (viewModelInActivityScope()) {
                injectVMWithActivityScope(viewModelFactory, getViewModelType())
            } else {
                injectVM(viewModelFactory, getViewModelType())
            }
            viewModel
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = savedInstanceState ?: arguments
        if (bundle != null) {
            extractData(bundle)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutRes(), container, false)
        initViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
    }

    private fun initViewModel() {
        if (::viewModel.isInitialized) {

            doObserve(viewModel.loadingLV) { status ->
                if (LoadingStatus.SHOW_LOADING == status) {
                    showLoading()
                } else if (LoadingStatus.HIDE_LOADING == status) {
                    hideLoading()
                }
            }

            doObserve(viewModel.errorLV) { alertData ->
                showDialog(alertData.title, alertData.message)
            }

            onObserve()?.invoke(viewModel)

        } else {
            throw Throwable("ViewModel is not initialized.")
        }
    }

    protected fun <T> doObserve(liveData: LiveData<T>, result: ((data: T) -> Unit)? = null) {
        liveData.observe(viewLifecycleOwner, Observer { result?.invoke(it) })
    }

    protected fun showDialog(title: String?, message: String?, cancelable: Boolean? = null,
                             onPositive: (() -> Unit)? = null, onNegative: (() -> Unit)? = null) {
        if (mDialog?.dialog?.isShowing == true) mDialog?.dismiss()

        val fm = activity?.supportFragmentManager ?: return

        mDialog = AppDialog.newInstance(
                title, message, cancelable = cancelable,
                onPositive = onPositive, onNegative = onNegative
        )
        mDialog?.show(fm, AppDialog::class.java.simpleName)
    }
}