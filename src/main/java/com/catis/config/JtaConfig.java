// package com.catis.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.transaction.annotation.EnableTransactionManagement;
// import org.springframework.transaction.jta.JtaTransactionManager;

// import com.atomikos.icatch.jta.UserTransactionImp;
// import com.atomikos.icatch.jta.UserTransactionManager;

// @Configuration
// @EnableTransactionManagement
// public class JtaConfig {

//     @Bean
//     public UserTransactionManager userTransactionManager() {
//         UserTransactionManager userTransactionManager = new UserTransactionManager();
//         userTransactionManager.setForceShutdown(true);
//         return userTransactionManager;
//     }

//     @Bean
//     public UserTransactionImp userTransaction() throws Throwable {
//         UserTransactionImp userTransaction = new UserTransactionImp();
//         userTransaction.setTransactionTimeout(10000);
//         return userTransaction;
//     }

//     @Bean
//     public JtaTransactionManager transactionManager() throws Throwable {
//         return new JtaTransactionManager(userTransaction(), userTransactionManager());
//     }
// }
