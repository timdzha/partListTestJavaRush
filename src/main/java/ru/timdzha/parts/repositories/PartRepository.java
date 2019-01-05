package ru.timdzha.parts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.timdzha.parts.entities.Part;
import ru.timdzha.parts.queries.PartCategoryStatistics;
import ru.timdzha.parts.queries.PartNameStatistics;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Integer> {

    /** Запрос списка комлпектующих с их количеством сгруппированных по имени, категории, необходимости
     * @return Список запчастей с их количеством сгруппированных по имени, категории, необходимости
     */
    @Query(value = "select p.id as id, p.name as name, p.isMainPart as isMainPart, count(p) as count from Part p group by p.name, p.category, p.isMainPart")
    List<PartNameStatistics> findPartCount();

    /** Запрос списка категорий запчастей, необходимых для сборки ПК категорий запчастей с их количеством
     * @return Список необходимых для сборки ПК категорий запчастей с их количеством
     */
    @Query(value = "select p.category as category, count(p) as count from Part p where p.isMainPart = true group by p.category")
    List<PartCategoryStatistics> findCategoryCount();

    /** Запрос списка одинаковых запчастей
     * @return Список необходимых для сборки ПК категорий запчастей с их количеством
     */
    //@Query(value = "select p.id from Part p where p.isMainPart = true group by p.category")
    List<Part> findAllByNameEqualsAndCategoryEqualsAndIsMainPartEquals(String name, String category, boolean isMainPart);

    /** Запрос списка запчастей, выбранных по наименованию
     * SELECT * FROM test.parts WHERE parts.name = 'motherboard 1';
     * @param name Наименование запчасти
     * @return Список запчастей, выбранных по наименованию
     */
    //List<Part> findByName(String name);


    /** Запрос первой найденной запчасти, выбранной по наименованию
     * @param name Наименование запчасти
     */
    //Part findFirstByName(String name);

    /** Поиск списка запчастей по наименованию с их количеством сгруппированных по имени, категории, необходимости
     * @return Список запчастей по наименованию с их количеством сгруппированных по имени, категории, необходимости
     */
    /*@Query(value = "select p.id as id, p.name as name, p.isMainPart as isMainPart, count(p) as count from Part p where p.name like %?1% group by p.name, p.category, p.isMainPart")
    List<PartNameStatistics> findPartCount(String searchName);
    */

}
