package uz.gita.myapplication.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.*
import uz.gita.myapplication.data.source.remote.response.CardResponse
import uz.gita.myapplication.data.source.remote.response.OwnerResponse
import uz.gita.myapplication.util.CardList

interface CardRepository {

    fun totalSum(): Flow<MainResult<String>>

    fun addCard(cardRequest: CardRequest): Flow<MainResult<Unit>>

    fun verify(verifyCardRequest: VerifyCardRequest): Flow<MainResult<CardResponse>>

    fun editCard(editCardRequest: EditCardRequest): Flow<MainResult<String>>

    fun deleteCard(deleteCardRequest: DeleteCardRequest): Flow<MainResult<String>>

    fun allCards(): Flow<MainResult<CardList>>

    fun color(colorRequest: ColorRequest): Flow<MainResult<ColorRequest>>

    fun ignoreBalance(ignoreBalanceRequest: IgnoreBalanceRequest): Flow<MainResult<IgnoreBalanceRequest>>

    fun ownerByPan(pan: String): Flow<MainResult<OwnerResponse>>

    fun ownerById(cardId: String): Flow<MainResult<OwnerResponse>>

}