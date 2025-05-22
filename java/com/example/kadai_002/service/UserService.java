package com.example.kadai_002.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kadai_002.entity.MembershipType;
import com.example.kadai_002.entity.Role;
import com.example.kadai_002.entity.User;
import com.example.kadai_002.form.SignupForm;
import com.example.kadai_002.form.UserEditForm;
import com.example.kadai_002.repository.RoleRepository;
import com.example.kadai_002.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentMethodAttachParams;

@Service
public class UserService {
	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;        
        this.passwordEncoder = passwordEncoder;
    }    
    
    @Transactional
    public User create(SignupForm signupForm) {
        User user = new User();
        Role role = roleRepository.findByName("ROLE_GENERAL");
        
        user.setName(signupForm.getName());
        user.setFurigana(signupForm.getFurigana());
        user.setPostalCode(signupForm.getPostalCode());
        user.setAddress(signupForm.getAddress());
        user.setPhoneNumber(signupForm.getPhoneNumber());
        user.setEmail(signupForm.getEmail());
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setRole(role);
        user.setEnabled(false);     
        
        return userRepository.save(user);
    }   
    
    @Transactional
    public void update(UserEditForm userEditForm) {
        User user = userRepository.getReferenceById(userEditForm.getId());
        
        user.setName(userEditForm.getName());
        user.setFurigana(userEditForm.getFurigana());
        user.setPostalCode(userEditForm.getPostalCode());
        user.setAddress(userEditForm.getAddress());
        user.setPhoneNumber(userEditForm.getPhoneNumber());
        user.setEmail(userEditForm.getEmail());      
        
        userRepository.save(user);
    }    
    
    // メールアドレスが登録済みかどうかをチェックする
    public boolean isEmailRegistered(String email) {
        User user = userRepository.findByEmail(email);  
        return user != null;
    }  

 // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
    public boolean isSamePassword(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }  

 // ユーザーを有効にする
    @Transactional
    public void enableUser(User user) {
        user.setEnabled(true); 
        userRepository.save(user);
    }  
    
 // メールアドレスが変更されたかどうかをチェックする
    public boolean isEmailChanged(UserEditForm userEditForm) {
        User currentUser = userRepository.getReferenceById(userEditForm.getId());
        return !userEditForm.getEmail().equals(currentUser.getEmail());      
    }  
    
    @Transactional
    public void upgradeToPaid(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getMembershipType() == MembershipType.FREE) {
            user.setMembershipType(MembershipType.PAID);
            userRepository.save(user);
        }
    }

    @Transactional
    public void downgradeToFree(String username) {
        User user = userRepository.findByEmail(username);
        if (user != null && user.isPaid()) {
            user.setMembershipType(MembershipType.FREE);
            userRepository.save(user);
        }
    }
    
    

    @Value("${stripe.api.key}")
    private static String stripeApiKey;

    static {
        Stripe.apiKey = stripeApiKey;
    }


    // ユーザーのカード情報を更新
    public void updateCardInformation(String customerId, String paymentMethodId) {
        try {
            // 新しいカード情報を保存
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            PaymentMethodAttachParams attachParams = PaymentMethodAttachParams.builder()
                    .setCustomer(customerId)
                    .build();
            paymentMethod.attach(attachParams);
        } catch (Exception e) {
            throw new RuntimeException("カード情報の更新に失敗しました", e);
        }
    }

}
