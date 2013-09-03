import models.FindResult;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Работа с БД
 */
@ContextConfiguration(locations = "classpath:context.xml")
public class DatabaseService {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public void saveToDB(String name, long salary) {
        FindResult findResult = new FindResult();

        em.persist(findResult);
    }

}
