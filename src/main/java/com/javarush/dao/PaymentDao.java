package com.javarush.dao;

import com.javarush.entity.Payment;
import com.javarush.config.SessionCreator;

public class PaymentRepository extends RepositoryImpl<Payment> {

    public PaymentRepository(SessionCreator sessionCreator, Class<Payment> paymentClass) {
        super(sessionCreator, paymentClass);
    }
}
