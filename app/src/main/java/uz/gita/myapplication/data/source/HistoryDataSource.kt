package uz.gita.myapplication.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.gita.myapplication.data.api.CardApi
import uz.gita.myapplication.data.api.TransferApi
import uz.gita.myapplication.data.source.remote.response.HistoryItem

class HistoryDataSource(val cardApi: CardApi, private val transferApi: TransferApi) :
    PagingSource<Int, HistoryItem>() {

    data class User(
        val name: String,
        val id: Int
    )

    private val userList = ArrayList<User>()

    override fun getRefreshKey(state: PagingState<Int, HistoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryItem> {
        return try {
            val nextPageNumber = params.key ?: 0
            val pageSize = 10
            val response = transferApi.history(nextPageNumber, pageSize)

            response.body()?.data?.data?.let { history ->

                for (i in history) {
                    if (i.receiver != null) {
                        val notFound = userList.none {
                            it.id == i.receiver
                        }

                        if (notFound) {
                            val receiverResponse = cardApi.ownerById(i.receiver.toString())
                            receiverResponse.body()!!.data!!.let {
                                i.owner = it.fio
                                userList.add(User(it.fio, i.receiver))
                            }
                        } else {
                            i.owner = userList.first {
                                it.id == i.receiver
                            }.name
                        }

                    }

                    if (i.sender != null) {
                        val notFound = userList.none {
                            it.id == i.sender
                        }

                        if (notFound) {
                            val senderResponse = cardApi.ownerById(i.sender.toString())
                            senderResponse.body()!!.data!!.let {
                                i.owner = it.fio
                                userList.add(User(it.fio, i.sender))
                            }
                        } else {
                            i.owner = userList.first {
                                it.id == i.sender
                            }.name
                        }

                    }


                }
            }

            LoadResult.Page(
                data = response.body()!!.data!!.data,
                prevKey = null,
                nextKey = if (nextPageNumber < (response.body()!!.data!!.totalCount / response.body()!!.data!!.pageSize) + 1)
                    nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(Throwable("Error fetching history"))
        }
    }

}
