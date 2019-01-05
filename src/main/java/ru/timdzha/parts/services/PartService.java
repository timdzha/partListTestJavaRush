package ru.timdzha.parts.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.timdzha.parts.entities.Part;
import ru.timdzha.parts.queries.PartCategoryStatistics;
import ru.timdzha.parts.queries.PartNameStatistics;

import java.util.List;

/** interface PartService
 * Working with table `parts`
 */
public interface PartService {
    Part findById(int id);
    void savePart(Part part);
    void saveById(int id);
    void updatePart(int id, String name,  String category, boolean isMainPart);
    void deleteById(int id);

    //void deleteFirstPartByName(String name);
    //List<Part> findPartsByName(String name);
    void findPartCount();
    List<PartNameStatistics> findPartCountContains(String searchName, String sortDir, int filterMain);
    List<PartCategoryStatistics> findMainCategoryCount();
    List<Part> findSameParts(String name,  String category, boolean isMainPart);
    Page<PartNameStatistics> findPaginated(Pageable pageable, List<PartNameStatistics> partsNameCount);
}
