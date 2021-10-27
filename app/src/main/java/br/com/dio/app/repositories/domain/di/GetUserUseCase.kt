package br.com.dio.app.repositories.domain.di

import br.com.dio.app.repositories.core.UseCase
import br.com.dio.app.repositories.data.model.Owner
import br.com.dio.app.repositories.data.repositories.OwnerRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(private val repository: OwnerRepository) : UseCase<String, Owner>() {
    override suspend fun execute(param: String): Flow<Owner> {
        return repository.getUser(param)
    }
}