package com.nucleus.product.dao;

import com.nucleus.product.model.Product;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDAO implements ProductDAOInterface {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession(){
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException E){
            session = sessionFactory.openSession();
        }
        return session;
    }

    @Override
    public List<Product> getProductList() {
        try(Session session = getSession()) {
            session.beginTransaction();
            Query<Product> query = session.createQuery("from Product p");
            List<Product> productList = query.list();
            session.getTransaction().commit();
            return productList;
        }
    }

    @Override
    public Boolean createNewProduct(Product product) {
        try(Session session = getSession()){
            session.beginTransaction();
            try {
                session.save(product);
                session.getTransaction().commit();
                return true;
            } catch (Exception e){
                e.printStackTrace();
                session.getTransaction().rollback();
                return false;
            }
        }
    }

    public Product getProductById(String id){
        try(Session session = getSession()){
            Product product;
            session.beginTransaction();
            try {
                product = session.get(Product.class, id);
                session.getTransaction().commit();
                return product;
            } catch (Exception e){
                e.printStackTrace();
                session.getTransaction().rollback();
                return null;
            }
        }
    }

    public Product updateProduct(Product product){
        try(Session session = getSession()){
            session.beginTransaction();
            try {
                session.update(product);
                session.getTransaction().commit();
                return product;
            } catch (Exception e){
                e.printStackTrace();
                session.getTransaction().rollback();
                return null;
            }
        }
    }

    public Boolean deleteProduct(String productId){
        try(Session session = getSession()){
            session.beginTransaction();
            try {
                Product product = session.get(Product.class, productId);
                session.delete(product);
                session.getTransaction().commit();
                return true;
            } catch (Exception e){
                e.printStackTrace();
                session.getTransaction().rollback();
                return false;
            }
        }
    }
}
