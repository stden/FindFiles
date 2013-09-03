package models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Результат поиска
 */
@Entity
public class FindResult {
    @Id
    public int id;
    public String keyword;
    public String filename;
    public int line;
}
