package com.nucleus.loanapplications.controller;

import com.nucleus.customer.model.Address;
import com.nucleus.customer.model.Customer;
import com.nucleus.customer.service.AddressService;
import com.nucleus.customer.service.NewCustomerService;
import com.nucleus.loanapplications.model.LoanApplications;
import com.nucleus.loanapplications.service.NewLoanApplicationService;
import com.nucleus.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class NewLoanApplicationController {

    @Autowired
    NewLoanApplicationService newLoanApplicationService;

    @Autowired
    NewCustomerService newCustomerService;

    @Autowired
    AddressService addressService;

    @GetMapping(value = "/newLoanApplication")
    public ModelAndView addNewLoanApplication(){
        ModelAndView modelAndView= new ModelAndView("views/loanapplication/loanInformation");
        modelAndView.addObject("loanApplications",new LoanApplications());
        return modelAndView;
    }

    @PostMapping(value = "/newLoanApplication")
    public ModelAndView addCustomer(@Valid @ModelAttribute LoanApplications loanApplications , HttpServletRequest request){
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        Address address = (Address) session.getAttribute("address");
/*
        Product product = new Product();
        product.setProductCode("P101");
        product.setProductName("homeLoan");
        product.setProductType("property");*/

        loanApplications.setCustomerCode(customer);

     /*   loanApplications.setProductCode(product);*/

       boolean a =  newCustomerService.createNewCustomer(customer);
        boolean b =addressService.insertAddress(address);
        boolean c = newLoanApplicationService.addLoanApplication(loanApplications);


        ModelAndView modelAndView = new ModelAndView("views/customerInfo/success");
/*
        modelAndView.addObject("a" ,a);
        modelAndView.addObject("b",b);
        modelAndView.addObject("c",c);
        */
        return modelAndView;
    }
}
