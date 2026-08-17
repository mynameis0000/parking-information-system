INSERT INTO station (name, latitude, longitude, total_slots, available_count, created_at, updated_at) 
VALUES ('강남역 1번출구 거치대', 37.4979, 127.0276, 10, 5, NOW(), NOW())
ON CONFLICT DO NOTHING;

INSERT INTO battery (station_id, serial_number, battery_level, status, created_at, updated_at)
VALUES (1, 'BAT-001', 95, 'AVAILABLE', NOW(), NOW())
ON CONFLICT DO NOTHING;