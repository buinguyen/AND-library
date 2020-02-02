package com.alan.app.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alan.app.mvvm.base.navigation.FragmentAnimation
import com.alan.app.mvvm.base.navigation.NavController
import com.alan.app.mvvm.base.navigation.NavController.Companion.KEY_ANIM_ENTER_RES_ID
import com.alan.app.mvvm.base.navigation.NavController.Companion.KEY_ANIM_EXIT_RES_ID
import com.alan.app.mvvm.base.navigation.NavControllerImpl
import com.alan.app.mvvm.base.navigation.NavControllerProvider

class OpenActivity : AppCompatActivity(), NavControllerProvider {

    private var animEnterResId: Int = 0
    private var animExitResId: Int = 0

    private val mNavController: NavController by lazy {
        NavControllerImpl(R.id.content, supportFragmentManager, this)
    }

    private fun extractData(intent: Intent?) {
        intent ?: return
        animEnterResId = intent.getIntExtra(KEY_ANIM_ENTER_RES_ID, 0)
        animExitResId = intent.getIntExtra(KEY_ANIM_EXIT_RES_ID, 0)
    }

    override fun finish() {
        super.finish()
        if (animEnterResId != 0 && animExitResId != 0)
            overridePendingTransition(animEnterResId, animExitResId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        extractData(intent)

        val bundle = intent.extras
        val fragmentClazzName = bundle?.getString(KEY_FRAGMENT, "")
        if (fragmentClazzName.isNullOrEmpty()) {
            finish()
            return
        }
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

    companion object {

        private const val KEY_FRAGMENT = "fragment"

        @JvmOverloads
        fun getIntent(
            context: Context?,
            fragmentClass: Class<out Fragment>,
            args: Bundle? = null
        ): Intent? {
            context ?: return null

            val intent = Intent(context, OpenActivity::class.java)
            intent.putExtra(KEY_FRAGMENT, fragmentClass.name)
            if (args != null) intent.putExtras(args)
            return intent
        }
    }
}
