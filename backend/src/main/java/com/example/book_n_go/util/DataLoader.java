package com.example.book_n_go.util;

import com.example.book_n_go.enums.Day;
import com.example.book_n_go.enums.Role;
import com.example.book_n_go.model.*;
import com.example.book_n_go.repository.UserRepository;
import com.example.book_n_go.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

        private final UserRepository userRepo;
        private final LocationRepo locationRepo;
        private final WorkspaceRepo workspaceRepo;
        private final HallRepo hallRepo;
        private final WorkdayRepo workdayRepo;

        public DataLoader(UserRepository userRepo,
                        LocationRepo locationRepo,
                        WorkspaceRepo workspaceRepo,
                        HallRepo hallRepo,
                        WorkdayRepo workdayRepo) {
                this.userRepo = userRepo;
                this.locationRepo = locationRepo;
                this.workspaceRepo = workspaceRepo;
                this.hallRepo = hallRepo;
                this.workdayRepo = workdayRepo;
        }

        @Override
        public void run(String... args) throws Exception {
                // Add sample users
                User user1 = new User(null, "provider1@example.com", "Provider One", "1234567890", "password1",
                                Role.PROVIDER);
                User user2 = new User(null, "provider2@example.com", "Provider Two", "0987654321", "password2",
                                Role.PROVIDER);
                userRepo.saveAll(Arrays.asList(user1, user2));

                // Add sample locations
                Location location1 = new Location(0, 123, "Main St", "New York");
                Location location2 = new Location(0, 456, "Broadway", "Los Angeles");
                Location location3 = new Location(0, 789, "Market St", "San Francisco");
                locationRepo.saveAll(Arrays.asList(location1, location2, location3));

                // Add sample workspaces
                Workspace workspace1 = new Workspace(0, "Hamada Space", user1.getId().intValue(), location1.getId(),
                                null, 3.0,
                                "A cozy workspace in NY, with a great view of the city. This workspace offers a comfortable and productive environment with modern amenities, high-speed internet, and a friendly community. Ideal for freelancers, remote workers");
                Workspace workspace2 = new Workspace(0, "Hamo Space", user2.getId().intValue(), location2.getId(),
                                null, 3.0,
                                "A modern workspace in LA, with a stunning view of the city. This workspace offers a comfortable and productive environment with modern amenities, high-speed internet, and a friendly community. Ideal for freelancers, remote workers");
                workspaceRepo.saveAll(Arrays.asList(workspace1, workspace2));

                // Add sample halls
                Hall hall1 = new Hall(0, "Main Hall", workspace1.getId(), workspace1, 100,
                                "Large hall with a beautiful view", 200.0, 4.0);
                Hall hall2 = new Hall(0, "Central Hall", workspace1.getId(), workspace1, 150,
                                "Spacious hall with modern amenities", 250.0, 4.5);
                Hall hall3 = new Hall(0, "West Hall", workspace1.getId(), workspace1, 80,
                                "Cozy hall with a great view", 150.0, 4.0);
                Hall hall4 = new Hall(0, "East Hall", workspace2.getId(), workspace2, 120,
                                "Elegant hall with state-of-the-art facilities", 300.0, 4.8);
                Hall hall5 = new Hall(0, "Grand Hall", workspace2.getId(), workspace2, 180,
                                "Grand hall with a luxurious design", 400.0, 5.0);
                Hall hall6 = new Hall(0, "North Hall", workspace1.getId(), workspace1, 200,
                                "Large hall with a panoramic view", 350.0, 4.9);
                Hall hall7 = new Hall(0, "South Hall", workspace2.getId(), workspace2, 90,
                                "Compact hall with modern design", 180.0, 4.2);
                Hall hall8 = new Hall(0, "Garden Hall", workspace1.getId(), workspace1, 110,
                                "Hall with a beautiful garden view", 220.0, 4.6);
                Hall hall9 = new Hall(0, "Skyline Hall", workspace2.getId(), workspace2, 130,
                                "Hall with a stunning skyline view", 280.0, 4.7);
                hallRepo.saveAll(Arrays.asList(hall1, hall2, hall3, hall4, hall5, hall6, hall7, hall8, hall9));

                // Add sample workdays
                Workday workday1 = new Workday(0, (int) workspace1.getId(), workspace1, Time.valueOf("09:00:00"),
                                Time.valueOf("17:00:00"), Day.MONDAY);
                Workday workday2 = new Workday(0, (int) workspace2.getId(), workspace2, Time.valueOf("10:00:00"),
                                Time.valueOf("18:00:00"), Day.TUESDAY);
                Workday workday3 = new Workday(0, (int) workspace1.getId(), workspace1, Time.valueOf("09:00:00"),
                                Time.valueOf("17:00:00"), Day.WEDNESDAY);
                Workday workday4 = new Workday(0, (int) workspace2.getId(), workspace2, Time.valueOf("10:00:00"),
                                Time.valueOf("18:00:00"), Day.THURSDAY);
                Workday workday5 = new Workday(0, (int) workspace1.getId(), workspace1, Time.valueOf("09:00:00"),
                                Time.valueOf("17:00:00"), Day.FRIDAY);
                Workday workday6 = new Workday(0, (int) workspace2.getId(), workspace2, Time.valueOf("10:00:00"),
                                Time.valueOf("18:00:00"), Day.SATURDAY);
                Workday workday7 = new Workday(0, (int) workspace1.getId(), workspace1, Time.valueOf("09:00:00"),
                                Time.valueOf("17:00:00"), Day.SUNDAY);
                workdayRepo.saveAll(
                                Arrays.asList(workday1, workday2, workday3, workday4, workday5, workday6, workday7));

                System.out.println("Sample data loaded successfully.");
        }
}
