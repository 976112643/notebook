package com.wq.notebook.home

import android.Manifest
import android.app.Fragment
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Gravity
import android.view.View
import com.wq.common.base.BaseActivity
import com.wq.common.service.NetTaskService
import com.wq.common.util.*
import com.wq.config.R
import com.wq.notebook.home.adapter.NoticeListAdapter
import com.wq.notebook.home.adapter.SimpleItemTouchHelper
import com.wq.notebook.home.fragment.NoteListFragment
import com.wq.notebook.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_notice_list.*
import kotlinx.android.synthetic.main.view_titlebar_simple.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 笔记列表
 */
class NoteListActivity : BaseActivity() {
    val permissHelper = PermissionHelper()
    val fragmentHelper=FragmentHelper()
    var adapter = NoticeListAdapter()
    override fun onViewCreated(savedInstanceState: Bundle?) {
        fragmentHelper.attatch(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
//        adapter.emptyView = layoutInflater.inflate(R.layout.lay_empty_view, null)
        var dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(_drawable(R.drawable.shape_list_divier))
        recyclerView.addItemDecoration(dividerItemDecoration)
        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelper(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.loadPageData("&c#d")
        initListener()
        //拿下设备码的权限
        permissHelper.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), object : PermissionHelper.PermissionsResult {
            override fun onDenied() {
                toast("需要相关权限才能正常工作")
            }
            override fun onGrant() {
                NetTaskService.startNetTask(this@NoteListActivity)//
            }

        })

    }

    /**
     * 初始化监听
     */
    private fun initListener() {
        adapter.setOnItemClickListener { _, _, position ->
            adapter.getItem(position)?.apply {
                startActivity<AddRichNoteActivity>("note_id" to note_id)
            }
        }
        titleBar.apply {
            setLeftAction {
                drawerLayout.openDrawer(Gravity.START)
            }
            setRightAction {
                rightIn {
                    laySearch.startAnimation(this)
                    laySearch.visibility = View.VISIBLE
                    recyclerView.visibility=View.VISIBLE
                    editSearch.openKeyboard()
                }
            }
        }
        imgClose.setOnClickListener {
            rightOut {
                listener {
                    laySearch.visibility = View.GONE
                }
                recyclerView.visibility=View.GONE
                laySearch.startAnimation(this)
                editSearch.closeKeyboard()
            }
        }
        editSearch.wathch {
            adapter.loadPageData(editSearch.text.toString())
        }
        navigation.setNavigationItemSelectedListener {
            "============setNavigationItemSelectedListener=============="._Log()
//            it.isChecked=true
            when(it.itemId){
                R.id.nav_notes->
                    fragmentHelper.displayFragment(R.id.fragmentContent,NoteListFragment::class.java)
                R.id.nav_recycle->
                    startActivity<RecycleBinListActivity>()
                R.id.nav_setting->
                        startActivity<SettingsActivity>()
            }
            drawerLayout.closeDrawer(Gravity.START)
            false
        }
//        navigation.setCheckedItem(R.id.nav_notes)
        fragmentHelper.displayFragment(R.id.fragmentContent,NoteListFragment::class.java)
        var drawerArrowDrawable = DrawerArrowDrawable(this)
        drawerArrowDrawable.color=_color(R.color.white)
        imgLeft.setImageDrawable(drawerArrowDrawable)
        drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
                drawerArrowDrawable.progress=slideOffset
                imgLeft.setImageDrawable(drawerArrowDrawable)
            }

            override fun onDrawerClosed(drawerView: View?) {
            }

            override fun onDrawerOpened(drawerView: View?) {
            }
        })
    }


    override fun getLayoutId(): Int = R.layout.activity_notice_list


    override fun onBackPressed() {
        if (laySearch.isShown) {
            imgClose.performClick()
            return
        }
        super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}
