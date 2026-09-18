


package com.payflow.services;

import com.payflow.domain.transaction.Transaction;
import com.payflow.domain.user.User;
import com.payflow.domain.user.UserType;
import com.payflow.domain.wallet.Wallet;
import com.payflow.dtos.TransactionDTO;
import com.payflow.repositories.TransactionRepository;
import com.payflow.repositories.UserRepository;
import com.payflow.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service

public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public Transaction createTransaction(TransactionDTO data) throws Exception {
        User payer = userRepository.findById(data.payerId())
                .orElseThrow(() -> new Exception("Pagador não encontrado"));

        User payee = userRepository.findById(data.payeeId())
                .orElseThrow(() -> new Exception("Recebedor não encontrado"));

        Wallet payerWallet = walletRepository.findWalletByUserId(payer.getId())
                .orElseThrow(() -> new Exception("Carteira do pagador não encontrada"));

        Wallet payeeWallet = walletRepository.findWalletByUserId(payee.getId())
                .orElseThrow(() -> new Exception("Carteira do recebedor não encontrada"));

        //(MERCHANT) não podem realizar transferências
        if (payer.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuários do tipo Lojista não podem realizar transferências");
        }

        //Validar saldo suficiente
        if (payerWallet.getBalance().compareTo(data.value()) < 0) {
            throw new Exception("Saldo insuficiente para realizar a transação");
        }

        //Atualiza saldos
        payerWallet.setBalance(payerWallet.getBalance().subtract(data.value()));
        payeeWallet.setBalance(payeeWallet.getBalance().add(data.value()));

        //Registra a transação
        Transaction newTransaction = new Transaction();
        newTransaction.setValue(data.value());
        newTransaction.setPayer(payer);
        newTransaction.setPayee(payee);
        newTransaction.setCreatedAt(LocalDateTime.now());

        walletRepository.save(payerWallet);
        walletRepository.save(payeeWallet);
        return transactionRepository.save(newTransaction);
    }
}
