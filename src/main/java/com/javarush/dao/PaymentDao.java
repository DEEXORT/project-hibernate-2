package com.javarush.dao;

import com.javarush.entity.Payment;
import com.javarush.config.SessionCreator;

public class PaymentDao extends RepositoryImpl<Payment> {

    public PaymentDao(SessionCreator sessionCreator, Class<Payment> paymentClass) {
        super(sessionCreator, paymentClass);
    }
}
