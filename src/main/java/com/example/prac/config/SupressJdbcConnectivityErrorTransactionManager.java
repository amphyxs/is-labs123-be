package com.example.prac.config;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

@Component
public class SupressJdbcConnectivityErrorTransactionManager extends JpaTransactionManager {
    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        try {
            super.doBegin(transaction, definition);
        } catch (RuntimeException e) {
            supressJdbcConnectionException(e);
        }
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        try {
            super.doCleanupAfterCompletion(transaction);
        } catch (RuntimeException e) {
            supressJdbcConnectionException(e);
        }
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        try {
            super.doCommit(status);
        } catch (RuntimeException e) {
            supressJdbcConnectionException(e);
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        try {
            super.doRollback(status);
        } catch (RuntimeException e) {
            supressJdbcConnectionException(e);
        }
    }

    @Override
    protected Object doGetTransaction() {
        try {
            return super.doGetTransaction();
        } catch (RuntimeException e) {
            supressJdbcConnectionException(e);

            return null;
        }

    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        try {
            super.doResume(transaction, suspendedResources);
        } catch (RuntimeException e) {
            supressJdbcConnectionException(e);
        }

    }

    @Override
    protected void doSetRollbackOnly(DefaultTransactionStatus status) {
        try {
            super.doSetRollbackOnly(status);
        } catch (RuntimeException e) {
            supressJdbcConnectionException(e);
        }
    }

    @Override
    protected Object doSuspend(Object transaction) {
        try {
            return super.doSuspend(transaction);
        } catch (RuntimeException e) {
            supressJdbcConnectionException(e);

            return null;
        }

    }

    private void supressJdbcConnectionException(RuntimeException e) {
        boolean isJDBCConnectionException = e.getCause() instanceof JDBCConnectionException;
        boolean isIllegalStateException = e.getMessage().equals("No EntityManagerHolder available");

        if (!isJDBCConnectionException && !isIllegalStateException) {
            throw e;
        }
    }
}
