package com.wq.common.util

import android.app.Activity
import android.app.Fragment
import android.app.FragmentManager

/**
 * Created by weiquan on 2018/5/15.
 */
class FragmentHelper {
  lateinit  var fragmentManager:FragmentManager
    fun attatch(activity:Activity){
        fragmentManager=activity.fragmentManager
    }

    var currentFragment: Fragment?=null
     fun  <T : Fragment>  displayFragment(resId:Int, clazz: Class< T >, useCache:Boolean=true)   {
        var fragmentTag = clazz.name

        var fragment = fragmentManager.findFragmentByTag(fragmentTag)
        var transaction = fragmentManager.beginTransaction()
//        fragmentManager.
//        fragmentManager.fragments.forEach { transaction.hide(it) }
        if(currentFragment!=null){
            transaction.hide(currentFragment)
        }
        if(fragment==null ||(!useCache)){
            if(!useCache&&fragment!=null){
                transaction.remove(fragment)
            }
            fragment=clazz.newInstance()
            transaction.add(resId,fragment,fragmentTag)
        }
        currentFragment=fragment
        transaction.show(fragment).commitAllowingStateLoss()
    }
}