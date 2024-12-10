package com.oocl.ita.web.service;

import com.oocl.ita.web.domain.bo.ReleaseTicketTaskBody;
import com.oocl.ita.web.domain.po.ConcertClass;
import com.oocl.ita.web.repository.ConcertClassRepository;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
public class ReleaseTicketTaskService {

    private ConcertClassRepository concertClassRepository;

    private final TaskScheduler taskScheduler = new ConcurrentTaskScheduler();

    private LocalDate lastExecutionDate;

    // 用于保存定时任务的返回值，以便取消定时任务
    private ScheduledFuture<?> scheduledFuture;

    public ReleaseTicketTaskService(ConcertClassRepository concertClassRepository) {
        this.concertClassRepository = concertClassRepository;
    }

    public void scheduleReleaseTicketTask(Integer concertId, ReleaseTicketTaskBody releaseTicketTaskBody) {
        int repeatCount = releaseTicketTaskBody.getRepeatCount();
        String cronExpression = "0 0 21 * * ?";

        Date startTime = releaseTicketTaskBody.getStartTime();
        Date endTime = releaseTicketTaskBody.getEndTime();

        List<ConcertClass> concertClasses = concertClassRepository.findByConcertId(concertId);
        final Map<Integer, List<Integer>> concertClassMapQuantities =
                generateConcertClassQuantityMapping(concertClasses, repeatCount);

        LocalDate startTimeLocalDate = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endTimeLocalDate = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long apartDay = ChronoUnit.DAYS.between(endTimeLocalDate, startTimeLocalDate) / repeatCount;
        lastExecutionDate = startTimeLocalDate;
        Runnable releaseTicketTask = new Runnable() {
            private int count = 0;
            @Override
            public void run() {
                LocalDate currentDate = LocalDate.now();
                long between = ChronoUnit.DAYS.between(currentDate, lastExecutionDate);
                if (between >= apartDay && count < repeatCount) {
                    List<ConcertClass> classes = concertClassRepository.findByConcertId(concertId);
                    classes.forEach(concertClass -> {
                        Integer quantity = concertClassMapQuantities.get(concertClass.getId()).get(count);
                        if (quantity == null) {
                            cancelReleaseTicketTask();
                            return;
                        }
                        concertClass.setAvailableSeats(concertClass.getAvailableSeats() + quantity);
                        concertClassRepository.save(concertClass);
                    });
                    count++;
                    lastExecutionDate = currentDate;
                }
                if (count >= repeatCount || currentDate.isAfter(endTimeLocalDate)) {
                    cancelReleaseTicketTask();
                }
            }
        };

        scheduledFuture = taskScheduler.schedule(releaseTicketTask, new CronTrigger(cronExpression));
    }

    private static Map<Integer, List<Integer>> generateConcertClassQuantityMapping(List<ConcertClass> concertClasses, int repeatCount) {
        final Map<Integer, List<Integer>> quantityMap = new HashMap<>();

        for (ConcertClass concertClass : concertClasses) {
            List<Integer> quantities = new ArrayList<>(repeatCount);
            int capacity = concertClass.getCapacity();
            for (int i = 0; i < repeatCount - 1; i++) {
                quantities.add(capacity / repeatCount);
            }
            quantities.add(capacity - (capacity / repeatCount) * (repeatCount - 1));
            quantityMap.put(concertClass.getId(), quantities);
        }
        return quantityMap;
    }

    private void cancelReleaseTicketTask() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }
    }
}