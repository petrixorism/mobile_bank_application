package uz.gita.myapplication.data.model

sealed class MainResult<T> {
    class Success<T>(val data: T) : MainResult<T>()
    class Message<T>(val message: String) : MainResult<T>()
    class Loading<T>(val isLoading: Boolean) : MainResult<T>()
}
