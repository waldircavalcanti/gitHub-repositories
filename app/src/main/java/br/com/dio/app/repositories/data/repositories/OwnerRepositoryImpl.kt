package br.com.dio.app.repositories.data.repositories

import br.com.dio.app.repositories.core.RemoteException
import br.com.dio.app.repositories.data.services.GitHubService
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class OwnerRepositoryImpl(private val service: GitHubService): OwnerRepository {
    override suspend fun getUser(user: String) = flow {
        try {
            val repoList = service.getUser(user)
            emit(repoList)
        }catch (err: HttpException){
            throw(RemoteException(err.message ?: "Não é possível realizar a busca no momento." ))
        }
    }
}