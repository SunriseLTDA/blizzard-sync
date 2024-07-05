package me.lucwsh.blizzardsync.managers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserManager {

    private String name;
    private String account;
    private Boolean synced;
    private String security;

    public UserManager(String name) {
        this.name = name;
        this.account = "Nenhuma";
        this.synced = false;
        this.security = "Nenhum";
    }
}
