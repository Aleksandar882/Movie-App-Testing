package com.example.movieapp.web;

import com.example.movieapp.model.ChargeRequest;
import com.example.movieapp.model.CustomOAuth2User;
import com.example.movieapp.model.ShoppingCart;
import com.example.movieapp.model.User;
import com.example.movieapp.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Value("pk_test_51J5tsQEyhuPvt6izDuHw4bdkk7tGOVzZeZ1XOfbBHEvE2fRqwbKfpOY8TM73NQeo5KBm8Qf3CrJew68JY77tLpeK00DN1VZz2k")
    private String stripePublicKey;


    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public String getShoppingCartPage(@RequestParam(required = false) String error,
                                      HttpServletRequest req,
                                      Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        String username = req.getRemoteUser();
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
        double prices=this.shoppingCartService.getPrice(shoppingCart.getId());
        model.addAttribute("amount", (int)prices * 100); // in cents
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.EUR);
        model.addAttribute("movies", this.shoppingCartService.listAllMoviesInShoppingCart(shoppingCart.getId()));
        return "shopping-cart.html";
    }

    @PostMapping("/add-movie/{id}")
    public String addMovieToShoppingCart(@PathVariable Long id, HttpServletRequest req, Authentication authentication) {
        CustomOAuth2User y=new CustomOAuth2User();
        if(authentication.getPrincipal().getClass().isInstance(y)){
            try {
                CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
                this.shoppingCartService.addMovieToShoppingCart(user.getName(), id);
                return "redirect:/shopping-cart";
            } catch (RuntimeException exception) {
                return "redirect:/shopping-cart?error=" + exception.getMessage();
            }
        }else
            try {
                User user = (User) authentication.getPrincipal();
                this.shoppingCartService.addMovieToShoppingCart(user.getUsername(), id);
                return "redirect:/shopping-cart";
            } catch (RuntimeException exception) {
                return "redirect:/shopping-cart?error=" + exception.getMessage();
            }



    }

    @PostMapping("/delete-movie/{id}")
    public String deleteMovieFromShoppingCart(@PathVariable Long id, HttpServletRequest req, Authentication authentication) {
        CustomOAuth2User y=new CustomOAuth2User();
        if(authentication.getPrincipal().getClass().isInstance(y)){
            try {
                CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
                this.shoppingCartService.deleteMovieFromShoppingCart(user.getName(), id);
                return "redirect:/shopping-cart";
            } catch (RuntimeException exception) {
                return "redirect:/shopping-cart?error=" + exception.getMessage();
            }
        }else
            try {
                User user = (User) authentication.getPrincipal();
                this.shoppingCartService.deleteMovieFromShoppingCart(user.getUsername(), id);
                return "redirect:/shopping-cart";
            } catch (RuntimeException exception) {
                return "redirect:/shopping-cart?error=" + exception.getMessage();
            }



    }
}