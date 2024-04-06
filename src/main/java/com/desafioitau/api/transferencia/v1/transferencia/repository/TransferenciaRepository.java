package com.desafioitau.api.transferencia.v1.transferencia.repository;

import com.desafioitau.api.transferencia.v1.transferencia.model.Transferencia;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransferenciaRepository extends MongoRepository<Transferencia, String> {
}
