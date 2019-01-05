package ru.timdzha.parts.queries;

import org.springframework.beans.factory.annotation.Value;

/**
 * Статистика по наименованию запчастей, их количества и необходимости
 */
public interface PartNameStatistics {
    @Value("#{target.id}")
    int getId();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.count}")
    int getCount();

    @Value("#{target.isMainPart}")
    boolean getIsMainPart();
}
