package it.unicam.hackhub.service;

import it.unicam.hackhub.model.Call;
import it.unicam.hackhub.repository.CallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CallService {
    private final CallRepository callRepo;

    public CallService(CallRepository callRepo) {
        this.callRepo = callRepo;
    }

    @Transactional
    public void respondToCall(String callId, boolean accepted) {
        Call call = callRepo.findById(callId)
                .orElseThrow(() -> new IllegalStateException("Call non trovata"));

        if (call.getStatus() != Call.CallStatus.WAITING_TEAM_RESPONSE) {
            throw new IllegalStateException("Call non in attesa di risposta");
        }

        if (accepted) {
            call.accept();
        } else {
            call.reject();
        }

        callRepo.save(call);
    }
 }