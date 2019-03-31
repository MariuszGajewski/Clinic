package com.example.Clinic.models.services;

import com.example.Clinic.models.entities.ExaminationEntity;
import com.example.Clinic.models.forms.ExaminationForm;
import com.example.Clinic.models.repositories.ExaminationRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.time.temporal.ChronoUnit.DAYS;


@Service

public class ExaminationService {
    public enum monthName{
        STYCZEŃ, LUTY, MARZEC, KWIECIEŃ, MAJ, CZERWIEC, LIPIEC, SIERPIEŃ, WRZESIEŃ, PAŹDZIERNIK, LISTOPAD, GRUDZIEŃ;
    }

    public enum daysName{
        PON, WT, ŚR, CZW, PT, SB, ND
    }
    @Getter
    private int firstDayOfMonth;
    @Getter
    private int lastDayOfMonth;
    @Getter
    private int monthLength;
    @Getter
    private int numberOfWeeks;
    @Getter
    private String nameOfMonth;
    @Getter
    private String[] nameOfDays;
    @Getter
    private int monthNumber;
    @Getter
    private LocalDate selectedDate;
    @Getter
    private List<Integer> blockedDays;

    private Map<LocalDate, List<LocalTime>> availableTimeMap;
    private String examinationType;
    private int daysDifference;


    final
    ExaminationRepository examinationRepository;
    final PatientService patientService;

    private ExaminationEntity examinationEntity;




    @Autowired
    public ExaminationService(ExaminationRepository examinationRepository, PatientService patientService) {
        this.patientService = patientService;
        nameOfDays=new String[daysName.values().length];

        for (int i = 0; i < daysName.values().length; i++) {
            nameOfDays[i] = (daysName.values())[i].toString();
        }
        this.examinationRepository = examinationRepository;

    }

    public void getCalendarMonth(int seed, String examinationType) {
        this.availableTimeMap = new LinkedHashMap<>();
        this.blockedDays=new ArrayList<>();
        this.examinationType=examinationType;
        LocalDate time=LocalDate.now().plusMonths(seed);
        selectedDate=time;
        firstDayOfMonth=time.with(TemporalAdjusters.firstDayOfMonth()).getDayOfWeek().getValue();
        lastDayOfMonth=time.with(TemporalAdjusters.lastDayOfMonth()).getDayOfWeek().getValue();
        monthNumber=time.getMonthValue();
        monthLength=time.lengthOfMonth();
        numberOfWeeks=(monthLength+(firstDayOfMonth-1)+(7-lastDayOfMonth))/7;
        monthName monthNames[]=monthName.values();
        nameOfMonth=monthNames[time.getMonthValue()-1].toString();

        int blockedDaysIndex = getBlockedDaysList(time);
        System.out.println("blocked Index : ");
        System.out.println(blockedDaysIndex);
        for (int i = 0; i <= blockedDaysIndex; i++) {
            blockedDays.add(i);
        }
    }

    private int getBlockedDaysList(LocalDate time){
        LocalDate startingDate = time.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate finishDate = startingDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        List<ExaminationEntity> examinationList = examinationRepository.findByDatesAndType(examinationType,startingDate + "%", finishDate + "%");
        AtomicInteger incomingExaminationDay= new AtomicInteger();
        int blockedDaysIndex= 0;
        if(daysDifference == 0) {
            getBlockedDaysBeforeFutureExamination(time.with(TemporalAdjusters.lastDayOfMonth()));
        }
        if(daysDifference != 0){
            blockedDaysIndex = time.lengthOfMonth();
            daysDifference = 0;
            return blockedDaysIndex;
        }
        if(!time.isAfter(LocalDate.now())){
            blockedDaysIndex= LocalDate.now().getDayOfMonth();
        }
        if(examinationList.isEmpty()) {
            return blockedDaysIndex;
        }
        examinationList.stream().forEach(s-> {
            if(s.getPatients().getId() == patientService.getPatientEntity().getId() &&
                    s.getExaminationTime().toLocalDate().isAfter(startingDate)) {
                incomingExaminationDay.set(s.getExaminationTime().getDayOfMonth());
            }
        } );
        if(incomingExaminationDay.intValue() > blockedDaysIndex) {
            blockedDaysIndex = incomingExaminationDay.intValue();
        }
        getAvailableDays(examinationList);
        for (LocalDate localDate : availableTimeMap.keySet()) {
            if(availableTimeMap.get(localDate).size() >=4) {
                blockedDays.add(localDate.getDayOfMonth());
            }
        }
        return blockedDaysIndex;
    }

    private void getBlockedDaysBeforeFutureExamination(LocalDate lastDayOfMonth){
        Optional<ExaminationEntity> userFutureExamination = Optional.empty();
        System.out.println("jestesmy w funkcji ");
        List<ExaminationEntity> userExaminationList = examinationRepository.findByPatientsIdAndExaminationTypeOrderByExaminationTimeDesc(patientService.getPatientEntity().getId(), examinationType);
        if (!userExaminationList.isEmpty()) {
            userFutureExamination = userExaminationList.stream().filter(s ->
                    s.getExaminationTime().isAfter(lastDayOfMonth.atStartOfDay()))
                    .findFirst();
        }
        if (userFutureExamination.isPresent()) {
            LocalDate date1 = userFutureExamination.get().getExaminationTime().toLocalDate();
            LocalDate date2 = LocalDate.now();
            daysDifference = (int) DAYS.between(date2, date1);
        }
    }
    private void getAvailableDays(List<ExaminationEntity> examList) {
        LocalDate tempDate = examList.get(0).getExaminationTime().toLocalDate();
        List<LocalTime> availableHoursAtDay = new ArrayList<>();
        for (ExaminationEntity examinationEntity : examList) {
            if (examinationEntity.getExaminationTime().toLocalDate().equals(tempDate)) {
                availableHoursAtDay.add(examinationEntity.getExaminationTime().toLocalTime());
                continue;
            } else {
                availableTimeMap.put(tempDate, availableHoursAtDay);
                tempDate = examinationEntity.getExaminationTime().toLocalDate();
                availableHoursAtDay = new ArrayList<>();
                availableHoursAtDay.add(examinationEntity.getExaminationTime().toLocalTime());
            }
            availableTimeMap.put(tempDate, availableHoursAtDay);
        }
        availableTimeMap.put(tempDate, availableHoursAtDay);
    }

    public List<LocalTime> getAvailableHoursAtDay(LocalDate chosenDay) {
        List<LocalTime>availableHours=new ArrayList<>();
        List<LocalTime> examinationAtDay;
        if(availableTimeMap.get(chosenDay) != null) {
            examinationAtDay = availableTimeMap.get(chosenDay);
        }else{
            examinationAtDay=new ArrayList<>();
        }
        System.out.println();
        LocalTime tempTime=LocalTime.of(9,40);
        int openHoursAtDay = 7;
        int examinationPerHour = 3;
        for (int i = 0; i < openHoursAtDay*examinationPerHour; i++) {
            tempTime=tempTime.plusMinutes(20);
            if(!examinationAtDay.contains(tempTime)){
                availableHours.add(tempTime);
            }
        }
        return availableHours;
    }

    public PatientService.userOperationStatus saveExamination (ExaminationForm examinationForm) {
        examinationEntity= new ExaminationEntity();
        System.out.println(examinationForm);
        examinationEntity.setPatients(patientService.getPatientEntity());
        examinationEntity.setExaminationTime(examinationForm.getExaminationTime());
        examinationEntity.setExaminationType(examinationForm.getExaminationType());
        examinationEntity.setExaminationNote(examinationForm.getExaminationNote());
        ExaminationEntity examination = examinationRepository.save(examinationEntity);
        if(examination == null) {
            return PatientService.userOperationStatus.ERROR;
        }
        return PatientService.userOperationStatus.OK;
    }

    public List<ExaminationEntity> showExaminations() {
        return patientService.getPatientEntity().getExaminations();
    }
}
