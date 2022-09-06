package com.george.copticorphanstask.base.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar

abstract class MainBaseFragment<T : ViewDataBinding?> : Fragment() {

    private var contentId = 0
    var bundle: Bundle? = null
    var a: Activity? = null
    var binding: T? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (contentId == 0) {
            bundle = arguments
            contentId = ActivityFragmentAnnoationManager.check(this)
            a = activity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, contentId, container, false)
        }
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.initialization()
        binding!!.setListener()
//        binding!!.setListener()
        //to handle when clicked in icon comment

    }

    open abstract fun T.initialization() // TODO : add viewModel declaration
    open abstract fun T.setListener() // TODO : Logic here

    //Y/////////////////////////////////////////////////////////////////////////////////////////////
    //Y///////////////////////////////////////// {SNACK_BAR} ///////////////////////////////////////
    //Y/////////////////////////////////////////////////////////////////////////////////////////////
    fun showSnackBar(view: View, message: String) =
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

    fun showSnackBar(context: Context, view: View, message: String) =
        Snackbar.make(context, view, message, Snackbar.LENGTH_LONG).show()

    //Y/////////////////////////////////////////////////////////////////////////////////////////////
    //Y//////////////////////////////////////// {NAVIGATION} ///////////////////////////////////////
    //Y/////////////////////////////////////////////////////////////////////////////////////////////
    inline fun <reified A : Activity> startActivity(
        replace: Boolean? = true, millis: Long? = null
    ) {
        millis?.let { Thread.sleep(millis) }
        startActivity(Intent(requireActivity(), A::class.java))
        replace?.let { requireActivity().finish() }

    }

    //Y/////////////////////////////////////////////////////////////////////////////////////////////
    //Y/////////////////////////////////////// {MaterialToolbar} ///////////////////////////////////
    //Y/////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * this function do the bellow actions
     * * when press navigation button {back button / left arrow icon} => __popTpBackStack()__
     * * when click on menu icon that will show always => __go to setting fragment__
     */
    fun MaterialToolbar.setupMaterialToolbar(hasMenu: Boolean = false, menuLogic: ((MenuItem) -> Boolean)? = null) {
        setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        if (hasMenu) {
            setOnMenuItemClickListener {
                menuLogic?.let { it1 -> it1(it) } ?: false
            }
        }
    }

}