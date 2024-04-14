package com.desafioitau.api.transferencia.v1.transferencia.repository;

import com.desafioitau.api.transferencia.v1.transferencia.model.StatusTransferenciaEnum;
import com.desafioitau.api.transferencia.v1.transferencia.model.Transferencia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

public interface TransferenciaRepository extends MongoRepository<Transferencia, String> {

    @Query("{ _id: ObjectId(?0) }")
    @Update("{ $set: {'statusTransferenciaEnum': ?1} }")
    void findByIdAndUpdateStatusTransferencia(String transferenciaID, StatusTransferenciaEnum statusTransferenciaEnum);
}
