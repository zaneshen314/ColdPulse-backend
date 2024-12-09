package com.oocl.ita.web.service;

import com.oocl.ita.web.CharityEventParticipationStatus;
import com.oocl.ita.web.domain.po.CharityEvent;
import com.oocl.ita.web.domain.po.CharityEventParticipation;
import com.oocl.ita.web.domain.po.key.CharityEventParticipationKey;
import com.oocl.ita.web.domain.vo.CharityEventParticipationsResp;
import com.oocl.ita.web.repository.CharityEventParticipationRepository;
import com.oocl.ita.web.repository.CharityEventRepository;
import com.oocl.ita.web.repository.UserRepository;
import jdk.jfr.Registered;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.oocl.ita.web.CharityEventParticipationStatus.REGISTERED;

@Service
public class CharityEventService {

    private final CharityEventRepository charityEventRepository;

    private final CharityEventParticipationRepository charityEventParticipationRepository;

    private final UserService userService;

    public CharityEventService(CharityEventRepository charityEventRepository, CharityEventParticipationRepository charityEventParticipationRepository, UserService userService, UserRepository userRepository) {
        this.charityEventRepository = charityEventRepository;
        this.charityEventParticipationRepository = charityEventParticipationRepository;
        this.userService = userService;
    }


    public List<CharityEvent> getAllCharityEvent() {
        return charityEventRepository.findAll();
    }

    public List<CharityEventParticipation> getCharityEventParticipationByUserid(Integer userId) {
        return charityEventParticipationRepository.findAllByUserId(userId);
    }

    public CharityEventParticipation registerCharityEvent(Integer userId, Integer charityEventId, boolean claimPoint) {
        return charityEventParticipationRepository.save(new CharityEventParticipation(userId, charityEventId, REGISTERED, claimPoint));
    }

    public CharityEventParticipationsResp getCharityEventParticipationByCharityEventId(Integer charityEventId) {
        CharityEvent charityEvent = charityEventRepository.getById(charityEventId);
        return new CharityEventParticipationsResp(charityEvent.getId(), charityEvent.getName(), charityEventParticipationRepository.findAllByCharityEventId(charityEventId));
    }


    public CharityEventParticipation updateCharityEventParticipationStatus(Integer userId,Integer charityEventId, CharityEventParticipationStatus status) {
        CharityEventParticipation charityEventParticipation = charityEventParticipationRepository.getById(new CharityEventParticipationKey(userId, charityEventId));
        charityEventParticipation.setStatus(status);
        return charityEventParticipationRepository.save(charityEventParticipation);
    }
}
