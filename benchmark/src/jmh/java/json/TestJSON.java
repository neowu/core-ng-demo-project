package json;

import core.framework.api.json.Property;
import core.framework.api.validate.NotBlank;
import core.framework.api.validate.NotNull;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

public class TestJSON {
    @Property(name = "planning")
    public Planning planning;

    public static class Planning {
        @NotNull
        @NotBlank
        @Property(name = "hdr_id")
        public String hdrId;

        @NotNull
        @Property(name = "is_test_hdr")
        public Boolean isTest;

        @NotNull
        @Property(name = "code")
        public String code;

        @NotNull
        @Property(name = "name")
        public String name;

        @NotNull
        @Property(name = "status")
        public HDRStatusView status;

        @NotNull
        @NotBlank
        @Property(name = "time_zone_id")
        public String timeZoneId;

        @NotNull
        @Property(name = "categories")
        public List<HDRCategory> categories;

        @Property(name = "close_time_for_today")
        public ZonedDateTime closeTimeForToday;

        @Property(name = "original_opening_time_for_today")
        public ZonedDateTime originalOpeningTimeForToday;

        @Property(name = "opening_time_for_today")
        public ZonedDateTime openingTimeForToday;

        @Property(name = "opening_time")
        public ZonedDateTime openingTime;

        @Property(name = "description")
        public String description;

        @NotNull
        @Property(name = "contact_number")
        public String contactNumber;

        @NotNull
        @Property(name = "address")
        public String address;

        @NotNull
        @Property(name = "latitude")
        public Double latitude;

        @NotNull
        @Property(name = "longitude")
        public Double longitude;

        @NotNull
        @NotBlank
        @Property(name = "address_line1")
        public String addressLine1;

        @Property(name = "address_2")
        public String address2;

        @NotNull
        @NotBlank
        @Property(name = "city")
        public String city;

        @NotNull
        @NotBlank
        @Property(name = "state_code")
        public String stateCode;

        @Property(name = "county")
        public String county;

        @NotBlank
        @Property(name = "zip_code")
        public String zipCode;

        @NotNull
        @Property(name = "logo_image_key")
        public String logoImageKey;

        @NotBlank
        @Property(name = "detail_image_key")
        public String detailImageKey;

        @NotBlank
        @Property(name = "coming_soon_image_key")
        public String comingSoonImageKey;

        @Property(name = "pickup_instructions")
        public String pickupInstructions;

        @Property(name = "customer_pickup_instructions")
        public String customerPickupInstructions;

        @Property(name = "location_details")
        public String locationDetails;

        @NotNull
        @Property(name = "location_type")
        public LocationType locationType = LocationType.WONDER;

        @Property(name = "google_review_url")
        public String googleReviewURL;

        @Property(name = "yelp_review_url")
        public String yelpReviewURL;

        @Property(name = "delivery_zone_id")
        public String deliveryZoneId;

        @NotNull
        @Property(name = "created_time")
        public ZonedDateTime createdTime;

        @NotNull
        @Property(name = "hdr_operating_hours")
        public List<OperatingHour> hdrOperatingHours;

        @NotNull
        @Property(name = "walk_in_operating_hours")
        public List<OperatingHour> walkInOperatingHours;

        @NotNull
        @Property(name = "restaurants")
        public List<Restaurant> restaurants;

        @NotNull
        @Property(name = "scheduled_order_configuration")
        public ScheduledOrderConfiguration scheduledOrderConfiguration;

        @NotNull
        @Property(name = "wonder_spot_configuration")
        public WonderSpotConfiguration wonderSpotConfiguration;

        public enum FulfillmentTypeView {
            @Property(name = "DELIVERY")
            DELIVERY,
            @Property(name = "PICK_UP")
            PICK_UP,
            @Property(name = "IN_STORE")
            IN_STORE
        }

        public enum LocationType {
            @Property(name = "WALMART")
            WALMART,
            @Property(name = "WONDER")
            WONDER
        }

        public enum RestaurantPlanningStatusView {
            @Property(name = "OPEN")
            OPEN
        }

        public enum RestaurantStatusView {
            @Property(name = "ACTIVE")
            ACTIVE
        }

        public enum HDRCloseReasonView {
            @Property(name = "OTHER")
            OTHER
        }

        public enum RestaurantStatusChangedByView {
            @Property(name = "OTHER")
            OTHER
        }

        public enum DayOfWeekView {
            @Property(name = "MONDAY")
            MONDAY,
            @Property(name = "TUESDAY")
            TUESDAY,
            @Property(name = "WEDNESDAY")
            WEDNESDAY,
            @Property(name = "THURSDAY")
            THURSDAY,
            @Property(name = "FRIDAY")
            FRIDAY,
            @Property(name = "SATURDAY")
            SATURDAY,
            @Property(name = "SUNDAY")
            SUNDAY;

        }

        public enum HDRStatusView {
            @Property(name = "OPEN")
            OPEN
        }

        public static class Restaurant {
            @NotNull
            @NotBlank
            @Property(name = "hdr_restaurant_id")
            public String hdrRestaurantId;

            @NotNull
            @NotBlank
            @Property(name = "restaurant_id")
            public String restaurantId;

            @NotNull
            @NotBlank
            @Property(name = "restaurant_name")
            public String restaurantName;

            @Property(name = "today_postponed_time")
            public ZonedDateTime todayPostponedTime;

            @Property(name = "today_original_opening_time")
            public ZonedDateTime todayOriginalOpeningTime;

            @NotNull
            @Property(name = "status")
            public RestaurantPlanningStatusView status;

            @NotNull
            @Property(name = "restaurant_status")
            public RestaurantStatusView restaurantStatus;

            @Property(name = "restaurant_close_reason")
            public HDRCloseReasonView restaurantCloseReason;

            @Property(name = "status_changed_by")
            public RestaurantStatusChangedByView statusChangedBy;

            @NotNull
            @Property(name = "fulfillment_types")
            public List<FulfillmentTypeView> fulfillmentTypes;

            @Property(name = "service_time")
            public ServiceTime serviceTime;

            @NotNull
            @Property(name = "next_service_times")
            public List<ServiceTime> nextServiceTimes;

            @NotNull
            @Property(name = "operating_hours")
            public List<OperatingHour> operatingHours;

            @Deprecated(since = "2024-sprint22")
            @Property(name = "scheduled_time_slots")
            public List<ScheduledTimeSlot> scheduledTimeSlots;
        }

        public static class ServiceTime {
            @NotNull
            @Property(name = "start_time")
            public ZonedDateTime startTime;

            @NotNull
            @Property(name = "end_time")
            public ZonedDateTime endTime;
        }

        public static class OperatingHour {
            @NotNull
            @Property(name = "day_of_week")
            public DayOfWeekView dayOfWeek;

            @NotNull
            @Property(name = "service_hours")
            public List<ServiceHour> serviceHours;
        }

        public static class ServiceHour {
            @Deprecated(since = "2024-sprint9")
            @NotNull
            @Property(name = "start_time_est")
            public LocalTime startTimeEST;

            @Deprecated(since = "2024-sprint9")
            @NotNull
            @Property(name = "end_time_est")
            public LocalTime endTimeEST;

            @NotNull
            @Property(name = "start_time")
            public LocalTime startTime;

            @NotNull
            @Property(name = "end_time")
            public LocalTime endTime;
        }

        public static class ScheduledTimeSlot {
            @NotNull
            @Property(name = "day_of_week")
            public DayOfWeekView dayOfWeek;

            @NotNull
            @Property(name = "time_slots")
            public List<TimeSlot> timeSlots;
        }

        public static class TimeSlot {
            @NotNull
            @Property(name = "start_time")
            public LocalTime startTime;

            @NotNull
            @Property(name = "end_time")
            public LocalTime endTime;
        }

        public static class ScheduledOrderConfiguration {
            @NotNull
            @Property(name = "enabled")
            public Boolean enabled = Boolean.FALSE;

            @NotNull
            @Property(name = "from_time")
            public String fromTime;

            @NotNull
            @Property(name = "to_time")
            public String toTime;

            @NotNull
            @Property(name = "cut_off_in_minutes")
            public Integer cutOffInMinutes;

            @NotNull
            @Property(name = "window_length_in_minutes")
            public Integer windowLengthInMinutes;

            @NotNull
            @Property(name = "release_buffer_in_minutes")
            public Integer releaseBufferInMinutes;
        }

        public static class WonderSpotConfiguration {
            @NotNull
            @Property(name = "partners")
            public List<Partner> partners = List.of();
        }

        public static class Partner {
            @NotNull
            @Property(name = "id")
            public String id;

            @NotNull
            @Property(name = "time_slots")
            public List<PartnerTimeSlot> timeSlots;

            @Deprecated(since = "2025 SP12")
            @Property(name = "cutoff_time")
            public LocalTime cutoffTime;

            @Deprecated(since = "2025 SP12")
            @Property(name = "delivery_time_from")
            public LocalTime deliveryTimeFrom;

            @Deprecated(since = "2025 SP12")
            @Property(name = "delivery_time_to")
            public LocalTime deliveryTimeTo;

            @Deprecated(since = "2025 SP12")
            @Property(name = "capacity")
            public Integer capacity;
        }

        public static class PartnerTimeSlot {
            @NotNull
            @Property(name = "delivery_time_from")
            public LocalTime deliveryTimeFrom;

            @NotNull
            @Property(name = "delivery_time_to")
            public LocalTime deliveryTimeTo;

            @NotNull
            @Property(name = "cutoff_time")
            public LocalTime cutoffTime;

            @NotNull
            @Property(name = "capacity")
            public Integer capacity;
        }

        public static class HDRCategory {
            @NotNull
            @Property(name = "id")
            public String id;

            @NotNull
            @Property(name = "name")
            public String name;

            @NotNull
            @Property(name = "properties")
            public List<HDRProperty> properties;
        }

        public static class HDRProperty {
            @NotNull
            @Property(name = "id")
            public String id;

            @NotNull
            @Property(name = "name")
            public String name;
        }
    }
}
