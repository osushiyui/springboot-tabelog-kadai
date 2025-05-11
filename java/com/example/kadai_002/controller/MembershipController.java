package com.example.kadai_002.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.kadai_002.service.UserService;

@Controller
public class MembershipController {
    
    private final UserService userService;

    // コンストラクタインジェクション
    public MembershipController(UserService userService) {
        this.userService = userService;
    }

    // 有料会員登録ページを表示
    @GetMapping("/membership/upgrade")
    public String showUpgradePage(Model model) {
        return "membership/upgrade";  // "upgrade.html" を返す
    }
    
    // 有料会員登録成功ページを表示
    @GetMapping("/membership/success")
    public String handleSuccess(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // 現在のログインユーザーを取得し、有料会員に切り替える
        userService.upgradeToPaid(userDetails.getUsername());

        // 成功メッセージをモデルに追加
        model.addAttribute("message", "有料会員への切り替えが完了しました。");

        return "membership/success";  // "success.html" を返す
    }
    
    // 解約処理ページ
    @GetMapping("/membership/cancel")
    public String showCancelPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // ユーザー情報をモデルに渡して解約ページを表示
        model.addAttribute("user", userDetails);
        return "membership/cancel";  // 解約ページのビュー
    }

    // 解約処理
    @PostMapping("/membership/cancel")
    public String handleCancel(@AuthenticationPrincipal UserDetails userDetails) {
        userService.downgradeToFree(userDetails.getUsername());
        return "membership/cancel-success";  // 解約成功後のページ
    }
    
    @GetMapping("/membership/edit/card")
    public String showEditCardPage() {
    	  return "membership/edit-card";
    }

}
