package com.thefuntasty.mvvm.crinteractors

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * This interface gives your class ability to execute [BaseCoroutineInteractor] and [BaseFlowInteractor] interactors
 * You may find handy to implement this interface in custom Presenters, ViewHolders etc.
 * It is your responsibility to cancel [coroutineScope] when when all running tasks should be stopped.
 */
interface CoroutineScopeOwner {

    /**
     * [CoroutineScope] scope used to execute coroutine based interactors. It is your responsibility to cancel it when all running
     * tasks should be stopped
     */
    val coroutineScope: CoroutineScope

    /**
     * Provides Dispatcher for background tasks. This may be overridden for testing purposes
     */
    fun getWorkerDispatcher() = Dispatchers.IO

    /**
     * Asynchronously executes interactor, all previous pending executions are canceled and error is not handled
     */
    fun <T : Any?> BaseCoroutineInteractor<T>.execute(onSuccess: (T) -> Unit) = execute(
        onSuccess,
        null
    )

    /**
     * Asynchronously executes interactor, all previous pending executions are canceled
     */
    fun <T : Any?> BaseCoroutineInteractor<T>.execute(
        onSuccess: (T) -> Unit,
        onError: ((Throwable) -> Unit)?
    ) {

        deferred?.cancel()
        deferred = coroutineScope.async(getWorkerDispatcher(), CoroutineStart.LAZY) {
            build()
        }.also {
            coroutineScope.launch(Dispatchers.Main) {
                try {
                    onSuccess(it.await())
                } catch (cancellation: CancellationException) {
                    // do nothing this is normal way of suspend function interruption
                } catch (error: Throwable) {
                    onError?.invoke(error) ?: throw error
                }
            }
        }
    }

    /**
     * Asynchronously executes interactor and consumes data from flow on UI thread,
     * all previous pending executions are canceled.
     * When suspend function in interactor ends onComplete is called on UI thread
     **/
    fun <T : Any?> BaseFlowInteractor<T>.execute(
        onNext: (T) -> Unit = {},
        onError: ((Throwable) -> Unit)? = null,
        onComplete: () -> Unit = {}
    ) {
        job?.cancel()
        job = build()
            .flowOn(getWorkerDispatcher())
            .onEach { onNext(it) }
            .onCompletion { error ->
                when {
                    error is CancellationException -> {
                        // ignore this exception
                    }
                    error != null -> {
                        onError?.invoke(error) ?: throw error
                    }
                    else -> onComplete()
                }
            }
            .catch { /* handled in onCompletion */ }
            .launchIn(coroutineScope)
    }
}
