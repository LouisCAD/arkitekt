package com.thefuntasty.mvvm.rxusecases.test

import com.thefuntasty.mvvm.rxusecases.disposables.SingleDisposablesOwner
import com.thefuntasty.mvvm.rxusecases.usecases.SingleUseCase
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.reactivex.Single
import io.reactivex.disposables.Disposable

/**
 * Mock [SingleDisposablesOwner.execute] method.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Single.just` will be passed then `onSuccess` will be called etc.
 *
 * Usage:
 * mockSingleUseCase.mockExecute(args = ...) { Single.just(...) }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : SingleUseCase<ARGS, RETURN_VALUE>> USE_CASE.everyExecute(args: ARGS, resultBlock: () -> Single<RETURN_VALUE>) {
    mockCurrentDisposable()
    every { this@everyExecute.create(args) } returns resultBlock()
}

/**
 * Mock [SingleDisposablesOwner.execute] method with `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Single.just` will be passed then `onSuccess` will be called etc.
 *
 * Usage:
 * mockSingleUseCase.mockExecute { Single.just(...) }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : SingleUseCase<ARGS, RETURN_VALUE>> USE_CASE.everyExecute(resultBlock: () -> Single<RETURN_VALUE>) {
    mockCurrentDisposable()
    every { this@everyExecute.create(any()) } returns resultBlock()
}

/**
 * Mock [SingleDisposablesOwner.execute] method for use cases with nullable input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Single.just` will be passed then `onSuccess` will be called etc.
 *
 * Usage:
 * mockSingleUseCase.mockExecute { Single.just(...) }
 */
inline fun <reified ARGS : Any?, RETURN_VALUE, USE_CASE : SingleUseCase<ARGS?, RETURN_VALUE>> USE_CASE.everyExecuteNullable(args: ARGS, resultBlock: () -> Single<RETURN_VALUE>) {
    mockCurrentDisposable()
    every { this@everyExecuteNullable.create(args) } returns resultBlock()
}

/**
 * Mock [SingleDisposablesOwner.execute] method for use cases with nullable input argument
 * and `any()` matcher argument used as input argument.
 *
 * When the execute method will be called then the argument passed in [resultBlock] will be used as a result of mocked use case
 * and corresponding methods for the given use case will be called.
 * So when `Single.just` will be passed then `onSuccess` will be called etc.
 *
 * Usage:
 * mockSingleUseCase.mockExecute { Single.just(...) }
 */
inline fun <reified ARGS : Any, RETURN_VALUE, USE_CASE : SingleUseCase<ARGS?, RETURN_VALUE>> USE_CASE.everyExecuteNullable(resultBlock: () -> Single<RETURN_VALUE>) {
    mockCurrentDisposable()
    every { this@everyExecuteNullable.create(any()) } returns resultBlock()
}

@PublishedApi
internal fun <RETURN_VALUE, USE_CASE : SingleUseCase<*, RETURN_VALUE>> USE_CASE.mockCurrentDisposable() {
    every { this@mockCurrentDisposable getProperty "currentDisposable" } returns null
    every { this@mockCurrentDisposable setProperty "currentDisposable" value any<Disposable>() } just runs
}
