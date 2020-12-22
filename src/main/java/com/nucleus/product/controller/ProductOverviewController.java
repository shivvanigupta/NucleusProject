package com.nucleus.product.controller;

import com.nucleus.login.logindetails.LoginDetailsImpl;
import com.nucleus.product.dao.ProductDAO;
import com.nucleus.product.model.Product;
import com.nucleus.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductOverviewController {

    @Autowired
    ProductService productService;

    @GetMapping(value = {"/product" })
    public ModelAndView productOverview() {
        ModelAndView modelAndView = new ModelAndView("views/product/productOverview");
        LoginDetailsImpl details = new LoginDetailsImpl();
        List<Product> productList = productService.getProductList();
//        productList.removeIf(p -> "Saved".equals(p.getStatus()) || !details.getUserName().equals(p.getCreatedBy()));
        modelAndView.addObject("products", productList);
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_CHECKER')")
    @GetMapping(value = "/product/{productId}")
    public ModelAndView productViewById(@PathVariable(value = "productId") String productId){
        Product product = productService.getProductById(productId);
        ModelAndView modelAndView = new ModelAndView("views/product/newProductCreationChecker");
        modelAndView.addObject("product", product);
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_CHECKER')")
    @PostMapping(value = "/product/{productId}/update")
    public ModelAndView updateProductStatus(@PathVariable(value = "productId") String productId, @RequestParam("action") String action){
        ModelAndView modelAndView = new ModelAndView("views/product/newProductSave");
        Product product = productService.getProductById(productId);
        LoginDetailsImpl details = new LoginDetailsImpl();

        product.setAuthorizedBy(details.getUserName());
        product.setAuthorizedDate(LocalDate.now());
        product.setStatus(action);

        product = productService.updateProduct(product);
        if(product==null){
            modelAndView.addObject("message", "Product was not " + action +" due to an error. Please try again.");
            return modelAndView;
        }
        modelAndView.addObject("message", "Product was " + action + " successfully.");
        return modelAndView;

    }

    @PreAuthorize("hasRole('ROLE_MAKER')")
    @GetMapping(value = "/product/{productId}/delete")
    public ModelAndView deleteProduct(@PathVariable(value = "productId") String productId){
        ModelAndView modelAndView = new ModelAndView("views/product/newProductSave");
        Boolean success = productService.deleteProduct(productId);
        if(success){
            modelAndView.addObject("message", "Product was deleted successfully.");
            return modelAndView;
        }
        modelAndView.addObject("message", "Product could not be deleted");
        return modelAndView;
    }
}
