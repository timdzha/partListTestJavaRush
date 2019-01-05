package ru.timdzha.parts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.timdzha.parts.entities.Part;
import ru.timdzha.parts.entities.PartCategory;
import ru.timdzha.parts.queries.PartCategoryStatistics;
import ru.timdzha.parts.queries.PartNameStatistics;
import ru.timdzha.parts.services.PartService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller // This means that this class is a Controller
public class PartController {

    private PartService partService;

    private final int PAGE_SIZE = 10;
    private final String NO_SORT = "NO_SORT";
    private final String SORT_ASC = "ASC";
    private final String SORT_DESC = "DESC";

    private String sortDir = NO_SORT;
    private String searchName = "";
    private int filterMain = -1;

    @Autowired
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    @GetMapping("/reset")
    public String reset() {
        sortDir = NO_SORT;
        searchName = "";
        return "redirect:/";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(Model model,
                       @RequestParam("page") Optional<Integer> page,
                       @RequestParam("size") Optional<Integer> size) {
        int pcCount = countPC();
        String pcCountText = getMessage(pcCount);
        Page<PartNameStatistics> partPage = getPartPage(page.orElse(1), size.orElse(PAGE_SIZE));
        List<Integer> pageNumbers = getPageNumbers(partPage);

        model.addAttribute("pcCount", pcCount);
        model.addAttribute("pcCountText", pcCountText);
        model.addAttribute("partPage", partPage);
        model.addAttribute("searchName", searchName);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("sortDir", sortDir);

        return "index";
    }

    private Page<PartNameStatistics> getPartPage(int page, int size) {
        List<PartNameStatistics> partsNameCount = partService.findPartCountContains(searchName, sortDir, filterMain);
        return partService.findPaginated(PageRequest.of(page - 1, size), partsNameCount);
    }

    private List<Integer> getPageNumbers(Page<PartNameStatistics> partPage) {
        int totalPages = partPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            return pageNumbers;
        }
        return Collections.emptyList();
    }

    private int countPC() {
        List<PartCategoryStatistics> partsMainGroupedByCategory = partService.findMainCategoryCount();
        if (partsMainGroupedByCategory.size() != PartCategory.countMainCategories()) return 0;
        partsMainGroupedByCategory.sort(Comparator.comparing(PartCategoryStatistics::getCount));
        return partsMainGroupedByCategory.get(0).getCount();
    }

    private String getMessage(int pcCount) {
        int kLast = pcCount % 10;
        pcCount = pcCount / 10;
        int kPrevLast = pcCount % 10;
        if (kPrevLast != 1) {
            if (kLast == 1) return "компьютер";
            if (kLast > 1 && kLast < 5) return "компьютера";
        }
        return "компьютеров";
    }

    @PostMapping("/search")
    public String foundByName(@RequestParam String searchName) {
        this.searchName = searchName;
        return "redirect:/";
    }

    @GetMapping("/sort/{sortDir}")
    public String sortByName(@PathVariable String sortDir) {
        if (sortDir.equals(NO_SORT)) this.sortDir = SORT_ASC;
        if (sortDir.equals(SORT_ASC)) this.sortDir = SORT_DESC;
        if (sortDir.equals(SORT_DESC)) this.sortDir = SORT_ASC;
        return "redirect:/";
    }

    @GetMapping("/filter/{filterMain}")
    public String sortByName(@PathVariable int filterMain) {
        this.filterMain = filterMain;
        return "redirect:/";
    }

    @GetMapping("/add")
    public String addPart(Model model) {
        model.addAttribute("partCategories", PartCategory.values());
        return "operations/add";
    }

    @PostMapping("/save")
    public String savePart(@RequestParam String name, @RequestParam String category, @RequestParam (value = "isMainPart", required = false) String isMainPart) { //@RequestParam boolean isMainPart
        partService.savePart(new Part(name, category, isMainPart == null ? false : true));
        return "redirect:/";
    }

    /*@PostMapping("/save")
    public String savePart(@RequestParam String name, @RequestParam String category, @RequestParam(value = "isMainPart", required = false) boolean isMainPart) {
        partService.savePart(new Part(name, category, isMainPart ));
        return "redirect:/";
    }*/

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id) {
        partService.saveById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Part part = partService.findById(id);
        model.addAttribute("part", part);
        return "operations/edit";
    }

    @PostMapping("/update")
    public String updatePart(@RequestParam int id, @RequestParam String name, @RequestParam String category, @RequestParam(value = "isMainPart", required = false) boolean isMainPart) {
        partService.updatePart(id, name, category, isMainPart);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        partService.deleteById(id);
        return "redirect:/";
    }








}
