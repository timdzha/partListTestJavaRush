package ru.timdzha.parts.queries;

import org.springframework.beans.factory.annotation.Value;

public interface PartCategoryStatistics {
    /**
     * Статистика по категориям с количеством запчастей
     */
    @Value("#{target.category}")
    String getCategory();

    @Value("#{target.count}")
    int getCount();
}
