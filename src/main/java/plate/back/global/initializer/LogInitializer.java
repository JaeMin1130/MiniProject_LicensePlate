package plate.back.global.initializer;
// package plate.back.initializer;

// import java.time.LocalDateTime;
// import java.time.ZoneId;
// import java.time.temporal.ChronoUnit;
// import java.util.Date;
// import java.util.Random;

// import org.springframework.boot.ApplicationArguments;
// import org.springframework.boot.ApplicationRunner;
// import org.springframework.stereotype.Component;

// import lombok.RequiredArgsConstructor;
// import plate.back.entity.Record;
// import plate.back.persistence.RecordRepository;

// @RequiredArgsConstructor
// @Component
// public class LogInitializer implements ApplicationRunner {

// private final RecordRepository logRepo;

// @Override
// public void run(ApplicationArguments args) throws Exception {
// logRepo.deleteAll();

// for (int num = 0; num < 100; num++) {
// Random random = new Random();
// int randomIdx = random.nextInt(10);
// int randomAccuracy = random.nextInt(100);

// LocalDateTime now = LocalDateTime.now();
// LocalDateTime lastYear = now.minus(1, ChronoUnit.YEARS);
// long daysBetween = ChronoUnit.DAYS.between(lastYear, now);
// int randomNum = random.nextInt((int) daysBetween);
// LocalDateTime randomTime = lastYear.plus(randomNum, ChronoUnit.DAYS);
// Date randomDate =
// Date.from(randomTime.atZone(ZoneId.systemDefault()).toInstant());

// int[] randomText = { 1063, 2580, 2591, 3231, 4365, 5061, 6214, 7136, 7979,
// 8080 };

// logRepo.save(Record.builder()
// .licensePlate(String.valueOf(randomText[randomIdx]))
// .modelType("model")
// .accuracy((double) randomAccuracy)
// .state("state")
// .build());
// }
// }
// }