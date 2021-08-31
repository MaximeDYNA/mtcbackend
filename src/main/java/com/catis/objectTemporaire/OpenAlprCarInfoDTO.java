package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenAlprCarInfoDTO {
            private int company;
            private int site_id;
            private String site;
            private int camera_id;
            private String camera;
            private Date epoch_time_start;
            private Date epoch_time_end;
            private int best_index;
            private String best_plate;
            private String best_uuid;
            private int crop_location;
            private int direction_of_travel_degrees;
            private int direction_of_travel_id;
            private int hit_count;
            private int img_height;
            private int img_width;
            private int plate_x1;
            private int plate_x2;
            private int plate_x3;
            private int plate_x4;
            private int plate_y1;
            private int plate_y2;
            private int plate_y3;
            private int plate_y4;
            private String region;
            private String vehicle_body_type;
            private String vehicle_color;
            private String vehicle_make;
            private String vehicle_make_model;
            private String vehicle_region_height;
            private String vehicle_region_width;
            private String vehicle_region_x;
            private String vehicle_region_y;
            private String gps_latitude;
            private String gps_longitude;
            private String best_confidence;
            private String region_confidence;
            private String processing_time_ms;
            private String vehicle_color_confidence;
            private String vehicle_make_confidence;
            private String vehicle_make_model_confidence;
            private String vehicle_body_type_confidence;
            private String agent_uid;
            private String agent_type;

}
