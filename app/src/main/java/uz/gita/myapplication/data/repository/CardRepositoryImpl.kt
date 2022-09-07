package uz.gita.myapplication.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.myapplication.data.api.CardApi
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.*
import uz.gita.myapplication.data.source.remote.response.CardResponse
import uz.gita.myapplication.data.source.remote.response.MessageResponse
import uz.gita.myapplication.data.source.remote.response.OwnerResponse
import uz.gita.myapplication.domain.repository.CardRepository
import uz.gita.myapplication.util.CardList
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardApi: CardApi
) : CardRepository {

    override fun totalSum(): Flow<MainResult<String>> = flow {

        emit(MainResult.Loading(true))
        try {
            val response = cardApi.totalSum()
            if (response.isSuccessful) {
                emit(MainResult.Success(response.body()!!.data!!))
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
            }
            emit(MainResult.Loading(false))

        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }

    }

    override fun addCard(cardRequest: CardRequest): Flow<MainResult<Unit>> = flow {

        emit(MainResult.Loading(true))
        try {
            val response = cardApi.addCard(cardRequest)
            if (response.isSuccessful) {
                emit(MainResult.Success(Unit))
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
            }
            emit(MainResult.Loading(false))

        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }
    }

    override fun verify(verifyCardRequest: VerifyCardRequest): Flow<MainResult<CardResponse>> =
        flow {
            emit(MainResult.Loading(true))
            try {
                val response = cardApi.verify(verifyCardRequest)
                if (response.isSuccessful) {
                    emit(MainResult.Success(response.body()!!.data!!))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        MessageResponse::class.java
                    )
                    emit(MainResult.Message(errorResponse.message!!))
                }
                emit(MainResult.Loading(false))

            } catch (e: Throwable) {
                emit(MainResult.Message(e.message.toString()))
                emit(MainResult.Loading(false))
            }
        }

    override fun editCard(editCardRequest: EditCardRequest): Flow<MainResult<String>> = flow {
        emit(MainResult.Loading(true))
        try {
            val response = cardApi.editCard(editCardRequest)
            if (response.isSuccessful) {
                emit(MainResult.Success("Card has been edited"))
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
            }
            emit(MainResult.Loading(false))

        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }
    }

    override fun deleteCard(deleteCardRequest: DeleteCardRequest): Flow<MainResult<String>> = flow {
        emit(MainResult.Loading(true))
        try {
            val response = cardApi.deleteCard(deleteCardRequest)
            if (response.isSuccessful) {
                emit(MainResult.Success("Card has been deleted"))
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
            }
            emit(MainResult.Loading(false))

        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }
    }

    override fun allCards(): Flow<MainResult<CardList>> = flow {
        emit(MainResult.Loading(true))
        try {
            val response = cardApi.allCard()
            if (response.isSuccessful) {
                emit(MainResult.Success(response.body()!!.data!!))
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
            }
            emit(MainResult.Loading(false))

        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }
    }

    override fun color(colorRequest: ColorRequest): Flow<MainResult<ColorRequest>> = flow {
        emit(MainResult.Loading(true))
        try {
            val response = cardApi.color(colorRequest)
            if (response.isSuccessful) {
                emit(MainResult.Success(response.body()!!.data!!))
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
            }
            emit(MainResult.Loading(false))

        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }
    }

    override fun ignoreBalance(ignoreBalanceRequest: IgnoreBalanceRequest): Flow<MainResult<IgnoreBalanceRequest>> =
        flow {
            emit(MainResult.Loading(true))
            try {
                val response = cardApi.ignoreBalance(ignoreBalanceRequest)
                if (response.isSuccessful) {
                    emit(MainResult.Success(response.body()!!.data!!))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        MessageResponse::class.java
                    )
                    emit(MainResult.Message(errorResponse.message!!))
                }
                emit(MainResult.Loading(false))

            } catch (e: Throwable) {
                emit(MainResult.Message(e.message.toString()))
                emit(MainResult.Loading(false))
            }
        }

    override fun ownerByPan(pan: String): Flow<MainResult<OwnerResponse>> = flow {
        try {

            emit(MainResult.Loading(true))

            val response = cardApi.ownerByPan(pan)

            if (response.isSuccessful) {
                emit(MainResult.Success(response.body()!!.data!!))
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))

            }

        } catch (e: Throwable) {
            emit(MainResult.Loading(false))
            emit(MainResult.Message(e.message.toString()))
        }
    }

    override fun ownerById(cardId: String): Flow<MainResult<OwnerResponse>> = flow {
        try {

            emit(MainResult.Loading(true))

            val response = cardApi.ownerById(cardId)

            if (response.isSuccessful) {
                emit(MainResult.Success(response.body()!!.data!!))
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
            }

        } catch (e: Throwable) {
            emit(MainResult.Loading(false))
            emit(MainResult.Message(e.message.toString()))
        }
    }
}