package com.joao.otavio.utils.mapper

interface Mapper<Entity, Domain> {
    fun Entity.toDomain(): Domain
    fun Domain.toEntity(): Entity
}
