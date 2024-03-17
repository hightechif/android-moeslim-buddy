package com.oppo.moeslimbuddy.data

sealed class Result<out T>(val data: T?, val code: String?, val message: String?) {

    open var status: Status = Status.LOADING

    sealed class Status {
        data object SUCCESS : Status()
        data object ERROR : Status()
        data object LOADING : Status()
        data object UNAUTHORIZED : Status()
    }

    class Success<T>(data: T?) : Result<T>(data, null, null) {
        override var status: Status = Status.SUCCESS
    }

    class Error<T>(code: String?, message: String?, data: T? = null) :
        Result<T>(data, code, message) {
        override var status: Status = Status.ERROR
    }

    class Loading<T>(data: T? = null) : Result<T>(data, null, null) {
        override var status: Status = Status.LOADING
    }

    class Unauthorized<T>(message: String?) : Result<T>(null, null, message) {
        override var status: Status = Status.UNAUTHORIZED
    }
}
