package com.ydn.fileview


import android.arch.paging.PositionalDataSource

class FileDataSource internal constructor(internal var fileStorage: FileStorage) : PositionalDataSource<Item>() {
    internal var initialPos = 0

    override fun loadInitial(params: PositionalDataSource.LoadInitialParams, callback: PositionalDataSource.LoadInitialCallback<Item>) {
        val result = fileStorage.getData(initialPos, params.requestedLoadSize)
        callback.onResult(result, initialPos)
    }

    override fun loadRange(params: PositionalDataSource.LoadRangeParams, callback: PositionalDataSource.LoadRangeCallback<Item>) {
        val result = fileStorage.getData(params.startPosition, params.loadSize)
        callback.onResult(result)
    }
}
