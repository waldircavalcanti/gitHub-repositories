package br.com.dio.app.repositories.data.repositories

import br.com.dio.app.repositories.data.services.GitHubService
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class RepoRepositoryImpl(private val service: GitHubService) : RepoRepository {

    override suspend fun listRepositories(user: String) = flow {
        try {
            val repoList = service.listRepositories(user, TAG)
            emit(repoList)
        } catch (ex: HttpException) {
            //throw RemoteException(ex.message ?: "Não foi possivel fazer a busca no momento!")
            throw android.os.RemoteException("O usuário $user não foi encontrado!")
        }
    }

    companion object {
        private const val TAG = "pushed_at"
    }
}