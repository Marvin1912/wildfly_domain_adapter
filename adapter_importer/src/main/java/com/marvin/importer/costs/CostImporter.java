package com.marvin.importer.costs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvin.common.costs.daily.DailyCostDTO;
import com.marvin.common.costs.monthly.MonthlyCostDTO;
import com.marvin.common.costs.salary.SalaryDTO;
import com.marvin.common.costs.special.SpecialCostDTO;
import com.marvin.jms.infrastructure.costs.daily.DailyCostDestination;
import com.marvin.jms.infrastructure.costs.monthly.MonthlyCostDestination;
import com.marvin.jms.infrastructure.costs.salary.SalaryDestination;
import com.marvin.jms.infrastructure.costs.special.SpecialCostDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CostImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CostImporter.class);

    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("([a-z_]+)_[0-9]{8}_[0-9]{6}\\.json");

    private final String in;
    private final String done;

    private final ObjectMapper objectMapper;
    private final MonthlyCostDestination monthlyCostDestination;
    private final SalaryDestination salaryDestination;
    private final SpecialCostDestination specialCostDestination;
    private final DailyCostDestination dailyCostDestination;

    public CostImporter(
            String in,
            String done,

            ObjectMapper objectMapper,
            MonthlyCostDestination monthlyCostDestination,
            SalaryDestination salaryDestination,
            SpecialCostDestination specialCostDestination,
            DailyCostDestination dailyCostDestination
    ) {
        this.in = in;
        this.done = done;

        this.objectMapper = objectMapper;
        this.monthlyCostDestination = monthlyCostDestination;
        this.salaryDestination = salaryDestination;
        this.specialCostDestination = specialCostDestination;
        this.dailyCostDestination = dailyCostDestination;
    }

    public void importFiles() {
        try (Stream<Path> pathStream = Files.walk(Path.of(in), 1)) {
            pathStream
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(this::readDataFromFile);
        } catch (Exception e) {
            LOGGER.error("Could read files!", e);
        }
    }

    private void readDataFromFile(Path path) {

        final Matcher matcher = FILE_NAME_PATTERN.matcher(path.getFileName().toString());
        if (!matcher.matches()) {
            LOGGER.warn("Could not match file name pattern for {}", path);
            return;
        }

        final String type = matcher.group(1);
        final Consumer<String> sender = switch (type) {
            case "monthly_costs" -> c -> send(readValue(c, MonthlyCostDTO.class), monthlyCostDestination::sendMessage);
            case "salaries" -> c -> send(readValue(c, SalaryDTO.class), salaryDestination::sendMessage);
            case "special_costs" -> c -> send(readValue(c, SpecialCostDTO.class), specialCostDestination::sendMessage);
            case "daily_costs" -> c -> send(readValue(c, DailyCostDTO.class), dailyCostDestination::sendMessage);
            default -> null;
        };

        if (sender == null) {
            LOGGER.warn("Could not find matching sender for {}!", type);
            return;
        }

        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(sender);
        } catch (Exception e) {
            LOGGER.error("Could read lines from {}!", path.getFileName(), e);
        }

        try {
            Files.move(path, Path.of(done).resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Could not move file {} to done!", path);
        }
    }

    private <T> T readValue(String value, Class<T> clazz) {
        try {
            return objectMapper.readValue(value, clazz);
        } catch (Exception e) {
            LOGGER.error("Could not parse {}!", value);
            return null;
        }
    }

    private <T> void send(T dto, Consumer<T> sender) {
        if (dto != null) {
            sender.accept(dto);
        }
    }
}
