package br.com.dio.app.repositories.data.repositories

import br.com.dio.app.repositories.data.model.Owner
import kotlinx.coroutines.flow.Flow

interface OwnerRepository {
    suspend fun getUser(user: String): Flow<Owner>
}