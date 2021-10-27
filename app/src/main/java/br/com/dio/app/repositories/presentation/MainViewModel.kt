package br.com.dio.app.repositories.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.dio.app.repositories.data.model.Owner
import br.com.dio.app.repositories.data.model.Repo
import br.com.dio.app.repositories.domain.ListUserRepositoriesUseCase
import br.com.dio.app.repositories.domain.di.GetUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(
    private val listUserRepositoriesUseCase: ListUserRepositoriesUseCase,
    private val ownerUseCase: GetUserUseCase
) : ViewModel() {

    private val _repos = MutableLiveData<State>()
    val repos: LiveData<State> = _repos

    private val _owner = MutableLiveData<OwnerState>()
    val owner: LiveData<OwnerState>
        get() = _owner

    fun getRepoList(user: String) {
        viewModelScope.launch {
            listUserRepositoriesUseCase(user)
                .onStart {
                    _repos.postValue(State.Loading)
                }
                .catch {
                    _repos.postValue(State.Error(it))
                    Log.e("TAG","Erro Usuario")
                }
                .collect {
                    _repos.postValue(State.Success(it))
                }
            ownerUseCase(user)
                .onStart { _owner.postValue(OwnerState.Loading) }
                .catch { _owner.postValue(OwnerState.Error(it)) }
                .collect { _owner.postValue(OwnerState.Success(it)) }
        }
    }


    //Essa classe selada representa os três estados possíveis do Flow: carregando, erro e sucesso
    sealed class State {
        object Loading : State()
        data class Success(val list: List<Repo>) : State()
        data class Error(val error: Throwable) : State()
    }

    sealed class OwnerState {
        object Loading: OwnerState()
        data class Success(val owner: Owner) : OwnerState()
        data class Error(val error: Throwable) : OwnerState()
    }

}