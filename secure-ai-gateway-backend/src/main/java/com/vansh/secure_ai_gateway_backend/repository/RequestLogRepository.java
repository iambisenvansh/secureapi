package com.vansh.secure_ai_gateway_backend.repository;

import com.vansh.secure_ai_gateway_backend.model.RequestLog;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Map;

public interface RequestLogRepository extends MongoRepository<RequestLog, String> {

    long countByThreatLabel(String threatLabel);

    // Field name must match RequestLog.clientIp
    long countByClientIpAndEndpoint(String clientIp, String endpoint);

    List<RequestLog> findTop20ByOrderByTimestampDesc();

    @Aggregation(pipeline = {
            "{ $group: { _id: '$endpoint', count: { $sum: 1 }, avgThreatScore: { $avg: '$threatScore' } } }",
            "{ $project: { endpoint: '$_id', count: 1, avgThreatScore: 1, _id: 0 } }"
    })
    List<Map<String, Object>> aggregateByEndpoint();
}
