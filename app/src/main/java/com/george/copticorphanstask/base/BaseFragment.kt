package com.george.copticorphanstask.base

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    inline fun <reified A:Activity> startActivity() {
        startActivity(Intent(requireActivity(),A::class.java))
    }

}