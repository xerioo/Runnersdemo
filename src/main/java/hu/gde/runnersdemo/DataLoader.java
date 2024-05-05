package hu.gde.runnersdemo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    private final RunnerRepository runnerRepository;
    private final ShoeRepository shoeRepository;

    @Autowired
    public DataLoader(RunnerRepository runnerRepository, ShoeRepository shoeRepository) {
        this.runnerRepository = runnerRepository;
        this.shoeRepository = shoeRepository;
    }

    @Override
    public void run(String... args) {
        //creating shoes first
        List<String> shoeNames = Arrays.asList("Nike Pegasus", "Adidas Ultraboost", "Brooks Ghost", "Saucony Triumph", 
                                                    "Hoka Mach", "lululemon Blissfeel", "Asics Gel-Kayano", "Supersonic", 
                                                    "ZoomX", "Endorphin Speed");
        List<ShoeEntity> shoes = new ArrayList<>();
        for (String shoeName : shoeNames) {
            ShoeEntity shoeEntity = new ShoeEntity();
            shoeEntity.setName(shoeName);
            shoes.add(shoeRepository.save(shoeEntity));
        }
        //creating runners with random data
        List<String> runnerNames = Arrays.asList("Tomi", "Zsuzsi", "Gazsi", "Forest", "Usain");
        Random random = new Random();
        for (String runnerName : runnerNames) {
            RunnerEntity runnerEntity = new RunnerEntity();
            runnerEntity.setRunnerName(runnerName);
            runnerEntity.setAge(16 + random.nextInt(65));
            runnerEntity.setAveragePace(200 + random.nextInt(150));

            for (int i = 1; i <= 2; i++) {
                LapTimeEntity lapTimeEntity = new LapTimeEntity();
                lapTimeEntity.setLapNumber(i);
                lapTimeEntity.setTimeSeconds(90 + random.nextInt(111));
                lapTimeEntity.setRunner(runnerEntity);
                runnerEntity.getLaptimes().add(lapTimeEntity);
            }

            ShoeEntity randomShoe = shoes.get(random.nextInt(shoes.size()));
            runnerEntity.setShoe(randomShoe);

            runnerRepository.save(runnerEntity);
        }

    }
}

