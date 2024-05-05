package hu.gde.runnersdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RunnerController {

    @Autowired
    private RunnerRepository runnerRepository;
    @Autowired
    private RunnerService runnerService;
    @Autowired
    private ShoeRepository shoeRepository;

    @GetMapping("/runners")
    public String getAllRunners(Model model) {
        List<RunnerEntity> runners = runnerRepository.findAll();
        Map<RunnerEntity, String> runnerShoeNames = new HashMap<>();
        for (RunnerEntity runner : runners) {
            String shoeName = runner.getShoe().getName();
            runnerShoeNames.put(runner, shoeName);
        }
        model.addAttribute("runners", runnerShoeNames);
        //model.addAttribute("runners", runners);
        double averageAge = Math.round(runnerService.getAverageAge()*100.0)/100.0;
        model.addAttribute("averageAge", averageAge);
        return "runners";
    }

    @GetMapping("/runner/{id}")
    public String getRunnerById(@PathVariable Long id, Model model) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        RunnerService runnerService = new RunnerService(runnerRepository);
        if (runner != null) {
            model.addAttribute("runner", runner);
            double averageLaptime = runnerService.getAverageLaptime(runner.getRunnerId());
            model.addAttribute("averageLaptime", averageLaptime);
            model.addAttribute("shoeName", runner.getShoe().getName());
            return "runner";
        } else {
            // handle error when runner is not found
            return "error";
        }
    }

    @GetMapping("/runner/{id}/addlaptime")
    public String showAddLaptimeForm(@PathVariable Long id, Model model) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        if (runner != null) {
            model.addAttribute("runner", runner);
            LapTimeEntity laptime = new LapTimeEntity();
            laptime.setLapNumber(runner.getLaptimes().size() + 1);
            model.addAttribute("laptime", laptime);
            return "addlaptime";
        } else {
            // handle error when runner is not found
            return "error";
        }
    }
    @PostMapping("/runner/{id}/addlaptime")
    public String addLaptime(@PathVariable Long id, @ModelAttribute LapTimeEntity laptime) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        if (runner != null) {
            laptime.setRunner(runner);
            laptime.setId(null);
            runner.getLaptimes().add(laptime);
            runnerRepository.save(runner);
        } else {
            // handle error when runner is not found
        }
        return "redirect:/runner/" + id;
    }
    @PutMapping("/runners/{runnerId}/shoe/{id}")
    public RunnerEntity changeRunnerShoe(@PathVariable Long runnerId, @PathVariable Long id) {
        RunnerEntity runner = runnerRepository.findById(runnerId).orElse(null);
        ShoeEntity shoe = shoeRepository.findById(id).orElse(null);
        if (runner != null && shoe != null) {
            runner.setShoe(shoe);
        } else {
            // handle error when runner or shoe is not found
        }
        return runnerRepository.save(runner);
    }

}

