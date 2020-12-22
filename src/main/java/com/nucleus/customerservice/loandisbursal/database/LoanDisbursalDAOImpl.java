package com.nucleus.customerservice.loandisbursal.database;

import com.nucleus.customer.model.Customer;
import com.nucleus.loanapplications.model.LoanApplications;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoanDisbursalDAOImpl implements LoanDisbursalDAO{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public LoanApplications getLoanDetails(int loanApplicationNumber){
        Session session = sessionFactory.openSession();
        LoanApplications loanApplication = session.get(LoanApplications.class, loanApplicationNumber);
        session.close();
        return loanApplication;
    }

    @Override
    public List<LoanApplications> getCustomerLoanDetails(String customerCode){
        Session session = sessionFactory.openSession();
        List<LoanApplications> loanApplications=null;
        Customer customer = session.get(Customer.class, customerCode);
        if(customer != null) {
            loanApplications=customer.getLoanApplications();
        }
        session.close();
        return loanApplications;
    }
}
