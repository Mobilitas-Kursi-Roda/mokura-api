package com.mokura.mokura_api.controller.dashboard;

import com.mokura.mokura_api.entity.SupportBuilding;
import com.mokura.mokura_api.repository.SupportBuildingRepository;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/dashboard/support-building")
public class SupportBuildingController {

    final SupportBuildingRepository supportBuildingRepository;

    private final ResourceLoader resourceLoader;

    public SupportBuildingController(SupportBuildingRepository supportBuildingRepository, ResourceLoader resourceLoader) {
        this.supportBuildingRepository = supportBuildingRepository;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping
    public ModelAndView index() {
        List<SupportBuilding> supportBuildings = supportBuildingRepository.findAll();
        return new ModelAndView("dashboard/support-building/index", "supportBuildings", supportBuildings);
    }

    @GetMapping("/add")
    public ModelAndView add() {
        SupportBuilding supportBuilding = new SupportBuilding();
        return new ModelAndView("dashboard/support-building/add", "supportBuilding", supportBuilding);
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute("supportBuilding") SupportBuilding supportBuilding, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            System.out.println("Please select a file to upload");
        }

        try {
            String uploadFolder = "uploads";
            // Ensure the upload folder exists
            Path uploadPath = Paths.get(uploadFolder);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate a unique filename using timestamp
            String originalFilename = file.getOriginalFilename();
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String uniqueFilename = timestamp + "-" + originalFilename;


            // Save the file
            byte[] bytes = file.getBytes();
            Path path = uploadPath.resolve(uniqueFilename);
            Files.write(path, bytes);

            supportBuilding.setThumbnail(String.valueOf(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        supportBuildingRepository.save(supportBuilding);
        return "redirect:/dashboard/support-building";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        SupportBuilding supportBuilding = supportBuildingRepository.findById(id).orElse(null);
        assert supportBuilding != null;
        return new ModelAndView("dashboard/support-building/edit", "supportBuilding", supportBuilding);
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("supportBuilding") SupportBuilding supportBuilding, @RequestParam("file") MultipartFile file) {
        if(!file.isEmpty()) {
            try {
                String uploadFolder = "uploads";
                // Ensure the upload folder exists
                Path uploadPath = Paths.get(uploadFolder);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate a unique filename using timestamp
                String originalFilename = file.getOriginalFilename();
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String uniqueFilename = timestamp + "-" + originalFilename;


                // Save the file
                byte[] bytes = file.getBytes();
                Path path = uploadPath.resolve(uniqueFilename);
                Files.write(path, bytes);

                // Delete the old file
                if(!supportBuilding.getThumbnail().isEmpty()) {
                    Files.deleteIfExists(Path.of(supportBuilding.getThumbnail()));
                }

                supportBuilding.setThumbnail(String.valueOf(path));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        supportBuildingRepository.save(supportBuilding);
        return "redirect:/dashboard/support-building";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws IOException {
        // Ensure the upload folder exists
        Path uploadPath = Paths.get("");

        SupportBuilding supportBuilding = supportBuildingRepository.findById(id).orElse(null);
        if(!supportBuilding.getThumbnail().isEmpty()) {
            Files.deleteIfExists(Path.of(supportBuilding.getThumbnail()));
        }
        supportBuildingRepository.deleteById(id);
        return "redirect:/dashboard/support-building";
    }
}
