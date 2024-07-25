package com.mokura.mokura_api.controller.dashboard;

import com.mokura.mokura_api.entity.Device;
import com.mokura.mokura_api.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/dashboard/device")
public class DeviceController {

    final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping
    public ModelAndView device() {
        List<Device> devices = deviceRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("dashboard/device/index");
        modelAndView.addObject("devices", devices);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView addDevice() {
        Device device = new Device();
        return new ModelAndView("dashboard/device/add", "device", device);
    }

    @PostMapping("/insert")
    public String insertDevice(@ModelAttribute("device") Device device) {
        deviceRepository.save(device);
        return "redirect:/dashboard/device";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editDevice(@PathVariable("id") Long id) {
        Device device = deviceRepository.findById(id).orElse(null);
        assert device != null;
        return new ModelAndView("dashboard/device/edit", "device", device);
    }

    @PostMapping("/update")
    public String updateDevice(@ModelAttribute("device") Device device) {
        deviceRepository.save(device);
        return "redirect:/dashboard/device";
    }

    @GetMapping("/delete")
    public String deleteDevice(@RequestParam("id") Long id) {
        deviceRepository.deleteById(id);
        return "redirect:/dashboard/device";
    }

}
