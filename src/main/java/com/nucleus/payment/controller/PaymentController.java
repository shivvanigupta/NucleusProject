package com.nucleus.payment.controller;

import com.nucleus.payment.model.Payment;
import com.nucleus.payment.service.PaymentServiceImpl;
import com.nucleus.payment.service.DateEditor;
import com.nucleus.payment.validator.PaymentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = "payment")
public class PaymentController {

    @Autowired
    PaymentServiceImpl paymentService;

//    @Autowired
//    PaymentValidator paymentValidator;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
//        dataBinder.setValidator(paymentValidator);
        dataBinder.registerCustomEditor(LocalDate.class, new DateEditor());
    }

    public PaymentServiceImpl getPaymentService(){
        return paymentService;
    }

    public void setPaymentService(PaymentServiceImpl paymentService){
        this.paymentService = paymentService;
    }

    @GetMapping(value = {"/", ""})
    public ModelAndView viewPayments(){
        List<Payment> paymentList = paymentService.getAllPayments();
        ModelAndView modelAndView = new ModelAndView("views/payment/checkPayment");
        modelAndView.addObject("paymentList", paymentList);
        return modelAndView;
    }

    @GetMapping(value = "/newPayment")
    public ModelAndView newPayment(){
        ModelAndView modelAndView = new ModelAndView("views/payment/newPayment");
        modelAndView.addObject("payment", new Payment());
        return modelAndView;
    }

    @PostMapping(value = "/add")
    public ModelAndView addPayment(@Valid @ModelAttribute("payment") Payment payment, BindingResult bindingResult){
        if(bindingResult.hasErrors())
        {
            ModelAndView modelAndView = new ModelAndView("views/payment/newPayment");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/payment/");
        payment.setPaymentStatus("PENDING");
        payment.setMadeBy(getModifiedBy());
        boolean insertStatus = paymentService.insertPayment(payment);
        return modelAndView;
    }

    @GetMapping(value = "/deletePayment/{loanID}")
    public ModelAndView deletePayment(@PathVariable(value = "loanID") int loanID){
        ModelAndView modelAndView = new ModelAndView("redirect:/payment/");
        boolean deleteStatus = paymentService.deletePayment(loanID);
        return modelAndView;
    }

    @GetMapping(value = "/editPayment/{loanID}")
    public ModelAndView editPayment(@PathVariable(value = "loanID") int loanID){
        ModelAndView modelAndView = new ModelAndView("views/payment/editPayment");
        modelAndView.addObject("editThisPayment", paymentService.getPaymentByLoanID(loanID));
        return modelAndView;
    }

    private String getModifiedBy(){
        String user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            user = ((UserDetails)principal).getUsername();
        } else {
            user = principal.toString();
        }
        return user;
    }

}
