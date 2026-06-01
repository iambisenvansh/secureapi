package com.vansh.secure_ai_gateway_backend.service;

import com.vansh.secure_ai_gateway_backend.model.ThreatEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThreatStore {

    private final List<ThreatEvent> events = new ArrayList<>();

    public void add(ThreatEvent event) {
        events.add(event);
    }

    public List<ThreatEvent> getAll() {
        return events;
    }

    public void clear() {
        events.clear();
    }
}
