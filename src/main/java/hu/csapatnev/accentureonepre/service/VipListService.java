package hu.csapatnev.accentureonepre.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Service
public class VipListService {

    private Set<User> vipList = new HashSet<>();

    public VipListService() {
    }

    public VipListService(Set<User> vipList) {
        this.vipList = vipList;
    }

    @PostConstruct
    public void init() {
        readVipListFromResource();
    }

    private void readVipListFromResource() {
        File file = new File(
                getClass().getClassLoader().getResource("vipList.txt").getFile()
        );
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                long userId = Long.parseLong(scanner.nextLine());
                this.vipList.add(new User(userId));
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public boolean isContains(User user) {
        return this.vipList.contains(user);
    }

    public boolean isContains(long userId) {
        return this.vipList.contains(new User(userId));
    }



}
