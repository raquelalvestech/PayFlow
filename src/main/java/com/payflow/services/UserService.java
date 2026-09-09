


package com.payflow.services;

import com.payflow.domain.user.User;
import com.payflow.domain.wallet.Wallet;
import com.payflow.dtos.UserDTO;
import com.payflow.repositories.UserRepository;
import com.payflow.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public User createUser(UserDTO data) {
        User newUser = new User();
        newUser.setFirstName(data.firstName());
        newUser.setLastName(data.lastName());
        newUser.setDocument(data.document());
        newUser.setEmail(data.email());
        newUser.setPassword(data.password());
        newUser.setUserType(data.userType());

        this.userRepository.save(newUser);

        Wallet wallet = new Wallet();
        wallet.setUser(newUser);
        wallet.setBalance(data.balance());

        this.walletRepository.save(wallet);

        return newUser;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
