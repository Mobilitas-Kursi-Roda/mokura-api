package com.mokura.mokura_api.controller.dashboard;

import com.mokura.mokura_api.entity.ChargingStation;
import com.mokura.mokura_api.entity.SupportBuilding;
import com.mokura.mokura_api.repository.ChargingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/dashboard/charging-station")
public class ChargingStationController {

    @Autowired
    ChargingStationRepository chargingStationRepository;

    private final ResourceLoader resourceLoader;

    public ChargingStationController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping
    public ModelAndView index() {
        List<ChargingStation> chargingStations = chargingStationRepository.findAll();
        return new ModelAndView("dashboard/charging-station/index", "chargingStations", chargingStations);
    }

    @GetMapping("/add")
    public ModelAndView add() {
        ChargingStation chargingStation = new ChargingStation();
        return new ModelAndView("dashboard/charging-station/add", "chargingStation", chargingStation);
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute("chargingStation") ChargingStation chargingStation, @RequestParam("file") MultipartFile file) {
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

            chargingStation.setThumbnail(String.valueOf(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        chargingStationRepository.save(chargingStation);
        return "redirect:/dashboard/charging-station";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        ChargingStation chargingStation = chargingStationRepository.findById(id).orElse(null);
        assert chargingStation != null;
        return new ModelAndView("dashboard/charging-station/edit", "chargingStation", chargingStation);
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("supportBuilding") ChargingStation chargingStation, @RequestParam("file") MultipartFile file) {
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
                if(!chargingStation.getThumbnail().isEmpty()) {
                    Files.deleteIfExists(Path.of(chargingStation.getThumbnail()));
                }

                chargingStation.setThumbnail(String.valueOf(path));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        chargingStationRepository.save(chargingStation);
        return "redirect:/dashboard/charging-station";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws IOException {
        ChargingStation chargingStation = chargingStationRepository.findById(id).orElse(null);
        if(!chargingStation.getThumbnail().isEmpty()) {
            Files.deleteIfExists(Path.of(chargingStation.getThumbnail()));
        }
        chargingStationRepository.deleteById(id);
        return "redirect:/dashboard/charging-station";
    }
}
