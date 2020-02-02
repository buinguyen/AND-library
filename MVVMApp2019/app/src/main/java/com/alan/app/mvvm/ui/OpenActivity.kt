package com.alan.app.mvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alan.app.mvvm.base.HasFragmentResult
import com.alan.app.mvvm.base.navigation.*

class OpenActivity : AppCompatActivity(), NavControllerProvider, HasNavTarget {

    private var targetRequestCode: Int? = null

    private val mNavController: NavController by lazy {
        NavControllerImpl(android.R.id.content, supportFragmentManager, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        val fragmentClazzName: String? = bundle?.getString(EXTRA_FRAGMENT, "")
        if (fragmentClazzName.isNullOrEmpty()) {
            finish()
            return
        }
        targetRequestCode = bundle.getInt(EXTRA_REQUEST_CODE)
        try {
            val fragmentClazz = Class.forName(fragmentClazzName) as Class<Fragment>
            val fragment = fragmentClazz.newInstance().apply { arguments = bundle }
            mNavController.openFragment(fragment, false, FragmentAnimation.TRANSITION_NONE)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        mNavController.goBack()
    }

    override fun onDestroy() {
        super.onDestroy()
        mNavController.clearController()
    }

    override fun provideNavController(): NavController {
        return mNavController
    }

    override fun getTargetCode(): Int? = targetRequestCode

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = mNavController.activeFragment
        if (fragment != null && fragment is HasFragmentResult) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {

        private const val EXTRA_FRAGMENT = "EXTRA_FRAGMENT"
        private const val EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"

        @JvmOverloads
        fun getIntent(context: Context?, fragmentClass: Class<out Fragment>,
                      args: Bundle? = null, requestCode: Int? = null): Intent? {
            context ?: return null

            val intent = Intent(context, OpenActivity::class.java)
            intent.putExtra(EXTRA_FRAGMENT, fragmentClass.name)
            intent.putExtra(EXTRA_REQUEST_CODE, requestCode)
            if (args != null) intent.putExtras(args)
            return intent
        }

    }
}