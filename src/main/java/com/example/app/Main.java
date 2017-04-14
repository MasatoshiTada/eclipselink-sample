package com.example.app;

import com.example.entity.Sample;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("samplePU");
            em = emf.createEntityManager();
            em.getTransaction().begin();

            Sample sample = new Sample();
            sample.setId(999); // TODO IDに値をセットするとダメ？
            sample.setText("aaa");

            em.persist(sample);
            em.getTransaction().commit();

            System.out.println(sample + "が正常にINSERTされました。");
        } catch (Exception e) {
            if (em != null && em.isOpen()) {
                em.getTransaction().rollback();
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
            e.printStackTrace();
        }
    }
}
