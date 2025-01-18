package com.example.prac.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.example.prac.service.imports.ImportService;

import lombok.AllArgsConstructor;

@Component
public class CustomTransactionManager extends JpaTransactionManager {
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        try {
            super.doBegin(transaction, definition);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        try {
            super.doCleanupAfterCompletion(transaction);
        } catch (Exception e) {
            return;
        }
        // TODO Auto-generated method stub
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        try {
            super.doCommit(status);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        try {
            super.doRollback(status);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    protected Object doGetTransaction() {
        try {
            return super.doGetTransaction();
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        try {
            super.doResume(transaction, suspendedResources);
        } catch (Exception e) {
            return;
        }

    }

    @Override
    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        try {
            super.doSetRollbackOnly(status);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    protected Object doSuspend(Object transaction) {
        try {
            return super.doSuspend(transaction);
        } catch (Exception e) {
            return null;
        }

    }
}
