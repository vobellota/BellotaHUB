/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author ahenao
 */
public class FacadeDAO<T> {

    private Class<T> entityClass;

    public FacadeDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

   @PersistenceContext(unitName = "BellotaHUBPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        try {
            java.sql.Connection connection = getEntityManager().unwrap(java.sql.Connection.class);
            System.out.println("Connection: "+connection.getTransactionIsolation());
            getEntityManager().merge(entity);
        } catch (SQLException ex) {
            Logger.getLogger(FacadeDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
