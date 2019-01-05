package ru.timdzha.parts.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.timdzha.parts.entities.Part;
import ru.timdzha.parts.queries.PartCategoryStatistics;
import ru.timdzha.parts.queries.PartNameStatistics;
import ru.timdzha.parts.repositories.PartRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartServiceImpl implements PartService {
    private PartRepository partRepository;
    private List<PartNameStatistics> partsNameCount;

    @Autowired
    public void setPartRepository(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Override
    public Part findById(int id) { return partRepository.findById(id).get(); }

    @Override
    public void saveById(int id) {
        Part part = findById(id);
        savePart(new Part(part.getName(), part.getCategory(), part.getIsMainPart()));
    }

    @Override
    public void savePart(Part part) {
        partRepository.save(part);
        findPartCount();
    }

    @Override
    public void updatePart(int id, String name,  String category, boolean isMainPart) {
        Part part = findById(id);
        List<Part> samePartIds = findSameParts(part.getName(), part.getCategory(), part.getIsMainPart());
        for (Part updatedPart : samePartIds) {
            updatedPart.setName(name);
            updatedPart.setCategory(category);
            updatedPart.setIsMainPart(isMainPart);
            savePart(updatedPart);
        }
    }

    @Override
    public List<Part> findSameParts(String name,  String category, boolean isMainPart) {
        return partRepository.findAllByNameEqualsAndCategoryEqualsAndIsMainPartEquals(name, category, isMainPart);
    }

    @Override
    public void deleteById(int id) {

        partRepository.deleteById(id);
        findPartCount();
    }

    @Override
    public void findPartCount() {
        partsNameCount = partRepository.findPartCount();
    }


    @Override
    public List<PartNameStatistics> findPartCountContains(String searchName, String sortDir, int filterMain) {
        if (searchName.equals("") && (filterMain == -1) && (sortDir.equals("NO_SORT"))) {
            findPartCount();
            return partsNameCount;
        }
        List<PartNameStatistics> partNameStatistics = getFilteredByMain(partsNameCount, filterMain);
        partNameStatistics = getFilteredByName(partNameStatistics, searchName);
        if (!sortDir.equals("NO_SORT"))
            return getSortedByName(partNameStatistics, sortDir);

        return partNameStatistics;
    }

    private List<PartNameStatistics> getFilteredByMain(List<PartNameStatistics> partsNameCount, int filterMain) {
        return partsNameCount.stream()
                .filter(part ->
                        (filterMain == 1 && part.getIsMainPart())
                                || (filterMain == 0 && !part.getIsMainPart())
                                || (filterMain == -1 && (part.getIsMainPart() || !part.getIsMainPart()))
                )
                .collect(Collectors.toList());
    }

    private List<PartNameStatistics> getFilteredByName(List<PartNameStatistics> partsNameCount, String searchName) {
        return partsNameCount.stream()
                .filter(part ->
                        (!searchName.equals("") && part.getName().startsWith(searchName))
                                || (searchName.equals(""))
                )
                .collect(Collectors.toList());
    }

    private List<PartNameStatistics> getSortedByName(List<PartNameStatistics> partsNameCount, String sortDir) {
        if (sortDir.equals("ASC"))
            return partsNameCount.stream()
                .sorted(Comparator.comparing((PartNameStatistics o) -> o.getName()).thenComparing(o -> o.getName()))
                    .collect(Collectors.toList());
        else return partsNameCount.stream()
                    .sorted(Comparator.comparing((PartNameStatistics o) -> o.getName()).thenComparing(o -> o.getName()).reversed())
                .collect(Collectors.toList());

    }

    @Override
    public List<PartCategoryStatistics> findMainCategoryCount() { return partRepository.findCategoryCount(); }

    public Page<PartNameStatistics> findPaginated(Pageable pageable, List<PartNameStatistics> partsNameCount) {

        List<PartNameStatistics> list;
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        if (partsNameCount.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, partsNameCount.size());
            list = partsNameCount.subList(startItem, toIndex);
        }

        Page<PartNameStatistics> partPage
                = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), partsNameCount.size());

        return partPage;
    }


    /*@Override
    public void deleteFirstPartByName(String name) {
        Part part = partRepository.findFirstByName(name);
        partRepository.deleteById(part.getId());
    }*/

    /*@Override
    public List<Part> findPartsByName(String name) {
        return partRepository.findByName(name);
    }*/

}
