package com.ydn.fileview

import android.app.ProgressDialog
import android.arch.paging.PagedList
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class FileView : RecyclerView {
    private var mFilename: String? = null
    private var mInitialPos = 0
    private var mPageSize = 10
    private var mProgressDialog: ProgressDialog? = null
    private val mAdapter = ItemAdapter()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    fun setFilename(filename: String): FileView {
        this.mFilename = filename
        return this
    }

    fun getFilename(): String? {
        return mFilename
    }

    fun setPosition(pos: Int): FileView {
        mInitialPos = pos
        return this
    }

    fun setLayoutRes(layoutRes: Int): FileView {
        mAdapter.setLayoutRes(layoutRes)
        return this
    }

    fun setProgressDialogRes(progressDialogRes: Int): FileView {
        mAdapter.setProgressDlgRes(progressDialogRes)
        return this
    }

    fun setPageSize(pageSize: Int): FileView {
        mPageSize = pageSize
        return this
    }

    fun getPageSize(): Int {
        return mPageSize
    }

    fun init(scrollBottom: Boolean) {
        clear()
        itemAnimator = null

        val loadData = LoadData(context, getFilename()!!, scrollBottom)
        mProgressDialog = ProgressDialog(context)
        loadData.execute()
    }

    fun scrollBottom() {
        init(true)
    }

    fun scrollTop() {
        setPosition(0)
        init(false)
    }

    fun clear() {
        recycledViewPool.clear()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mProgressDialog?.dismiss()
     }

    private inner class LoadData internal constructor(internal var context: Context?, internal var filename: String, internal var scrollBottom: Boolean) : AsyncTask<Void, Void, Void?>() {
        var config: PagedList.Config? = null
        var pagedList: PagedList<Item>? = null
        var fileStorage: FileStorage? = null
        var dataSource: FileDataSource? = null
        var position = 0

        override fun onPreExecute() {
            position = 0

            fileStorage = FileStorage(filename)
            dataSource = FileDataSource(fileStorage!!)

            config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPageSize(mPageSize)
                    .build()

            mProgressDialog?.setMessage("Please wait...")
            mProgressDialog?.show()
            super.onPreExecute()
        }

        override fun doInBackground(vararg voids: Void): Void? {
            if (scrollBottom) {
                val count = fileStorage!!.numberOfLines - 2
                position = if (count < 0) 0 else count
            }

            dataSource?.initialPos = position
            pagedList = PagedList.Builder(dataSource!!, config!!)
                    .setNotifyExecutor(MainThreadExecutor())
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(object : PagedList.BoundaryCallback<Item>() {
                        override fun onItemAtEndLoaded(itemAtEnd: Item) {
                            super.onItemAtEndLoaded(itemAtEnd)
                        }
                    })
                    .build()
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)

            mAdapter.submitList(pagedList)
            mAdapter.dataSource = dataSource!!

            adapter = mAdapter
            layoutManager = CustomLinearLayoutManager(context!!)
            invalidate()

            mProgressDialog?.dismiss()
            context = null
        }
    }

    private class MainThreadExecutor : Executor {
        private val mHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mHandler.post(command)
        }
    }

    private class CustomLinearLayoutManager : LinearLayoutManager {
        constructor(context: Context) : super(context) {}

        constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {}

        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }
}
